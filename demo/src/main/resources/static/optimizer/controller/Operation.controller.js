sap.ui.define([
		"dhi/optimizer/controller/BaseController"
	], function (BaseController) {
		"use strict";
	
	return BaseController.extend("dhi.optimizer.controller.Operation", {
		oParam: {
			path:  "/operation/data",
			type: "get",
			callback: "callbackAjax"
		},
		oPostParam: {
			path:  "/operation/data",
			data: {item: "", status: ""},
			type: "post",
			callback: "callbackPostAjax"
		},
		callbackAjax: function(oModel) {
			var result = oModel.getData().result;
			if(result==undefined||result==null){ this.setModel(); return; }
			var data = [];
			for (var stat in result) {
				for (var i = 0; i < result[stat].length; i++) {
					var obj = {};
					obj.type = stat;
					for (var item in result[stat][i]) { obj[item] = result[stat][i][item]; }
					data.push(obj);
				}//i
			}//for: item
			result.data = data;
			this.setModel(oModel);

			this.arrKey = [];
			this.arrId = [];
			this.arrTitle = [];
			this.arrPostTitle = [];
			for (var i = 0; i < data.length; i++) {
				if(data[i].item.search(/pso/i)>-1)				{ this.arrId.push("btnOperationPSO"); 			this.arrTitle.push("Optimization Mode"); }
				else if(data[i].item.search(/optimizer/i)>-1)	{ this.arrId.push("btnOperationOptimizer"); 	this.arrTitle.push("Combustion Optimizer"); }
				else if(data[i].item.search(/learning/i)>-1)	{ this.arrId.push("btnOperationLearing"); 		this.arrTitle.push(data[i].item); }
				else if(data[i].item.search(/control/i)>-1)		{ this.arrId.push("btnOperationController"); 	this.arrTitle.push("Control Mode"); }
				this.arrKey.push(data[i].status);
				this.arrPostTitle.push(data[i].item);
			}
		},
		callbackPostAjax: function(oModel){ this.onAjaxCall(this.oParam); },
		onInterval: function() { this.onAjaxCall(this.oParam); },
		onInit : function () {
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
				   $("div[id$=operation] span[id$=-inner]").css("outline","none");
				   $("div[id$=operation] ul li").css("outline","none");
			   }
			});
		},
		onCloseDialog: function(oEvent){
			oEvent.getSource().oParent.close();
		},
		afterOpen: function(oEvent) {
			jQuery.sap.log.info("afterOpen");
			$("footer span[id$=-inner]").css("outline","none");
		},
		selectionChange : function(oEvent) {
			if (!this.oConfirmDialog) {
				this.oConfirmDialog = sap.ui.xmlfragment("dhi.optimizer.fragment.OperationConfirm", this);
				this.getView().addDependent(this.oConfirmDialog);
			}
			//to onYes: 선택 전의 값으로 노출을 유지하고 선택 후의 값을 저장한다
			for (var i = 0; i < this.arrId.length; i++) {
//				jQuery.sap.log.info("selectionChange() arrId[i]: "+this.arrId[i]+", sId: "+oEvent.getSource().sId
//						+", arrKey[i]: "+this.arrKey[i]+", getSelectedKey(): "+oEvent.getSource().getSelectedKey()+", "+oEvent.getSource().sId.search(this.arrId[i]));
				if(oEvent.getSource().sId.search(this.arrId[i])>-1){
					this.selectedKey	=	oEvent.getSource().getSelectedKey();
					this.selectedId		=	this.arrId[i];
					this.fgmtBtn		=	oEvent.getSource();
					this.oConfirmDialog.getCustomHeader().getContentMiddle()[0].setText(this.arrTitle[i]);
					oEvent.getSource().setSelectedKey(this.arrKey[i]);
					break;
				}
			}
			this.oConfirmDialog.getContent()[1].setText(oEvent.getParameter("item").getText());
			this.oConfirmDialog.open();
		},
		onConfirmDialog: function(str, title) {
			if (!this.oConfirmDialog) {
				this.oConfirmDialog = sap.ui.xmlfragment("dhi.optimizer.fragment.OperationConfirm", this);
				this.getView().addDependent(this.oConfirmDialog);
			}
			this.oConfirmDialog.setTitle(title);
			this.oConfirmDialog.getContent()[1].setText(str);
			this.oConfirmDialog.open();
		},
		onYes: function(oEvent) {
			//from selectionChange: 저장된 값을 노출한다
			for (var i = 0; i < this.arrId.length; i++) {
//				jQuery.sap.log.info("onYes() arrId[i]: "+this.arrId[i]+", selectedId: "+this.selectedId
//						+", arrKey[i]: "+this.arrKey[i]+", selectedKey: "+this.selectedKey);
				if(this.arrId[i]==this.selectedId){
					this.arrKey[i]=this.selectedKey;
					this.oPostParam.url = this.getApiUrl()+this.oPostParam.path;
					this.oPostParam.data.item = this.arrPostTitle[i];
					this.oPostParam.data.status = this.selectedKey;
					this.onAjaxCall(this.oPostParam);
					break;
				}
			}
			this.oConfirmDialog.close();
		},
		createColumnConfig: function() {
			return [
				{ label: 'Type', 	property: 'type', 	textAlign: "center", type: 'String', width: 20 },
				{ label: 'Item', 	property: 'item', 	textAlign: "center", type: 'String', width: 30 },
				{ label: 'Status', 	property: 'status', textAlign: "center", type: 'String', width: 20 }
			];
		},
		onPressExport: function(oEvent) {
			this.onExport(this.createColumnConfig(), this.getModel().getProperty("/result/data"), "operation");
		},
		onUnitChange: function() {
			this.oParam.url = this.getApiUrl()+this.oParam.path;
			this.onAjaxCall(this.oParam);
		}
	});

}, /* bExport= */ true);