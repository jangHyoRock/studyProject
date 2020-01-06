sap.ui.define(["dhi/optimizer/controller/BaseController"], function (BaseController) { "use strict";
	
	return BaseController.extend("dhi.optimizer.controller.algorithm.OutputController", {
		oParam: {
			path:  "/algorithm/output_controller/config",
			type: "get",
			callback: "callbackAjax"
		}, 
		oPostParam: {
			path:  "/algorithm/output_controller/config",
			data: {},
			type: "post",
			callback: "callbackPostAjax"
		},
		callbackAjax: function(oModel) {
			this.setModel(oModel);
		},
		callbackPostAjax: function(oModel){
			this.onAjaxCall(this.oParam);
			if(oModel.getData().code=200){ this.showMsgToast("Saved"); }
		},
		onInit : function () {
			this.setModel(new sap.ui.model.json.JSONModel({"left":"230px","right":"160px","box":"450px"}),"width");
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
				   $("div[id$=OutputController] span[id$=-inner]").css("outline","none");
			   }
			});
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