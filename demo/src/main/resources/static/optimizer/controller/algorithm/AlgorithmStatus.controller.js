sap.ui.define(["dhi/optimizer/controller/BaseController", "dhi/common/util/Formatter", "dhi/common/util/ChartUtil"
	], function (BaseController, Formatter, ChartUtil) {
		"use strict";
	
	return BaseController.extend("dhi.optimizer.controller.algorithm.AlgorithmStatus", {
		vizFrameSet: {
			id: "algorithmStatusChart",
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
				data: { path: "/result/static_optimizer/optimization_performance_prediction" }
			},
			feedItems: [
				{ 'uid': "valueAxis", 'type': "Measure", 'values': ["before","after"] }, 
				{ 'uid': "categoryAxis", 'type': "Dimension", 'values': ["item"] }
			]
        },
		oParam : {
			path: "/algorithm/process_status/",
			callback: "callbackAjax"
		},
		onInit : function () {
			var that = this;
			this.byId(this.vizFrameSet.id).setVizScales([{'feed':'valueAxis', 'max':100}], {replace: true});
			ChartUtil.setVizFrame(this.vizFrameSet, this);
			this.getView().addEventDelegate({
			   onBeforeShow: function(oEvent) {
				   that.onUnitChange();
			   },
			   onAfterShow: function(oEvent) {
			   }
			});
		},
		dateFormat: function (s) { if(!s){return"";} return Formatter.getDateformat(new Date(s),true); },
		formatStatus : function (s) { return null==s||undefined==s||s==1? "Error" : "Success"; },
		formatStatusText : function (s) { return null==s||undefined==s||s==1? "ON" : "N ON"; },
		callbackAjax: function (oModel) {
			this.setModel(oModel);
			this.byId(this.vizFrameSet.id).setModel(oModel);
		},
		renderComplete: function(oEvent) {
			var colorArr = ["#9bca3f","#4cb6e3","#ff6666","#f0ab00"]; 
			$("div[id$=AlgorithmStatus] g.v-datapoint-group g:nth-last-child(1) g rect").each(function (idx) { $(this).css("fill",colorArr[idx]); });
			if(!this.getModel()){return;}
			
			var unitArr = this.getModel().getProperty("/result/static_optimizer/optimization_performance_prediction");
			$("div[id$=AlgorithmStatus] svg text[item^=unit]").each(function (idx) {
				$(this).html("("+unitArr[idx].unit+")");
				$(this).attr("x", 79*idx+39.5-$(this)[0].getBoundingClientRect().width/2);
			});
			$("div[id$=AlgorithmStatus] svg text[item^=impact]").each(function (idx) {
				$(this).html(unitArr[idx].impact+"%");
				$(this).attr("x", 79*idx+39.5-$(this)[0].getBoundingClientRect().width/2);
			});
			
			$("div[id$=AlgorithmStatus] svg g.v-datalabel-group text").each(function (idx) {
				var transY = $(this).parent().attr("transform").replace("translate(","").replace(")","").split(",")[1];
				var gap = 79.4;
				if(idx<4){
					$(this).html(unitArr[idx].before);
					$(this).parent().attr("transform", "translate("+(9+gap*idx+14.3-$(this)[0].getBoundingClientRect().width/2)+","+transY+")");
				}else{
					idx-=4;
					$(this).html(unitArr[idx].after);
					$(this).parent().attr("transform", "translate("+(41+gap*idx+14.3-$(this)[0].getBoundingClientRect().width/2)+","+transY+")");
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