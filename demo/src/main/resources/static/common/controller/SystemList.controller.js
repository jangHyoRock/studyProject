sap.ui.define([
	"dhi/common/controller/BaseController",
	'sap/m/MessageBox',
	'sap/ui/model/json/JSONModel'
	], function (BaseController, MessageBox, JSONModel) {
		"use strict";
		
		return BaseController.extend("dhi.common.controller.SystemList", {
			onInit : function () {
				var that = this;
				this.getView().addEventDelegate({
				   onBeforeShow: function(oEvent) {
					   if(!that.getSessionValue("user_id")) {
						   that.getRouter().navTo("login");
							return;
						} else {
							var oParam = {
								url: "/user/system/info/" + that.getSessionValue("role_id") + "/" + that.getSessionValue("user_id"),
								callback: "callbackAjaxSystemList"
							};
							that.onAjaxCall(oParam);
						}
				   }
				});
			},
			
			// 시스템 목록 콜백
			callbackAjaxSystemList : function (oModel) {
				var oResult = oModel.getData().result;
				jQuery.sap.log.info('systemList: ' + JSON.stringify(oResult));
				
				var _systemSolution = jQuery('.systemSolution');
				_systemSolution.html("");
				_systemSolution.append("<ul />");
				var _ul = _systemSolution.find('ul');
				var _systemLink = [];
				var _self = this;
				
				oResult.forEach(function(oSystemInfo, index) {
					var _class = '';
					if (index != 0 && index % 3 == 0) {
						_class = ' clearBoth marginTop30';
					} else if (index != 0 && index / 3 > 1) {
						_class = ' marginTop50';
					}
					
					var _li = '<li class="solutionList'+ _class +'">'
						+'<a href="'+ oSystemInfo.system_id +'/index.html">'
						+'	<div class="systemMenuIcon"><img src="common/img/systemlogo_'+ oSystemInfo.system_id +'.png" /></div>'
						+'	<div class="systemMenuTxt">'+ oSystemInfo.system_name +'</div>'
						+'	<div class="systemMenuBg"></div>'
						+'</a>'
						+'</li>';
					_ul.append(_li);
					
					_systemLink.push({
						"system_id" : oSystemInfo.system_id,
						"system_name" : oSystemInfo.system_name
					});					
					
					_self.setSessionValue(oSystemInfo.system_id, JSON.stringify({
						"default_plant_unit_id" : oSystemInfo.default_plant_unit_id,
						"default_plant_unit_ip" : oSystemInfo.default_plant_unit_ip,
						"default_plant_unit_port" : oSystemInfo.default_plant_unit_port
					}));
				});
				
				this.setSessionValue("systemLink", JSON.stringify(_systemLink));
			}
		});
	}
);