sap.ui.define([
		"sap/ui/core/mvc/Controller",
		"sap/ui/model/json/JSONModel",
		"sap/m/MessageToast",
		"sap/m/MessageBox",
		'sap/ui/model/SimpleType',
		"sap/ui/export/Spreadsheet",
		"dhi/common/util/Formatter",
		"sap/ui/core/util/Export",
		"sap/ui/core/util/ExportTypeCSV",
	], function (Controller, JSONModel, MessageToast, MessageBox, SimpleType, Spreadsheet, Formatter, Export, ExportTypeCSV) {
		"use strict";

		return Controller.extend("dhi.common.controller.BaseController", {
			/**
			 * Convenience method for getting the server api url.
			 * @public
			 * @returns {string} the api url
			 */
			getApiUrl : function () {
				var _sSystemInfo = JSON.parse(sessionStorage.getItem(this.getOwnerComponent().getManifestEntry("/sap.ui5/config/systemId")));
				return "http://" + _sSystemInfo["default_plant_unit_ip"] + ":" + _sSystemInfo["default_plant_unit_port"];
			},

			getManifestEntry : function (sEntry) {
				return this.getOwnerComponent().getManifestEntry(sEntry);
			},

			/**
			 * Convenience method for getting the session value.
			 * @public
			 * @returns {string} the session value
			 */
			getSessionValue : function (sKey) {
				if (sKey.indexOf("default_plant_unit_") > -1) {
					var _sSystemInfo = JSON.parse(sessionStorage.getItem(this.getOwnerComponent().getManifestEntry("/sap.ui5/config/systemId")));

					return _sSystemInfo[sKey];
				} else {
					return window.sessionStorage.getItem(sKey);
				}
			},

			/**
			 * Convenience method for setting the session value.
			 * @public
			 */
			setSessionValue : function (sKey, sVal) {
				window.sessionStorage.setItem(sKey, sVal);
			},

			/**
			 * Convenience method for accessing the router in every controller of the application.
			 * @public
			 * @returns {sap.ui.core.routing.Router} the router for this component
			 */
			getRouter : function () {
				return this.getOwnerComponent().getRouter();
			},

			/**
			 * Convenience method for getting the view model by name in every controller of the application.
			 * @public
			 * @param {string} sName the model name
			 * @returns {sap.ui.model.Model} the model instance
			 */
			getModel : function (sName) {
				return this.getView().getModel(sName);
			},

			/**
			 * Convenience method for setting the view model in every controller of the application.
			 * @public
			 * @param {sap.ui.model.Model} oModel the model instance
			 * @param {string} sName the model name
			 * @returns {sap.ui.mvc.View} the view instance
			 */
			setModel : function (oModel, sName) {
				return this.getView().setModel(oModel, sName);
			},

			/**
			 * Convenience method for getting the resource bundle.
			 * @public
			 * @returns {sap.ui.model.resource.ResourceModel} the resourceModel of the component
			 */
			getResourceBundle : function () {
				return this.getOwnerComponent().getModel("i18n").getResourceBundle();
			},

			getResourceBundleCommon : function () {
				return this.getOwnerComponent().getModel("i18nCommon").getResourceBundle();
			},

			showMsgToast : function (oText) {
				MessageToast.show(oText, {duration: 1000, at: 'center center'});
			},

			/**
			 * Event handler for navigating back.
			 * It there is a history entry we go one step back in the browser history
			 * If not, it will replace the current entry of the browser history with the master route.
			 * @public
			 */
			onNavBack : function() {
				var sPreviousHash = sap.ui.core.routing.History.getInstance().getPreviousHash();

				if (sPreviousHash !== undefined) {
					history.go(-1);
				} else {
					this.getRouter().navTo("overview");
				}
			},

			/**
			 * Event handler for ajax call.
			 * example(controller에서 호출)
			// onInit 내에서 작성
			var oParam = {
				url: "https://sapui5.hana.ondemand.com/sdk/test-resources/sap/ui/demokit/explored/products.json",
				data: {tag_id: "111", start_date: "", end_date: ""},
				type: "get",
				callback: "callbackAjax"
			};
			this.onAjaxCall(oParam);

			// 원하는 callback 함수 작성
			callbackAjax : function (oModel) {
				this.setModel(oModel, "name");
				jQuery.sap.log.info("successed to callback");
			}
			 */
			
			onAjaxCall : function(oParam) {				
				if (!oParam.callback) { return; }
				var that = this;
				var _oData = oParam.data || '';
				var _sType = oParam.type || "get";

				if(_sType.toLowerCase() != 'get' && typeof _oData == 'object') {
					_oData = JSON.stringify(_oData);
				}
				
				$.ajaxSetup({
				    beforeSend: function(xhr) {
				    	xhr.setRequestHeader('x-auth-token', that.getSessionValue("auth_token"));				        
				    }
				});
				
				jQuery.ajax({
					type : _sType,
					data : _oData,
					dataType : "json",
					contentType : "application/json; charset=utf-8",
					mediatype : "application/json",					
					url : oParam.url,
					success : function(oData) {
						if(oData.status && oData.status == 'fail') {
							jQuery.sap.log.error(jQuery.sap.formatMessage("Api response code: {0}, message: {1}", [oData.code, oData.message]));

							var errMsg = 'apiErr_' + (oData.message || "Server error.");
							if (!that.getSessionValue(errMsg)) { that.setSessionValue(errMsg, 0); }
							if (that.getSessionValue(errMsg) == 0) {
								that.onOpenApiMessageBox(errMsg);
							}

							sap.ui.core.BusyIndicator.hide();

							return;
						}

						var oModel = new JSONModel();
						oModel.setData(oData);

						var proxyFunc = jQuery.proxy(that, oParam.callback, oModel);
						proxyFunc();

						jQuery.sap.log.info("Succeeded to api execution");
					},
					/*statusCode: {
						403: function() {
							that.getRouter().navTo("notFound");
						}
					},*/
					error : function(xhr, e) {
						sap.ui.core.BusyIndicator.hide();

						var errMsg = null;
					    if(e.responseBody) {
					    	errMsg = e.responseBody;
					    } else {
					    	errMsg = e.responseText;
					    }
					    jQuery.sap.log.error('errMsg: ' + errMsg);

						
						if(xhr.status = 403) {
							//that.getRouter().navTo("overview");
							//jQuery.sap.log.error("Api execution failed.");

							errMsg = "The session has expired or is not authorized.";
							if (!that.getSessionValue(errMsg)) { that.setSessionValue(errMsg, 0); }
							if (that.getSessionValue(errMsg) == 0) {
								that.onOpenApiMessageBox(errMsg, 'info');
							}
							window.location = '/';
						} else {							
						    try {
								var returnJsonObj = JSON.parse(errMsg);
								console.log("error = " + e);
								if(returnJsonObj) {
									jQuery.sap.log.error("Failed to api execution: " + returnJsonObj.error);

									errMsg = "apiErr_Failed to api execution: " + returnJsonObj.error;
									if (!that.getSessionValue(errMsg)) { that.setSessionValue(errMsg, 0); }
									if (that.getSessionValue(errMsg) == 0) {
										that.onOpenApiMessageBox(errMsg);
									}
								}
							} catch(e) {
								jQuery.sap.log.error("Api execution failed.");

								errMsg = "apiErr_Api execution failed.";
								if (!that.getSessionValue(errMsg)) { that.setSessionValue(errMsg, 0); }
								if (that.getSessionValue(errMsg) == 0) {
									that.onOpenApiMessageBox(errMsg);
								}
							}
						}
						
					}
				});
			},
			
			onFileDownload: function(downloadUrl) {
				var req = new XMLHttpRequest();
				req.open('GET', downloadUrl, true); //true means request will be async
				req.setRequestHeader('x-auth-token', this.getSessionValue("auth_token"));
				req.responseType = "blob";
			    req.onload = function (event) {
			         var blob = req.response;
			         var filename = ""; 
					 var disposition = req.getResponseHeader('Content-Disposition');
			         if (disposition && disposition.indexOf('attachment') !== -1) {
			        	 var filenameRegex = /filename[^;=\n]*=((['"]).*?\2|[^;\n]*)/;
			        	 var matches = filenameRegex.exec(disposition);
			        	 if (matches != null && matches[1]) { 
			        		 filename = matches[1].replace(/['"]/g, '');
			        	 }
			         }
			         var link=document.createElement('a');
			         link.href=window.URL.createObjectURL(blob);
			         link.download=filename;
			         link.click();
			     };
				req.send();
			},

			// API Error 관련 메시지 창 띄우기
			onOpenApiMessageBox : function (sMessage, sType) {
				var that = this;
				that.setSessionValue(sMessage, eval(that.getSessionValue(sMessage)) + 1);

				if(sType == 'info') {
					MessageBox.alert(sMessage.replace('apiErr_', ''), {
						onClose: function (oAction) {
							that.setSessionValue(sMessage, eval(that.getSessionValue(sMessage)) - 1);
						}
					});
				} else {
					MessageBox.error(sMessage.replace('apiErr_', ''), {
						onClose: function (oAction) {
							that.setSessionValue(sMessage, eval(that.getSessionValue(sMessage)) - 1);
						}
					});
				}
			},

			// 이메일 체크 Validation
			customEMailType :
				SimpleType.extend("email", {
					formatValue: function (oValue) {
						return oValue;
					},
					parseValue: function (oValue) {
						//parsing step takes place before validating step, value could be altered here
						return oValue;
					},
					validateValue: function (oValue) {
						// The following Regex is NOT a completely correct one and only used for demonstration purposes.
						// RFC 5322 cannot even checked by a Regex and the Regex for RFC 822 is very long and complex.
						var rexMail = /^\w+[\w-+\.]*\@\w+([-\.]\w+)*\.[a-zA-Z]{2,}$/;
						if (!oValue.match(rexMail)) {
							throw new sap.ui.model.ValidateException("Enter a valid email address.");
						}
					}
				})
			,
			/**
			 * export Spreadsheet.
			 * oVizFrame 작성은 Trend.controller.js 참고
			 */
			onExport: function(aCols, aProducts, _aTitle) {
				var oSettings = {workbook: {columns: aCols}, dataSource: aProducts, fileName: _aTitle};
				var that=this;
				var oSpreadsheet = new Spreadsheet(oSettings);
				oSpreadsheet.onprogress = function(iValue) {
					$("div[id*=indicator][id$=bar]").css("flex-basis",iValue+"%");
				};
				oSpreadsheet.build().then(function(){that.showMsgToast("Spreadsheet export has finished");});
			},
			onExportSeq: function(aJsonModel, _aTitle) {				
				var arrTitle=[];
				arrTitle.push({label:"Item",property:"Item",textAlign:"center",type:'String',width:60});
				arrTitle.push({label:"Value",property:"Value",textAlign:"center",type:'String',width:20});
				
				var arrData=[];
				this.exportSeqData(_aTitle, aJsonModel, arrData);
				
				var oSettings = {workbook: {columns: arrTitle}, dataSource: arrData, fileName: _aTitle};
				var that=this;
				var oSpreadsheet = new Spreadsheet(oSettings);
				oSpreadsheet.onprogress = function(iValue) {
					$("div[id*=indicator][id$=bar]").css("flex-basis",iValue+"%");
				};
				oSpreadsheet.build().then(function(){that.showMsgToast("Spreadsheet export has finished");});
			},
			exportSeqData: function(parentItem, result, arrData){
				for (var stat in result) {				
					if(Array.isArray(result[stat])) {
						for (var i = 0; i < result[stat].length; i++) {
							for (var cItem in result[stat][i]) { 
								this.exportSeqData(parentItem + "." + stat, result[stat][i], arrData);
							}
						}					
					}
					else if (typeof(result[stat]) == 'object') {
						this.exportSeqData(parentItem + "." + stat, result[stat], arrData);
					}
					else {
						var item = parentItem + "." + stat;
						arrData.push({Item:item, Value:result[stat]});	
					}
				}
			},
			/**
			 * print current content.
			 */
			onPrint: function() {
				window.print();
			},
			setPeriodD: function(s,start,end,flag,defaultDate){
				var dpStart=start;
				var dpEnd=end;
				if(typeof s != "string"){
					dpStart = s.getSource().oParent.getItems()[7];
					dpEnd = s.getSource().oParent.getItems()[9];
					s = s.getSource().data("type");
				}
				
				var d = defaultDate != null ? defaultDate : new Date();
				if(flag=="date")
					dpEnd.setValue(Formatter.getDateOnlyformat(d,true)).setValueState(sap.ui.core.ValueState.None);			
				else 
					dpEnd.setValue(Formatter.getDateformat(d,true)).setValueState(sap.ui.core.ValueState.None);
				
				if(s=="6h")			{ d.setHours(d.getHours()-6); }
				else if(s=="d")		{ d.setDate(d.getDate()-1); }
				else if(s=="w")		{ d.setDate(d.getDate()-7); }
				else if(s=="3w")	{ d.setDate(d.getDate()-21); }
				else if(s=="m")		{ d.setMonth(d.getMonth()-1); }
				else if(s=="3m")	{ d.setMonth(d.getMonth()-3); }
				else if(s=="6m")	{ d.setMonth(d.getMonth()-6); }
				else if(s=="y")	{ d.setMonth(d.getMonth()-12); }
				
				if(flag=="date")
					dpStart.setValue(Formatter.getDateOnlyformat(d,true)).setValueState(sap.ui.core.ValueState.None);
				else
					dpStart.setValue(Formatter.getDateformat(d,true)).setValueState(sap.ui.core.ValueState.None);				
			},
			dpHandleChange: function (oEvent) {
				if (oEvent.getParameter("valid")) {
					oEvent.oSource.setValueState(sap.ui.core.ValueState.None);
				} else {
					oEvent.oSource.setValueState(sap.ui.core.ValueState.Error);
				}
			},
			onUnitChange: function() { },
			onInterval: function() { },
			onExportCSV: function(oModel, oPath, oColumns, ReportTitle) {
				this.showMsgToast("CSV Exporting....");
				// download exported file
				var oData=oModel.getProperty(oPath);
				var that=this;
				setTimeout(function() {
					if(Array.isArray(oData) && oData.limitOver){
						for (var i = 0; i < oData.length; i++) {
							var oExport = that._setExport(oModel, oPath+"/"+i, oColumns);
							oExport.saveFile(ReportTitle).catch(function(oError) {
								MessageBox.error("Error when downloading data.\n\n" + oError);
							}).then(function() { oExport.destroy(); });
						}
					}else{
						var oExport = that._setExport(oModel, oPath, oColumns);
						oExport.saveFile(ReportTitle).catch(function(oError) {
							MessageBox.error("Error when downloading data.\n\n" + oError);
						}).then(function() { oExport.destroy(); });
					}
					that.showMsgToast("Export Complete");
				},100);
			},
			_setExport: function(oModel, oPath, oColumns){
				return new Export({
					exportType : new ExportTypeCSV({ separatorChar : "," }),
					models : oModel,
					rows : { path : oPath },
					columns : oColumns
				});
			},
			onClosePress: function(oEvent) {
				var e = oEvent.getSource();
				for (var i = 0; i < 2; i++) {
					e = e.getParent();
					if(e.getId().search(/dialog/i)>-1){ e.close(); break; }
				}//for
			}
		});
	}
);