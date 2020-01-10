sap.ui.define(["dhi/optimizer/controller/BaseController", "dhi/common/util/Formatter", "dhi/common/util/ChartUtil"
	], function (BaseController, Formatter, ChartUtil) {
		"use strict";
	
	return BaseController.extend("dhi.optimizer.controller.algorithm.Status", {
		vizFrameSet: {
			id: "solutionStatusChart",
			type: 'column',
			properties: {
				plotArea: { 
					dataLabel: { 
						visible: true, 	
						renderer : function(e) { 
							e.text = 'ㅤ';
						}
					}, 
					colorPalette: ["#b2b2b2"] 
				},
				valueAxis: { title: { visible: false } },
				categoryAxis: { title: { visible: false } },
				title: { visible: false },
				legend: { visible: false },
				interaction: {
					noninteractiveMode: true,
					hover: { stroke: { visible: false } },
					selectability: { plotStdSelection: false },
					selected: { stroke: { visible: false } }
				}
			},
            dataset: {
				dimensions: [{ name: 'item', value: "{item}" }],
				measures: [
					{name: 'before', value: '{before_rate}' },
					{name: 'after', value: '{after_rate}' }
				],
				data: { path: "/result/optimization_performance_overview" }
			},
			feedItems: [
				{ 'uid': "valueAxis", 'type': "Measure", 'values': ["before","after"] }, 
				{ 'uid': "categoryAxis", 'type': "Dimension", 'values': ["item"] }
			]
        },
        vizFrameSet2: {
			id: "algorithmStatusChart",
			type: 'column',
			properties: {
				plotArea: { 
					dataLabel: { 
						visible: true, 	
						renderer : function(e) { 
							e.text = 'ㅤ'; 
						}
					},
					colorPalette: ["#b2b2b2","#a4b9c6"]
				},
				valueAxis: { title: { visible: false } },
				categoryAxis: { title: { visible: false } },
				title: { visible: false },
				legend: { visible: false },
				interaction: {
					noninteractiveMode: true,
					hover: { stroke: { visible: false } },
					selectability: { plotStdSelection: false },
					selected: { stroke: { visible: false } }
				}
			},
            dataset: {
				dimensions: [{ name: 'item', value: "{item}" }],
				measures: [
					{name: 'before', value: '{before_rate}' },
					{name: 'after_nn', value: '{after_nn_rate}' },
					{name: 'after', value: '{after_rate}' }
				],
				data: { path: "/result/static_optimizer/optimization_performance_prediction" }
			},
			feedItems: [
				{ 'uid': "valueAxis", 'type': "Measure", 'values': ["before","after_nn","after"] }, 
				{ 'uid': "categoryAxis", 'type': "Dimension", 'values': ["item"] }
			]
        },
		oParam : {
			path: "/algorithm/solution_status/",
			callback: "callbackAjax"
		},
		onInit : function () {
			this.setModel(new sap.ui.model.json.JSONModel({key:"s"}),"selectedKey");
			//solution
			ChartUtil.setVizFrame(this.vizFrameSet, this);
			this.byId(this.vizFrameSet.id).setVizScales([{'feed':'valueAxis', 'max':100}], {replace: true});
			this.byId(this.vizFrameSet.id).zoom({direction:"in"});
			
			//algorithm
		//	this.vizFrameSet2 = eval('(' + JSON.stringify(this.vizFrameSet) + ')');
		//	this.vizFrameSet2.id="algorithmStatusChart";
		//	this.vizFrameSet2.dataset.data.path="/result/static_optimizer/optimization_performance_prediction";
			ChartUtil.setVizFrame(this.vizFrameSet2, this);
			this.byId(this.vizFrameSet2.id).setVizScales([{'feed':'valueAxis', 'max':100}], {replace: true});
			
			var that = this;
			this.getView().addEventDelegate({ onBeforeShow: function(oEvent) { that.onUnitChange(); } });
			
			this.byId("solutionOptimizerMVstatusTable").addEventDelegate({
				onAfterRendering: function(e) {
					$("div[id$=solutionOptimizerMVstatusTable] tbody tr td.sapUiTableTd:nth-last-child(1)").each(function (idx) {
						if(idx)	{ $(this).remove(); }
						else	{ $(this).attr("rowspan","9"); }
					});
				}
			});
		},
		selectionChange: function (oEvent) { this.onUnitChange(); },
		dateFormat: function (s) { if(!s){return"";} return Formatter.getDateformat(new Date(s),true); },
		formatStatusText : function (s) { return null==s||undefined==s||s==1? "ON" : "N ON"; },
		formatStatus : function (s) { return null==s||undefined==s||s==1? "Error" : "Success"; },
		formatNoneStatus : function (s) { return null==s||undefined==s||s==0? "Success" : "None"; },
		callbackAjax: function (oModel) {
			this.setModel(oModel,"solution");
			this.byId(this.vizFrameSet.id).setModel(oModel);
			if(!oModel){return;}
			
			var diag = oModel.getProperty("/result/solution_diagnostic");
			var mv = oModel.getProperty("/result/optimizer_mv_status");
			setTimeout(function(){
				for (var i = 2; i < diag.length; i++) {
					var text="";
					if(diag[i].current=="1"){
						$("div[id$=solutionDiagnosticTable] tbody tr:nth-child("+(i+1)+") td.sapUiTableTd:last-child div.sapMObjStatus")
							.attr("class","sapMObjStatus sapMObjStatusError");
						if(i==2){text="ON";}
						else 	{text="BAD";}
					}else{
						$("div[id$=solutionDiagnosticTable] tbody tr:nth-child("+(i+1)+") td.sapUiTableTd:last-child div.sapMObjStatus")
							.attr("class","sapMObjStatus sapMObjStatusSuccess");
						if(i==2){text="N ON";}
						else 	{text="GOOD";}
					}
					$("div[id$=solutionDiagnosticTable] span.sapMObjStatusText").eq(i).html(" "+text);
				}
				
				var html="<div>ALL HOLD</div><div class='";
				if(mv.all_hold_status!="0"){ html+="sapMObjStatusError"; } else	{ html+="sapMObjStatusSuccess"; }
				html+="'><span class='sapMObjStatusIcon'><span data-sap-ui-icon-content='' class='sapUiIcon' style='font-family:SAP-icons;'></span></span></div>";
				html+="<div style='margin-top:30px'>INITIALIZE</div><div class='";
				if(mv.initialize_status!="0"){ html+="sapMObjStatusError"; } else	{ html+="sapMObjStatusSuccess"; }
				html+="'><span class='sapMObjStatusIcon'><span data-sap-ui-icon-content='' class='sapUiIcon' style='font-family:SAP-icons;'></span></span></div>";
				$("div[id$=solutionOptimizerMVstatusTable] tbody tr:first-child td.sapUiTableTd:nth-last-child(1)").html(html);
				
				var rowspan = 1;
				var group = mv.mv_status[0].group;
				for (var i = 1; i < mv.mv_status.length; i++) {
					if(group!=mv.mv_status[i].group){
						if(rowspan>1){
							$("[id$=solutionOptimizerMVstatusTable-table] tbody tr:nth-child("+(i-rowspan+1)+") td:first-child").attr("rowspan",rowspan);
						}
						rowspan=1;
						group=mv.mv_status[i].group;
					}else{
						$("[id$=solutionOptimizerMVstatusTable-table] tbody tr:nth-child("+(i+1)+") td:first-child").css("display","none");
						rowspan++;
					}
				}//for:i
				if(rowspan>1){
					$("[id$=solutionOptimizerMVstatusTable-table] tbody tr:nth-child("+(rows.length-rowspan+1)+") td:first-child").attr("rowspan",rowspan);
				}
			}, 100);
		},
		callbackAjaxAlgorithm: function (oModel) {
			this.setModel(oModel,"algorithm");
			this.byId(this.vizFrameSet2.id).setModel(oModel);
			var nn = oModel.getProperty("/result/nn_model_generator");
			var that=this;
			setTimeout(function(){
				if(nn.seed_model_1_save_time.used){
					$("#"+that.byId("seed_model_1_save_time").sId).css("background","yellow").css("color","black");
				}else{
					$("#"+that.byId("seed_model_1_save_time").sId).css("background","black").css("color","white");
				}
				if(nn.seed_model_2_save_time.used){
					$("#"+that.byId("seed_model_2_save_time").sId).css("background","yellow").css("color","black");
				}else{
					$("#"+that.byId("seed_model_2_save_time").sId).css("background","black").css("color","white");
				}
				if(nn.fruit_model_1_save_time.used){
					$("#"+that.byId("fruit_model_1_save_time").sId).css("background","yellow").css("color","black");
				}else{
					$("#"+that.byId("fruit_model_1_save_time").sId).css("background","black").css("color","white");
				}
				if(nn.fruit_model_2_save_time.used){
					$("#"+that.byId("fruit_model_2_save_time").sId).css("background","yellow").css("color","black");
				}else{
					$("#"+that.byId("fruit_model_2_save_time").sId).css("background","black").css("color","white");
				}
				if(nn.fruit_model_3_save_time.used){
					$("#"+that.byId("fruit_model_3_save_time").sId).css("background","yellow").css("color","black");
				}else{
					$("#"+that.byId("fruit_model_3_save_time").sId).css("background","black").css("color","white");
				}
			},100);
		},
		getWidthOfText: function(txt, font){			
			    // re-use canvas object for better performance
		        var canvas = document.createElement("canvas");
		        canvas.style.cssText="position:absolute;top:-1000px;z-index:-9000;width:0px;height:0px;visibility:hidden"
		        //document.body.appendChild(canvas)
		        	
		        var context = canvas.getContext("2d");
		        context.font = font;
		        var metrics = context.measureText(txt);
		        return metrics.width;
		},
		renderComplete: function(oEvent) {			
			var arrStickTransXStart = [];
			$("div[id$=---Status] g.v-datapoint-group g rect").each(function () {
				var transX = $(this).parent().attr("transform").replace("translate(","").replace(")","").split(",")[0];
				arrStickTransXStart.push(transX);
			});
			
			var stickWidth = 0;
			var arrColor = ["#9bca3f","#4cb6e3","#9932cc","#ff6666","#f0ab00"];
			$("div[id$=---Status] g.v-datapoint-group g:nth-last-child(1) g rect").each(function (idx) {
				$(this).css("fill",arrColor[idx]);
				if(stickWidth == 0) { stickWidth = $(this).attr("width"); }
			});		
			
			if(!this.getModel("solution")){return;}
			
			var that = this;
			var stickWidthOfText;			
			var stickGroupCount = arrColor.length;
			var stickCount = $("div[id$=---Status] g.v-datapoint-group").children().length;
			var stickType = stickCount == 2 ? "solution" : "algorithm";
			
			var unitArr;
			if(stickCount == 2) {
				stickType = "solution"
				unitArr = this.getModel(stickType).getProperty("/result/optimization_performance_overview");				
			}
			else {
				stickType = "algorithm"
				unitArr = this.getModel(stickType).getProperty("/result/static_optimizer/optimization_performance_prediction");
			}
		
			$("div[id$=---Status] svg g.v-datalabel-group text").each(function (idx) {
				var unitIdx = idx % stickGroupCount;
				var unitText;
				switch(stickType) {
					case "solution" :
						if(Math.floor(idx/stickGroupCount) == 0 )
							unitText = unitArr[unitIdx].before;
						else
							unitText = unitArr[unitIdx].after;
						
						$(this).css("font-size","11px");
						stickWidthOfText = that.getWidthOfText(unitText, "bold 11px arial");
						break;
					case "algorithm" :
						if(Math.floor(idx/stickGroupCount) == 0 )
							unitText = unitArr[unitIdx].before;
						else if(Math.floor(idx/stickGroupCount) == 1 )
							unitText = unitArr[unitIdx].after_nn;
						else
							unitText = unitArr[unitIdx].after;
						
						$(this).css("font-size","9px");
						stickWidthOfText = that.getWidthOfText(unitText, "bold 9px arial");
						break;
				}
				
				$(this).html(unitText);
				var transX = parseFloat(arrStickTransXStart[idx]) + (stickWidth - stickWidthOfText) / 2;
				var transY = $(this).parent().attr("transform").replace("translate(","").replace(")","").split(",")[1];				
				$(this).parent().attr("transform", "translate("+transX+","+transY+")");	
			});
			
			var axisXLabelWidth = 0;
			var arrStickLabelXStart = [];
			$("div[id$=---Status] g.v-label-group g rect").each(function () {				
				var labelStartX = $(this).parent().attr("transform").replace("translate(","").replace(")","").split(",")[0];
				if(axisXLabelWidth == 0) {
					axisXLabelWidth = $(this).attr("width");
				}				
				arrStickLabelXStart.push(labelStartX);
			});
			
			$("div[id$=---Status] svg text[item^=unit]").each(function (idx) {
				var unitTextWidth = that.getWidthOfText(unitArr[idx].unit, "12px arial")
				var transX = parseFloat(arrStickLabelXStart[idx]) + (axisXLabelWidth - unitTextWidth) / 2;
				
				$(this).html("("+unitArr[idx].unit+")");
				$(this).attr("x", transX);
			});
		},
		onInterval: function () { this.onUnitChange(); },
		onUnitChange: function() {
			if(this.byId("segment").getSelectedKey()=="a"){
				this.oParam.url = this.getApiUrl()+"/algorithm/process_status/"+this.getSessionValue("default_plant_unit_id");
				this.oParam.callback = "callbackAjaxAlgorithm";
			}else{
				this.oParam.url = this.getApiUrl()+"/algorithm/solution_status/"+this.getSessionValue("default_plant_unit_id");
				this.oParam.callback = "callbackAjax";
			}
			this.onAjaxCall(this.oParam);
		}
	});
}, /* bExport= */ true);