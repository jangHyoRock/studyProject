sap.ui.define([
		"dhi/common/controller/BaseController",
		"dhi/common/controller/Left.controller",
		"dhi/common/util/Formatter"
	], function (BaseController, LeftController, Formatter) {
		"use strict";
	
	return BaseController.extend("dhi.common.controller.Index", {
		onInit : function () {
			sap.ui.core.BusyIndicator.show(0);
			
			var oParam = {
				url: "/user/menu/info",
				data: {system_id: this.getManifestEntry("/sap.ui5/config/systemId"), role_id: this.getSessionValue("role_id")},
				callback: "callbackAjaxMenuInfo"
			};
			this.onAjaxCall(oParam);
			
			this.getRouter().attachBypassed(function(oEvent) {
				var sHash = oEvent.getParameter("hash");
				jQuery.sap.log.error("Sorry, but the hash '" + sHash + "' is invalid.", "The resource was not found.");
			});
		},
		
		// 메뉴 목록 콜백
		callbackAjaxMenuInfo : function (oModel) {
			var oResult = oModel.getData().result;
			
			//console.log(JSON.stringify(oResult, null, 2));
			//console.log(this.getSessionValue("systemLink"));
			//console.log(this.getManifestEntry("/sap.ui5/config/systemId"));
			
        	// Temp LHK  2019-11-20 Overview2 사용 안 함
			//if (this.getManifestEntry("/sap.ui5/config/systemId") == "sbos")
			//{
        	//    var oOverview2 = {
        	//        "menu_id": "Overview2",
        	//        "menu_name": "Overview2"
        	//    };
        	//
        	//    oResult.splice(1, 0, oOverview2);
			//}

        	//console.log(JSON.stringify(oResult, null, 2));
        	
			var _self = this;
			var oRouter = this.getRouter();
			
			oResult.forEach(function(oMenuInfo, index) {
				var _rootView = _self.getOwnerComponent().getAggregation("rootControl").getId();
				var _menuId = Formatter.formatFirstLowerCase(oMenuInfo.menu_id);
				
				if(index > 0) {
					if(oMenuInfo.sub_menu) {
						oMenuInfo.sub_menu.forEach(function(item) {
							var _subMenuId = _menuId + "." + item.menu_id;
							oRouter.getTargets().addTarget(item.menu_id, {viewName: _subMenuId, viewLevel: index+1, viewId: item.menu_id, rootView: _rootView});
							oRouter.addRoute({name: _subMenuId, pattern: _menuId + "/" + item.menu_id, target: item.menu_id});
						});
					} else {
						oRouter.getTargets().addTarget(_menuId, {viewName: oMenuInfo.menu_id, viewLevel: index+1, viewId: _menuId, rootView: _rootView});
						oRouter.addRoute({name: _menuId, pattern: _menuId, target: _menuId});
					}
				}
			});
			
			oRouter.initialize();
			LeftController.prototype.onMenuSetting(oResult);
		}
	});

}, /* bExport= */ true);