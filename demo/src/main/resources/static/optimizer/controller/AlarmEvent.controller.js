sap.ui.define([
		"dhi/optimizer/controller/BaseController","sap/ui/model/json/JSONModel","dhi/optimizer/util/AlarmFormatter",
		"sap/ui/core/util/Export","sap/ui/core/util/ExportTypeCSV","sap/m/MessageBox","dhi/common/util/DateUtil",
		"sap/ui/table/SortOrder"
	], function (BaseController,JSONModel,AlarmFormatter,Export,ExportTypeCSV,MessageBox,DateUtil,SortOrder) {
		"use strict";
	var pageNo = 0;
	return BaseController.extend("dhi.optimizer.controller.AlarmEvent", {
		/**
		 * alarm & event init
		 */
		onInit : function () {
			this.getOwnerComponent().getRouter().getRoute("alarmEvent").attachPatternMatched(this._onRouteMatched, this);
		},
		_onRouteMatched: function(oEvent) {
			this.initLayout();
			this.dpStart = this.byId("__component0---alarmEvent--dateSearchFragment--dpStart");
			this.dpEnd = this.byId("__component0---alarmEvent--dateSearchFragment--dpEnd");
			this.setPeriodD("d",this.dpStart,this.dpEnd);
			this.onSearch();//default search
		},
		onUnitChange: function() {
			this.initLayout();
			this.dpStart = this.byId("__component0---alarmEvent--dateSearchFragment--dpStart");
			this.dpEnd = this.byId("__component0---alarmEvent--dateSearchFragment--dpEnd");
			this.setPeriodD("d",this.dpStart,this.dpEnd);
			this.onSearch();//default search
		},
		initLayout: function() {
			this.byId("alarmTable").setNoData("");
			this.byId("alarmTable").setBusy(false);
			this.byId("alarmTable").sort(this.byId("datetimeCol"),SortOrder.Descending);
			this.setModel(new JSONModel({}), "alarmData");
			this.setModel(new JSONModel(this.tmp),"count");
		},
		clearAllFilters: function() {
			var oTable = this.byId("alarmTable");
			if(this.getModel("alarmData").getData()){
				var aColumns = oTable.getColumns();
				for (var i = 0; i < aColumns.length; i++) {
					oTable.filter(aColumns[i], null);
				}
			}
		},
		clearSort: function() {this.byId("alarmTable").sort();},
		onExit: function(oEvent){

		},
		/**
		 * alarm & event rendering 이후 이벤트처리
		 */
		onAfterRendering: function(oEvent){
		},
		/**
		 * alarm & event 검색 ajax 파라미터를 정의한다.
		 * @param start_date: 시작일자 end_date:종료일자 tag_id: 기기
		 */
		oParam: {
			url: "",
			data: {start_date: "", end_date: "", page_no: 0},
			type: "get",
			callback: ""
		},
		/**
		 * alarm & event 검색 이벤트 및 validation을 처리한다.
		 */
		onSearch: function(oEvent) {
			pageNo = 0;
			this.byId("alarmTable").setNoData("Searching...");
			this.byId("alarmTable").setBusy(true);
			var startDt = this.toYYYYMMDD(this.dpStart.getValue());
			var endDt = this.toYYYYMMDD(this.dpEnd.getValue());
			if(new Date(startDt)>new Date(endDt)){
				MessageBox.warning("Start date is greater than end date");
				this.byId("alarmTable").setNoData("");
				this.byId("alarmTable").setBusy(false);
				return this.setPeriodD("d",this.dpStart,this.dpEnd);
			}else{
				this.oParam.url = this.getApiUrl()+"/alarm_event/data/";
				this.oParam.data.start_date = startDt;
				this.oParam.data.end_date = endDt;
				this.oParam.data.page_no = pageNo;
				this.oParam.callback = "fnSearchCallback";
				this.onAjaxCall(this.oParam);
			}
		},
		toYYYYMMDD: function(val){ return val.substr(6,4)+"-"+val.substr(3,2)+"-"+val.substr(0,2)+" "+val.substr(11); },
		/**
		 * alarm & event 검색 후 callback 이벤트를 처리한다.
		 */
		fnSearchCallback: function(oModel){
			var rtnData = oModel.getData();
			if(rtnData.code==200){
				this.byId("alarmTable").setNoData("");
				this.byId("alarmTable").setBusy(false);
				var oData = new JSONModel(rtnData.result);
				this.setModel(oData,"alarmData");
				this.byId("alarmTable").setFirstVisibleRow(0);
				var bindingData = this.getModel("alarmData").getProperty("/alarm_event");
				if(bindingData.length>0){
					this.getModel("count").setProperty("/cnt", bindingData.length);
					this.getModel("count").setProperty("/index", 1);
					var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi;
					var dateTime, ndate;
					for(var i = 0; i<bindingData.length; i++){
						dateTime = bindingData[i]["datetime"];
						if(regExp.test(dateTime)){//특수문자를 제거후 yyyyddmm형식으로 변환한다.
							ndate = dateTime.replace(regExp, "");
							dateTime = ndate.substr(4,4) +"-"+ ndate.substr(2,2) +"-"+ ndate.substr(0,2) + " " + ndate.substr(9,2) +":" + ndate.substr(11,2) +":" + ndate.substr(13,2);
						}else{
							ndate = ndate.substr(4,4) +"-"+ ndate.substr(2,2) +"-"+ ndate.substr(0,2) + " " + ndate.substr(9,2) +":" + ndate.substr(11,2) +":" + ndate.substr(13,2);
							dateTime = ndate;
						}
						this.getModel("alarmData").setProperty("/alarm_event/"+i+"/sortDate", dateTime);
					}
				}else{
					this.initLayout();
					this.getModel("count").setProperty("/cnt", 0);
					this.getModel("count").setProperty("/index", 0);
					this.byId("alarmTable").setNoData("No data matches the condition.");
				}
			}else{
				this.initLayout();
			}
		},
		tmp: {
			cnt: 0,
			index: 0
		},
		rowScrolling: function(oEvent) {
			var oTable = this.byId("alarmTable");
			var lastRowIndex = oTable.getVisibleRowCount() + oTable.getFirstVisibleRow();
			var bindingData = this.getModel("alarmData").getProperty("/alarm_event");
			var lastDataLength = bindingData.length;
			if(oTable.getFirstVisibleRow() == 0)
				this.getModel("count").setProperty("/index", 1);
			else
				this.getModel("count").setProperty("/index", lastRowIndex);
			if(lastDataLength>0){
				if((lastRowIndex==lastDataLength)&&lastDataLength>1999){
					pageNo++;
					oTable.setBusy(true);
					this.oParam.url = this.getApiUrl()+"/alarm_event/data/";
					this.oParam.data.page_no = pageNo;
					this.oParam.callback = "fnPagingCallback";
					this.onAjaxCall(this.oParam);
				}
			}
		},
		fnPagingCallback: function(oModel) {
			var rtnData = oModel.getData();
			if(rtnData.code==200){
				var newData = rtnData.result.alarm_event;
				var existData = this.getModel("alarmData").getProperty("/alarm_event");
				for(var i in newData){
					existData.push(newData[i]);
				}
				this.getModel("alarmData").setProperty("/alarm_event", existData);
				this.getModel("count").setProperty("/cnt", existData.length);
				var bindingData = this.getModel("alarmData").getProperty("/alarm_event");
				var regExp = /[\{\}\[\]\/?.,;:|\)*~`!^\-_+<>@\#$%&\\\=\(\'\"]/gi;
				var dateTime, ndate;
				for(var i = 0; i<bindingData.length; i++){
					dateTime = bindingData[i]["datetime"];
					if(regExp.test(dateTime)){//특수문자를 제거후 yyyyddmm형식으로 변환한다.
						ndate = dateTime.replace(regExp, "");
						dateTime = ndate.substr(4,4) +"-"+ ndate.substr(2,2) +"-"+ ndate.substr(0,2) + " " + ndate.substr(9,2) +":" + ndate.substr(11,2) +":" + ndate.substr(13,2);
					}else{
						ndate = ndate.substr(4,4) +"-"+ ndate.substr(2,2) +"-"+ ndate.substr(0,2) + " " + ndate.substr(9,2) +":" + ndate.substr(11,2) +":" + ndate.substr(13,2);
						dateTime = ndate;
					}
					this.getModel("alarmData").setProperty("/alarm_event/"+i+"/sortDate", dateTime);
				}
				this.byId("alarmTable").setBusy(false);
			}else{
				this.initLayout();
			}
		},
		/**
		 * alarm & event 엑셀파일 export에 대한 설정을 한다.(엑셀라벨, 바인딩데이터의 컬럼명, 텍스트정렬, 타입정의)
		 * textAlign:[left, right, center, begin, end]
		 * type:[Boolean, Currency, Date, DateTime, Number, String, Time]
		 */
		createColumnConfig: function() {
			return [
				{ label: 'DATETIME', 	property: 'datetime', 			textAlign: "center", type: 'String',	width: 20 },
				{ label: 'TYPE', 		property: 'type', 		textAlign: "center", type: 'String' },
				{ label: 'NAME', 		property: 'item', 		textAlign: "center", type: 'String' },
				{ label: 'STATUS', 		property: 'status', 	textAlign: "center", type: 'String' },
				{ label: 'DESCRIPTION', 	property: 'description', 	textAlign: "center", type: 'String',	width: 15 }
			];
		},
		/**
		 * alarm & event 엑셀 파일로 export 한다.
		 */
		onPressExport: function(oEvent) {
			var that = this;
			var exportData = this.getModel("alarmData").getProperty("/alarm_event");
			if(typeof(exportData.length)=="undefined"){
				MessageBox.alert("NO DATA TO EXPORT.");
				return;
			}
			MessageBox.confirm("GENERATE EXCEL FILE (ALARM & EVENT DATA)", {
				actions: [MessageBox.Action.YES, MessageBox.Action.NO],
				onClose: function(oAction){
					if(oAction === MessageBox.Action.YES)
						that.onExport(that.createColumnConfig(), exportData, DateUtil.getToday("-")+"_Alarm History");
					else
						return false
				}
			});
		}
	});

}, /* bExport= */ true);