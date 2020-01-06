sap.ui.define([
		"dhi/common/controller/BaseController",
		"sap/ui/model/json/JSONModel",
		"dhi/common/util/Formatter"
	], function (BaseController, JSONModel, Formatter) {
		"use strict";
	
	return BaseController.extend("dhi.common.controller.Left", {
		onInit : function () {
			window.left = {};
			window.left.v = this;

			// 로그인 정보 세팅
			this.setModel(new JSONModel({
				userName: this.getSessionValue("user_name"),
				roleName: this.getSessionValue("role_name")
			}), "user");
			
			// 시스템 콤보박스
			this.setModel(new JSONModel({"list": JSON.parse(this.getSessionValue("systemLink"))}), "systemLink");
			this.setModel(new JSONModel({"systemId" : this.getManifestEntry("/sap.ui5/config/systemId")}));
			
			// 개별 Left 초기화
			this.onInitLeft();
		},
		
		onLogout : function () {
			var oParam = {
				url: "/user/logout",
				type: "delete",
				callback: "callbackAjaxLogout"
			};
			this.onAjaxCall(oParam);
		},
		
		// 로그아웃 콜백
		callbackAjaxLogout : function (oModel) {
			window.sessionStorage.clear();
			window.location.href = "/";
		},
		
		// Panel 확장/축소 여부
		formatExpanded : function(mPlant) {
			var isExpanded = false;
			var default_plant_unit_id = this.getSessionValue("default_plant_unit_id");
			
			mPlant.some(function(oPlantUnit) {
				if(oPlantUnit.plant_unit_id == default_plant_unit_id) {
					isExpanded = true;
				}
				return oPlantUnit.plant_unit_id == default_plant_unit_id;
			});
			
			return isExpanded;
		},
		
		// 상태 표시
		formatStatus : function(status) {
			return !status || status == "Warning" ? "Error" : "Success";
		},
		
		// 메뉴 세팅(item 추가)
		onMenuSetting : function (oResult)
		{
			var oMenu = window.left.v.byId("menuLayout");

			oResult.forEach(function(oMenuInfo, index)
			{
				var oHbox = new sap.m.HBox({
					id: Formatter.formatFirstLowerCase(oMenuInfo.menu_id),
					alignItems: "Center"
					// ,height: window.hd.h
				});

				var oImage = new sap.m.Image({
					id: oHbox.getId() + "_icon",
					src: "img/naviIcon_" + oMenuInfo.menu_id + ".png"
				});
				oImage.addStyleClass("sapUiTinyMarginEnd");

				var oText = new sap.m.Text({
					id: oHbox.getId() + "_txt",
					text: oMenuInfo.menu_name
				});


				oHbox.addItem(oImage);
				oHbox.addItem(oText);
				oMenu.addContent(oHbox);

				if (oMenuInfo.sub_menu) {
					oHbox.attachBrowserEvent("mouseover", function() {
						window.hd.v.onMouseOverMenuItem(oHbox.getId(), oMenuInfo.sub_menu);
					});
					oHbox.attachBrowserEvent("mouseout", function() {
						window.hd.v.onMouseOutMenuItem(oHbox.getId());
					});
				} else {
					oHbox.attachBrowserEvent("click", window.hd.v.onClickMenuItem);
				}
			});

			window.hd.v.onMenuSettingAfter();
		}

	});

}, /* bExport= */ true);