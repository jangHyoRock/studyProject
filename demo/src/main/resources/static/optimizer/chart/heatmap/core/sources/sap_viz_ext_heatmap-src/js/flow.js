define("sap_viz_ext_heatmap-src/js/flow", ["sap_viz_ext_heatmap-src/js/module"], function(moduleFunc) {
	"use strict";
	var flowRegisterFunc = function() {
		var flow = sap.viz.extapi.Flow.createFlow({
			id: "sap.viz.ext.heatmap",
			name: " Heatmap",
			dataModel: "sap.viz.api.data.CrosstableDataset",
			type: "DIV"
		});

		var element = sap.viz.extapi.Flow.createElement({
			id: "sap.viz.ext.heatmap.PlotModule",
			name: " Heatmap Module"
		});
		element.implement("sap.viz.elements.common.BaseGraphic", moduleFunc);

		/*Feeds Definition*/
		var ds1 = {
			"id": "sap.viz.ext.heatmap.PlotModule.DS1",
			"name": "Location (X, Y)",
			"type": "Dimension",
			"min": 0, //minimum number of data container
			"max": 2, //maximum number of data container
			"aaIndex": 1
		};
		element.addFeed(ds1);

		var ms1 = {
			"id": "sap.viz.ext.heatmap.PlotModule.MS1",
			"name": "Value",
			"type": "Measure",
			"min": 0, //minimum number of measures
			"max": Infinity, //maximum number of measures
			"mgIndex": 1
		};
		element.addFeed(ms1);

		element.addProperty({
			name: "colorPalette",
			type: "StringArray",
			supportedValues: "",
			defaultValue: d3.scale.category20().range().concat(d3.scale.category20b().range()).concat(d3.scale.category20c().range())
		});

		flow.addElement({
			"element": element,
			"propertyCategory": "plotArea"
		});
		sap.viz.extapi.Flow.registerFlow(flow);
	};
	flowRegisterFunc.id = "sap.viz.ext.heatmap";
	return {
		id: flowRegisterFunc.id,
		init: flowRegisterFunc
	};
});