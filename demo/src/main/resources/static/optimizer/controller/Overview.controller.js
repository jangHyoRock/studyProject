sap.ui.define([
		"dhi/optimizer/controller/BaseController", "sap/ui/model/json/JSONModel", "dhi/common/util/ChartUtil"
	], function (BaseController, JSONModel, ChartUtil) {
		"use strict";
	
	return BaseController.extend("dhi.optimizer.controller.Overview", {
		vizFrameSet: {
			id: "chartOverview",
			url: "model/overviewStatus.json",
			type: "pie",
			dataset: {
				dimensions: [{ name: 'name', value: "{name}" }],
				measures: [{ name: 'value', value: '{value}' }],
				data: { path: "/data" }
			},
			properties: {
				title: { visible: false },
				legend: { visible: false },
				plotArea: { 
					dataLabel: { visible: "true" },
					colorPalette :  ["#FF6666","#b2b2b2","#F0AB00","#b2b2b2","#4CB6E3","#b2b2b2","#9bca3f","#b2b2b2"]
				},
				interaction: { noninteractiveMode: true }
			},
			feedItems: [
				{ 'uid': "size", 'type': "Measure", 'values': ["value"] },
				{ 'uid': "color", 'type': "Dimension", 'values': ["name"] }
			]
		},
		oParam: {
			path:  "/overview/data",
			type: "get",
			callback: "callbackAjax"
		},
		callbackAjax : function (oModel) {
			//Export 데이터 생성
			var result = oModel.getData().result;
			if(result==undefined||result==null){
				this.setModel();
				return;
			}
			
			this.optmz=false;
			var combstFlag=false, ctrlFlag=false;
			for (var i = 0; i < result.optimizer_mode.length; i++) {
				if(result.optimizer_mode[i].item.search(/Combst/i)>-1){
					if(result.optimizer_mode[i].status.search(/ON/i)>-1){combstFlag=true;}
					else{break;} 
				}else if(result.optimizer_mode[i].item.search(/Ctrl/i)>-1){
					if(result.optimizer_mode[i].status.search(/RUN/i)>-1){ctrlFlag=true;}
					else{break;}
				}
			}
			if(combstFlag&&ctrlFlag){this.optmz=true;}
			
			var data = [];
			for (var stat in result) {
				if(!result[stat]){continue;}
				for (var i = 0; i < result[stat].length; i++) {
					var obj = {};
					obj.type = stat;
					for (var item in result[stat][i]) {
						obj[item] = result[stat][i][item];
					}
					data.push(obj);
				}//i
			}//for: item
			result.data = data;
			this.setModel(oModel);
			
			//ON RUN
			if(this.byId("txtCombst").getText().search(/ON/i)>-1){
				$("div[id$=overview] span[id$=txtCombst]").addClass("modeText").removeClass("modeBlueText");
			}else{
				$("div[id$=overview] span[id$=txtCombst]").addClass("modeBlueText").removeClass("modeText");
			}
			if(this.byId("txtCtrl").getText().search(/RUN/i)>-1){
				$("div[id$=overview] span[id$=txtCtrl]").addClass("modeText").removeClass("modeBlueText");
				$("div[id$=overview] span[id$=txtOpt]").addClass("modeText").removeClass("modeBlueText");
			}else{
				$("div[id$=overview] span[id$=txtCtrl]").addClass("modeBlueText").removeClass("modeText");
				$("div[id$=overview] span[id$=txtOpt]").addClass("modeBlueText").removeClass("modeText");
			}
			this.chartRender(this.chart.sId);
		},//callbackAjax
		onImageLoad: function() { $("div[id$=overview] img.imgCtrlNotReady").attr("title","check Alarm & Event"); },
		onInterval: function() { this.onAjaxCall(this.oParam); },
		onInit : function() {
			window.OverView = this;
			jQuery.sap.log.info("onInit");
			this.chart=this.byId(this.vizFrameSet.id);
			ChartUtil.setVizFrame(this.vizFrameSet, this.getView());
			this.oModel=new JSONModel(this.vizFrameSet.url);
			this.chart.setVizProperties(this.vizFrameSet.properties);
			this.chart.setModel(this.oModel);
			
			var that = this; 
			this.getView().addEventDelegate({
			   onBeforeShow: function(oEvent) {
				   jQuery.sap.log.info("onBeforeShow");
				   that.chart.setBusy(true);
				   that.oParam.url = that.getApiUrl()+that.oParam.path+"/"+that.getSessionValue("default_plant_unit_id");
				   that.onAjaxCall(that.oParam);
			   },
			   onAfterShow: function(oEvent) {
				   jQuery.sap.log.info("onAfterShow");
				   $("div[id$=overview] span[id$=-inner]").css("outline","none");
				   $("div[id$=overview] img").css("outline","none");
				   $("div[id$=overview] tbody td").css("outline","none");
			   }
			});
		},
		afterOpen: function(oEvent) {
			jQuery.sap.log.info("afterOpen");
			$("footer span[id$=-inner]").css("outline","none");
			$("div[id*=dialog] ul li").css("outline","none");
		},
		renderComplete: function(oEvent) {this.render=true;this.chartRender(this.chart.sId);},
		chartRender: function(sId) {
			if(!this.render){return;}
			if(undefined==this.getModel()||null==this.getModel()){return;}

			var target = this.getModel().getData().result.optimum_zone[0].target;
//			target=40;
			//target:40 width:118px top:-259.5px 253;left:190.5px x=249.5 y=194
			var optimumSize = target*2.6-2;
			var circleTop = 175+optimumSize/2;
			var circleLeft = 175-optimumSize/2;
			if(!this[sId]){
				this[sId] = true;
				var html= "<img class='optimumchartZone' style='position:absolute;width:350px;height:350px;left:-1px;top:1px;outline:none;display:none;' src='img/chartZoneOn.png' />" +
						"<img class='optimumchartZone' style='position:absolute;width:350px;height:350px;left:-1px;top:1px;outline:none;display:none;' src='img/chartZoneOff.png' />";
				$("#"+sId+" svg").parent().before(html);
			}
			if(this.optmz){
				$("img[class=optimumchartZone][src*=chartZoneOn]").css("display","initial");
				$("img[class=optimumchartZone][src*=chartZoneOff]").css("display","none");
			}else{
				$("img[class=optimumchartZone][src*=chartZoneOn]").css("display","none");
				$("img[class=optimumchartZone][src*=chartZoneOff]").css("display","initial");
			}
			
			if($("#optimumCircle").length<1){
				var html= "<div id='optimumCircle' style='position:absolute;width:"+optimumSize+"px;height:"+optimumSize+"px;" +
				"background-color:#FFFFFF;opacity:0.4;"+"top:"+-circleTop+"px;left:"+circleLeft+"px;border-radius:50%;' />"+
				"<div id='optimumBorder' style='position:absolute;width:"+optimumSize+"px;height:"+optimumSize+"px;" +
				"top:"+(-circleTop-2)+"px;left:"+(circleLeft-2)+"px;border-radius:50%;border:2px dashed;' />";
				$("img.optimumCross").after(html);
			}else{
				$("#optimumCircle").css("width",optimumSize+"px").css("height",optimumSize+"px")
				.css("top",-circleTop+"px").css("left",circleLeft+"px");
				$("#optimumBorder").css("width",optimumSize+"px").css("height",optimumSize+"px")
				.css("top",(-circleTop-2)+"px").css("left",(circleLeft-2)+"px");
			}
			
			var arrPieSize = [];
			var arrImpact = [];
			var data = this.getModel().getData().result.data;
			for (var i = 0; i < data.length; i++) {
				if(data[i]["type"]=="optimum_zone") {
					if(data[i]["item"].search(/TEMP/i)>-1) {
						arrPieSize[7]=data[i]["before"];	arrPieSize[0]=data[i]["after"];
						arrImpact[7]="";					arrImpact[0]=data[i]["impact"]+data[i]["unit"];
					}else if(data[i]["item"].search(/NOx/i)>-1) {
						arrPieSize[1]=data[i]["before"];	arrPieSize[2]=data[i]["after"];
						arrImpact[1]="";					arrImpact[2]=data[i]["impact"]+data[i]["unit"];
					}else if(data[i]["item"].search(/CO/i)>-1) {
						arrPieSize[3]=data[i]["before"];	arrPieSize[4]=data[i]["after"];
						arrImpact[3]="";					arrImpact[4]=data[i]["impact"]+data[i]["unit"];
					}else if(data[i]["item"].search(/O2/i)>-1) {
						arrPieSize[5]=data[i]["before"];	arrPieSize[6]=data[i]["after"];
						arrImpact[5]="";					arrImpact[6]=data[i]["impact"]+data[i]["unit"];
					}
				}
			}//i
			
//			arrPieSize = [10,20,30,40,50,60,70,80];
			var that = this;
			var baseNumber = 80;
			$("#"+sId+" .v-datapoint-group path").each(function(idx) {
				//1. 레이블 값 수정
				for (var i = 0; i < arrImpact.length; i++) {
					$("#"+sId+" .v-datalabel-group text").eq(idx).html(arrImpact[idx]);
				}
				
				//2. pie 크기 수정
				var d = $(this).attr("d");
				var arrD = d.split("A");
				var arrAL = arrD[1].split("L");
				var arrA = arrAL[0].split(" 0 0,1 ");
				if(arrA.length==1){arrA = arrAL[0].split(" 0 0 1 ");}
				var arrPath = [];
				arrPath[0] = "M";
				arrPath[1] = arrD[0].slice(arrD[0].indexOf("M")+1,arrD[0].indexOf(","));
				arrPath[2] = ",";
				arrPath[3] = arrD[0].slice(arrD[0].indexOf(",")+1);
				arrPath[4] = "A";
				arrPath[5] = arrA[0];
				arrPath[6] = " 0 0,1 ";
				arrPath[7] = arrA[1].slice(0,arrA[1].indexOf(","));
				arrPath[8] = ",";
				arrPath[9] = arrA[1].slice(arrA[1].indexOf(",")+1);
				arrPath[10] = "L";
				arrPath[11] = arrAL[1];
				
				var sqrt2 = Math.sqrt(2);
				var ratio = arrPieSize[idx]/baseNumber;
				var pieLength=106.26;
				//길이 좌표로 작성
				if(idx==0){
					arrPath[3] = "-"+String(ratio*pieLength);
					arrPath[7] = String(Math.abs(arrPath[3])/sqrt2);
					arrPath[9] = "-"+arrPath[7];
				}else if(idx==1){
					arrPath[7] = String(ratio*pieLength);
					arrPath[1] = String(arrPath[7]/sqrt2);
					arrPath[3] = "-"+arrPath[1];
				}else if(idx==2){
					arrPath[1] = String(ratio*pieLength);
					arrPath[7] = String(Math.abs(arrPath[1])/sqrt2);
					arrPath[9] = arrPath[7];
				}else if(idx==3){
					arrPath[9] = String(ratio*pieLength);
					arrPath[1] = String(Math.abs(arrPath[9])/sqrt2);
					arrPath[3] = arrPath[1];
				}else if(idx==4){
					arrPath[3] = String(ratio*pieLength);
					arrPath[9] = String(arrPath[3]/sqrt2);
					arrPath[7] = "-"+arrPath[9];
				}else if(idx==5){
					arrPath[7] = "-"+String(ratio*pieLength);
					arrPath[3] = String(Math.abs(arrPath[7])/sqrt2);
					arrPath[1] = "-"+arrPath[3];
				}else if(idx==6){
					arrPath[1] = "-"+String(ratio*pieLength);
					arrPath[7] = String(arrPath[1]/sqrt2);
					arrPath[9] = arrPath[7];
				}else if(idx==7){
					arrPath[9] = "-"+String(ratio*pieLength);
					arrPath[1] = String(arrPath[9]/sqrt2);
					arrPath[3] = arrPath[1];
				}
				
				d="";
				for (var i = 0; i < arrPath.length; i++) { d+=arrPath[i]; }
				$(this).attr("d", d);
				
				//3. 레이블 위치 수정
				var arrBaseTrans = [20,30,60,80,100,120];
				var arrTrans = [];
				
				//in circle
				if(ratio<(target/100+0.1)){ratio=(target/100+0.1);}
				if(ratio>1){ratio=ratio-0.4;}
				
				ratio = 1-ratio;
				if(idx==0){
					arrTrans[0] = 186.52526359927018-arrBaseTrans[1]*ratio;
					arrTrans[1] = 54.9199889191182+arrBaseTrans[4]*ratio;
				}else if(idx==2){
					arrTrans[0] = 264.08001108088183-arrBaseTrans[4]*ratio;
					arrTrans[1] = 207.69713859927018-arrBaseTrans[1]*ratio;
				}else if(idx==4){
					arrTrans[0] = 101.13098640072984+arrBaseTrans[1]*ratio;
					arrTrans[1] = 275.08001108088183-arrBaseTrans[4]*ratio;
				}else if(idx==6){
					arrTrans[0] = 23.57623891911821+arrBaseTrans[4]*ratio;
					arrTrans[1] = 122.3028614007298+arrBaseTrans[1]*ratio;
				}
				if(idx%2==0){
					$("#"+sId+" .v-datalabel-group g").eq(idx).attr("transform", "translate("+arrTrans.toString()+")");
					$("#"+sId+" .v-datalabel-group g").eq(idx).attr("fill", "#7ab800");
				}
			});
			this.chart.setBusy(false);
		},//chartRendering
		onPressChart: function(oEvent) {},
		formatStatus : function (status) { return null==status||undefined==status||status.search("required")>-1? "Error" : "Success"; },
		tableOptimizerClick: function(oEvent){
			//role check
			var role = this.getSessionValue("role_id");
			if(role.search(/AD/i)<0 && role.search(/OP/i)<0 && role.search(/CE/i)<0){ return; }
			
			if (!this.oOptimizerDialog) {
				this.oOptimizerDialog = sap.ui.xmlfragment("dhi.optimizer.fragment.OverViewOptimizer", this);
				this.getView().addDependent(this.oOptimizerDialog);
				this.oOptimizerDialog.getContent()[2].setVisible(false);
				this.oOptimizerDialog.getContent()[3].setVisible(false);
				this.oOptimizerDialog.getContent()[1].sId="btnOverviewOptimizer";
				this.oOptimizerDialog.getContent()[5].sId="btnOverviewController";
				this.oOptimizerDialog.getContent()[7].sId="btnOverviewPSO";
			}
			this.oOperationParam.url = this.getApiUrl()+this.oOperationParam.path;
			this.onAjaxCall(this.oOperationParam);
			this.oOptimizerDialog.open();
		},
		onCloseDialog: function(oEvent){
			if(oEvent.getSource().oParent.sId.search(/dialog/i)>-1) { oEvent.getSource().oParent.close(); }
			else { oEvent.getSource().oParent.oParent.close(); }
		},
		selectionChange : function(oEvent) {
			if (!this.oConfirmDialog) {
				this.oConfirmDialog = sap.ui.xmlfragment("dhi.optimizer.fragment.OverViewConfirm", this);
				this.getView().addDependent(this.oConfirmDialog);
			}
			//선택 전의 값으로 노출을 유지하고 선택 후의 값을 저장한다
			for (var i = 0; i < this.arrId.length; i++) {
//				jQuery.sap.log.info("selectionChange() arrId[i]: "+this.arrId[i]+", sId: "+oEvent.getSource().sId
//						+", arrKey[i]: "+this.arrKey[i]+", getSelectedKey(): "+oEvent.getSource().getSelectedKey());
				if(this.arrId[i]==oEvent.getSource().sId){
					this.selectedKey	=	oEvent.getSource().getSelectedKey();
					this.selectedId		=	oEvent.getSource().sId;
					this.fgmtBtn		=	oEvent.getSource();
					this.oConfirmDialog.getCustomHeader().getContentMiddle()[0].setText(this.arrTitle[i]);
					oEvent.getSource().setSelectedKey(this.arrKey[i]);
					break;
				}
			}
			
			this.oConfirmDialog.getContent()[1].setText(oEvent.getParameter("item").getText());
			this.oConfirmDialog.openBy(oEvent.getSource().oParent.getContent()[1]);
		},
		onYes: function(oEvent) {
			//저장된 값을 노출한다
			for (var i = 0; i < this.arrId.length; i++) {
//				jQuery.sap.log.info(this.getView().byId(this.vizFrameSet.id).getModel().getData());("onYes() arrId[i]: "+this.arrId[i]+", selectedId: "+this.selectedId
//						+", arrKey[i]: "+this.arrKey[i]+", selectedKey: "+this.selectedKey);
				if(this.arrId[i]==this.selectedId){
					this.arrKey[i]=this.selectedKey;
					this.oOperationPostParam.url = this.getApiUrl()+this.oOperationPostParam.path;
					this.oOperationPostParam.data.item = this.arrPostTitle[i];
					this.oOperationPostParam.data.status = this.selectedKey;
					this.onAjaxCall(this.oOperationPostParam);
					break;
				}
			}
			this.fgmtBtn.setSelectedKey(this.selectedKey);
			this.oConfirmDialog.close();
		},
		createColumnConfig: function() {
			var obj={};
			var data=this.getModel().getData().result.data;
			for (var i = 0; i < data.length; i++) { for(var aItem in data[i]){ obj[aItem]=""; } }
			
			var arr=[];
			for (var item in obj) {
				if("type"==item||"item"==item){
					arr.push({label:item,property:item,textAlign:"center",type:'String',width:17});
				}else{
					arr.push({label:item,property:item,textAlign:"center",type:'String',width:10});
				}
			}
			return arr;
		},
		onPressExport: function(oEvent) {
			this.onExport(this.createColumnConfig(), this.getModel().getProperty("/result/data"),"overView");
		},
		oOperationParam: {
			path:  "/operation/data",
			type: "get",
			callback: "callbackOperationAjax"
		},
		oOperationPostParam: {
			path:  "/operation/data",
			data: {item: "", status: ""},
			type: "post",
			callback: "callbackOperationPostAjax"
		},
		callbackOperationAjax: function(oModel) {
			var result = oModel.getData().result;
			this.setModel(oModel,"operation");
			if(!result||!result.optimize_mode){return;}

			this.arrKey = [];
			this.arrId = [];
			this.arrTitle = [];
			this.arrPostTitle = [];
			var data = result.optimize_mode;
			for (var i = 0; i < data.length; i++) {
				if(data[i].item.search(/pso/i)>-1) {
					this.arrId.push("btnOverviewPSO");
					this.arrTitle.push("Optimization Mode");
				} else if(data[i].item.search(/optimizer/i)>-1) {
					this.arrId.push("btnOverviewOptimizer");
					this.arrTitle.push("Combustion Optimizer");
				} else if(data[i].item.search(/learning/i)>-1) {
					this.arrId.push("btnOperationLearing");
					this.arrTitle.push(data[i].item);
				} else if(data[i].item.search(/control/i)>-1) {
					this.arrId.push("btnOverviewController");
					this.arrTitle.push("Control Mode");
				}
				this.arrKey.push(data[i].status);
				this.arrPostTitle.push(data[i].item);
			}
		},
		callbackOperationPostAjax: function(oModel){
			this.onAjaxCall(this.oOperationParam);
			this.oParam.url = this.getApiUrl()+this.oParam.path+"/"+this.getSessionValue("default_plant_unit_id");
			this.onAjaxCall(this.oParam);
		},
		onUnitChange: function(){
			this.chart.setBusy(true);
			this.oParam.url = this.getApiUrl()+this.oParam.path+"/"+this.getSessionValue("default_plant_unit_id");
			this.onAjaxCall(this.oParam);
		}
	});

}, /* bExport= */ true);