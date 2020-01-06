sap.ui.define(["sap/ui/core/mvc/Controller", "dhi/optimizer/controller/BaseController", "dhi/common/util/DateUtil","sap/viz/ui5/data/FlattenedDataset",
		"sap/suite/ui/microchart/RadialMicroChart","sap/ui/core/routing/History", "sap/ui/Device", "sap/m/MessageToast",
		"sap/ui/model/json/JSONModel", "sap/m/MessageStrip","sap/ui/commons/Area","sap/ui/commons/ImageMap","sap/m/MessageBox"
		], function (Controller, BaseController, DateUtil, FlattenedDataset, RadialMicroChart, History, Device,
				MessageToast, JSONModel, MessageStrip, Area, ImageMap, MessageBox) {
		"use strict";

	return BaseController.extend("dhi.optimizer.controller.CombustionDynamics", {
		/**
		 * combustion dynamics init
		 */
		onInit : function () {
			this.getRouter().getRoute("combustionDynamics").attachPatternMatched(this._onRouteMatched, this);
			this.oPanel = this.byId("hbLoading");
			this.oRadial = this.byId("radChart");
		},
		/**
		 * combustion dynamics 루트 매칭 시 이벤트를 정의한다.
		 * [model binding, create imagemap, default view, navigation fixed]
		 */
		_onRouteMatched: function(oEvent) {
			//binding menu data
			var oModel = new sap.ui.model.json.JSONModel("./chart/heatmap/dynamicsMenu.json");
			this.getView().setModel(oModel,"mData");
			this.oPanel.setBusy(true);
			this.handlePopoverPress(oEvent,"FOT");
	        //진입시 default FOT 설정
		},
		onUnitChange: function(oEvent) {
			//binding menu data
			var oModel = new sap.ui.model.json.JSONModel("./chart/heatmap/dynamicsMenu.json");
			this.getView().setModel(oModel,"mData");
			this.oPanel.setBusy(true);
			this.handlePopoverPress(oEvent,"FOT");
	        //진입시 default FOT 설정
		},
		onExit: function(oEvent){
			if(this._oPopover){
				this._oPopover.destroy();
			}
		},
		/**
		 * combustion dynamics 화면 이미지 영역 클릭 시 메뉴 popover open event
		 */
		onLclick: function(oEvent){
			var _id = this.getView().byId("inv1");
			this._oPopover = sap.ui.xmlfragment("dhi.optimizer.fragment.CombustionDynamics", this);
			this.getView().addDependent(this._oPopover);
			this._oPopover.bindElement("mData>/menu/0");
			this._oPopover.openBy(_id);
		},
		onRclick: function(oEvent){
			var _id = this.getView().byId("inv2");
			this._oPopover = sap.ui.xmlfragment("dhi.optimizer.fragment.CombustionDynamics", this);
			this.getView().addDependent(this._oPopover);
			this._oPopover.bindElement("mData>/menu/1");
			this._oPopover.openBy(_id);
		},
		/**
		 * combustion dynamics 히트맵차트를 렌더링 한다.
		 * @param _s: 타이틀명,
		 */
		handlePopoverPress: function (oEvent,_s) {
			this.oPanel.setBusy(true);
			var chartContainerId = this.getView().createId("chart");
			jQuery('#'+chartContainerId).empty();//initialize image one more
			var _text="";
			if(_s)
				_text = _s;
			else
				_text = oEvent.getSource().getTitle();

			var _url = this.getApiUrl();// data url
			var _plantId = this.getSessionValue("default_plant_unit_id");
			var _this = this;
	        require(["chart/heatmap/dynamicData"], function(previewData) {
				//filtering data
	        	var gData = previewData.getData(_text,_url,_plantId);
				if(!gData){
	        		MessageBox.error("Generate Chart-DATA An ERROR has occurred. PLEASE CONTACT SERVICE TEAM");
	        		return false;
	        	}
	        	_this.setModel(new JSONModel(gData), "info");
		        var rate = _this.getModel("info").getProperty("/cross/fot_rate");
		        _this.oRadial.setPercentage(parseInt(rate));
	        	// Configure the baseUrl for requireJS to find the specific module or file
	        	requirejs.config({
	                baseUrl : "chart/heatmap/core/sources"
	            });
	            require([ "heatmap-bundle" ], function() {
	                //chart option
	                var chartPoperties = {
	                    "Heatmap Module": {
	                        borderColor: "none"
	                    },
	                    legend:{
	                        visible : true,
	                        drawingEffect : "normal"
	                    }
	                };
	                //data set used by the chart
	                var data = gData.cross.data;
	                var ds = new sap.viz.api.data.CrosstableDataset();//data binding
	                ds.data(data);
	                function onTplLoad() {
	                    //create chart after applying template
	                	_this.oPanel.setBusy(false);
	                    try {
	                        var chart = sap.viz.api.core.createViz({
	                            type : "sap.viz.ext.heatmap",
	                            data : ds,
	                            bindings : gData.cross.bindings,
	                            container :jQuery('#'+chartContainerId),
	                            properties : chartPoperties,
	                        });
	                    } catch (err) {
	                    	MessageBox.error(err);
	                    	jQuery.sap.log.error(err);
	                        return;
	                    }
	                    //listen the barData event raised from the extension
	                    chart.on("barData", function(d) {
	                        alert("Data: " + d.join(" = "));
	                    });
	                    $(window).resize(function(){
	                        chart.size({
	                            width: jQuery('#'+chartContainerId).width(),
	                            height: jQuery('#'+chartContainerId).height()
	                        })
	                    });
	                }
	                function onTplFail() {}

	                function onLocaleApplied() {
	                    // set template loadPath
	                    sap.viz.api.env.Resource.path("sap.viz.api.env.Template.loadPaths", ["chart/heatmap/core/sources/sap_viz_ext_heatmap-src/resources/templates"]);
	                    sap.viz.api.env.Template.set("standard", onTplLoad, onTplFail);
	                }

	                var lang, params; //paring language from param
	                if (window.location && window.location.search) {
	                    params = window.location.search.match(/sap-ui-language=(\w*)/);
	                    lang = params && params[1] ? params[1] : lang;
	                }

	                if (lang) {
	                    sap.viz.api.env.Resource.path("sap.viz.api.env.Language.loadPaths", ["chart/heatmap/core/sources/sap_viz_ext_heatmap-src/resources/languages"]);
	                    sap.viz.api.env.Language.set(lang, onLocaleApplied);
	                } else {
	                    onLocaleApplied();
	                }
	            });
	            _this.customDataConfig(_text, gData.cross);//generate export data
	        });//render end
	        this.setModel(new JSONModel({
				titleNm : _text
			}),"ttData");
			if(_text=="Fireball"||_text=="FOT"){
				$("svg[name='dynamicsLong']").css("visibility","visible");
				$("svg[name='dynamicsShort']").css("visibility","hidden");
			}else{
				$("svg[name='dynamicsLong']").css("visibility","hidden");
				$("svg[name='dynamicsShort']").css("visibility","visible");
			}
			if(this._oPopover){
				this._oPopover.close();
			}
		},
		/**
		 * combustion dynamics 엑셀 파일 export 데이터를 커스터마이징 한다.
		 * @param _t : part명, _obj: 전송받은 좌표데이터
		 */
		customDataConfig: function(_t, _obj) {
			if(_obj){
				var x, y, z;
				var d = [];
				x = _obj.data.analysisAxis[0].data[0].values;
				y = _obj.data.analysisAxis[0].data[1].values;
				z = _obj.data.measureValuesGroup[0].data[0].values[0];
				for(var idx=0; idx<x.length;idx++){
					d[idx] = {"part": _t, "xCord": x[idx], "yCord": y[idx], "value": z[idx]}
				}
				if(d){
					var oData = new sap.ui.model.json.JSONModel(d);
					this.getView().setModel(oData,"exData");
				}else{
					MessageBox.error("Generate Export DATA An ERROR has occurred. PLEASE CONTACT SERVICE TEAM");
					return false;
				}
			}else{
				MessageBox.error("Generate Export DATA An ERROR has occurred. PLEASE CONTACT SERVICE TEAM");
				return false;
			}
		},
		/**
		 * combustion dynamics 엑셀 파일 export에 대한 설정을 한다.(엑셀라벨, 바인딩데이터의 컬럼명, 텍스트정렬, 타입정의)
		 * textAlign:[left, right, center, begin, end]
		 * type:[Boolean, Currency, Date, DateTime, Number, String, Time]
		 */
		createColumnConfig: function() {
			return [
				{ label: 'PART', 		property: 'part', 			textAlign: "center", type: 'String' },
				{ label: 'X CORD', 		property: 'xCord', 			textAlign: "center", type: 'Number' },
				{ label: 'Y CORD', 		property: 'yCord', 		textAlign: "center", type: 'Number' },
				{ label: 'TEMPERATURE', 	property: 'value', 	textAlign: "center", type: 'Number' }
			];
		},
		/**
		 * combustion dynamics 엑셀 파일로 export 한다.
		 */
		onPressExport: function(oEvent) {
			var _this = this;
			MessageBox.confirm("GENERATE EXCEL FILE (COMBUSTION DYNAMICS DATA)", {
				actions: [MessageBox.Action.YES, MessageBox.Action.NO],
				onClose: function(oAction){
					if(oAction === MessageBox.Action.YES)
						_this.onExport(_this.createColumnConfig(), _this.getView().getModel("exData").getProperty("/"), DateUtil.getToday("-")+"_CombustionDynamics");
					else
						return false
				}
			});
		}
	});

}, /* bExport= */ true);