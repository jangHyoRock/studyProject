sap.ui.define(["dhi/optimizer/controller/BaseController"], function (BaseController) { "use strict";
	
	return BaseController.extend("dhi.optimizer.controller.algorithm.PSO", {
		oParam: {
			path:  "/algorithm/pso/config",
			type: "get",
			callback: "callbackAjax"
		},
		oPostParam: {
			path:  "/algorithm/pso/config",
			data: {},
			type: "post",
			callback: "callbackPostAjax"
		},
		callbackAjax: function(oModel) {
			this.setModel(oModel);
			var result=oModel.getData().result;
			this.byId("iptPSOtotalPacticles").setValue(Math.pow(result.particles_count, result.mv_count));

			$("[id$=sgmtPSOprofit]").css("color","#346187").css("background-color","white");
			$("[id$=sgmtPSOemission]").css("color","#346187").css("background-color","white");
			$("[id$=sgmtPSOequipment]").css("color","#346187").css("background-color","white");
			if(result.opt_mode.search(/P/i)>-1){
				$("[id$=sgmtPSOprofit]").css("color","white").css("background-color","#346187");
			}else if(result.opt_mode.search(/E/i)>-1){
				$("[id$=sgmtPSOemission]").css("color","white").css("background-color","#346187");
			}else if(result.opt_mode.search(/S/i)>-1){
				$("[id$=sgmtPSOequipment]").css("color","white").css("background-color","#346187");
			}
			
			if(result.function_o2_avg_boundary_use){
				this.byId("iptAvgBoundary").setEditable(true);
				this.byId("iptAvgPenalty").setEditable(true);
			}else{
				this.byId("iptAvgBoundary").setEditable(false);
				this.byId("iptAvgPenalty").setEditable(false);
			}
		},
		callbackPostAjax: function(oModel){
			this.onAjaxCall(this.oParam);
			if(oModel.getData().code=200){ this.showMsgToast("Saved"); }
		},
		onInit : function () {
			this.setModel(new sap.ui.model.json.JSONModel({"left":"190px","right":"200px"}),"width");
			this.setModel(new sap.ui.model.json.JSONModel({"result":[{"item":"ResilientBackpropagation","key":"ResilientBackpropagation"},{"item":"dummy","key":"dummy"}]}),"select");
			var that = this;
			this.getView().addEventDelegate({
			   onBeforeShow: function(oEvent) {
				   jQuery.sap.log.info("onBeforeShow");
				   that.oParam.url = that.getApiUrl()+that.oParam.path;
				   that.onAjaxCall(that.oParam);
			   },
			   onAfterShow: function(oEvent) {
				   jQuery.sap.log.info("onAfterShow");
				   //focus 방지
				   $("div[id$=PSO] span[id$=-inner]").css("outline","none");
			   }
			});
		},
		onSelect: function(oEvent){
			if(oEvent.getSource().getSelected()){
				this.byId("iptAvgBoundary").setEditable(true);
				this.byId("iptAvgPenalty").setEditable(true);
			}else{
				this.byId("iptAvgBoundary").setEditable(false);
				this.byId("iptAvgPenalty").setEditable(false);
			}
		},
		onPress: function(oEvent){
			this.oPostParam.data = eval('(' + JSON.stringify(this.getModel().getData().result) + ')'); //object copy
			this.oPostParam.url = this.getApiUrl()+this.oPostParam.path;
			this.onAjaxCall(this.oPostParam);
		},
		onUnitChange: function() {
			this.oParam.url = this.getApiUrl()+this.oParam.path;
			this.onAjaxCall(this.oParam);
		}
	});
}, /* bExport= */ true);