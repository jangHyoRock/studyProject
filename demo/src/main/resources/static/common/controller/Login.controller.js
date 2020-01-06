sap.ui.define([
	"dhi/common/controller/BaseController",
	'sap/m/MessageBox',
	'sap/ui/model/json/JSONModel',
	"sap/ui/core/ValueState",
	"dhi/common/util/Validator"
	], function (BaseController, MessageBox, JSONModel, ValueState, Validator) {
		"use strict";
	
	return BaseController.extend("dhi.common.controller.Login", {
		onInit : function () { window.c=this; 
			// Validator 등록
			sap.ui.getCore().attachValidationError(function (oEvent) {
				oEvent.getParameter("element").setValueState(ValueState.Error);
			});
			sap.ui.getCore().attachValidationSuccess(function (oEvent) {
				oEvent.getParameter("element").setValueState(ValueState.None);
			});
			
			this.setModel(new JSONModel({id : null}));
		},
		
		onLogin : function () {
			var validator = new Validator();
			if(!validator.validate(this.byId("loginBox"))) {
				return;
			}
			
			sap.ui.core.BusyIndicator.show(0);
			
			var oParam = {
				url: "/user/login",
				type: "post",
				data: {user_id: this.byId("iptId").getValue(), user_password: this.byId("iptPwd").getValue()},
				callback: "callbackAjaxLogin"
			};
			this.onAjaxCall(oParam);
		},
		
		// 로그인 처리 후 콜백
		callbackAjaxLogin : function (oModel) {
			var oResult = oModel.getData().result;
			jQuery.sap.log.info(JSON.stringify(oResult));
			
			var password_update_yn = oResult.password_update_yn || 'N';
			
			window.sessionStorage.clear(); // session reset
			this.setSessionValue("user_id", this.byId("iptId").getValue());
			this.setSessionValue("user_name", oResult.user_name);
			this.setSessionValue("role_id", oResult.role_id);
			this.setSessionValue("role_name", oResult.role_name);
			this.setSessionValue("auth_token", oResult.auth_token);
			
			if(password_update_yn == 'Y') {
				// 패스워드 변 Dialog 열기
				if (!this.oPwdChangeDialog) {
					this.oPwdChangeDialog = sap.ui.xmlfragment("dhi.common.fragment.PwdChange", this);
					this.getView().addDependent(this.oPwdChangeDialog);
				}
				
				var oData = this.getModel().getData();
				oData.pwdChange = '';
				oData.pwdChangeConfirm = '';
				this.getModel().setData(oData);
				
				this.oPwdChangeDialog.open();
			} else {
				this.getRouter().navTo("systemList");
			}
			
			sap.ui.core.BusyIndicator.hide();
		},
		
		// 비밀번호 변경
		onPwdChange: function(oEvent) {
			var validator = new Validator();
			if(!validator.validate(sap.ui.getCore().byId("dlgPwdChange"))) {
				return;
			}
			
			var oData = this.getModel().getData();
			var sId = oData.id;
			var sPwdChange = oData.pwdChange;
			var sPwdChangeConfirm = oData.pwdChangeConfirm;
			
			if(sPwdChange != sPwdChangeConfirm) {
				MessageBox.alert("The password value is different from the password confirm.");
				return;
			} else {
				var oParam = {
					url: "/user/password/update",
					type: "post",
					data: {user_id: sId, user_password: sPwdChange},
					callback: "callbackAjaxChangePwd"
				};
				this.onAjaxCall(oParam);
			}
		},
		
		// 비밀번호 변경 처리 요청 후 콜백
		callbackAjaxChangePwd : function (oModel) {
			this.getRouter().navTo("systemList");
		},
		
		onCloseDialog: function(oEvent) {
			oEvent.getSource().oParent.close();
		}
	});
});