sap.ui.define(["dhi/optimizer/controller/BaseController","sap/m/MessageBox"], function (BaseController, MessageBox) { "use strict";
	
	return BaseController.extend("dhi.optimizer.controller.PerformanceEfficiency", {
		oParam: {
			path:  "/performance_efficiency/data",
			type: "get",
			callback: "callbackAjax"
		},		
		oReportParam: {
			path_check:  "/performance_efficiency/report/check",
			path_download:  "/performance_efficiency/report/download",			
			type: "get",
			callback: "callbackReportAjax"
		},
		oResetParam: {
			path:  "/operation/reset_effect",
			type: "post",
			callback: "callbackResetAjax"
		},
		callbackAjax: function(oModel) {
			this.setModel(oModel);
			var item=oModel.getData().result.combustion_condition;
			if(undefined==item||null==item){
				this.setModel(); return;
			}
			var html="<div class='divPEmill'/>";
			for (var i = 0; i < item.length; i++) {
				if(item[i].item.search(/Mill Operation/i)>-1){
					var total_mill=item[i].total_mill;
					var operation_mill=item[i].operation_mill;
					for (var j = 0; j < total_mill.length; j++) {
						var oFlag=true;
						for (var k = 0; k < operation_mill.length; k++) {
							if(total_mill[j]==operation_mill[k]){
								html+="<span class='txtPEmillRed' style='width:20px;text-align:center'>"+total_mill[j]+"</span>";
								oFlag=false; break;
							}
						}//for:k
						if(oFlag){
							html+="<span class='txtPEmillBlue' style='width:20px;text-align:center'>"+total_mill[j]+"</span>";
						}
					}//for:j
				}//if
			}//for:i
			$(".divPEmill").parent().html(html);
			
			var sasan=oModel.getData().result.cost_saving_information_version_sasan;
			for (var i = 0; i < sasan.length; i++) {
				for ( var item in sasan[i]) {
					var dot=String(sasan[i][item]).indexOf(".");
					if(dot>-1){
						$("[id$=imgCostSavingDot"+item.charAt(0).toUpperCase()+item.slice(1)+(i+1)+"]").attr("class","CostSavingDot"+(5-String(sasan[i][item]).length+dot));
					}else if(Number.isFinite(sasan[i][item])){
						$("[id$=imgCostSavingDot"+item.charAt(0).toUpperCase()+item.slice(1)+(i+1)+"]").attr("class","CostSavingDot0");
					}
				}//for:item
			}//for:i
			
			var result=oModel.getData().result;
			var data=[];
			for (var item in result) {
				if(Array.isArray(result[item])){
					for (var i = 0; i < result[item].length; i++) {
						var obj={};
						if(result[item][i].additional_information){
							data.push(this.setObject(obj,result[item][i],item));
							var info=result[item][i].additional_information;
							for (var j = 0; j < info.length; j++) {
								obj={};
								data.push(this.setObject(obj,info[j],item,result[item][i].item));
							}
						}else if(result[item][i].total_mill){
							obj.total_mill=result[item][i].total_mill.toString();
							obj.operation_mill=result[item][i].operation_mill.toString();
							data.push(this.setObject(obj,result[item][i],item));
						}else{ data.push(this.setObject(obj,result[item][i],item)); }
					}//for:i
				}else{
					for (var aItem in result[item]) {
						for (var i = 0; i < result[item][aItem].length; i++) {
							var obj={};
							data.push(this.setObject(obj,result[item][aItem][i],item+"."+aItem));
						}//for:i
					}
				}//if
			}//for:item
			result.data=data;
			this.byId("btnReset").setBusy(false);
		},//callbackAjax
		callbackResetAjax: function (oModel) {
			if(oModel.getData().code=200){ this.onAjaxCall(this.oParam); }
		},
		capitalizeFirstLetter: function (s) { return s.charAt(0).toUpperCase() + s.slice(1); },
		formatDot : function (s) { return String(s).replace(".",""); },
		setObject : function (obj,item,type,tree) {
			obj.type=type;
			obj.item=(tree?tree+".":"")+item.item;
			obj.baseline=item.baseline;
			obj.current=item.current;
			obj.unit=item.unit;
			if(undefined!=item.hourly){
				obj.hourly=item.hourly+" "+item.hourly_unit;
				obj.daily=item.daily+" "+item.daily_unit;
				obj.monthly=item.monthly+" "+item.monthly_unit;
				obj.annually=item.annually+" "+item.annually_unit;
			}
			return obj;
		},
		onInterval: function() { this.onAjaxCall(this.oParam); },
		onInit : function () {
			var that = this;
			this.getView().addEventDelegate({
			   onBeforeShow: function(oEvent) {
				   jQuery.sap.log.info("onBeforeShow");
				   that.oParam.url = that.getApiUrl()+that.oParam.path+"/"+that.getSessionValue("default_plant_unit_id");
				   that.onAjaxCall(that.oParam);
				   
				   //admin reset button display
				   var role = that.getSessionValue("role_id");
				   if(role.search(/AD/i)>-1){
					   that.byId("btnReset").setVisible(true);
				   }
				   
				   if(role.search(/AD/i)>-1 || role.search(/CE/i)>-1 || role.search(/EF/i)>-1){
					   that.byId("__component0---performanceEfficiency--exportPrint--btnReport").setVisible(true);
				   }
			   },
			   onAfterShow: function(oEvent) {
				   jQuery.sap.log.info("onAfterShow");
				   //focus 방지
				   $("div[id$=performanceEfficiency] span[id$=-inner]").css("outline","none");
				   $("div[id$=performanceEfficiency] tbody td").css("outline","none");
			   }
			});
		},
		onReset: function(oEvent) {
			this.byId("btnReset").setBusy(true);
			this.oResetParam.url = this.getApiUrl()+this.oResetParam.path;
			this.onAjaxCall(this.oResetParam);
		},
		createColumnConfig: function() {
			return [
				{ label: 'Type', 			property: 'type', 			textAlign: "center", type: 'String', width: 40 },
				{ label: 'Item', 			property: 'item', 			textAlign: "center", type: 'String', width: 30 },
				{ label: 'Baseline', 		property: 'baseline', 		textAlign: "center", type: 'Number', width: 10 },
				{ label: 'Current', 		property: 'current', 		textAlign: "center", type: 'Number', width: 10 },
				{ label: 'Unit', 			property: 'unit', 			textAlign: "center", type: 'String', width: 10 },
				{ label: 'Total_mill', 		property: 'total_mill', 	textAlign: "center", type: 'String', width: 15 },
				{ label: 'Operation_mill', 	property: 'operation_mill', textAlign: "center", type: 'String', width: 15 },
				{ label: 'Hourly', 			property: 'hourly', 		textAlign: "center", type: 'String', width: 10 },
				{ label: 'Daily', 			property: 'daily', 			textAlign: "center", type: 'String', width: 10 },
				{ label: 'Monthly', 		property: 'monthly', 		textAlign: "center", type: 'String', width: 10 },
				{ label: 'Annually', 		property: 'annually', 		textAlign: "center", type: 'String', width: 10 },
			];
		},
		setPeriodDate: function(s){
			if(typeof s != "string"){
				s = s.getSource().data("type");
			}
			
			var d = new Date();
			d.setDate(d.getDate()-1); 
			this.setPeriodD(s, this.dpStart, this.dpEnd, "date", d);			
		},
		toYYYYMMDD: function(val){ return val.substr(6,4)+"-"+val.substr(3,2)+"-"+val.substr(0,2); },
		onPressReport: function(oEvent) {
			if (!this.reportDialog) {
				this.reportDialog = sap.ui.xmlfragment("dhi.optimizer.fragment.PerformanceEfficiencyReport", this);				
				this.getView().addDependent(this.reportDialog);
				this.dpStart=sap.ui.getCore().byId("dpStart");
				this.dpEnd=sap.ui.getCore().byId("dpEnd");
			}			
			this.reportDialog.open();
			
			var d = new Date();
			d.setDate(d.getDate()-1); 
			this.setPeriodD("d", this.dpStart, this.dpEnd, "date", d);
		},		
		onPressExport: function(oEvent) {
			this.onExport(this.createColumnConfig(), this.getModel().getProperty("/result/data"),"performanceEfficiency");
		},
		onUnitChange: function() {
			this.oParam.url = this.getApiUrl()+this.oParam.path+"/"+this.getSessionValue("default_plant_unit_id");
			this.onAjaxCall(this.oParam);
		},		
		onReportClose: function(oEvent) { oEvent.getSource().oParent.close(); },
		onReportDownload: function (oEvent) {
			var startDate=this.toYYYYMMDD(this.dpStart.getValue());
			var endDate=this.toYYYYMMDD(this.dpEnd.getValue());			
			var startDay = new Date(startDate);
			var endDay = new Date(endDate);
			if(startDay > endDay){
				MessageBox.warning("Start date is greater than end date.");
				return;
			}
			
			var today = new Date();
			var tCount = today.getFullYear() + today.getMonth() + today.getDate();
			var eCount = endDay.getFullYear() + endDay.getMonth() + endDay.getDate();
			if(eCount >= tCount){
				MessageBox.warning("You can only see data from before yesterday.");
				return;
			}
			
			this.oReportParam.url = this.getApiUrl()+this.oReportParam.path_check+"/"+this.getSessionValue("default_plant_unit_id")+"?start_date="+startDate+"&end_date="+endDate;			
			this.onAjaxCall(this.oReportParam);
		},
		callbackReportAjax: function(oModel) {
			var isCheck = oModel.getData().result;			
			if(isCheck=="true"){				
				var startDate=this.toYYYYMMDD(this.dpStart.getValue());
				var endDate=this.toYYYYMMDD(this.dpEnd.getValue());				
				this.oReportParam.url = this.getApiUrl()+this.oReportParam.path_download+"/"+this.getSessionValue("default_plant_unit_id");
				this.onFileDownload(this.oReportParam.url + "?start_date="+startDate+"&end_date="+endDate);
				this.reportDialog.close();				
			}else{
				MessageBox.warning("There are no results for the date you searched for.");
			}
		}
	});
}, /* bExport= */ true);