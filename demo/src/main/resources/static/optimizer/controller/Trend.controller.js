sap.ui.define([
		"dhi/optimizer/controller/BaseController", "sap/ui/model/json/JSONModel", "sap/ui/model/Filter", "dhi/common/util/ChartUtil",
		"sap/viz/ui5/format/ChartFormatter", "sap/m/MessageBox", "dhi/common/util/Formatter"
	], function (BaseController, JSONModel, Filter, ChartUtil, ChartFormatter, MessageBox, Formatter) {
		"use strict";
	
	return BaseController.extend("dhi.optimizer.controller.Trend", {
		vizFrameSet: {
			id: "trendChart",
			sliderId: "trendSlider",
			type: "dual_timeseries_combination",
			sliderType: "timeseries_line",
			dataset: {
				dimensions: [{ name: 'Trend', value: "{trend}", dataType:'date' }],
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
					isFixedDataPointSize: true,
					showGap: false,
					line: { marker:{ size: 4 } },
					gap: { barSpacing: 0 },
					dataPointSize:{ max: 200, min: 100 },
					window : { start:"firstDataPoint", end:"lastDataPoint" }
				},
				valueAxis: 	{ title: { visible: false }, axisTick: { visible : false }, label: { formatstring:ChartFormatter.DefaultPattern.STANDARDPERCENT}},
				valueAxis2: { title: { visible: false }, axisTick: { visible : false }, label: { formatstring:ChartFormatter.DefaultPattern.STANDARDPERCENT}},
				categoryAxis: { title: { visible: false } },
				interaction: {
					noninteractiveMode: false,
					hover: { stroke: { visible: true } },
					selectability: { plotStdSelection: false, mode: "NONE" },
					selected: { stroke: { visible: false } }
				}
			},
			sliderFeedItems: [
				{ 'uid': "valueAxis", 'type': "Measure", 'values': ["value1", "value2"] }, 
				{ 'uid': "timeAxis", 'type': "Dimension", 'values': ["trend"] }
			]
		},
		oParam: {
			path: "/trend/data/chart/seconds_cycle",
			data: {items: "", start_date: "", end_date: ""},
			type: "get",
			callback: "callbackAjax"
		},
		oParamExport: {
			path: "/trend/data/chart/seconds_cycle/item/all",
			data: {start_date: "", end_date: ""},
			type: "get",
			callback: "callbackAjaxExport"
		},
		callbackAjax : function (oModel) {
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
								} else{ obj["unit"+(l+1)]=""; }
							}
							data.push(obj);
						}//if
					}//k
				}//j
				arrTag[i].trend={};
			}//i
			
			//2. optimizer 매핑
			var optimizer = result.optimizer;
			this.vizFrameSet.properties.general = {};
			this.vizFrameSet.properties.general.timePeriodStyle = [];

			for (var i = 0; i < data.length; i++) {
				for (var j = 0; j < optimizer.length; j++) {
					//export 데이터 세팅
					if(i==j){
						data[i].startDateEx=Formatter.getDateformat(new Date(optimizer[j].start_date),true);
						data[i].endDateEx=Formatter.getDateformat(new Date(optimizer[j].end_date),true);
						data[i].statusEx=optimizer[j].status;
					}
					if(data[i].optimizerEx=="On"){
					}else if(optimizer[j].status==1&&optimizer[j].start_date<=data[i].trend&&data[i].trend<=optimizer[j].end_date){
						data[i].optimizerEx="On";
					}else{
						data[i].optimizerEx="Off";
					}
					//타임피리어드 세팅
					if(i==0&&optimizer[j].status==1){
						this.vizFrameSet.properties.general.timePeriodStyle.push(
							{"start": optimizer[j].start_date, "end": optimizer[j].end_date, "color": "#c5c5c5"}
						);
					}
				}//j
			}//i
			
			var items=this.byId("tagId").getSelectedItems();
			if(arrTag.length<2){
				this.vizFrameSet.type="timeseries_combination";
				this.vizFrameSet.properties.plotArea.colorPalette=["#E5E5E5","#5899DA"];
				delete this.vizFrameSet.properties.plotArea.primaryValuesColorPalette;
				delete this.vizFrameSet.properties.valueAxis2;
				this.vizFrameSet.dataset.measures=
					[{name:items[0].getText(),value:'{value1}'},{name:'Optimizer On',value:'{dummy}'}];
				this.vizFrameSet.feedItems=
					[{uid:"valueAxis",type:"Measure",values:["Optimizer On", items[0].getText()]},{uid:"timeAxis",type:"Dimension",values:["Trend"]}];
			}else {
				this.vizFrameSet.type="dual_timeseries_combination";
				delete this.vizFrameSet.properties.plotArea.colorPalette;
				this.vizFrameSet.properties.plotArea.primaryValuesColorPalette=["#E5E5E5","#5899DA"];
				this.vizFrameSet.properties.valueAxis2=
					{title:{visible:false},axisTick:{visible:false},label:{formatstring:ChartFormatter.DefaultPattern.STANDARDPERCENT}};
				this.vizFrameSet.dataset.measures=
					[{name:items[0].getText(),value:'{value1}'},{name:items[1].getText(),value:'{value2}'},{name:'Optimizer On',value:'{dummy}'}];
				this.vizFrameSet.feedItems=
					[{uid:"valueAxis",type:"Measure",values:["Optimizer On", items[0].getText()]},{uid:"valueAxis2",type:"Measure",values:[items[1].getText()]},{uid:"timeAxis",type:"Dimension",values:["Trend"]}];
			}
			
			//set legend
			for (var i = 0; i < result.tag.length; i++) {
				$("svg[name=svgTR] tspan").eq(i+2).html(result.tag[i].name);
				if(i==0)		{ $("svg[name=svgTR] g[name=secondBalance]").css("display","none"); this.byId("txtUnit2").setText();}
				else if(i==1)	{ $("svg[name=svgTR] g[name=secondBalance]").css("display","initial"); }
				this.byId("txtUnit"+(i+1)).setText(result.tag[i].unit);
			}
			
			ChartUtil.setVizFrame(this.vizFrameSet, this);
			this.chart.setModel(oModel);
			
			var oTooltip = new sap.viz.ui5.controls.VizTooltip({});
            oTooltip.connect(this.chart.getVizUid());
           
			if(this.byId("ckb24").getSelected()){
				var r = this.slider._getRangeSlider().getRange();
				var f = this.slider._getVizFrame()._getDataRange(r[0], r[1]);
				this.slider.fireRangeChanged(f);
			}else {
				this.slider._getRangeSlider().setRange([0,100]);
			}
			this.slider.setModel(oModel);
			
		},//callbackAjax
		callbackAjaxExport : function (oModel) {
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
								} else{ obj["unit"+(l+1)]=""; }
							}
							data.push(obj);
						}//if
					}//k
				}//j
				arrTag[i].trend={};
			}//i
			
			//2. optimizer 매핑
			var optimizer = result.optimizer;
			for (var i = 0; i < data.length; i++) {
				for (var j = 0; j < optimizer.length; j++) {
					//export 데이터 세팅
					if(i==j){
						data[i].startDateEx=Formatter.getDateformat(new Date(optimizer[j].start_date),true);
						data[i].endDateEx=Formatter.getDateformat(new Date(optimizer[j].end_date),true);
						data[i].statusEx=optimizer[j].status;
					}
					if(data[i].optimizerEx=="On"){
					}else if(optimizer[j].status==1&&optimizer[j].start_date<=data[i].trend&&data[i].trend<=optimizer[j].end_date){
						data[i].optimizerEx="On";
					}else{
						data[i].optimizerEx="Off";
					}					
				}//j
			}//i
			
			if(data&&!data[0].d){
				for (var i = 0; i < data.length; i++) { data[i].d = Formatter.getDateformat(new Date(data[i].trend),true); }
			}
						
			var arr = [{ label: 'Date', 	property: 'd', 			textAlign: 'center', type: 'String',	width: 20 }];
			var oTag = result.tag;
			for (var i = 0; i < oTag.length; i++) {
				arr.push({label:oTag[i].name,property:'value'+(i+1),textAlign:'center',type:'Number'});
				arr.push({label:'Unit',property:'unit'+(i+1),textAlign:'center',type:'String',width: 10});
			}
			arr.push({ label: 'Optimizer', 	property: 'optimizerEx', 	textAlign: 'center', type: 'String', });
			arr.push({ label: ' ', 			property: 'dummy', 			textAlign: 'center', type: 'Number',	width: 10 });
			arr.push({ label: 'start date', property: 'startDateEx', 	textAlign: 'center', type: 'String',	width: 20 });
			arr.push({ label: 'end date', 	property: 'endDateEx', 		textAlign: 'center', type: 'String',	width: 20 });
			arr.push({ label: 'status', 	property: 'statusEx', 		textAlign: 'center', type: 'Number',	width: 15 });
						
			this.byId("__component0---trend--btnExport").setBusy(false);			
			this.onExport(arr, data, "trend");
			
		},//callbackAjaxExport
		onInterval: function() {
			if(this.byId("ckb24").getSelected()){
				this.setPeriodD("6h",this.dpStart,this.dpEnd);
				this.onSearch();
			}
		},
		onInit: function() {
			window.trend = this;
			this.chart = this.byId(this.vizFrameSet.id);
			this.slider = this.byId(this.vizFrameSet.sliderId);
			this.dpStart = this.byId("__component0---trend--dateSearchFragment--dpStart");
			this.dpEnd = this.byId("__component0---trend--dateSearchFragment--dpEnd");
			this.btnSearch = this.byId("__component0---trend--dateSearchFragment--btnSearch");
			this.onAjaxCall({url:this.getApiUrl()+"/trend/category/ddl",callback:"callbackItemAjax"});
			ChartUtil.setSlider(this.vizFrameSet, this);
			var that = this;
			this.getView().addEventDelegate({ 
			   onBeforeShow: function(oEvent) {
				   jQuery.sap.log.info("onBeforeShow");
				   that.setPeriodD("6h",that.dpStart,that.dpEnd);
				   that.oParam.url = that.getApiUrl()+that.oParam.path;
				   that.oParamExport.url = that.getApiUrl()+that.oParamExport.path;
				   that.onSearch();
			   },
			   onAfterShow: function(oEvent) {
				   jQuery.sap.log.info("onAfterShow");
				   $("div[id$=trend] span[id$=-inner]").css("outline","none");
				   $("div[id$=trend] input[type=CheckBox]").parent().css("outline","none");
			   }
			});
		},
		callbackItemAjax: function(oModel){
			this.setModel(oModel,"tag");
			var result=oModel.getData().result;
			this.byId("tagId").setSelectedKeys([result[0].key,result[1].key]);
			this.onSearch();
		},
		rangeChanged: function(oEvent){
			var data = oEvent.getParameters().data;
			if(!data){ data = oEvent.getParameters(); }
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
		},
		onSearch: function(oEvent) {
			if(this.byId("tagId").getSelectedKeys().length<1){ return; }
			var startDate=this.toYYYYMMDD(this.dpStart.getValue());
			var endDate=this.toYYYYMMDD(this.dpEnd.getValue());
			if(new Date(startDate)>new Date(endDate)){
				MessageBox.warning("Start date is greater than end date");
				return this.setPeriodD("6h",this.dpStart,this.dpEnd);
			}
			this.oParam.data.start_date = startDate;
			this.oParam.data.end_date = endDate;
			this.oParam.data.items = this.byId("tagId").getSelectedKeys().toString();
			this.setChartBusy(true);
			this.onAjaxCall(this.oParam);
		},
		toYYYYMMDD: function(val){ return val.substr(6,4)+"-"+val.substr(3,2)+"-"+val.substr(0,2)+" "+val.substr(11); },
		selectionChange: function(oEvent) {
			var oKeys = oEvent.getSource().getSelectedKeys();
			if(oKeys.length>2){ oKeys.shift(); oEvent.getSource().setSelectedKeys(oKeys); }
		},
		renderComplete: function(oEvent) {
			jQuery.sap.log.info("renderComplete"+oEvent.getSource().sId);
			$(".viz-controls-chartTooltip").css("visibility","hidden");
			if(!oEvent.getSource().getModel()){return;}
			var html="";
			$('div[id$=trend] .v-vbcline').each(function (idx) {
				html+="<rect x='"+$(this).attr("x1")+"' y='0' width='"+($(this).attr("x2")-$(this).attr("x1"))+"' height='300' fill='#e5e5e5'/>";
			});
			$("div[id$=trend] .v-gridline-mainline").eq(0).after(html);
			html = $("div[id$=trend] .v-gridline").eq(0).html();
			$("div[id$=trend] .v-gridline").eq(0).html('');
			$("div[id$=trend] .v-gridline").eq(0).html(html);
			
			var oTag = oEvent.getSource().getModel().getData().result.tag;
			for (var i = 0; i < oTag.length; i++) {
				if(i==0){
					$("div[id$=trend] .v-m-yAxis .viz-axis-label text").each(function (idx) {
						$(this).attr("fill","#5899DA").html($(this).html());
					});
				} else if(i==1){
					$("div[id$=trend] .v-m-yAxis2 .viz-axis-label text").each(function (idx) {
						$(this).attr("fill","#E8743B").html($(this).html());
					});
				}
			}
			
			$("div[id$=trendChart] g.v-gridline:gt(0)").attr("stroke","#c5c5c5");
			this.setChartBusy(false);
		},
		setChartBusy: function(b) {
			this.chart.setBusy(b);
			this.slider.setBusy(b);
			this.btnSearch.setBusy(b);			
		},
		onPressExport: function(oEvent) {
			var startDate=this.toYYYYMMDD(this.dpStart.getValue());
			var endDate=this.toYYYYMMDD(this.dpEnd.getValue());
			if(new Date(startDate)>new Date(endDate)){
				MessageBox.warning("Start date is greater than end date");
				return this.setPeriodD("6h",this.dpStart,this.dpEnd);
			}
			this.oParamExport.data.start_date = startDate;
			this.oParamExport.data.end_date = endDate;
			
			this.byId("__component0---trend--btnExport").setBusy(true);
			this.onAjaxCall(this.oParamExport);			
		},
		onUnitChange: function() {
			this.oParam.url = this.getApiUrl()+this.oParam.path;
			this.onSearch();
		}
	});

}, /* bExport= */ true);