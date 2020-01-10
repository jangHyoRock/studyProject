define([""],function(){
    return {
    	getData: function(_t,_url,_plantId){

    		var xCords, yCords, values;
    		var rtnData;
    		var oXCords = new Array();
    		var oYCords = new Array();
    		var oValues = new Array();
    		var message = "";
    		var fot_rate, mean_value, max_value, min_value, left_mean, right_mean, standard_deviation;
			$.ajax({
    	        url: _url+"/combustion_dynamics/data/"+_plantId,
    	        dataType:"json",
    	        async:false,
    	        success: function(data){

    	            if(data.code=="200"){

    	            	rtnData = data.result.boiler_part
    	            	//get(filtering) x cords
    	            	for(var idx=0;idx<rtnData.length;idx++){
    	            		if(rtnData[idx].part == _t){
    	            			xCords = rtnData[idx].data.x
    	            		}
    	            	}
    	            	//generate x cords
    	            	for(var idx=0;idx<xCords.length;idx++){
    	            		for(var i=0;i<xCords.length;i++){
    	            			oXCords.push(xCords[idx]);
    	            		}
    	            	}
    	            	//get(filtering) y cords
    	            	for(var idx=0;idx<rtnData.length;idx++){
    	            		if(rtnData[idx].part == _t){
    	            			yCords = rtnData[idx].data.x
    	            		}
    	            	}
    	            	//generate y cords
    	            	for(var idx=0;idx<yCords.length;idx++){
    	            		for(var i=0;i<yCords.length;i++){
    	            			oYCords.push(yCords[i]);
    	            		}
    	            	}

    	            	//get(filtering) values
    	            	for(var idx=0;idx<rtnData.length;idx++){
    	            		if(rtnData[idx].part == _t){
    	            			values = rtnData[idx].data.z
    	            		}
    	            	}
    	            	//generate values
    	            	for (var i = 0; i < values.length; i++) {
    	            		oValues = oValues.concat(values[i]);
    	                }

    	            	//get message
    	            	for(var idx=0;idx<rtnData.length;idx++){
    	            		if(rtnData[idx].part == _t){
    	            			message = rtnData[idx].data.message
    	            		}
    	            	}

    	            	//get fot_rate
    	            	for(var idx=0;idx<rtnData.length;idx++){
    	            		if(rtnData[idx].part == _t){
    	            			fot_rate = rtnData[idx].data.fot_rate
    	            		}
    	            	}

    	            	//get mean_value
    	            	for(var idx=0;idx<rtnData.length;idx++){
    	            		if(rtnData[idx].part == _t){
    	            			mean_value = rtnData[idx].data.mean_value
    	            		}
    	            	}

    	            	//get max_value
    	            	for(var idx=0;idx<rtnData.length;idx++){
    	            		if(rtnData[idx].part == _t){
    	            			max_value = rtnData[idx].data.max_value
    	            		}
    	            	}

    	            	//get min_value
    	            	for(var idx=0;idx<rtnData.length;idx++){
    	            		if(rtnData[idx].part == _t){
    	            			min_value = rtnData[idx].data.min_value
    	            		}
    	            	}

    	            	//get left_mean
    	            	for(var idx=0;idx<rtnData.length;idx++){
    	            		if(rtnData[idx].part == _t){
    	            			left_mean = rtnData[idx].data.left_mean
    	            		}
    	            	}

    	            	//get right_mean
    	            	for(var idx=0;idx<rtnData.length;idx++){
    	            		if(rtnData[idx].part == _t){
    	            			right_mean = rtnData[idx].data.right_mean
    	            		}
    	            	}

    	            	//get standard_deviation
    	            	for(var idx=0;idx<rtnData.length;idx++){
    	            		if(rtnData[idx].part == _t){
    	            			standard_deviation = rtnData[idx].data.standard_deviation
    	            		}
    	            	}
    	            }else{
    	            	false
    	            }

    	        },
    	        error: function(xhr, status, error){

    	        }
    	     });

    		var previewData = {
    				cross : {
    					"data" : {
    						"analysisAxis" : [ {
    							"index" : 1,
    							"data" : [
    									{
    										"type" : "Dimension",
    										"name" : "XCoordinate",
    										"values" :  oXCords
    									},
    									{
    										"type" : "Dimension",
    										"name" : "YCoordinate",
    										"values" : oYCords
    									} ]
    						} ],
    						"measureValuesGroup" : [ {
    							"index" : 1,
    							"data" : [ {
    								"type" : "Measure",
    								"name" : "Value",
    								"values" : [ oValues ]
    							} ]
    						} ]
    					},
    					"bindings" : [ {
    						"feed" : "sap.viz.ext.footballheatmap.PlotModule.DS1",
    						"source" : [ {
    							"type" : "analysisAxis",
    							"index" : 1
    						} ]
    					}, {
    						"feed" : "sap.viz.ext.footballheatmap.PlotModule.MS1",
    						"source" : [ {
    							"type" : "measureValuesGroup",
    							"index" : 1
    						} ]
    					} ],
    					"message" : message,
    					"fot_rate": fot_rate,
						"mean_value": mean_value,
						"max_value": max_value,
						"min_value": min_value,
						"left_mean": left_mean,
						"right_mean": right_mean,
						"standard_deviation": standard_deviation
    				}
    			};
    		return previewData;
        }//getdata end
    };
});