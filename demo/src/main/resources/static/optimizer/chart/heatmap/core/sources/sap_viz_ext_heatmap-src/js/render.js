define("sap_viz_ext_heatmap-src/js/render", ['heatmap2'], function() {
	/*
	 * This function is a drawing function; you should put all your drawing logic in it.
	 * it's called in moduleFunc.prototype.render
	 * @param {Object} data - proceessed dataset, check dataMapping.js
	 * @param {Object} container - the target d3.selection element of plot area
	 * @example
	 *   container size:     this.width() or this.height()
	 *   chart properties:   this.properties()
	 *   dimensions info:    data.meta.dimensions()
	 *   measures info:      data.meta.measures()
	 */
	"use strict";
	var render = function(data, container) {

		var width = this.width(),
			height = this.height(),
			colorPalette = this.colorPalette(),
			properties = this.properties(),
			dispatch = this.dispatch();
		//Prepare canvas with width and height of container
		var vis = container.append('svg').attr('width',width).attr('height',height).append('g').attr('class','vis').attr('width',width).attr('height',height);

		container.selectAll('svg').remove();

		var mset1 = data.meta.measures(0);
		var dset1 = data.meta.dimensions(0),
			measureValue = mset1[0],
			xDimension = dset1[0],
			yDimension = dset1[1];
		var hLine = Math.sqrt(data.length);
		var vLine = Math.sqrt(data.length);
		//Convert measure into number
		data.forEach(function(d) {
			d[xDimension] = +d[xDimension];
			d[yDimension] = +d[yDimension];
			d[measureValue] = +d[measureValue];

		});
		var canvasWidth = container.node().offsetWidth*1.5;
		var canvasHeight = container.node().offsetHeight;
		var fieldWidth, fieldHeight, leftMargin;

		if (canvasWidth / canvasHeight > 1.5) {
			fieldWidth = (canvasHeight * 1.5)-20;
			fieldHeight = canvasHeight;
			leftMargin = 0;
		} else {
			fieldWidth = canvasWidth-20;
			fieldHeight = canvasWidth / 1.5;
			leftMargin = 0;
		}
		var centerRadius = 9.15 * fieldHeight / 68;
		// var sideLengthOfPenaltyArc = 5.5 * fieldWidth / 105;

		//!!!!!!!!!!!!If you know min and max point, please assign manually!!!!!!!!!!!!!!!1
		var xMin = d3.min(data, function(d) {
			return d[xDimension];
		});
		var xMax = d3.max(data, function(d) {
			return d[xDimension];
		});

		var yMin = d3.min(data, function(d) {
			return d[yDimension];
		});
		var yMax = d3.max(data, function(d) {
			return d[yDimension];
		});

		var maxValue = d3.max(data, function(d) {
			return d[measureValue];
			//return 180;
		});
		var minValue = d3.min(data, function(d) {
			return d[measureValue];
			//return 180;
		});
		//Calculators for finding place of points on pitch
		var y = d3.scale.linear()
			.range([0, fieldHeight])
			.domain([yMin, yMax]);

		var x = d3.scale.linear()
			.range([0, fieldWidth])
			.domain([xMin, xMax]);

		var textData = [];
		var rangeNum = (maxValue-minValue)/10;
		textData[0] = Math.floor(minValue);
		for(var i = 1; i<11; i++){
			textData[i] = Math.round(minValue+rangeNum*i);
		}
		var heatData = [];

		var datasetControl = 0; //to check if x and y has proper data.

		data.forEach(function(d) {
			var point = {
				x: x(d[xDimension]),
				y: y(d[yDimension]),
				value: d[measureValue]

			};
			if (!isNaN(point.x) && !isNaN(point.y)) {
				datasetControl = 1;
			}

			heatData.push(point);
		});
		if (datasetControl === 1) {
			initializeHeatMap();
		}

		function initializeHeatMap() {

			//Main div for including heatmap and pitch
			container.node().innerHTML = "";
			var mainContainer = document.createElement('div');
			mainContainer.style.width = canvasWidth + 'px';
			mainContainer.style.height = fieldHeight + 'px';
			mainContainer.style.left = leftMargin+'px';
			mainContainer.className = 'sap_viz_ext_heatmap_divGreen';
			container.node().appendChild(mainContainer);

			//Div for heatmap
			var heatmapContainer = document.createElement('div');
			heatmapContainer.style.width = fieldWidth + 'px';
			heatmapContainer.style.height = fieldHeight + 'px';
			heatmapContainer.style.left = leftMargin+'px';
			heatmapContainer.className = 'sap_viz_ext_heatmap_divCSS';

			//Div for drawing  pitch
			var pitchContainer = document.createElement('div');
			pitchContainer.style.width = fieldWidth + 'px';
			pitchContainer.style.height = fieldHeight + 'px';
			//pitchContainer.style.left = leftMargin + 'px';
			//pitchContainer.style.strokeWidth = fieldWidth / 150;
			pitchContainer.className = 'sap_viz_ext_heatmap_divCSS';

			var lineContainer = document.createElement('div');
			lineContainer.style.width = fieldWidth + 'px';
			lineContainer.style.height = fieldHeight + 'px';
			lineContainer.style.left = leftMargin + 'px';
			lineContainer.style.strokeWidth = fieldWidth / 150;
			lineContainer.className = 'sap_viz_ext_heatmap_lines';

			//Div for drawing  pitch
			// var greenLayer = document.createElement('div');
			// greenLayer.style.width = fieldWidth + 'px';
			// greenLayer.style.height = fieldHeight + 'px';
			//greenLayer.style.left = leftMargin + 'px';
			//greenLayer.style.strokeWidth = fieldWidth / 150;
			// greenLayer.className = 'sap_viz_ext_heatmap_divCSS';

			mainContainer.appendChild(pitchContainer);
			pitchContainer.appendChild(heatmapContainer);
			//mainContainer.appendChild(greenLayer);
			mainContainer.appendChild(lineContainer);
			var cRadius = 0;
			if(data.length==49){
				cRadius = Math.abs(fieldWidth/4.77);
			}else if(data.length==25||data.length==36){
				cRadius = 105;
			}
			var heatmap = h337.create({
				container: heatmapContainer,
				radius: cRadius
			});

			heatmap.setData({
				max: maxValue,
				min: minValue,
				data: heatData
			});

			//Calculate angle of penalty arc.
			var sideLengthOfPenaltyArc = 5.5 * fieldWidth / 105;
			var arcAngle = Math.asin(sideLengthOfPenaltyArc / centerRadius);

			//svg container for drawing all pitch lines
			var svgContainerLines = d3.select(lineContainer).append("svg")
				.attr("width", fieldWidth)
				.attr("height", fieldHeight);

			//vertical Line
			for(var idx=1; idx<vLine; idx++){
				svgContainerLines.append("line")
				.attr("x1", fieldWidth * idx/vLine)
				.attr("y1", 0)
				.attr("x2", fieldWidth * idx/vLine)
				.attr("y2", fieldHeight);
			}

			//horizonal Line
			for(var idx=1; idx<hLine; idx++){
				svgContainerLines.append("line")
				.attr("x1", 0)
				.attr("y1", fieldHeight * idx/hLine)
				.attr("x2", fieldWidth)
				.attr("y2", fieldHeight * idx/hLine);
			}

			//Create a svg container for green rectangle (green pitch)
			var svgContainerGreen = d3.select(mainContainer).append("svg")
				.attr("width", canvasWidth)
				.attr("height", canvasHeight);
			
			//navigator exception
			if(window.hd.selSubMenuId.search(/Navigator/i)>-1){return;}
			
			for(var idx=0; idx<textData.length; idx++){
			//color scale label
				svgContainerGreen.selectAll(".text")
					.data(textData)
					.enter()
					.append("text")
					.text(textData[idx])
					.attr("y", 16 + idx*15)//(d-1)
					.attr("x", canvasWidth-25)
					.attr("font-size","9px")
					.attr("text-anchor","left");
			}
		}
	};

	return render;
});