sap.ui.define([
    "dhi/common/controller/BaseController",
    "dhi/common/util/Formatter",
    "sap/ui/model/json/JSONModel"
], function (BaseController, Formatter, JSONModel) {
    "use strict";
    
    return BaseController.extend("dhi.common.controller.Header", {
        onInit: function()
        {
            window.hd                = {}               ;
            window.hd.v              = this             ;
            window.hd.w              = this.fnGetWidth(); // 헤더 메뉴 width
            window.hd.h              = "87px"           ; // 헤더 메뉴 height
            window.hd.selMenuId      = null             ; // 선택된 메뉴 ID
            window.hd.selSubMenuId   = null             ; // 선택된 서브메뉴 ID
            window.hd.subMenuPopover = {}               ; // 서브메뉴 팝업

            // 로그인 정보 세팅
            this.setModel(new JSONModel({
                userName: this.getSessionValue("user_name"),
                roleName: this.getSessionValue("role_name")
            }), "user");

            // 시스템 콤보박스
            this.setModel(new JSONModel({"list": JSON.parse(this.getSessionValue("systemLink"))}), "systemLink");
            this.setModel(new JSONModel({"systemId" : this.getManifestEntry("/sap.ui5/config/systemId")}));

            // System 별 헤더 초기화
            this.onInitHeader();
        },
        
        // 메뉴 동적 세팅 완료 후 호툴
        onMenuSettingAfter: function(oEvent)
        {
            this.getRouter().attachRoutePatternMatched(this._onRouteMatched, this);
        },

        fnGetWidth : function()
        {
            return Math.max(
                document.body.scrollWidth           ,
                document.documentElement.scrollWidth,
                document.body.offsetWidth           ,
                document.documentElement.offsetWidth,
                document.documentElement.clientWidth
            ) - 192;
        },
        
        // 메뉴 이동시마다 발생
        _onRouteMatched: function(oEvent)
        {
            sap.ui.core.BusyIndicator.hide();
            
            var sRouteName = oEvent.getParameter("name");
            var _menuId    = sRouteName;
            var _subMenuId = sRouteName;
            
            if (sRouteName.indexOf('.') > -1)
            {
                _menuId    = sRouteName.substring(0, sRouteName.indexOf("."));
                _subMenuId = sRouteName.substring(sRouteName.indexOf(".") + 1);
            }

            // todo 메뉴 선택한 색으로 변경
            var menuList = window.left.v.byId("menuLayout").getContent();
            for (var i = 0; i < menuList.length; i++) {
                menuList[i].removeStyleClass("menuSelection");
            }
            sap.ui.getCore().byId(_menuId).addStyleClass("menuSelection");

            window.hd.selMenuId    = _menuId   ;  // 현재 선택된 메뉴로 변경
            window.hd.selSubMenuId = _subMenuId;
            
            // System 별 개별 처리
            this.onRouteMatchedAfter(oEvent, _subMenuId);
        },

        // 서브 메뉴 클릭 시 호출
        onClickSubMenu : function (oEvent)
        {
            sap.ui.core.BusyIndicator.show(0);
            
            var _id = oEvent.getSource().getId();
            var sId = _id.substr(0, _id.indexOf("."));
            
            window.hd.subMenuPopover[sId].close();
            window.hd.v.getRouter().navTo(_id);
            sap.ui.getCore().byId(sId).removeStyleClass("headerHboxOver");
            sap.ui.getCore().byId(_id).addStyleClass("sapMBtnActive");
            
            window.hd.v.hideBusyIndicator();
        },

        // 하위 메뉴 없는 메뉴에 마우스 오버할 경우 호출
        onMouseOutMenuItem : function (sId) {
            var isHoverMenu = $("#" + sId + ":hover").length == 0 ? false : true;
            var isHoverSubMenu = $("#" + "pop_" + sId + ":hover").length == 0 ? false : true;

            if (!isHoverMenu && !isHoverSubMenu && window.hd.subMenuPopover[sId] && window.hd.subMenuPopover[sId].isOpen()) {
                window.hd.subMenuPopover[sId].close();
            }
        },

        // 하위 메뉴 있는 메뉴에 마우스 오버할 경우 호출
        onMouseOverMenuItem : function (sId, oSubMenu) {

            if (window.hd.subMenuPopover[sId] && window.hd.subMenuPopover[sId].isOpen()) {
                return;
            }

            if (!window.hd.subMenuPopover[sId]) {
                var _xml = '<Popover xmlns="sap.m" id="pop_'+ sId +'" showHeader="false" offsetX="-9" contentWidth="13rem" placement="Right" class="popoverSubMenu">'
                    + '<FlexBox height="'+ (oSubMenu.length*36+10) +'px" direction="Column">';

                oSubMenu.forEach(function(oMenuInfo) {
                    _xml +=	jQuery.sap.formatMessage('<Button id="{0}.{1}" text="{2}" width="13rem" class="popSubMenu" press="onClickSubMenu" type="Transparent" icon="sap-icon://feeder-arrow" />', [sId, oMenuInfo.menu_id, oMenuInfo.menu_name]);
                });

                _xml +=	'</FlexBox></Popover>';

                var oFragment = sap.ui.xmlfragment({fragmentContent : _xml}, window.hd.v);
                window.hd.subMenuPopover[sId] = oFragment;
                window.hd.v.getView().addDependent(oFragment);
                oFragment.attachBrowserEvent("mouseout", function() {
                    window.hd.v.onMouseOutMenuItem(sId);
                });
            }

            var _menu = sap.ui.getCore().byId(sId);

            window.hd.subMenuPopover[sId].openBy(_menu);
        },

        // 하위 메뉴 없는 메뉴 클릭 시 호출
        onClickMenuItem: function (oEvent)
        {
            sap.ui.core.BusyIndicator.show(0);
            window.hd.v.getRouter().navTo(oEvent.currentTarget.id); // 메뉴 이동
            window.hd.v.hideBusyIndicator();
        },

        // 처음 페이지로 이동
        onMain : function ()
        {
            sap.ui.core.BusyIndicator.show(0);
            this.getRouter().navTo(window.hd.mId);
            window.hd.v.hideBusyIndicator();
        },
        
        hideBusyIndicator : function(iDuration)
        {
            var url = window.location.href;
            var _subMenuId = url.substring(url.lastIndexOf("index.html")+12);
            
            if (_subMenuId == '') {
                _subMenuId = window.hd.mId;
            } else if (_subMenuId.indexOf('/') > -1) {
                _subMenuId = _subMenuId.substring(_subMenuId.indexOf("/") + 1);
            }
            
            // 같은 메뉴 클릭 시 바로 창 닫기
            if (_subMenuId == window.hd.selSubMenuId) {
                sap.ui.core.BusyIndicator.hide();
            }
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

        // 시스템 콤보 변경
        onChangeSystem : function(oEvent) {
            window.location.href = "/" + oEvent.getSource().getSelectedKey() + "/index.html";
        }
    });
}, /* bExport= */ true);