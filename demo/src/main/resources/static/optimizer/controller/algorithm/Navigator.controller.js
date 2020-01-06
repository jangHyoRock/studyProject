sap.ui.define([
	"dhi/optimizer/controller/BaseController",
	"dhi/common/util/ChartUtil",
	"sap/m/FlexBox",
	"sap/m/CheckBox",
	"sap/m/Text",
	"sap/m/HBox",
	"sap/m/Input",
	"sap/m/VBox",
	"dhi/common/util/Formatter",
	"sap/ui/model/Filter",
	"sap/m/MessageBox"
	], function (BaseController,ChartUtil,FlexBox,CheckBox,Text,HBox,Input,VBox,Formatter,Filter,MessageBox) {
	"use strict";

	return BaseController.extend("dhi.optimizer.controller.algorithm.Navigator", {
		vizFrameSet: {
			type: "timeseries_line",
			dataset: {
				dimensions: [{ name: 'trend', value: "{trend}", dataType:'date' }],
				data: { path: "/result/data" }
			},
			properties: {
				title: { visible: false },
				legend: { visible: true },
				plotArea: {
					isFixedDataPointSize: true,
					showGap: false,
					line: { marker:{ size: 4 } },
					gap: { barSpacing: 0 },
					dataPointSize:{ max: 200, min: 100 }
				},
				valueAxis: 	{ title: { visible: false } },
				timeAxis: { title: { visible: false }, levels: ["month", "day", "hour", "minute", "second"] },
				interaction: {
					noninteractiveMode: true,
					hover: { stroke: { visible: false } },
					selectability: { axisLabelSelection: false, plotStdSelection: false },
					selected: { stroke: { visible: false } }
				}
			},
			feedItems: [ 
				{ uid: "valueAxis", type: "Measure", values: [] },
				{ uid: "timeAxis", type: "Dimension", values: ["trend"] }
			],
			sliderType: "timeseries_line",
			sliderProperties: {
				title: { visible: false },
				legend: { visible: false },
				valueAxis: 	{ title: { visible: false } },
				timeAxis: { title: { visible: false }, levels: ["month", "day", "hour", "minute", "second"] },
			},
			silderDataset: {
				dimensions: [{ name: 'trend', value: "{trend}", dataType:'date' }],
				data: { path: "/result/data" }
			},
			sliderFeedItems: [ 
				{ uid: "valueAxis", type: "Measure", values: [] },
				{ uid: "timeAxis", type: "Dimension", values: ["trend"] }
			]
		},
		oParam: {
			path:  "/algorithm/navigator/info",
			type: "get",
			callback: "callbackAjax"
		},
		oLimitParam: {
			path:  "/algorithm/navigator/limit_fireball",
			type: "get",
			callback: "callbackLimitAjax"
		},
		oSettingsParam: {
			path:  "/algorithm/navigator/conf",
			type: "get",
			callback: "callbackSettingsAjax"
		},
		oCategoryParam: {
			path:  "/algorithm/navigator/trend/category/ddl",
			type: "get",
			callback: "callbackCategoryAjax"
		},
		callbackCategoryAjax: function(oModel) { this.setModel(oModel,"category"); },
		callbackSettingsAjax: function(oModel) { this.setModel(oModel,"settings"); },
		callbackSettingsSaveAjax: function(oModel) {
			if(oModel.getData().code=200){this.showMsgToast("Saved");}
			this.onAjaxCall(this.oSettingsParam);
		},
		callbackAjax: function(oModel) {
			if(oModel.getData().code!=200){return;}
			var result = oModel.getData().result;
			this.setModel(oModel);
			
			var data = result.gas_press_temp.data;
			for (var i = 0; i < data.length; i++) {
				var item = data[i].item;
				if(item.search(/primry/i)>-1||item.search(/horizontal_gas/i)>-1||item.search(/rh_finish_out_temp/i)>-1
					||item.search(/rh_finish_out_press/i)>-1){
					$("svg[name=svgNavigator] text[item="+item+"]").html(data[i].current);
					$("svg[name=svgNavigator] text[item="+item+"_unit]").html(data[i].unit);
				}else{
					$("svg[name=svgNavigator] text[item="+item+"]").html(data[i].current+" "+data[i].unit);
				}
			}
			
			//emission button
			if(this.byId("tgBtnO2").getPressed())		{ this.setEmission("O2"); }
			else if(this.byId("tgBtnCO").getPressed())	{ this.setEmission("CO"); }
			else if(this.byId("tgBtnNOx").getPressed())	{ this.setEmission("NOx"); }
			else if(this.byId("tgBtnT").getPressed())	{ this.setEmission("T"); }
			
			//uofa total and flow
			for (var i = 0; i < result.combustion_status.combustion_uofa.length; i++) {
				var uofa = result.combustion_status.combustion_uofa[i].uofa;
				$("div[id$=Navigator] div.uofa"+i+" div.sapUiTableCCnt td[id$=row3-col1] div.sapUiTableCell").each(function(){
					$(this).html("<text>"+uofa[3].yaw+"</text>");
				});
				$("div[id$=Navigator] div.uofa"+i+" div.sapUiTableCCnt td[id$=row4-col1] div.sapUiTableCell").each(function(){
					$(this).html("<text>"+uofa[4].flow+" "+uofa[4].flow_unit+"</text>");
					$(this).parent().attr("colspan","3");
				});
			}
			
			//fireball
			var wall_temp = result.combustion_status.spiral_water_wall.sprial_water_wall_temp
			for (var i = 0; i < wall_temp.length; i++) {
				if(wall_temp[i].item.search(/left/i)>-1){
					$("svg[name=svgDeg] text[item=txtLeftAverage]").html(wall_temp[i].current+" Deg");
					this.setFireballAverageCenter("txtLeftAverage","w");
				}else if(wall_temp[i].item.search(/right/i)>-1){
					$("svg[name=svgDeg] text[item=txtRightAverage]").html(wall_temp[i].current+" Deg");
					this.setFireballAverageCenter("txtRightAverage","w");
				}else if(wall_temp[i].item.search(/front/i)>-1){
					$("svg[name=svgDeg] text[item=txtFrontAverage]").html(wall_temp[i].current+" Deg");
					this.setFireballAverageCenter("txtFrontAverage","h");
				}else if(wall_temp[i].item.search(/rear/i)>-1){
					$("svg[name=svgDeg] text[item=txtRearAverage]").html(wall_temp[i].current+" Deg");
					this.setFireballAverageCenter("txtRearAverage","h");
				}
			}

			var position = result.combustion_status.spiral_water_wall.fire_ball_postion;
			if(!position){position={x:0,y:0};}else if(!position.x){position.x=0;}else if(!position.y){position.y=0;}
			$("svg[name=svgDeg] circle[item=fireball]").attr("cx",position.x*0.92+32);
			$("svg[name=svgDeg] circle[item=fireball]").attr("cy",126-position.y*0.72);
			$("svg[name=svgDeg] text[item=fireballText]").html("("+(position.x-50)+","+(position.y-50)+")");
			$("svg[name=svgDeg] text[item=fireballText]").attr("x",position.x*0.92+32-$("svg[name=svgDeg] text[item=fireballText]")[0].getBoundingClientRect().width/2);
			$("svg[name=svgDeg] text[item=fireballText]").attr("y",140-position.y*0.72);
			
			//set FDE color: after
			if(this.flag){
				var sa=result.combustion_status.combustion_sa;
				for (var i = 0; i < sa.length; i++) {
					for (var j = 0; j < sa[i].sa.length; j++) {
						var fde=sa[i].sa[j].fde;
						if(fde.length<3){continue;}
						var html="";
						for (var k = 0; k < fde.length; k++) {
							html += "<text style='color:";
							if(fde[k].search(/U/i)>-1){html+="red";}
							else if(fde[k].search(/D/i)>-1){html+="gold";}
							else if(fde[k].search(/H/i)>-1){html+="black";}
							html+=";'>"+fde[k]+"</text>";
						}
						$("div[id$=Navigator] div.sa"+i+" div.sapUiTableCCnt td[id$=row"+j+"-col3] div.sapUiTableCell").html(html);
					}//for: j
				}//for: i
				
				var uofa=result.combustion_status.combustion_uofa;
				for (var i = 0; i < uofa.length; i++) {
					for (var j = 0; j < uofa[i].uofa.length; j++) {
						var fde=uofa[i].uofa[j].fde;
						if(!fde){continue;}
						if(fde.length<3){continue;}
						var html="";
						for (var k = 0; k < fde.length; k++) {
							html += "<text style='color:";
							if(fde[k].search(/U/i)>-1){html+="red";}
							else if(fde[k].search(/D/i)>-1){html+="gold";}
							else if(fde[k].search(/H/i)>-1){html+="black";}
							html+=";'>"+fde[k]+"</text>";
						}
						$("div[id$=Navigator] div.uofa"+i+" div.sapUiTableCCnt td[id$=row"+j+"-col3] div.sapUiTableCell").html(html);
					}//for: j
				}//for: i
				return;
			}
			
			//set FDE color: first once
			var that=this;
			setTimeout(function(){
				$("div.fde div.sapUiTableCCnt td[id$=col3] div").each(function(idx){
					var fde = $(this).find("span").html();
					var html = "";
					if(!fde){return;}
					if(fde.length<3){return;}
					that.flag=true;
					for (var i = 0; i < fde.length; i++) {
						html += "<text style='color:";
						if(fde[i].search(/U/i)>-1){html+="red";}
						else if(fde[i].search(/D/i)>-1){html+="gold";}
						else if(fde[i].search(/H/i)>-1){html+="black";}
						html+=";'>"+fde[i]+"</text>";
					}
					$(this).html(html);
				});
			},100);
		},//callbackAjax
		setFireballAverageCenter: function(item, type){
			var item = $("svg[name=svgDeg] text[item="+item+"]");
			if(type.search(/w/i)>-1){ item.attr("x",27+51-item[0].getBoundingClientRect().width/2); }
			else{ item.attr("x",-30+40-item[0].getBoundingClientRect().height/2); }
		},
		setEmission: function(s) {
			var analyzer = this.getModel().getData().result.gas_press_temp.gas_analyzer;
			for (var i = 0; i < analyzer.length; i++) {
				if(analyzer[i].item.search(new RegExp(s,"i"))>-1){
					if(!analyzer[i].front1){break;}
					for (var j = 0; j < analyzer[i].front1.length; j++) {
						$("text[item=gas_analyzer_front1_"+j+"]").html(analyzer[i].front1[j]);
					}
					if(!analyzer[i].front2){break;}
					for (var j = 0; j < analyzer[i].front2.length; j++) {
						$("text[item=gas_analyzer_front2_"+j+"]").html(analyzer[i].front2[j]);
					}
					if(!analyzer[i].rear1){break;}
					for (var j = 0; j < analyzer[i].rear1.length; j++) {
						$("text[item=gas_analyzer_rear1_"+j+"]").html(analyzer[i].rear1[j]);
					}
					if(!analyzer[i].rear2){break;}
					for (var j = 0; j < analyzer[i].rear2.length; j++) {
						$("text[item=gas_analyzer_rear2_"+j+"]").html(analyzer[i].rear2[j]);
					}
					
					//set G.A. table
					for ( var item in analyzer[i]) {
						if(item.search(/min/i)==-1&&item.search(/max/i)==-1&&item.search(/avg/i)==-1){continue;}
						this.byId(item).setText(analyzer[i][item]);
					}
				}//if
			}//for: i
		},//setEmission
		onInit : function () {
			var that = this;
			this.setModel(new sap.ui.model.json.JSONModel({data:[{value:"U"},{value:"D"},{value:"H"}]}),"tubeStatus");
			this.getView().addEventDelegate({
			   onBeforeShow: function(oEvent) {
				   jQuery.sap.log.info("onBeforeShow");
				   that.onUnitChange();
			   },
			   onAfterShow: function(oEvent) {
				   jQuery.sap.log.info("onAfterShow");
				   //focus 방지
				   $("div[id$=Navigator] span[id$=-inner]").css("outline","none");
				   $("div[id$=Navigator] tbody td").css("outline","none");
				   that.initTime=performance.now();
			   }
			});
			
			this.byId("tblBnr").addEventDelegate({
				onAfterRendering: function(e) {
					$("div[id$=tblBnr] tbody tr.sapUiTableColHdrTr:nth-child(1) td:lt(3)").each(function (idx){ $(this).attr("rowspan","2"); });
					$("div[id$=tblBnr] tbody tr.sapUiTableColHdrTr:nth-child(2) td:lt(3)").each(function (idx){ $(this).remove(); });
				}
			});
		},
		formatStatus : function (status) { return null==status||undefined==status||status.search(/warning/i)>-1? "Error" : "Success"; },
		onAfterOpen: function() {
			$("section[id*=dialog] input[type=CheckBox]").parent().css("outline","none");
		},
		onAfterOpenTube: function() {
			$("div[id=tblNavigatorAA] div.sapUiTableColRowHdr.sapUiTableSelAllDisabled").css("border-right","none");
			$("div[id=tblNavigatorGG] div.sapUiTableColRowHdr.sapUiTableSelAllDisabled").css("border-right","none");
			
			$("section[id*=dialog] [id$=tblNavigatorAA] tbody tr:first-child td:lt(4)").attr("rowspan","2");
			$("section[id*=dialog] [id$=tblNavigatorAA] tbody tr:last-child td:lt(4)").remove();
			$("section[id*=dialog] [id$=tblNavigatorGG] tbody tr:first-child td:lt(2)").attr("rowspan","2");
			$("section[id*=dialog] [id$=tblNavigatorGG] tbody tr:last-child td:lt(2)").remove();
			
			$("[id$=tblNavigatorAA-table] tr td:nth-child(7)").css("background","rgba(242,242,242,0.5)");
			$("[id$=tblNavigatorAA-table] tr td:nth-child(10)").css("background","rgba(242,242,242,0.5)");
			$("[id$=tblNavigatorAA-table] tr td:nth-child(13)").css("background","rgba(242,242,242,0.5)");
			$("[id$=tblNavigatorAA-table] tr td:nth-last-child(1)").css("background","rgba(242,242,242,0.5)");
			
			$("[id$=tblNavigatorGG-table] tbody tr:even td:first-child").attr("rowspan","2");
			$("[id$=tblNavigatorGG-table] tbody tr:odd td:first-child").remove();
			
			$("[id$=tblNavigatorGG-table] tbody tr td:nth-last-child(2)").css("background","rgba(242,242,242,0.5)");
			$("[id$=tblNavigatorGG-table] tbody tr td:nth-last-child(3)").css("background","rgba(242,242,242,0.5)");
			$("[id$=tblNavigatorGG-table] tbody tr td:nth-last-child(5)").css("background","rgba(242,242,242,0.5)");
			$("[id$=tblNavigatorGG-table] tbody tr td:nth-last-child(6)").css("background","rgba(242,242,242,0.5)");
			$("[id$=tblNavigatorGG-table] tbody tr td:nth-last-child(8)").css("background","rgba(242,242,242,0.5)");
			$("[id$=tblNavigatorGG-table] tbody tr td:nth-last-child(9)").css("background","rgba(242,242,242,0.5)");
			$("[id$=tblNavigatorGG-table] tbody tr td:nth-last-child(11)").css("background","rgba(242,242,242,0.5)");
			$("[id$=tblNavigatorGG-table] tbody tr td:nth-last-child(12)").css("background","rgba(242,242,242,0.5)");
			
			//#a0eab0 #fde468
			$("[id$=tblNavigatorAA-table] tbody tr:lt(6) td:nth-child(1)").css("background","#a0eab0");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-child(7) td:nth-child(4)").css("background","#a0eab0");
			$("[id$=tblNavigatorAA-table] tbody tr:gt(6):lt(2) td:nth-child(3)").css("background","#a0eab0");
			$("[id$=tblNavigatorAA-table] tbody tr:gt(9):lt(2) td:nth-child(2)").css("background","#a0eab0");
			$("[id$=tblNavigatorAA-table] tbody tr:gt(13):lt(2) td:nth-child(2)").css("background","#a0eab0");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-child(13) td:nth-child(3)").css("background","#a0eab0");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-child(14) td:nth-child(4)").css("background","#a0eab0");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-child(17) td:nth-child(4)").css("background","#a0eab0");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-child(18) td:nth-child(3)").css("background","#a0eab0");
			$("[id$=tblNavigatorAA-table] tbody tr:gt(17):lt(2) td:nth-child(2)").css("background","#a0eab0");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-last-child(4) td:nth-child(3)").css("background","#a0eab0");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-last-child(2) td:nth-child(3)").css("background","#a0eab0");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-last-child(3) td:nth-child(4)").css("background","#a0eab0");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-last-child(1) td:nth-child(4)").css("background","#a0eab0");
			
			$("[id$=tblNavigatorAA-table] tbody tr:lt(2) td:nth-child(2)").css("background","#fde468");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-child(3) td:nth-child(3)").css("background","#fde468");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-child(5) td:nth-child(3)").css("background","#fde468");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-child(4) td:nth-child(4)").css("background","#fde468");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-child(6) td:nth-child(4)").css("background","#fde468");
			$("[id$=tblNavigatorAA-table] tbody tr:gt(5):lt(6) td:nth-child(1)").css("background","#fde468");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-child(13) td:nth-child(4)").css("background","#fde468");
			$("[id$=tblNavigatorAA-table] tbody tr:gt(12):lt(2) td:nth-child(3)").css("background","#fde468");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-child(16) td:nth-child(4)").css("background","#fde468");
			$("[id$=tblNavigatorAA-table] tbody tr:gt(15):lt(2) td:nth-child(2)").css("background","#fde468");
			$("[id$=tblNavigatorAA-table] tbody tr:gt(19):lt(2) td:nth-child(2)").css("background","#fde468");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-last-child(6) td:nth-child(3)").css("background","#fde468");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-last-child(1) td:nth-child(3)").css("background","#fde468");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-last-child(2) td:nth-child(4)").css("background","#fde468");
			$("[id$=tblNavigatorAA-table] tbody tr:nth-last-child(5) td:nth-child(4)").css("background","#fde468");
		},
		onBeforeOpenTube: function() {
			this.setPopoverStyle(true);
		},
		onAfterCloseTube: function() {
			this.setPopoverStyle(false);
		},
		setPopoverStyle: function(b){
			if(this.cssStyle){
				if(b){
					this.cssStyle[0].height="20px"; this.cssStyle[0].lineHeight="20px";
					this.cssStyle[1].minWidth="40px"; this.cssStyle[2].minWidth="40px";
				}else{
					this.cssStyle[0].height=""; this.cssStyle[0].lineHeight="";
					this.cssStyle[1].minWidth=""; this.cssStyle[2].minWidth="";
				}
				return;
			}
			this.cssStyle=[];
			for (var i= 0; i < document.styleSheets.length; i++) {
				if(!document.styleSheets[i].href){continue;}
				if(document.styleSheets[i].href.search(/optimizer/i)>-1){
					for (var j = 0; j < document.styleSheets[i].cssRules.length; j++) {
						var selectorText=document.styleSheets[i].cssRules[j].selectorText
						if(selectorText=='div[id*="popover"] ul li.sapMSelectListItem'){
							document.styleSheets[i].cssRules[j].style.height="20px";
							document.styleSheets[i].cssRules[j].style.lineHeight="20px";
							this.cssStyle.push(document.styleSheets[i].cssRules[j].style);
						}else if(selectorText=='.sapMPopover'){
							document.styleSheets[i].cssRules[j].style.minWidth="40px";
							this.cssStyle.push(document.styleSheets[i].cssRules[j].style);
						}else if(selectorText=='.sapMPopoverCont'){
							document.styleSheets[i].cssRules[j].style.minWidth="40px";
							this.cssStyle.push(document.styleSheets[i].cssRules[j].style);
						}
					}//for:j
				}//if
			}//for:i
		},
		validNumber: function(oEvent) {
			if(oEvent.getParameter("value").length>3){oEvent.getSource().setValue(999);}
		},
		onUnitChange: function() {
			this.oParam.url = this.getApiUrl()+this.oParam.path;
			this.oLimitParam.url = this.getApiUrl()+this.oLimitParam.path;
			this.oSettingsParam.url = this.getApiUrl()+this.oSettingsParam.path;
			this.onAjaxCall(this.oParam);
		},
		onInterval: function() {
			if(performance.now()-this.initTime<2000){return;}
			this.onUnitChange();
			
			//Mt Temp Dialog
			if(this.myTempDialog&&this.myTempDialog.isOpen()){
				var oParam = {
					url:this.getApiUrl()+"/algorithm/navigator/spiral_vertical_water_wall_metal_temp/info",
					callback: "callbackMtTempAjax"
				};
				this.onAjaxCall(oParam);
			}
		},
		onSave: function(oEvent) {
			var oSaveParam = {
				url:  this.getApiUrl()+"/algorithm/navigator/info/setting",
				type: "post",
				callback: "callbackSaveAjax"
			};
			oSaveParam.data = eval('(' + JSON.stringify(this.getModel().getData().result) + ')'); //object copy
			delete oSaveParam.data.gas_press_temp;
			delete oSaveParam.data.combustion_status.combustion_flow;
			delete oSaveParam.data.combustion_status.fireball_data;
			this.onAjaxCall(oSaveParam);
		},
		callbackSaveAjax: function (oModel) {
			if(oModel.getData().code=200){ this.showMsgToast("Saved"); }
			this.onUnitChange();
		},
		onSelectChange: function (oEvent) {
			var e = oEvent.getSource();
			if(e==this.select1)		{ this.callTrend(this.select1,this.chart1,this.slider1,"callbackTrendAjax1"); }
			else if(e==this.select2){ this.callTrend(this.select2,this.chart2,this.slider2,"callbackTrendAjax2"); }
			else if(e==this.select3){ this.callTrend(this.select3,this.chart3,this.slider3,"callbackTrendAjax3"); }
			else if(e==this.select4){ this.callTrend(this.select4,this.chart4,this.slider4,"callbackTrendAjax4"); }
		},
		afterTrendOpen: function (oEvent) {
			var arr=this.getManifestEntry("/sap.ui5/config/navigatorTrendSelect")
			this.select1.setSelectedItem(this.select1.getItems()[arr[0]]);
			this.select2.setSelectedItem(this.select2.getItems()[arr[1]]);
			this.select3.setSelectedItem(this.select3.getItems()[arr[2]]);
			this.select4.setSelectedItem(this.select4.getItems()[arr[3]]);
			this.setPeriodD("d",this.dpStart,this.dpEnd);
			this.onSearch();
		},
		beforeTrendClose: function (oEvent) {
			this.chart1.setModel(null);this.slider1.setModel(null);
			this.chart2.setModel(null);this.slider2.setModel(null);
			this.chart3.setModel(null);this.slider3.setModel(null);
			this.chart4.setModel(null);this.slider4.setModel(null);
		},
		onSearch: function(oEvent) {
			this.callTrend(this.select1,this.chart1,this.slider1,"callbackTrendAjax1");
			this.callTrend(this.select2,this.chart2,this.slider2,"callbackTrendAjax2");
			this.callTrend(this.select3,this.chart3,this.slider3,"callbackTrendAjax3");
			this.callTrend(this.select4,this.chart4,this.slider4,"callbackTrendAjax4");
		},
		callTrend: function (oSelect, oChart, oSlider, callbackName) {
			var startDate=this.toYYYYMMDD(this.dpStart.getValue());
			var endDate=this.toYYYYMMDD(this.dpEnd.getValue());
			if(new Date(startDate)>new Date(endDate)){
				MessageBox.warning("Start date is greater than end date");
				return this.setPeriodD("d",this.dpStart,this.dpEnd);
			}
			oSelect.setBusy(true);
			oChart.setBusy(true);
			oSlider.setBusy(true);
			var oTrendParam = {
				url:this.getApiUrl()+"/algorithm/navigator/trend/data",
				data:{item:oSelect.getSelectedKey(),start_date:startDate,end_date:endDate},
				callback: callbackName
			};
			this.onAjaxCall(oTrendParam);
		},
		toYYYYMMDD: function(val){ return val.substr(6,4)+"-"+val.substr(3,2)+"-"+val.substr(0,2)+" "+val.substr(11); },
		callbackTrendAjax: function (oModel, oChart, oSlider) {
			var result = oModel.getData().result;
			// trend 매핑
			var data = result.data = [];
			var arrTag = result.tag;
			var arrName = [], arrUnit = [];
			for (var i = 0; i < arrTag.length; i++) {
				arrName.push(arrTag[i].name); arrUnit.push(arrTag[i].unit);
			}
			for (var i = 0; i < arrTag.length; i++) {
				var arrTrend = arrTag[i].trend;
				for (var j = 0; j < arrTrend.length; j++) {
					for (var k in arrTrend[j]) {
						var keyflag = true;
						if(i!=0){
							for (var l = 0; l < data.length; l++) {
								if(data[l].trend==k){
									data[l][arrName[i]]=arrTrend[j][k];
									keyflag=false; break;
								}
							}
						}
						if(keyflag){
							var obj = {};
							obj.trend=k;
							for (var l = 0; l < arrTag.length; l++) {
								if(i==l){ obj[arrName[i]]=arrTrend[j][k]; }
							}
							data.push(obj);
						}//if
					}//k
				}//j
				arrTag[i].trend={};
			}//i
			
			// set feedItems and measures
			var vizFrameSet = eval('(' + JSON.stringify(this.vizFrameSet) + ')');
			vizFrameSet.dataset.measures=[];
			vizFrameSet.feedItems[0].values=[];
			vizFrameSet.silderDataset.measures=[];
			vizFrameSet.sliderFeedItems[0].values=[];
			for (var i = 0; i < arrTag.length; i++) {
				var obj = {};
				obj.name=arrName[i]+" ("+arrUnit[i]+")";
				obj.value="{"+arrName[i]+"}";
				vizFrameSet.dataset.measures.push(obj);
				vizFrameSet.feedItems[0].values.push(arrName[i]+" ("+arrUnit[i]+")");
				vizFrameSet.silderDataset.measures.push(obj);
				vizFrameSet.sliderFeedItems[0].values.push(arrName[i]+" ("+arrUnit[i]+")");
			}
			// no data
			if(arrName.length==0){
				vizFrameSet.feedItems[0].values=["a"];
				vizFrameSet.dataset.measures.push({name:"a",value:"{a}"});
				vizFrameSet.sliderFeedItems[0].values=["a"];
				vizFrameSet.silderDataset.measures.push({name:"a",value:"{a}"});
			}
			ChartUtil.setVizFrame(vizFrameSet, this, oChart);
			oChart.setModel(oModel);
			
			//slider
			ChartUtil.setSlider(vizFrameSet,oSlider,true);
			oSlider._getRangeSlider().setRange([0,100]);
			oSlider.setModel(oModel);
		},
		callbackTrendAjax1: function (oModel) { this.callbackTrendAjax(oModel, this.chart1, this.slider1); },
		callbackTrendAjax2: function (oModel) { this.callbackTrendAjax(oModel, this.chart2, this.slider2); },
		callbackTrendAjax3: function (oModel) { this.callbackTrendAjax(oModel, this.chart3, this.slider3); },
		callbackTrendAjax4: function (oModel) { this.callbackTrendAjax(oModel, this.chart4, this.slider4); },
		renderComplete: function(oEvent) {
			var sId = oEvent.getSource().sId;
			if(sId.search(1)>-1)		{ this.select1.setBusy(false); this.slider1.setBusy(false);}
			else if(sId.search(2)>-1)	{ this.select2.setBusy(false); this.slider2.setBusy(false);}
			else if(sId.search(3)>-1)	{ this.select3.setBusy(false); this.slider3.setBusy(false);}
			else if(sId.search(4)>-1)	{ this.select4.setBusy(false); this.slider4.setBusy(false);}
			oEvent.getSource().setBusy(false);
		},
		onMaskPress: function() {
			if (!this.myTempDialog) {
				this.myTempDialog = sap.ui.xmlfragment("dhi.optimizer.fragment.NavigatorMtTemp", this);
				this.getView().addDependent(this.myTempDialog);
			}
			this.myTempDialog.open();
			var oParam = {
				url:this.getApiUrl()+"/algorithm/navigator/spiral_vertical_water_wall_metal_temp/info",
				callback: "callbackMtTempAjax"
			};
			this.onAjaxCall(oParam);
		},
		onDialogPress: function(oEvent) {
			var txt = oEvent.getSource().getText();
			if(txt.search(/Trend/i)>-1){
				if (!this.trendDialog) {
					this.trendDialog = sap.ui.xmlfragment("dhi.optimizer.fragment.NavigatorTrend", this);
					this.getView().addDependent(this.trendDialog);
					this.chart1=sap.ui.getCore().byId("chartNavigatorTrend1");
					this.chart2=sap.ui.getCore().byId("chartNavigatorTrend2");
					this.chart3=sap.ui.getCore().byId("chartNavigatorTrend3");
					this.chart4=sap.ui.getCore().byId("chartNavigatorTrend4");
					
					this.select1=sap.ui.getCore().byId("selectNavigatorTrend1");
					this.select2=sap.ui.getCore().byId("selectNavigatorTrend2");
					this.select3=sap.ui.getCore().byId("selectNavigatorTrend3");
					this.select4=sap.ui.getCore().byId("selectNavigatorTrend4");
					
					this.slider1=sap.ui.getCore().byId("sliderNavigatorTrend1");
					this.slider2=sap.ui.getCore().byId("sliderNavigatorTrend2");
					this.slider3=sap.ui.getCore().byId("sliderNavigatorTrend3");
					this.slider4=sap.ui.getCore().byId("sliderNavigatorTrend4");
					
					this.dpStart=sap.ui.getCore().byId("datePickerStartNavigatorTrend");
					this.dpEnd=sap.ui.getCore().byId("datePickerEndNavigatorTrend");
					this.btnSearch=sap.ui.getCore().byId("buttonSearchNavigatorTrend");
				}
				this.trendDialog.open();
				this.oCategoryParam.url = this.getApiUrl()+this.oCategoryParam.path;
				this.onAjaxCall(this.oCategoryParam);
			}else if(txt.search(/Limit/i)>-1){
				if (!this.limitsDialog) {
					this.limitsDialog = sap.ui.xmlfragment("dhi.optimizer.fragment.NavigatorLimits", this);
					this.getView().addDependent(this.limitsDialog);
				}
				this.limitsDialog.open();
				this.oLimitParam.url = this.getApiUrl()+this.oLimitParam.path;
				this.onAjaxCall(this.oLimitParam);
			}else if(txt.search(/Settings/i)>-1){
				if (!this.settingsDialog) {
					this.settingsDialog = sap.ui.xmlfragment("dhi.optimizer.fragment.NavigatorSettings", this);
					this.getView().addDependent(this.settingsDialog);
				}
				this.settingsDialog.open();
				this.oSettingsParam.url = this.getApiUrl()+this.oSettingsParam.path;
				this.onAjaxCall(this.oSettingsParam);
			}else if(txt.search(/MT Temp./i)>-1){
				this.onMaskPress();
			}else if(txt.search(/Tube Project Set/i)>-1){
				if (!this.tubeProjectDialog) {
					this.tubeProjectDialog = sap.ui.xmlfragment("dhi.optimizer.fragment.NavigatorTubeProject", this);
					this.getView().addDependent(this.tubeProjectDialog);
				}
				this.tubeProjectDialog.open();
				var oParam = {
					url:this.getApiUrl()+"/algorithm/navigator/water_wall_matching_table/info",
					callback: "callbackTubeAjax"
				};
				this.onAjaxCall(oParam);
			}
		},
		callbackTubeAjax: function(oModel){
			var deviation = oModel.getProperty("/result/deviation");
			for (var i = 0; i < deviation.length; i++) { deviation[i].status="None"; }
			var highLow = oModel.getProperty("/result/highLow");
			for (var i = 0; i < highLow.length; i++) { highLow[i].status="None"; }
			this.setModel(oModel,"tube");
		},
		callbackMtTempAjax: function(oModel){
			this.setModel(oModel,"MtTemp");
			this.setMtTemp();
		},//callbackMtTempAjax
		setMtTemp: function(){
			var oModel = this.getModel("MtTemp");
			if(!oModel){return;}
			var rectHtml="", txtHtml="", tubeHtml="", borderHtml="", maximumHtml="";
			var selectedKey = sap.ui.getCore().byId("sgmtMtTemp").getSelectedKey();
			var tubeWall;
			var ckbMtTempMaximum = sap.ui.getCore().byId("ckbMtTempMaximum");
			var ckbMtTempNumber = sap.ui.getCore().byId("ckbMtTempNumber");
			
			var min=0;
			if(sap.ui.getCore().byId("ckbMtTempMinimum").getSelected()){
				min=Number(sap.ui.getCore().byId("inputMtTempMinimum").getValue());
			}
			
			var sMax = oModel.getData().result.spiral_tube_wall_metal_temp.max_metal_temp;
			var vMax = oModel.getData().result.vertical_tube_wall_metal_temp.max_metal_temp;
			var bothMax = sMax>vMax?sMax:vMax;
			if(selectedKey=="3"){
				ckbMtTempMaximum.setEditable(false);
				ckbMtTempNumber.setEditable(false);
				$("svg[name=svgMtTemp] g text[item=txtCenterName1]").html("");
				$("svg[name=svgMtTemp] g text[item=txtCenterName2]").html("");
				return this.setMtTempBoth(oModel.getData().result, min, bothMax);
			}
			
			ckbMtTempMaximum.setEditable(true);
			ckbMtTempNumber.setEditable(true);
			if(selectedKey=="1"){
				tubeWall = oModel.getData().result.spiral_tube_wall_metal_temp;
				$("svg[name=svgMtTemp] g text[item=txtCenterName1]").html("SPIRAL");
				$("svg[name=svgMtTemp] g[item=rectG]").attr("fill","skyblue");
			}else{
				tubeWall=oModel.getData().result.vertical_tube_wall_metal_temp;
				$("svg[name=svgMtTemp] g text[item=txtCenterName1]").html("VERTICAL");
				$("svg[name=svgMtTemp] g[item=rectG]").attr("fill","#ebbe87");
			}
			$("svg[name=svgMtTemp] g text[item=txtCenterName2]").html("EVAPORATOR Temp.");
			this.setItemCenter("txtCenterName1","w");
			this.setItemCenter("txtCenterName2","w");
			
			//set Minimum
			var max = tubeWall.max_metal_temp;
			for (var item in tubeWall) {
				if(typeof tubeWall[item]=="string"){continue;}
				var temp_item=tubeWall[item].temp_item;
				for (var i = 0; i < temp_item.length; i++) {
					temp_item[i].per=Math.round((temp_item[i].temp_value-min)/(bothMax-min)*100);
					if(temp_item[i].per<0 || !Number.isFinite(temp_item[i].per) || temp_item[i].temp_value-min<0){
						temp_item[i].per=0;
					}
				}
			}
			
			var temp_item=tubeWall.tube_wall_front.temp_item;
			for (var i = 0; i < temp_item.length; i++) {
				var lineY = 85+(i+1)*20;
				borderHtml+="<rect x='58' y='"+lineY+"' width='100' height='10'/>";
				if(max==temp_item[i].temp_value){
					maximumHtml+="<rect x='"+(158-temp_item[i].per)+"' y='"+lineY+"' width='"+temp_item[i].per+"' height='10'/>";
				}
				rectHtml+="<rect x='"+(158-temp_item[i].per)+"' y='"+lineY+"' width='"+temp_item[i].per+"' height='10' />";
				
				var txtX=118;
				if(temp_item[i].temp_value<100){txtX+=9;}else if(temp_item[i].temp_value<10){txtX+=19;}
				txtHtml+="<text x='"+txtX+"' y='"+(lineY+9)+"'>"+temp_item[i].temp_value+"</text>";
				
				var tubeY=114+i*20;
				var tubeX=165;
				if(temp_item[i].tube_no>99){tubeX-=6;} else if(temp_item[i].tube_no>9){tubeX-=2;}
				tubeHtml+="<text x='"+tubeX+"' y='"+tubeY+"'>"+temp_item[i].tube_no+"</text>";
			}
			
			var temp_item=tubeWall.tube_wall_rear.temp_item;
			for (var i = 0; i < temp_item.length; i++) {
				var lineY = 85+(i+1)*20;
				borderHtml+="<rect x='732' y='"+lineY+"' width='100' height='10'/>";
				if(max==temp_item[i].temp_value){
					maximumHtml+="<rect x='732' y='"+lineY+"' width='"+temp_item[i].per+"' height='10'/>";
				}
				rectHtml+="<rect x='732' y='"+lineY+"' width='"+temp_item[i].per+"' height='10'/>";
				txtHtml+="<text x='734' y='"+(lineY+9)+"'>"+temp_item[i].temp_value+"</text>";
				
				var tubeY=114+i*20;
				var tubeX=718;
				if(temp_item[i].tube_no>99){tubeX-=6;} else if(temp_item[i].tube_no>9){tubeX-=2;}
				tubeHtml+="<text x='"+tubeX+"' y='"+tubeY+"'>"+temp_item[i].tube_no+"</text>";
			}
			
			var temp_item=tubeWall.tube_wall_left.temp_item;
			for (var i = 0; i < temp_item.length; i++) {
				var lineX = 160+(i+1)*20;
				borderHtml+="<rect x='"+lineX+"' y='0' width='10' height='85'/>";
				if(max==temp_item[i].temp_value){
					maximumHtml+="<rect x='"+lineX+"' y='"+(85-temp_item[i].per*0.85)+"' width='10' height='"+temp_item[i].per*0.85+"'/>";
				}
				rectHtml+="<rect x='"+lineX+"' y='"+(85-temp_item[i].per*0.85)+"' width='10' height='"+temp_item[i].per*0.85+"' />";
				txtHtml+="<text x='25' y='"+(lineX+9)+"' transform='rotate(270 50,50)'>"+temp_item[i].temp_value+"</text>";
				
				tubeX=181+i*20;
				if(temp_item[i].tube_no>99){tubeX-=5;} else if(temp_item[i].tube_no>9){tubeX-=2;}
				tubeHtml+="<text x='"+tubeX+"' y='99'>"+temp_item[i].tube_no+"</text>";
			}
			
			var temp_item=tubeWall.tube_wall_right.temp_item;
			for (var i = 0; i < temp_item.length; i++) {
				var lineX = 160+(i+1)*20;
				borderHtml+="<rect x='"+lineX+"' y='715' width='10' height='85'/>";
				if(max==temp_item[i].temp_value){
					maximumHtml+="<rect x='"+lineX+"' y='715' width='10' height='"+temp_item[i].per*0.85+"'/>";
				}
				rectHtml+="<rect x='"+lineX+"' y='715' width='10' height='"+temp_item[i].per*0.85+"'/>";
				
				var txtY=-657;
				if(temp_item[i].temp_value<100){txtY+=9;}else if(temp_item[i].temp_value<10){txtY+=19;}
				txtHtml+="<text x='"+txtY+"' y='"+(lineX+9)+"' transform='rotate(270 50,50)'>"+temp_item[i].temp_value+"</text>";
				
				tubeX=181+i*20;
				if(temp_item[i].tube_no>99){tubeX-=5;} else if(temp_item[i].tube_no>9){tubeX-=2;}
				tubeHtml+="<text x='"+tubeX+"' y='710'>"+temp_item[i].tube_no+"</text>";
			}
			
			$("svg[name=svgMtTemp] g[item=rectG]").html(rectHtml);
			$("svg[name=svgMtTemp] g[item=rectMaximumG]").html(maximumHtml);
			$("svg[name=svgMtTemp] g[item=rectSpiralG]").html("");
			$("svg[name=svgMtTemp] g[item=rectVerticalG]").html("");
			$("svg[name=svgMtTemp] g[item=borderG]").html(borderHtml);
			$("svg[name=svgMtTemp] g[item=txtG]").html(txtHtml);
			$("svg[name=svgMtTemp] g[item=tubeG]").html(tubeHtml);
			$("svg[name=svgMtTemp] g text[item=txtLeftAverage]").html("LEFT("+tubeWall.tube_wall_left.temp_avg_value+")");
			$("svg[name=svgMtTemp] g text[item=txtRightAverage]").html("RIGHT("+tubeWall.tube_wall_right.temp_avg_value+")");
			$("svg[name=svgMtTemp] g text[item=txtFrontAverage]").html("FRONT("+tubeWall.tube_wall_front.temp_avg_value+")");
			$("svg[name=svgMtTemp] g text[item=txtRearAverage]").html("REAR("+tubeWall.tube_wall_rear.temp_avg_value+")");
			this.setItemCenter("txtLeftAverage","w");
			this.setItemCenter("txtRightAverage","w");
			this.setItemCenter("txtFrontAverage","h");
			this.setItemCenter("txtRearAverage","h");
		},//setMtTemp
		setMtTempBoth: function(result, min, bothMax){
			for (var obj in result) {
				for (var item in result[obj]) {
					if(typeof result[obj][item]=="string"){continue;}
					var temp_item=result[obj][item].temp_item;
					for (var i = 0; i < temp_item.length; i++) {
						temp_item[i].per=Math.round((temp_item[i].temp_value-min)/(bothMax-min)*100);
						if(temp_item[i].per<0 || !Number.isFinite(temp_item[i].per) || temp_item[i].temp_value-min<0){
							temp_item[i].per=0;
						}
					}
				}
			}
			
			var rectSpiralHtml="", rectVerticalHtml="", borderHtml="";

			var temp_item=result.spiral_tube_wall_metal_temp.tube_wall_front.temp_item;
			for (var i = 0; i < temp_item.length; i++) {
				var lineY = 85+(i+1)*20;
				borderHtml+="<rect x='58' y='"+lineY+"' width='100' height='10'/>";
				rectSpiralHtml+="<rect x='"+(158-temp_item[i].per)+"' y='"+lineY+"' width='"+temp_item[i].per+"' height='5' />";
			}
			var temp_item=result.vertical_tube_wall_metal_temp.tube_wall_front.temp_item;
			for (var i = 0; i < temp_item.length; i++) {
				var lineY = 90+(i+1)*20;
				rectVerticalHtml+="<rect x='"+(158-temp_item[i].per)+"' y='"+lineY+"' width='"+temp_item[i].per+"' height='5' />";
			}
			
			var temp_item=result.spiral_tube_wall_metal_temp.tube_wall_rear.temp_item;
			for (var i = 0; i < temp_item.length; i++) {
				var lineY = 85+(i+1)*20;
				borderHtml+="<rect x='732' y='"+lineY+"' width='100' height='10'/>";
				rectSpiralHtml+="<rect x='732' y='"+lineY+"' width='"+temp_item[i].per+"' height='10'/>";
			}
			//vertical rear
			
			var temp_item=result.spiral_tube_wall_metal_temp.tube_wall_left.temp_item;
			for (var i = 0; i < temp_item.length; i++) {
				var lineX = 160+(i+1)*20;
				borderHtml+="<rect x='"+lineX+"' y='0' width='10' height='85'/>";
				rectSpiralHtml+="<rect x='"+lineX+"' y='"+(85-temp_item[i].per*0.85)+"' width='5' height='"+temp_item[i].per*0.85+"' />";
			}
			var temp_item=result.vertical_tube_wall_metal_temp.tube_wall_left.temp_item;
			for (var i = 0; i < temp_item.length; i++) {
				var lineX = 165+(i+1)*20;
				rectVerticalHtml+="<rect x='"+lineX+"' y='"+(85-temp_item[i].per*0.85)+"' width='5' height='"+temp_item[i].per*0.85+"' />";
			}
			
			var temp_item=result.spiral_tube_wall_metal_temp.tube_wall_right.temp_item;
			for (var i = 0; i < temp_item.length; i++) {
				var lineX = 160+(i+1)*20;
				borderHtml+="<rect x='"+lineX+"' y='715' width='10' height='85'/>";
				rectSpiralHtml+="<rect x='"+lineX+"' y='715' width='5' height='"+temp_item[i].per*0.85+"'/>";
			}
			var temp_item=result.vertical_tube_wall_metal_temp.tube_wall_right.temp_item;
			for (var i = 0; i < temp_item.length; i++) {
				var lineX = 165+(i+1)*20;
				rectVerticalHtml+="<rect x='"+lineX+"' y='715' width='5' height='"+temp_item[i].per*0.85+"'/>";
			}
			
			$("svg[name=svgMtTemp] g[item=rectG]").html("");
			$("svg[name=svgMtTemp] g[item=rectMaximumG]").html("");
			$("svg[name=svgMtTemp] g[item=rectSpiralG]").html(rectSpiralHtml);
			$("svg[name=svgMtTemp] g[item=rectVerticalG]").html(rectVerticalHtml);
			$("svg[name=svgMtTemp] g[item=borderG]").html(borderHtml);
			$("svg[name=svgMtTemp] g[item=txtG]").html("");
			$("svg[name=svgMtTemp] g[item=tubeG]").html("");
			$("svg[name=svgMtTemp] g text[item=txtLeftAverage]").html("LEFT("+result.spiral_tube_wall_metal_temp.tube_wall_left.temp_avg_value+"/"+result.vertical_tube_wall_metal_temp.tube_wall_left.temp_avg_value+")");
			$("svg[name=svgMtTemp] g text[item=txtRightAverage]").html("RIGHT("+result.spiral_tube_wall_metal_temp.tube_wall_right.temp_avg_value+"/"+result.vertical_tube_wall_metal_temp.tube_wall_right.temp_avg_value+")");
			$("svg[name=svgMtTemp] g text[item=txtFrontAverage]").html("FRONT("+result.spiral_tube_wall_metal_temp.tube_wall_front.temp_avg_value+"/"+result.vertical_tube_wall_metal_temp.tube_wall_front.temp_avg_value+")");
			$("svg[name=svgMtTemp] g text[item=txtRearAverage]").html("REAR("+result.spiral_tube_wall_metal_temp.tube_wall_rear.temp_avg_value+")");
			this.setItemCenter("txtLeftAverage","w");
			this.setItemCenter("txtRightAverage","w");
			this.setItemCenter("txtFrontAverage","h");
			this.setItemCenter("txtRearAverage","h");
		},
		setItemCenter: function(item, type){
			var item = $("svg[name=svgMtTemp] g text[item="+item+"]");
			if(type.search(/w/i)>-1){ item.attr("x",180+265-item[0].getBoundingClientRect().width/2); }
			else{ item.attr("x",-595+295-item[0].getBoundingClientRect().height/2); }
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
            var sId = oEvent.getSource().sId;
			if(sId.search(1)>-1)		{ this.chart1.getDataset().getBinding('data').filter([dateFilter]);}
			else if(sId.search(2)>-1)	{ this.chart2.getDataset().getBinding('data').filter([dateFilter]);}
			else if(sId.search(3)>-1)	{ this.chart3.getDataset().getBinding('data').filter([dateFilter]);}
			else if(sId.search(4)>-1)	{ this.chart4.getDataset().getBinding('data').filter([dateFilter]);}
		},
		onSettingsSave: function(oEvent) {
			var oSettingsSaveParam = {
				url: this.getApiUrl()+"/algorithm/navigator/conf/setting",
				data: this.getModel("settings").getProperty("/result"),
				type: "post",
				callback: "callbackSettingsSaveAjax"
			};
			this.onAjaxCall(oSettingsSaveParam);
		},
		onTubeSave: function(oEvent) {
			var oParam = {
				url:this.getApiUrl()+"/algorithm/navigator/water_wall_matching_table/setting",
				type:"post",
				data: this.getModel("tube").getProperty("/result"),
				callback: "callbackTubeSaveAjax"
			};
			this.onAjaxCall(oParam);
		},
		callbackTubeSaveAjax: function(oModel) {
			if(oModel.getData().code=200){this.showMsgToast("Saved");}
			var oParam = {
				url:this.getApiUrl()+"/algorithm/navigator/water_wall_matching_table/info",
				callback: "callbackTubeAjax"
			};
			this.onAjaxCall(oParam);
		},
		onSelectionChange: function(oEvent) { this.setMtTemp(); },
		onCheckBoxMinimum: function(oEvent) { this.setMtTemp(); },
		onLiveChangeMinimum: function(oEvent) {
			if(!oEvent.getParameters().value){ oEvent.getSource().setValue(0); }
			this.setMtTemp();
		},
		onCheckBoxMaximum: function(oEvent) {
			if(oEvent.getParameters().selected){ $("svg[name=svgMtTemp] g[item=rectMaximumG]").attr("fill","#ff3434"); }
			else{ $("svg[name=svgMtTemp] g[item=rectMaximumG]").attr("fill","none"); }
		},
		onCheckBoxNumber: function(oEvent) {
			if(oEvent.getParameters().selected){ $("svg[name=svgMtTemp] g[item=txtG]").attr("stroke-width","1"); }
			else{ $("svg[name=svgMtTemp] g[item=txtG]").attr("stroke-width","0"); }
		},
		onCheckBoxBorder: function(oEvent) {
			if(oEvent.getParameters().selected){ $("svg[name=svgMtTemp] g[item=borderG]").attr("stroke-width","1"); }
			else{ $("svg[name=svgMtTemp] g[item=borderG]").attr("stroke-width","0"); }
		},
		onEmissionPress: function(oEvent) {
			var button=oEvent.getSource();
			if(button.getPressed()){
				this.byId("tgBtnO2").setPressed(false);
				this.byId("tgBtnCO").setPressed(false);
				this.byId("tgBtnNOx").setPressed(false);
				this.byId("tgBtnT").setPressed(false);
			}
			button.setPressed(true);
			
			//emission button
			if(this.byId("tgBtnO2").getPressed())		{ this.setEmission("O2"); }
			else if(this.byId("tgBtnCO").getPressed())	{ this.setEmission("CO"); }
			else if(this.byId("tgBtnNOx").getPressed())	{ this.setEmission("NOx"); }
			else if(this.byId("tgBtnT").getPressed())	{ this.setEmission("T"); }
		},
		callbackLimitAjax: function(oModel) {
			if(oModel.getData().code!=200){return;}
			this.setModel(oModel,"limit");
			
			//do once
			if(this.buildLimit){return;}
			this.buildLimit=true
			
			var that= this;
			var limit = oModel.getData().result.limit;
			for (var i = 0; i < limit.length; i++) {
				//limits column
				var oFlexBox = new FlexBox({
					alignItems: "Center",
					justifyContent: "Start",
					direction: "Row"
				});
				var oText = new Text({text: "{limit>/result/limit/"+i+"/title}"});
				if(limit[i].type.search(/group/i)>-1){
					oFlexBox.addItem(new HBox({width: "48px"}));
					oText.addStyleClass("txtNavigatorUnit");
				}else{
					oFlexBox.addItem(new CheckBox({selected: "{limit>/result/limit/"+i+"/use}"}));
				}
				oFlexBox.addItem(oText);
				this.limitsDialog.getContent()[0].getItems()[0].getItems()[2].addItem(oFlexBox);
				
				//high column
				if(limit[i].type.search(/group/i)>-1){
					this.limitsDialog.getContent()[0].getItems()[2].getItems()[2].addItem(new HBox({height: "36px"}));
				}else{
					var oInput=new Input({width: "60px", type: "Number", value: "{limit>/result/limit/"+i+"/high}", liveChange: function(oEvent){that.validNumber(oEvent)}});
					this.limitsDialog.getContent()[0].getItems()[2].getItems()[2].addItem(oInput);
				}
				
				//margin column
				if(limit[i].type.search(/group/i)>-1){
					this.limitsDialog.getContent()[0].getItems()[4].getItems()[2].addItem(new VBox({height: "36px"}));
				}else{
					var oInput=new Input({width: "60px", type: "Number", value: "{limit>/result/limit/"+i+"/margin}", liveChange: function(oEvent){that.validNumber(oEvent)}});
					this.limitsDialog.getContent()[0].getItems()[4].getItems()[2].addItem(oInput);
				}
				
				//unit column
				if(limit[i].type.search(/group/i)>-1){
					this.limitsDialog.getContent()[0].getItems()[6].getItems()[2].addItem(new VBox({height: "36px"}));
				}else {
					oText=new Text({width: "60px", text: "{limit>/result/limit/"+i+"/unit}", textAlign: "Center"}).addStyleClass("txtNavigatorUnit");
					this.limitsDialog.getContent()[0].getItems()[6].getItems()[2].addItem(oText);
				}
			}//for: i
			
			var fireBall = oModel.getData().result.fire_ball_center;
			for (var i = 0; i < fireBall.length; i++) {
				var oFlexBox = new FlexBox({alignItems: "Center", justifyContent: "Start", direction: "Row"});
				var oFlexBox1 = new FlexBox({alignItems: "Center", justifyContent: "Center", direction: "Row", width: "60px"});
				var oFlexBox2 = new FlexBox({alignItems: "Center", justifyContent: "Center", direction: "Row", width: "60px"});
				var oFlexBox3 = new FlexBox({alignItems: "Center", justifyContent: "Center", direction: "Row", width: "60px"});
				var oFlexBox4 = new FlexBox({alignItems: "Center", justifyContent: "Center", direction: "Row", width: "60px"});
				oFlexBox1.addItem(new CheckBox({selected: "{limit>/result/fire_ball_center/"+i+"/corner1}"}));
				oFlexBox2.addItem(new CheckBox({selected: "{limit>/result/fire_ball_center/"+i+"/corner2}"}));
				oFlexBox3.addItem(new CheckBox({selected: "{limit>/result/fire_ball_center/"+i+"/corner3}"}));
				oFlexBox4.addItem(new CheckBox({selected: "{limit>/result/fire_ball_center/"+i+"/corner4}"}));
				
				oFlexBox.addItem(new Text({width: "60px", text: "{limit>/result/fire_ball_center/"+i+"/direction}", textAlign: "Center"}));
				oFlexBox.addItem(oFlexBox1);
				oFlexBox.addItem(oFlexBox2);
				oFlexBox.addItem(oFlexBox3);
				oFlexBox.addItem(oFlexBox4);
				this.limitsDialog.getContent()[0].getItems()[8].getItems()[2].getItems()[0].getItems()[1].getItems()[1].addItem(oFlexBox);
			}//for: i
		},//callbackLimitAjax
		onLimitSave: function (oEvent) {
			var oLimitSaveParam = {
				url:  this.getApiUrl()+"/algorithm/navigator/limit_fireball/setting",
				type: "post",
				data: this.getModel("limit").getProperty("/result"),
				callback: "callbackLimitSaveAjax"
			};
			this.onAjaxCall(oLimitSaveParam);
		},
		callbackLimitSaveAjax: function (oModel) {
			if(oModel.getData().code=200){ this.showMsgToast("Saved"); }
			this.onAjaxCall(this.oLimitParam);
		},
		onLimitClose: function(oEvent) { oEvent.getSource().oParent.close(); },
		onDeviationChange: function(oEvent) { this.onTubeStatusChange(oEvent, "deviation"); },
		onHighLowChange: function(oEvent) { this.onTubeStatusChange(oEvent, "highLow"); },
		onTubeStatusChange: function(oEvent, type) {
			var path = oEvent.getSource().getParent().getBindingContext("tube").getPath();
			var idx = path.substr(path.lastIndexOf("/")+1);
			this.getModel("tube").setProperty("/result/"+type+"/"+idx+"/status", "Information");
		},
		onPressExport: function(oEvent) {
			this.onExportSeq(this.getModel().getProperty("/result"), "navigator");
		}
	});

}, /* bExport= */ true);