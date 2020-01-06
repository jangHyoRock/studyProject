sap.ui.define([
		"dhi/common/controller/Left.controller",
		"sap/ui/model/json/JSONModel"
	], function (LeftController, JSONModel) {
		"use strict";
	
	return LeftController.extend("dhi.optimizer.controller.Left", {
		
		/**
		 * 초기화 함수
		 * Call from {dhi.common.controller.Left.controller}
		 */
		onInitLeft : function () {
			window.left.isViewSysInfo = false;
			
			var oParam = {
				url: "/common/left_menu/info/" + this.getManifestEntry("/sap.ui5/config/systemId"),
				callback: "callbackAjaxLeftMenuInfo"
			};
			this.onAjaxCall(oParam);
			
			// AD or CE 권한 사용자에게만 System 정보 표출
			if(this.getSessionValue("role_id") == 'AD' || this.getSessionValue("role_id") == 'CE') {
				window.left.isViewSysInfo = true;
				this.byId('tblSystemInfo').setVisible(true);
				this.setModel(new JSONModel({}), "systemInfo");
			}
		},

		// Left 메뉴 정보 콜백
		callbackAjaxLeftMenuInfo : function (oModel) {
			var oResult = oModel.getData().result;
			oResult.company[0].plant.push({plant_unit_id:"SS001",plant_unit_name:"test1",plant_unit_ip: "localhost",plant_unit_port: "9091"});
			this.setModel(new JSONModel(oResult), "plantUnits");

			// Unit 선택
			var defaultPlantUnitId = this.getSessionValue("default_plant_unit_id");
			var leftList = this.byId("leftList").getItems();
			var breakFlag = false;
			for (var i = 0; i < leftList.length; i++) {
				var itemList = leftList[i].getContent()[0].getContent()[0].getItems();
				for (var j = 0; j < itemList.length; j++) {
					if(itemList[j].getInfo() == defaultPlantUnitId){
						itemList[j].setSelected(true);
						breakFlag = true; break;
					}
				}
				if(breakFlag){break;}
			}

			var aUnitStatus = window.left.v.getModel("plantUnitsStatus");
			if(!aUnitStatus){return;}
			aUnitStatus = aUnitStatus.getData();
			var oStatus = {};
			for (var i = 0; i < aUnitStatus.length; i++) {
				oStatus[aUnitStatus[i].plant_unit_id] = aUnitStatus[i].status;
			}

			oResult.company.forEach(function(oCompany) {
				oCompany.plant.forEach(function(oPlant) {
					oPlant.status = oStatus[oPlant.plant_unit_id];
				});
			});

			this.setModel(new JSONModel(oResult), "plantUnits");
		},

		// system callBack
		onSystemInform : function (oModel) {
			var result = oModel.getData().result;
			if(result==undefined||result==null){ return; }
			if (window.left.isViewSysInfo) {
				result.forEach(function(oSysInfo) {
					oSysInfo.item_name = window.left.v.getResourceBundleCommon().getText("left.sysinfo." + oSysInfo.item);
				});
				window.left.v.getModel("systemInfo").setData(result);
			}
		},

		// Unit item 선택
		onUnitItemPressed : function(oEvent) {
			var oUnitInof = this.getModel("plantUnits").getProperty(oEvent.getParameters().listItem.getBindingContextPath());

			this.setSessionValue(this.getManifestEntry("/sap.ui5/config/systemId"), JSON.stringify({
				"default_plant_unit_id" : oUnitInof.plant_unit_id,
				"default_plant_unit_ip" : oUnitInof.plant_unit_ip,
				"default_plant_unit_port" : oUnitInof.plant_unit_port
			}));

			sap.ui.getCore().byId("__component0---" + window.hd.selSubMenuId).getController().onUnitChange();
		},
		
		// unit 상태 변경(Footer에서 호출)
		onPlantUnitStatus : function(aUnitStatus) {
			if(!aUnitStatus){return;}
			window.left.v.setModel(new JSONModel(aUnitStatus), "plantUnitsStatus");

			var oStatus = {};
			for (var i = 0; i < aUnitStatus.length; i++) {
				oStatus[aUnitStatus[i].plant_unit_id] = aUnitStatus[i].status;
			}

			var oModel = window.left.v.getModel("plantUnits");
			if(!oModel){return;}

			var oData = oModel.getData();

			oData.company.forEach(function(oCompany) {
				oCompany.plant.forEach(function(oPlant) {
					oPlant.status = oStatus[oPlant.plant_unit_id];
				});
			});
			oModel.setData(oData);

			window.left.v.setModel(oModel, "plantUnits");
		}
	});

}, /* bExport= */ true);