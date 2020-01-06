sap.ui.define([
		"dhi/common/controller/Header.controller"
	], function (HeaderController) {
		"use strict";
	
	return HeaderController.extend("dhi.optimizer.controller.Header", {
		
		/**
		 * 초기화 함수
		 * Call from {dhi.common.controller.Header.controller}
		 */
		onInitHeader : function() {
			window.hd.mId = "overview"; //메인 메뉴 ID
		},
		
		/**
		 * 메뉴 이동 후 개별 처리
		 * Call from {dhi.common.controller.Header.controller._onRouteMatched}
		 */
		onRouteMatchedAfter: function(oEvent, _subMenuId) {
			
		}
	});

}, /* bExport= */ true);