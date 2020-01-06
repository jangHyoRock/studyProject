sap.ui.define(["dhi/optimizer/controller/BaseController", "dhi/common/util/Formatter", "dhi/common/util/ChartUtil"
	], function (BaseController, Formatter, ChartUtil) {
		"use strict";
	
	return BaseController.extend("dhi.optimizer.controller.algorithm.SolutionStatus", {
		vizFrameSet: {
			id: "solutionStatusChart",
			type: 'column',
			properties: {
				plotArea: { dataLabel: { visible: true }, colorPalette: ["#b2b2b2"] },
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
		oParam : {
			path: "/algorithm/solution_status/",
			callback: "callbackAjax"
		},
		onInit : function () {
			var that = this;
			ChartUtil.setVizFrame(this.vizFrameSet, this);
			this.byId(this.vizFrameSet.id).setVizScales([{'feed':'valueAxis', 'max':100}], {replace: true});
			this.getView().addEventDelegate({
			   onBeforeShow: function(oEvent) {
				   that.onUnitChange();
			   },
			   onAfterShow: function(oEvent) {
			   }
			});
			
			this.byId("solutionOptimizerMVstatusTable").addEventDelegate({
				onAfterRendering: function(e) {
					$("div[id$=solutionOptimizerMVstatusTable] tbody tr td.sapUiTableTd:nth-last-child(1)").each(function (idx) {
						if(idx)	{ $(this).remove(); }
						else	{ $(this).attr("rowspan","9"); }
					});
				}
			});
		},
		dateFormat: function (s) { if(!s){return"";} return Formatter.getDateformat(new Date(s),true); },
		formatStatusText : function (s) { return null==s||undefined==s||s==1? "ON" : "N ON"; },
		formatStatus : function (s) { return null==s||undefined==s||s==1? "Error" : "Success"; },
		formatNoneStatus : function (s) { return null==s||undefined==s||s==0? "Success" : "None"; },
		callbackAjax: function (oModel) {
			this.setModel(oModel);
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
							$("[id$=SolutionStatus--solutionOptimizerMVstatusTable-table] tbody tr:nth-child("+(i-rowspan+1)+") td:first-child").attr("rowspan",rowspan);
						}
						rowspan=1;
						group=mv.mv_status[i].group;
					}else{
						$("[id$=SolutionStatus--solutionOptimizerMVstatusTable-table] tbody tr:nth-child("+(i+1)+") td:first-child").css("display","none");
						rowspan++;
					}
				}//for:i
				if(rowspan>1){
					$("[id$=SolutionStatus--solutionOptimizerMVstatusTable-table] tbody tr:nth-child("+(rows.length-rowspan+1)+") td:first-child").attr("rowspan",rowspan);
				}
			}, 100);
		},
		renderComplete: function(oEvent) {
			var colorArr = ["#9bca3f","#4cb6e3","#ff6666","#f0ab00"]; 
			$("div[id$=SolutionStatus] g.v-datapoint-group g:nth-last-child(1) g rect").each(function (idx) { $(this).css("fill",colorArr[idx]); });
			if(!this.getModel()){return;}
			
			var unitArr = this.getModel().getProperty("/result/optimization_performance_overview");
			$("div[id$=SolutionStatus] svg text[item^=unit]").each(function (idx) {
				$(this).html("("+unitArr[idx].unit+")");
				$(this).attr("x", 101*idx+50.5-$(this)[0].getBoundingClientRect().width/2);
			});
			
			$("div[id$=SolutionStatus] svg g.v-datalabel-group text").each(function (idx) {
				var transY = $(this).parent().attr("transform").replace("translate(","").replace(")","").split(",")[1];
				var gap = 100.8;
				if(idx<4){
					$(this).html(unitArr[idx].before);
					$(this).parent().attr("transform", "translate("+(12.5+gap*idx+18.15-$(this)[0].getBoundingClientRect().width/2)+","+transY+")");
				}else{
					idx-=4;
					$(this).html(unitArr[idx].after);
					$(this).parent().attr("transform", "translate("+(53+gap*idx+18.15-$(this)[0].getBoundingClientRect().width/2)+","+transY+")");
				}
			});
		},
		setChartBusy: function(b) { this.chart.setBusy(b); this.chartSlider.setBusy(b); },
		onInterval: function () { this.onUnitChange(); },
		onUnitChange: function() {
			this.oParam.url = this.getApiUrl()+this.oParam.path+this.getSessionValue("default_plant_unit_id");
			this.onAjaxCall(this.oParam);
		}
	});
}, /* bExport= */ true);