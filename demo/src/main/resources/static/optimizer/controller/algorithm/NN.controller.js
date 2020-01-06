sap.ui.define([
		"dhi/optimizer/controller/BaseController","sap/m/MessageBox","dhi/common/util/ChartUtil","dhi/common/util/Formatter",
		"sap/ui/model/json/JSONModel","sap/ui/model/Filter","sap/ui/core/ValueState"
	], function (BaseController, MessageBox, ChartUtil, Formatter, JSONModel, Filter, ValueState) {
		"use strict";
	
	return BaseController.extend("dhi.optimizer.controller.algorithm.NN", {
		vizFrameSet: {
			id: "NNChart",
			sliderId: "NNSlider",
			type: "dual_timeseries_combination",
			sliderType: "timeseries_line",
			dataset: {
				dimensions: [{ name: 'trend', value: "{trend}", dataType:'date' }],
				measures: [
					{name: 'value1', value: '{value1}' },
					{name: 'value2', value: '{value2}' },
					{name: "Training Data", value: "{dummy}" }
				],
				data: { path: "/result/data" }
			},
			silderDataset: {
				dimensions: [{ name: 'trend', value: "{trend}", dataType:'date' }],
				measures: [
					{name: 'value1', value: '{value1}' },
					{name: 'value2', value: '{value2}' }
				],
				data: { path: "/result/data" }
			},
			properties: {
				title: { visible: false },
				legend: { visible: false },
				timeAxis: {
                    title: { visible: false },
                    levels: ["month", "day", "hour", "minute", "second"]
                },
				plotArea: {
					primaryValuesColorPalette :  ["#ff0000","#5899DA"],
					isFixedDataPointSize: true,
					showGap: false,
					line: { marker:{ size: 4 } },
					gap: { barSpacing: 0 },
					dataPointSize:{ max: 200, min: 100 }
				},
				valueAxis: 	{ title: { visible: false }, axisTick: { visible : false } },
				valueAxis2: { title: { visible: false }, axisTick: { visible : false } },
				categoryAxis: { title: { visible: false } },
				interaction: {
					noninteractiveMode: true,
					hover: { stroke: { visible: false } },
					selectability: { plotStdSelection: false },
					selected: { stroke: { visible: false } }
				}
			},
			feedItems: [
				{ 'uid': "valueAxis", 'type': "Measure", 'values': ["Training Data", "value1"] },
				{ 'uid': "valueAxis2", 'type': "Measure", 'values': ["value2"] },
				{ 'uid': "timeAxis", 'type': "Dimension", 'values': ["trend"] }
			],
			sliderFeedItems: [
				{ 'uid': "valueAxis", 'type': "Measure", 'values': ["value1", "value2"] }, 
				{ 'uid': "timeAxis", 'type': "Dimension", 'values': ["trend"] }
			]
		},
		oParam: {
			path:  "/algorithm/nn/config",
			type: "get",
			callback: "callbackAjax"
		},
		oPostParam: {
			path:  "/algorithm/nn/config",
			data: {},
			type: "post",
			callback: "callbackPostAjax"
		},
		oStudioParam: {
			path:  "/algorithm/nn/input/trend/data",
			data: {},
			type: "get",
			callback: "callbackStudioAjax"
		},
		callbackAjax: function(oModel) { this.setModel(oModel); },
		callbackPostAjax: function(oModel){
			this.onAjaxCall(this.oParam);
			if(oModel.getData().code=200){ this.showMsgToast("Saved"); }
		},
		callbackStudioAjax : function (oModel) {
			var result = oModel.getData().result;
			//1. trend 매핑
			var data = result.data = [];
			var arrTag = result.tag;
			var arrName = [];
			for (var i = 0; i < arrTag.length; i++) {
				arrName.push("value"+(i+1));
			}
			for (var i = 0; i < arrTag.length; i++) {
				var arrTrend = arrTag[i].trend;
				for (var j = 0; j < arrTrend.length; j++) {
					for (var k in arrTrend[j]) {
						var keyflag = true;
						if(i!=0){
							for (var l = 0; l < data.length; l++) {
								if(data[l].trend==k){
									data[l][arrName[i]]=arrTrend[j][k]; data[l]["unit"+(i+1)]=arrTag[i].unit;
									keyflag=false; break;
								}
							}
						}
						if(keyflag){
							var obj = {};
							obj.trend=k;
							for (var l = 0; l < arrTag.length; l++) {
								if(i==l){
									obj[arrName[i]]=arrTrend[j][k]; obj["unit"+(l+1)]=arrTag[i].unit;
								} else{ obj[arrName[l]]=""; obj["unit"+(l+1)]="";}
							}
							data.push(obj);
						}//if
					}//k
				}//j
				arrTag[i].trend={};
			}//i
			
			//set legend
			for (var i = 0; i < arrTag.length; i++) {
				$("svg[name=svgNN] tspan").eq(i).html(arrTag[i].name+" ("+arrTag[i].unit+")");
				if(i==0)		{ $("svg[name=svgNN] g[name=secondBalance]").css("display","none"); }
				else if(i==1)	{ $("svg[name=svgNN] g[name=secondBalance]").css("display","initial"); }
			}
			
			//reset
			this.dpTrainStart.setValue(); this.dpTrainEnd.setValue();
			this.dpValidStart.setValue(); this.dpValidEnd.setValue();
			
			this.rangeStart=this.toYYYYMMDD(this.dpStart.getValue());
			this.rangeEnd=this.toYYYYMMDD(this.dpEnd.getValue());
			 
			this.vizFrameSet.properties.general = {};
			this.vizFrameSet.properties.general.timePeriodStyle = [];
			this.chart.setVizProperties(this.vizFrameSet.properties);
			this.chart.setModel(oModel);
			this.chartSlider._getRangeSlider().setRange([0,100]);
			this.chartSlider.setModel(oModel);
		},//callbackStudioAjax
		onInit : function () {
			this.legendColor={"training":"#ff0000","validation":"#0000ff"}; 
			this.setModel(new JSONModel({"left":"190px","right":"220px"}),"width");
			this.setModel(new JSONModel({"result":[{"item":"ResilientBackpropagation","key":"ResilientBackpropagation"},{"item":"dummy","key":"dummy"}]}),"select");
			var that = this;
			this.getView().addEventDelegate({
			   onBeforeShow: function(oEvent) {
				   jQuery.sap.log.info("onBeforeShow");
				   that.oParam.url = that.getApiUrl()+that.oParam.path;
				   that.onAjaxCall(that.oParam);
			   },
			   onAfterShow: function(oEvent) {
				   jQuery.sap.log.info("onAfterShow");
				   //focus 방지
				   $("div[id$=NN] span[id$=-inner]").css("outline","none");
			   }
			});
		},
		validNumber: function(oEvent) {
			var value = oEvent.getParameter("value");
			var source = oEvent.getSource();
			if(value < Number(source.data("min")))		{ source.setValueState(ValueState.Error); }
			else if(value > Number(source.data("max")))	{ source.setValueState(ValueState.Error); }
			else { source.setValueState(ValueState.None); }
			
			if (source.sId.search(/iptValidTime/i)>-1) {
				this.byId("iptValidTimeCopy").setValue(source.getValue());
			}
		},
		onPress: function(oEvent){
			this.oPostParam.data = eval('(' + JSON.stringify(this.getModel().getData().result) + ')'); //object copy
			this.oPostParam.url = this.getApiUrl()+this.oPostParam.path;
			this.onAjaxCall(this.oPostParam);
		},
		onStudioDialog: function() {
			if (!this.oStudioDialog) {
				this.oStudioDialog = sap.ui.xmlfragment("dhi.optimizer.fragment.NN", this);
				this.getView().addDependent(this.oStudioDialog);
				this.chart=this.oStudioDialog.getContent()[0].getItems()[2].getItems()[0].getItems()[0];
				this.chartSlider=this.oStudioDialog.getContent()[0].getItems()[2].getItems()[1];
				ChartUtil.setVizFrame(this.vizFrameSet, this, this.chart);
				ChartUtil.setSlider(this.vizFrameSet, this.chartSlider, true);
			}
			this.oStudioDialog.setTitle("Data Studio");
			this.oStudioDialog.open();
		},
		onCloseDialog: function(oEvent){ oEvent.getSource().oParent.close(); },
		onAfterOpen: function(oEvent){
			this.dpStart = this.oStudioDialog.getContent()[0].getItems()[1].getItems()[7];
			this.dpEnd = this.oStudioDialog.getContent()[0].getItems()[1].getItems()[9];
			this.dpTrainStart = this.oStudioDialog.getContent()[0].getItems()[3].getItems()[1];
			this.dpTrainEnd = this.oStudioDialog.getContent()[0].getItems()[3].getItems()[3];
			this.dpValidStart = this.oStudioDialog.getContent()[0].getItems()[4].getItems()[1];
			this.dpValidEnd = this.oStudioDialog.getContent()[0].getItems()[4].getItems()[3];
			this.setPeriodD("d",this.dpStart,this.dpEnd, true);
			this.onSearch();
		},
		onSearch: function(oEvent) {
			if(this.dpStart.getValueState()!=sap.ui.core.ValueState.None||this.dpEnd.getValueState()!=sap.ui.core.ValueState.None){
				this.showMsgToast("Invalid date"); return;
			}
			
			if(new Date(this.toYYYYMMDD(this.dpStart.getValue()))>new Date(this.toYYYYMMDD(this.dpEnd.getValue()))){
				MessageBox.warning("Start date is greater than end date");
				return this.setPeriodD("d",this.dpStart,this.dpEnd, true);
			}
			this.oStudioParam.data.start_date=this.toYYYYMMDD(this.dpStart.getValue());
			this.oStudioParam.data.end_date=this.toYYYYMMDD(this.dpEnd.getValue());
			this.oStudioParam.url = this.getApiUrl()+this.oStudioParam.path;
			this.setChartBusy(true);
			this.onAjaxCall(this.oStudioParam);
		},
		toYYYYMMDD: function(val){ return val.substr(6,4)+"-"+val.substr(3,2)+"-"+val.substr(0,2)+" "+val.substr(11); },
		toDDMMYYYY: function(val){ return val.substr(8,2)+"-"+val.substr(5,2)+"-"+val.substr(0,4)+" "+val.substr(11); },
		rangeChanged: function(oEvent){
			var data = oEvent.getParameters().data;
            var start = data.start.trend;
            var end = data.end.trend;
            var dateFilter =  new Filter({
                path: "trend",
                test: function(oValue) {
                    var time = Date.parse(new Date(oValue));
                    return (time >= start && time <= end);
                }
            });
            this.chart.getDataset().getBinding('data').filter([dateFilter]);
            this.rangeStart=Formatter.getDateformat(new Date(start));
            this.rangeEnd=Formatter.getDateformat(new Date(end));
		},
		renderComplete: function(oEvent) {
			jQuery.sap.log.info("renderComplete");
			$("#sap-ui-preserve").html("");
			var oTag = oEvent.getSource().getModel().getData().result.tag;
			if(null==oTag||undefined==oTag){return;}
			var that=this;
			var html="";
			$('div[id$=NNChart] .v-vbcline').each(function (idx) {
				html+="<rect x='"+$(this).attr("x1")+"' y='0' width='"+($(this).attr("x2")-$(this).attr("x1"))+"' height='300' fill='"+$(this).attr("stroke")+"'/>";
			});
			$("div[id$=NNChart] .v-gridline-mainline").eq(0).after(html);
			html = $("div[id$=NNChart] .v-gridline").eq(0).html();
			$("div[id$=NNChart] .v-gridline").eq(0).html('');
			$("div[id$=NNChart] .v-gridline").eq(0).html(html);
			
			for (var i = 0; i < oTag.length; i++) {
				if(i==0){
					$("div[id$=NNChart] .v-m-yAxis .viz-axis-label text").each(function (idx) {
						$(this).attr("x","0").attr("fill","#5899DA").html($(this).html());
					});
				} else if(i==1){
					$("div[id$=NNChart] .v-m-yAxis2 .viz-axis-label text").each(function (idx) {
						$(this).attr("x","1").attr("fill","#E8743B").html($(this).html());
					});
				}
			}
			that.setChartBusy(false);
		},
		setChartBusy: function(b) { this.chart.setBusy(b); this.chartSlider.setBusy(b); },
		OnSetPress: function(oEvent) {
			var color=this.legendColor.validation;
			var rangeStart=this.rangeStart;
			var rangeEnd=this.rangeEnd;
			var otherStart=this.toYYYYMMDD(this.dpTrainStart.getValue());
			var otherEnd=this.toYYYYMMDD(this.dpTrainEnd.getValue());
			
			if(oEvent.getSource().sId.search(/train/i)>-1){
				color=this.legendColor.training;
				otherStart=this.toYYYYMMDD(this.dpValidStart.getValue());
				otherEnd=this.toYYYYMMDD(this.dpValidEnd.getValue());
			}
			if( (otherStart<rangeStart&&rangeStart<otherEnd)||(otherStart<rangeEnd&&rangeEnd<otherEnd)||
				(rangeStart<otherStart&&otherStart<rangeEnd)||(rangeStart<otherEnd&&otherEnd<rangeEnd)||
				(rangeStart==otherStart&&rangeEnd==otherEnd) )
			{ this.showMsgToast("overlapped period"); return; }
			
			oEvent.getSource().getParent().getItems()[1].setValue(this.toDDMMYYYY(this.rangeStart)).setValueState(sap.ui.core.ValueState.None);
			oEvent.getSource().getParent().getItems()[3].setValue(this.toDDMMYYYY(this.rangeEnd)).setValueState(sap.ui.core.ValueState.None);
			
			var arr=this.vizFrameSet.properties.general.timePeriodStyle;
			for (var i = 0; i < arr.length; i++) { if(arr[i].color==color){ arr.splice(i,1); } }
			arr.push({"start":this.rangeStart, "end":this.rangeEnd,"color":color});
			this.chart.setVizProperties(this.vizFrameSet.properties);
			
			//slider reset
			this.chartSlider._getRangeSlider().setRange([0,100]);
			var start = Date.parse(new Date(this.toYYYYMMDD(this.dpStart.getValue())));
			var end = Date.parse(new Date(this.toYYYYMMDD(this.dpEnd.getValue())));
			var dateFilter =  new Filter({
                path: "trend",
                test: function(oValue) {
                    var time = Date.parse(new Date(oValue));
                    return (time >= start && time <= end);
                }
            });
            this.chart.getDataset().getBinding('data').filter([dateFilter]);
		},
		OnResetPress : function(oEvent) {
			oEvent.getSource().getParent().getItems()[1].setValue();
			oEvent.getSource().getParent().getItems()[3].setValue();
			
			var color=this.legendColor.validation;
			if(oEvent.getSource().sId.search(/train/i)>-1){
				color=this.legendColor.training;
			}
			var arr=this.vizFrameSet.properties.general.timePeriodStyle;
			for (var i = 0; i < arr.length; i++) { if(arr[i].color==color){ arr.splice(i,1); } }
			this.chart.setVizProperties(this.vizFrameSet.properties);
			
			//slider reset
			this.chartSlider._getRangeSlider().setRange([0,100]);
			var start = Date.parse(new Date(this.toYYYYMMDD(this.dpStart.getValue())));
			var end = Date.parse(new Date(this.toYYYYMMDD(this.dpEnd.getValue())));
			var dateFilter =  new Filter({
                path: "trend",
                test: function(oValue) {
                    var time = Date.parse(new Date(oValue));
                    return (time >= start && time <= end);
                }
            });
            this.chart.getDataset().getBinding('data').filter([dateFilter]);
		},
		OnDownloadPress : function(oEvent) {
			var startPicker=oEvent.getSource().getParent().getItems()[1];
			var endPicker=oEvent.getSource().getParent().getItems()[3];
			var startDate=this.toYYYYMMDD(startPicker.getValue());
			var endDate=this.toYYYYMMDD(endPicker.getValue());
			
			if(startPicker.getValueState()!=sap.ui.core.ValueState.None||endPicker.getValueState()!=sap.ui.core.ValueState.None
					||startPicker.getValue()==""||endPicker.getValue()==""){ this.showMsgToast("Invalid Date"); return; }
			if(new Date(startDate)>new Date(endDate)){ MessageBox.warning("Start date is greater than end date"); return; }
			
			var that=this;
			if(oEvent.getSource().sId.search(/train/i)>-1){
				alert("1111");
				window.location=this.getApiUrl()+"/algorithm/nn/input/download?start_date="+startDate+"&end_date="+endDate;
				setTimeout(function() {
					window.location=that.getApiUrl()+"/algorithm/nn/output/download?start_date="+startDate+"&end_date="+endDate;
				},1000);
			}else{
				alert("2222");
				window.location=this.getApiUrl()+"/algorithm/nn/valid_input/download?start_date="+startDate+"&end_date="+endDate;
				setTimeout(function() {
					window.location=that.getApiUrl()+"/algorithm/nn/valid_output/download?start_date="+startDate+"&end_date="+endDate;
				},1000);
			}
		},
		onExcelUpload: function(oEvent) {
			if(!this.btnUpload){ this.btnUpload=oEvent.getSource(); }
			$("input[name=NNFileUpload]").click();
		},
		handleUploadPress: function(oEvent) {
			this.btnUpload.setBusy(true);
			
			var filedata = new FormData(); // FormData 인스턴스 생성
			filedata.append('file', $("input[name=NNFileUpload]")[0].files[0]);
			var _xml = new XMLHttpRequest();
			_xml.open('POST', this.getApiUrl()+"/algorithm/nn/model/upload", true);
			var that=this;
			_xml.onload = function(event) {
				if(_xml.status == 200){ that.showMsgToast('Uploaded'); }
				else { that.showMsgToast('Error'); }
				that.btnUpload.setBusy(false);
			};
			_xml.send(filedata);
		},
		typeMissmatch: function(oEvent) { this.showMsgToast("Type missmatch: "+oEvent.getParameter("fileName")); },
		onUnitChange: function() {
			this.oParam.url = this.getApiUrl()+this.oParam.path;
			this.onAjaxCall(this.oParam);
		}
	});
}, /* bExport= */ true);