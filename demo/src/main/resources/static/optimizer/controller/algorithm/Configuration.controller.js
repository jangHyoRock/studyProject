sap.ui.define(["dhi/optimizer/controller/BaseController", "sap/ui/model/json/JSONModel", "sap/m/MessageBox",
	"dhi/common/util/DateUtil", "sap/ui/table/SortOrder"
	], function (BaseController, JSONModel, MessageBox, DateUtil, SortOrder) {
		"use strict";
	return BaseController.extend("dhi.optimizer.controller.algorithm.Configuration", {
		numTypeCellsObj : {commondata:	["min_raw", "max_raw", "min_eu", "max_eu", "tag_no"],
						controldata:["min_raw", "max_raw", "min_eu", "max_eu", "tag_no"],
						nntraindata:["tag_no"],
						psomvinfo:	["pso_mv_max", "pso_mv_min", "pso_mv_order"],
						opdata:		["min_raw", "max_raw", "min_eu", "max_eu", "tag_no"]},
		urlObj: {commondata:	"/settings/commondata/conf/",
				 controldata:	"/settings/controldata/conf/",
				 nntraindata:	"/settings/nntraindata/conf/",
				 psomvinfo:		"/settings/psomvinfo/conf/",
				 opdata:		"/settings/opdata/conf/"},
		idObj: {commondata:		"commonDataConfTable",
				controldata:	"controlConfTable",
				nntraindata:	"nnConfTable",
				psomvinfo:		"psoConfTable",
				opdata:			"opConfTable"},
		selectArr: [{key:"opdata",value:"OP Data"},
					{key:"commondata",value:"Common Data"},
					{key:"controldata",value:"Control Data"},
					{key:"nntraindata",value:"NNTrain Data"},
					{key:"psomvinfo",value:"Pso MV"}],
		csvTempArr: {commondata:	{"tag_id":"","tag_nm":"","description":"","min_raw":0,"max_raw":0,"unit":"","min_eu":0,"max_eu":0,"plant_unit_id":"","tag_no":0},
					 controldata:	{"tag_id":"","tag_nm":"","description":"","min_raw":0,"max_raw":0,"unit":"","min_eu":0,"max_eu":0,"plant_unit_id":"","tag_no":0},
					 nntraindata:	{"tag_id":"","tag_nm":"","pso_mv":"","zero_plate":"","io_type":"","tag_no":0},
					 psomvinfo:		{"pso_mv":"","pso_mv_type":"","pso_mv_max":0,"pso_mv_min":0,"pso_mv_order":0,"auto_mode_tag_id":"","hold_tag_id":"","input_bias_tag_id":"","output_bias_tag_id":""},
					 opdata:{"tag_id":"","tag_nm":"","description":"","min_raw":0,"max_raw":0,"unit":"","min_eu":0,"max_eu":0,"plant_unit_id":"","tag_no":0}},
		onInit : function() {
			//component 객체 생성
			this._oTable = this.byId("commonDataConfTable");
			this._iptUpload = this.byId("opUploader");
			this._select = this.byId("selectSettings"); window.s = this._select;
			this.setModel(new JSONModel({key:"opdata"}),"selectedKey");
			this.setModel(new JSONModel(this.selectArr),"select");
			for (var i = 0; i < this.selectArr.length; i++) {
				this.byId(this.idObj[this.selectArr[i].key]).addEventDelegate({
					onAfterRendering: function(e) { $('#'+e.srcControl.getId()+"-selall").removeClass("sapUiTableColRowHdr"); }
				});
			}
			this.getOwnerComponent().getRouter().getRoute("algorithm.Configuration").attachPatternMatched(this._onRouteMatched, this);
		},
		_onRouteMatched: function(oEvent) { this.onSearch(); },
		oParam: { url:"", data:{}, type:"", callback:"" },
		onChange: function(oEvent) {
			this._oTable=this.byId(this.idObj[oEvent.getSource().getSelectedKey()]);
			this.onSearch();
		},
		onUnitChange : function() {
			this.initLayout();
			this.onSearch();
		},
		/**
		 * layout을 초기화한다.
		 */
		initLayout: function(){
			if(this._iptUpload.getValue()){//업로드 버튼초기화
				this._iptUpload.setValue("");
			}
			this._oTable.setBusy(false);
			this._oTable.setNoData("");
			this.setModel(new JSONModel({}),"uploadData");//바인딩데이터초기화
		},
		/**
		 * 렌더링 이후 검색 영역의 selectbox 항목을 세팅한다.
		 */
		onAfterRendering: function() {
		},
		/**
		 * template를 다운로드한다.
		 */
		onTemplateDownload: function() {			
			this.onFileDownload(this.getApiUrl()+"/settings/"+this._select.getSelectedKey().toLowerCase()+"/conf/download");
		},
		/**
		 * 업로드 된 엑셀파일의 데이터를 읽어온다.
		 * 사용라이브러리 : dhi/btms/util/jszip, dhi/btms/util/xlsx
		 */
		onExcelUpload: function(oEvent) {
			var file = oEvent.getParameter("files") && oEvent.getParameter("files")[0];
			this._oTable.setBusy(true);
			this._oTable.setNoData("Registering...");
			var that = this;
			var fileName = file.name;
			if(fileName.toLowerCase().indexOf(this._select.getSelectedKey().toLowerCase())!=-1){
				var oFileToRead = oEvent.getParameters().files["0"];
				var reader = new FileReader();
				// Read file into memory as UTF-8
				reader.readAsText(oFileToRead);
				reader.onload = function loadHandler(event) {
					var csv = event.target.result;
					var lines = csv.split(/\r\n|\n/);
					var headers = Object.keys(that.csvTempArr[that._select.getSelectedKey()]);
					var result = [];
					for (var i=1; i<lines.length-1; i++) {
						var currentline = lines[i].split(',');
						var obj = {};
						for (var j=0; j<headers.length; j++) {
							obj[headers[j]] = currentline[j];
						}
						result.push(obj);
					}
					that.onExcelBinding(result);
				};
				// Handle errors load
				reader.onerror = function errorHandler(evt) {
					if(evt.target.error.name == "NotReadableError") {
						alert("Cannot read file !");
					}
				};
			}else{
				MessageBox.warning("IT IS NOT AN "+this._select.getSelectedKey().toUpperCase()+"Conf TEMPLATE.");
				this.initLayout();
				return;
			}
		},
		/**
		 * 업로드 된 엑셀파일을 json model로 변환한다.
		 * @param _obj: 엑셀 데이터, 조회된 데이터
		 */
		onExcelBinding : function(_obj) {
			var oModel = new JSONModel(_obj);
			this.setModel(oModel,"uploadData");
			if(this._iptUpload.getValue()){//업로드 버튼초기화
				this._iptUpload.setValue("");
			}
			if(this._oTable.getFirstVisibleRow()!=0){
				this._oTable.setFirstVisibleRow(0);//scrolltop
			}
			this._oTable.setBusy(false);
		},
		clearAllFilters: function() {
			var oTable = this._oTable;
			if(this.getModel("uploadData").getData()){
				var aColumns = oTable.getColumns();
				for (var i = 0; i < aColumns.length; i++) {
					oTable.filter(aColumns[i], null);
				}
			}
		},
		clearSort: function() {this._oTable.sort();},
		/**
		 * 테이블에 있는 데이터를 조건에 따라 저장한다.
		 */
		onSavePress: function() {
			var tableData = this.getModel("uploadData").getProperty("/");
			var numTypeCells = this.numTypeCellsObj[this._select.getSelectedKey()];
			if(this._select.getSelectedKey()=="nntraindata"){
				var validData = tableData.filter(function(e){
					return !e["tag_id"] || !e["tag_nm"] || !e["io_type"] || !String(e["tag_no"]);
				});
				if(validData.length>0){
					MessageBox.warning("SOME DATA FIELDS ARE EMPTY OR INVALID. PLEASE CHECK THE DATA.");
					return;
				}
				
				this.oParam.url = this.getApiUrl() + "/settings/nntraindata/nnmodel/delete/check/";
				this.oParam.data = tableData;
				this.oParam.type = "post";
				this.oParam.callback = "fnSaveCheckCallback";
				this.onAjaxCall(this.oParam);
				return;
			}else if(this._select.getSelectedKey()=="psomvinfo"){
				var validData = tableData.filter(function(e, i){
					return !e["pso_mv"] || !e["pso_mv_type"] || !String(e["pso_mv_max"]) || e["pso_mv_max"]<0 || !String(e["pso_mv_min"]) || e["pso_mv_min"]>0 || !String(e["pso_mv_order"]) || !e["auto_mode_tag_id"]
							|| !e["hold_tag_id"]|| !e["input_bias_tag_id"]|| !e["output_bias_tag_id"];
				});
				if(validData.length>0){
					MessageBox.warning("SOME DATA FIELDS ARE EMPTY OR INVALID. PLEASE CHECK THE DATA.");
					return;
				}
				//number type 으로 parsing 한다.
				for(var i = 0; i<tableData.length; i++){
					for(var j=0; j<numTypeCells.length; j++){
						if(tableData[i][numTypeCells[j]]!=null){
							tableData[i][numTypeCells[j]] = parseFloat(tableData[i][numTypeCells[j]]);
						}
					}
				}
			}else{
				var validData = tableData.filter(function(e){
					return !e["tag_id"] || !e["tag_nm"] || !e["plant_unit_id"] || !String(e["tag_no"]);
				});
				if(validData.length>0){
					MessageBox.warning("SOME DATA FIELDS ARE EMPTY OR INVALID. PLEASE CHECK THE DATA.");
					return;
				}
				//공백인 number 데이터 filter
				var numData = tableData.filter(function(e){
					return ( String(e["min_raw"]) == "" ) ||
					( String(e["max_raw"]) == "" ) ||
					( String(e["min_eu"]) == "" )  ||
					( String(e["max_eu"]) == "" );
				});
				//공백 null 값 처리
				for(var i = 0; i<numData.length; i++){
					for(var j = 0; j<numTypeCells.length-1; j++){
						var str = String(numData[i][numTypeCells[j]]);
						if(str == ""){
							numData[i][numTypeCells[j]] = null;
						}
					}
					
				}
				//number type 으로 parsing 한다.
				for(var i = 0; i<tableData.length; i++){
					for(var j=0; j<numTypeCells.length; j++){
						if(tableData[i][numTypeCells[j]]!=null){
							tableData[i][numTypeCells[j]] = parseFloat(tableData[i][numTypeCells[j]]);
						}
					}
				}
			}
			var that = this;
			var url = this.getApiUrl()+this.urlObj[this._select.getSelectedKey()];
			//init progressbar
			MessageBox.confirm("DO YOU WANT TO SAVE "+this._select.getSelectedKey().toUpperCase()+"?", {
				actions: [MessageBox.Action.YES, MessageBox.Action.NO],
				onClose: function(oAction){
					if(oAction === MessageBox.Action.YES){
						that.oParam.url = url;
						that.oParam.data = tableData;
						that.oParam.type = "post";
						that.oParam.callback = "fnSaveCallback";
						that.onAjaxCall(that.oParam);
					}else{
						return false
					}
				}
			});
		},
		/**
		 * nn 삭제여부 callback
		 * @param oData: callback 데이터
		 */
		fnSaveCheckCallback: function(oData) {
			var rtnData = oData.getData();
			var resultCode = rtnData.code;
			var that = this;
			if(resultCode == 200){
				var flag = rtnData.result;
				var tableData = this.getModel("uploadData").getProperty("/");
				var url = this.getApiUrl()+"/settings/nntraindata/conf/"+flag;
				var msg = "";
				if(flag=="false"){
					msg = "DO YOU WANT TO SAVING NNTrainingData?";
				}else{
					msg = "DO YOU WANT TO DELETE EXISTING NNTrainingData AND SAVING NEW NNTrainingData?";
				}
				MessageBox.confirm(msg, {
					actions: [MessageBox.Action.YES, MessageBox.Action.NO],
					onClose: function(oAction){
						if(oAction === MessageBox.Action.YES){
							that.oParam.url = url;
							that.oParam.data = tableData;
							that.oParam.type = "post";
							that.oParam.callback = "fnSaveCallback";
							that.onAjaxCall(that.oParam);
						}else{
							return false
						}
					}
				});
			}else{
				this.showMsgToast("Save Failed.");
				return;
			}
		},
		/**
		 * 저장 callback
		 * @param oData: callback 데이터
		 */
		fnSaveCallback: function(oData) {
			var rtnData = oData.getData();
			var resultCode = rtnData.code;
			if(resultCode == 200){
				this.showMsgToast("Save Completed.");
				this.onSearch();
			}else{
				this.showMsgToast("Save Failed.");
				return;
			}
		},
		/**
		 * 검색 버튼 클릭시 이벤트를 처리한다.
		 */
		onSearch: function(oEvent) {
			this.initLayout();
			this._oTable.setNoData("Searching...");
			this._oTable.setBusy(true);
			this.oParam.url = this.getApiUrl()+this.urlObj[this._select.getSelectedKey()];
			this.oParam.type = "get";
			this.oParam.data = {};
			this.oParam.callback = "fnGetSearchCallback";
			this.onAjaxCall(this.oParam);
		},
		/**
		 * 검색 callback
		 */
		fnGetSearchCallback: function(oData) {
			var rtnData = oData.getData();
			var resultCode = rtnData.code;
			if(resultCode == 200){
				var result = rtnData.result;
				if(result.length>0){
					this.onExcelBinding(result);
				}else{
					this.initLayout();
					this._oTable.setNoData("No data.");
				}
			}else{
				this.initLayout();
				this._oTable.setNoData("Server Error occurred.");
			}
		}
	});
}, /* bExport= */ true);