sap.ui.define([
		"dhi/optimizer/controller/BaseController",
		"sap/ui/model/json/JSONModel",
		"dhi/common/util/Formatter"
	], function (BaseController, JSONModel, Formatter) {
		"use strict";
	
	return BaseController.extend("dhi.optimizer.controller.Footer", {
		oParam: {
			path: "/common/data/",
			callback: "callbackAjax"
		},
		callbackAjax: function(oModel) {
			var result = oModel.getData().result;
			if(result==undefined||result==null){ this.setModel(); return; }
			var data = [];
			result.column = {};
			for (var aItem in result) {
				if("always_display"==aItem){
					for (var i = 0; i < result[aItem].length; i++) {
						var obj = {};
						obj.plant = result[aItem][i].plant;
						for (var j = 0; j < result[aItem][i].data.length; j++) {
							var oItem = result[aItem][i].data[j];
							obj[oItem.item.toLowerCase()] = oItem.current;
							
							if(i==0&&oItem.item.length>5){
								result.column[oItem.item.toLowerCase()+"_unit"] = oItem.item+"\n("+oItem.unit+")";
							}else if(i==0){
								result.column[oItem.item.toLowerCase()+"_unit"] = oItem.item+"("+oItem.unit+")";
							}
						}
						data.push(obj);
					}//for:i
				}else if("current_time"==aItem){
					var d = new Date(result[aItem]);
					$("div[id$=leftView] span.txtFTDay").html((d.getDate()>9?d.getDate():("0"+d.getDate()))+"-"+(d.getMonth()>8?(d.getMonth()+1):"0"+(d.getMonth()+1))+"-"+d.getFullYear());
					$("div[id$=leftView] span.txtFTTime").html(Formatter.formatDigit(d.getHours())+":"+Formatter.formatDigit(d.getMinutes())+":"+Formatter.formatDigit(d.getSeconds()));
				}else if("plant_unit_status"==aItem) {
					// Left Menu 상태 세팅
					dhi.optimizer.controller.Left.prototype.onPlantUnitStatus(result[aItem]);
				}
			}//for:aItem
			result.data = data;
			this.setModel(oModel);
		},
		callbackAjaxSystemInformation: function(oModel) {
			// Left Menu 시스템 정보 세팅 
			dhi.optimizer.controller.Left.prototype.onSystemInform(oModel);
		},
		onInit : function () {
			this.oParam.url = this.oParam.path+this.getManifestEntry("/sap.ui5/config/systemId")+"/"+this.getSessionValue("default_plant_unit_id");
			this.onAjaxCall(this.oParam);
			var _Param = { url: this.getApiUrl()+"/common/system_information", callback: "callbackAjaxSystemInformation" };
			this.onAjaxCall(_Param);
			
			
			this.commonInterval = this.getManifestEntry("/sap.ui5/config/commonInterval");
			this.systemInterval = this.getManifestEntry("/sap.ui5/config/systemInterval");
			var _self = this;
			this.reloadFlag=false;
			setInterval(function(){ _self.onInterval(); }, 1000);
		},
		onInterval : function () {
			var d = new Date();
			if(!this.reloadFlag&&d.getMinutes()==59){this.reloadFlag=true;}
			if(this.reloadFlag&&d.getMinutes()==0&&(d.getHours()==6||d.getHours()==20)){ location.reload(); }
			var second = d.getSeconds();
			if(second % this.commonInterval == 0) {
				this.oParam.url = this.oParam.path+this.getManifestEntry("/sap.ui5/config/systemId")+"/"+this.getSessionValue("default_plant_unit_id");
				this.onAjaxCall(this.oParam);
				// sap.ui.getCore().byId("__component0---" + window.hd.selSubMenuId).getController().onInterval();
			}
			if(second % this.systemInterval == 0) {
				var _Param = { url: this.getApiUrl()+"/common/system_information", callback: "callbackAjaxSystemInformation" };
				this.onAjaxCall(_Param);
			}
		}
	});

}, /* bExport= */ true);