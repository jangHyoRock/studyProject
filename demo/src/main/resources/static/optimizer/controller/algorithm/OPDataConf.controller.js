sap.ui.define(["dhi/optimizer/controller/BaseController", "sap/ui/model/json/JSONModel", "sap/m/MessageBox",
	"dhi/common/util/DateUtil", "sap/ui/table/SortOrder"
	], function (BaseController, JSONModel, MessageBox, DateUtil, SortOrder) {
		"use strict";
	var numTypeCells = ["min_raw", "max_raw", "min_eu", "max_eu", "tag_no"];
	return BaseController.extend("dhi.optimizer.controller.algorithm.OPDataConf", {
		onInit : function() {
			//component 객체 생성
			this._oSaveTmp = this.byId("saveTmp");//save button event process
			this._oTable = this.byId("opConfTable");
			this._iptUpload = this.byId("opUploader");
			this._oTable.addEventDelegate({
				onAfterRendering: function(e) {
					$('#'+e.srcControl.getId()+"-selall").removeClass("sapUiTableColRowHdr");
				}
			});
			this.getOwnerComponent().getRouter().getRoute("algorithm.OPDataConf").attachPatternMatched(this._onRouteMatched, this);
		},
		_onRouteMatched: function(oEvent) {
			this.onSearch();
		},
		/**
		 * ajax 통신용 parameter
		 */
		oParam: {
			url: "",
			data: {},
			type: "",
			callback: ""
		},
		/**
		 * left unit 변경시 이벤트
		 */
		onUnitChange : function() {
			this.initLayout();
			this.unitID = this.getSessionValue("default_plant_unit_id");
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
			this.onFileDownload(this.getApiUrl()+"/settings/opdata/conf/download");
		},
		csvTemp: {
			"tag_id": "",
			"tag_nm": "",
			"description": "",
			"min_raw": 0,
			"max_raw": 0,
			"unit": "",
			"min_eu": 0,
			"max_eu": 0,
			"plant_unit_id": "",
			"tag_no": 0
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
			if(fileName.toLowerCase().indexOf("opdataconf")!=-1){
				var oFileToRead = oEvent.getParameters().files["0"];
				var reader = new FileReader();
				// Read file into memory as UTF-8
				reader.readAsText(oFileToRead);
				reader.onload = function loadHandler(event) {
					var csv = event.target.result;
					var lines = csv.split(/\r\n|\n/);
					var headers = Object.keys(that.csvTemp);
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
				MessageBox.warning("IT IS NOT AN OPDataConf TEMPLATE.");
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
		/**
		 * data를 조건에 따라 재정의 한 뒤 리턴한다.
		 * @param d: 업로드 / 검색된 데이터
		 */
		validBindingData: function(d) {

		},
		clearAllFilters: function() {
			var oTable = this._oTable;;
			if(this.getModel("uploadData").getData()){
				var aColumns = oTable.getColumns();
				for (var i = 0; i < aColumns.length; i++) {
					oTable.filter(aColumns[i], null);
				}
			}
		},
		clearSort: function() {this.byId("opConfTable").sort();},
		/**
		 * 테이블에 있는 데이터를 조건에 따라 저장한다.
		 */
		onSavePress: function() {
			var tableData = this.getModel("uploadData").getProperty("/");
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
			var that = this;
			var url = this.getApiUrl()+"/settings/opdata/conf/";
			//init progressbar
			MessageBox.confirm("DO YOU WANT TO SAVE OPData?", {
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
			this.oParam.url = this.getApiUrl()+"/settings/opdata/conf/";
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