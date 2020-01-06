sap.ui.define([
		"dhi/optimizer/controller/BaseController"
	], function (BaseController) {
		"use strict";
	
	return BaseController.extend("dhi.optimizer.controller.CombustionStatus", {
		oParam: {
			path: "/combustion_status/data",
			type: "get",
			callback: "callbackAjax"
		},
		callbackAjax: function(oModel) {
			var result = oModel.getData().result;
			if(result==undefined||result==null){
				this.setModel(); return;
			}
			
			var data = [];
			for (var aItem in result) {
				for (var bItem in result[aItem]) {
					if("position"==bItem){
						for (var i = 0; i < result[aItem][bItem].length; i++) {
							for (var cItem in result[aItem][bItem][i]) {
								if(Array.isArray(result[aItem][bItem][i][cItem])){
									for (var j = 0; j < result[aItem][bItem][i][cItem].length; j++) {
										if("ofa_supply"==cItem&&j==0){
											result[aItem][bItem][i].ofa_supply1=result[aItem][bItem][i][cItem].slice(0,2);
											result[aItem][bItem][i].ofa_supply2=result[aItem][bItem][i][cItem].slice(2);
										}
										var obj = {};
										obj.type = bItem+"."+result[aItem][bItem][i].item+"."+cItem;
										for (var dItem in result[aItem][bItem][i][cItem][j]) {
											obj[dItem] = result[aItem][bItem][i][cItem][j][dItem];
											if("sa_unit"==dItem||"coal_unit"==dItem){
												result[aItem][bItem][i][cItem][j][dItem]=result[aItem][bItem][i][cItem][j][dItem];
											}
										}
										data.push(obj);
									}//for:j
								}//if
							}//for:cItem
						}//for:i
					}else if(Array.isArray(result[aItem][bItem])){
						for (var i = 0; i < result[aItem][bItem].length; i++) {
							var obj = {};
							obj.type = bItem;
							for (var cItem in result[aItem][bItem][i]) { obj[cItem] = result[aItem][bItem][i][cItem]; }
							data.push(obj);
						}
					}else{
						for (var cItem in result[aItem][bItem]) {
							if(Array.isArray(result[aItem][bItem][cItem])){
								for (var i = 0; i < result[aItem][bItem][cItem].length; i++) {
									var obj = {};
									obj.type = bItem+"."+cItem;
									for (var dItem in result[aItem][bItem][cItem][i]) { obj[dItem] = result[aItem][bItem][cItem][i][dItem]; }
									data.push(obj);
								}
							}else{
								result[aItem][bItem][cItem].type = bItem+"."+cItem;
								data.push(result[aItem][bItem][cItem]);
							}
						}
					}//if
				}//for:bItem
			}//for:aItem
			result.data = data;
			
			for (var i = 0; i < result.data.length; i++) {
				if("coal_property.chemical_composition"==result.data[i].type){
					this.byId("txtCSChemicalComposition"+(i+1)).setText(result.data[i].item+" "+result.data[i].current+result.data[i].unit);
				}
			}
			
			//fireball_position 
			var move = result.combustion_status.fireball_position;
//			var move=[{x:0,y:0}];
			var center = {top: -90, left: -9};
			var baseColor = 208; //d0 208
			var startRatio = move.length*3;
			var html = "<Image class='imgFireball' />";
			for (var i = 0; i < move.length; i++) {
				var iColor = Math.round(baseColor-baseColor/move.length*(i+1)+32).toString(16);
				iColor=iColor.length<2?"0"+iColor:iColor;
				var ilength = 70*(i+1+startRatio)/(move.length+startRatio);
				html+="<div style='height:20px;width:20px;position:absolute;background:red;font-size:xx-small;line-height:20px;" +
					"text-align:center;color:white;top:"+(center.top-move[i].y)+"px;left:"+(center.left+move[i].x)+"px;"+
					"background:radial-gradient(#ff"+iColor+"00 0%,transparent "+ilength+"%);'></div>";
				if(i==move.length-1){ html=html.substring(0,html.length-6)+"+</div>"; }
			}//i
			$("div[id$=combustionStatus] img.imgFireball").parent().html(html);
			
			this.setModel(oModel);
		},
		onInterval: function() { this.onAjaxCall(this.oParam); },
		onInit : function () {
			var that = this;
			this.getView().addEventDelegate({
			   onBeforeShow: function(oEvent) {
				   jQuery.sap.log.info("onBeforeShow");
				   that.oParam.url = that.getApiUrl()+that.oParam.path+"/"+that.getSessionValue("default_plant_unit_id");
				   that.onAjaxCall(that.oParam);
			   },
			   onAfterShow: function(oEvent) {
				   jQuery.sap.log.info("onAfterShow");
				   //focus 방지
				   $("div[id$=combustionStatus] span[id$=-inner]").css("outline","none");
				   $("div[id$=combustionStatus] img[class^=fireball]").css("outline","none");
				   $("div[id$=combustionStatus] tbody td").css("outline","none");
			   }
			});
		},
		onPress : function (oEvent) {
			var oModel = this.getModel();
			this.setModel();
			var result =oModel.getData().result;
			var idx = oEvent.getSource().data("index");
			$("svg[name=svgCS] g[name=LeftTopRedBox]").attr("stroke-width",0);
			$("svg[name=svgCS] g[name=RightTopRedBox]").attr("stroke-width",0);
			$("svg[name=svgCS] g[name=LeftBottomRedBox]").attr("stroke-width",0);
			$("svg[name=svgCS] g[name=RightBottomRedBox]").attr("stroke-width",0);
			var source="left";
			if(idx==0){
				$("svg[name=svgCS] g[name=LeftTopRedBox]").attr("stroke-width",3);
				this.byId("txtOFA").setText("(Left)");
				this.byId("txtCoal").setText("(Front Left)");
				$("svg[name=svgCS] g[name=goldBox] rect").attr("x",77);
				$("svg[name=svgCS] g[name=limeBox] rect").attr("x",51);
				$("svg[name=svgCS] g[name=topView] path").attr("d","M10 188 h5 v131 h59");
				$("svg[name=svgCS] g[name=topView] circle").eq(1).attr("cx",75);
				$("svg[name=svgCS] g[name=sideView] path").attr("d","M10 374 h40");
				$("svg[name=svgCS] g[name=sideView] circle").eq(1).attr("cx",49);
			}else if(idx==1){
				source="rear";
				$("svg[name=svgCS] g[name=RightTopRedBox]").attr("stroke-width",3);
				this.byId("txtOFA").setText("(Rear)");
				this.byId("txtCoal").setText("(Rear Left)");
				$("svg[name=svgCS] g[name=goldBox] rect").attr("x",105);
				$("svg[name=svgCS] g[name=limeBox] rect").attr("x",115);
				$("svg[name=svgCS] g[name=topView] path").attr("d","M10 188 h5 v131 h87");
				$("svg[name=svgCS] g[name=topView] circle").eq(1).attr("cx",103);
				$("svg[name=svgCS] g[name=sideView] path").attr("d","M10 374 h104");
				$("svg[name=svgCS] g[name=sideView] circle").eq(1).attr("cx",113);
			}else if(idx==2){
				source="front";
				$("svg[name=svgCS] g[name=LeftBottomRedBox]").attr("stroke-width",3);
				this.byId("txtOFA").setText("(Front)");
				this.byId("txtCoal").setText("(Front Right)");
				$("svg[name=svgCS] g[name=goldBox] rect").attr("x",51);
				$("svg[name=svgCS] g[name=limeBox] rect").attr("x",51);
				$("svg[name=svgCS] g[name=topView] path").attr("d","M10 188 h5 v131 h33");
				$("svg[name=svgCS] g[name=topView] circle").eq(1).attr("cx",49);
				$("svg[name=svgCS] g[name=sideView] path").attr("d","M10 374 h40");
				$("svg[name=svgCS] g[name=sideView] circle").eq(1).attr("cx",49);
			}else if(idx==3){
				source="right";
				$("svg[name=svgCS] g[name=RightBottomRedBox]").attr("stroke-width",3);
				this.byId("txtOFA").setText("(Right)");
				this.byId("txtCoal").setText("(Rear Right)");
				$("svg[name=svgCS] g[name=goldBox] rect").attr("x",77);
				$("svg[name=svgCS] g[name=limeBox] rect").attr("x",115);
				$("svg[name=svgCS] g[name=topView] path").attr("d","M10 188 h5 v131 h59");
				$("svg[name=svgCS] g[name=topView] circle").eq(1).attr("cx",75);
				$("svg[name=svgCS] g[name=sideView] path").attr("d","M10 374 h104");
				$("svg[name=svgCS] g[name=sideView] circle").eq(1).attr("cx",113);
			}
			for (var i = 0; i < result.combustion_status.position.length; i++) {
				if(result.combustion_status.position[i].item.toLowerCase().search(source)>-1){
					this.byId("tblOfa1").bindRows("/result/combustion_status/position/"+i+"/ofa_supply1");
					this.byId("tblOfa2").bindRows("/result/combustion_status/position/"+i+"/ofa_supply2");
					this.byId("tblCoal").bindRows("/result/combustion_status/position/"+i+"/coal_supply");
					this.byId("tblPaSa").bindRows("/result/combustion_status/position/"+i+"/pa_sa_supply");
				}
			}
			this.setModel(oModel);
		},
		formatStatus : function (status) { return null==status||undefined==status||status.search(/warning/i)>-1? "Error" : "Success"; },
		createColumnConfig: function() {
			var obj={};
			var data=this.getModel().getData().result.data;
			for (var i = 0; i < data.length; i++) { for(var aItem in data[i]){ obj[aItem]=""; } }
			
			var arr=[];
			for ( var item in obj) {
				if("type"==item||"item"==item){
					arr.push({label:item,property:item,textAlign:"center",type:'String',width:30});
				}else{
					arr.push({label:item,property:item,textAlign:"center",type:'String',width:10});
				}
			}
			return arr;
		},
		onPressExport: function(oEvent) { this.onExport(this.createColumnConfig(), this.getModel().getProperty("/result/data"), "combustionStatus"); },
		onUnitChange: function() {
			this.oParam.url = this.getApiUrl()+this.oParam.path+"/"+this.getSessionValue("default_plant_unit_id");
			this.onAjaxCall(this.oParam);
		}
	});

}, /* bExport= */ true);