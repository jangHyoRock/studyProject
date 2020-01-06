sap.ui.define(function() {
	"use strict";

	var Formatter = {
		alarmText: function(_s) {
			if(_s=="A"){
				return "Alarm";
			}else if(_s=="E"){
				return "Event";
			}
		},
		alarmState :  function (_s) {
			if (!_s) {
				return "None";
			} else {

				if (_s==null || _s=="") {
					return "None";
				} else if (_s=="A") {
					return "Error";
				} else if (_s=="E") {
					return "None";
				} else {
					return "None";
				}
			}
		},
		alarmIcons : function(_s){
			if (!_s) {
				return;
			} else {
				if (_s==null || _s=="") {
					return;
				} else if (_s=="A") {
					return "sap-icon://alert";
				} else if (_s=="E") {
					return "sap-icon://message-error";
				} else {
					return "sap-icon://message-error";
				}
			}
		},
		alarmType: function(_s) {
			if (!_s) {
				return;
			} else {
				if (_s==null || _s=="") {
					return;
				} else if (_s=="S") {
					return "SYSTEM";
				} else if (_s=="D") {
					return "DCS";
				} else {
					return "";
				}
			}
		},
		alarmTextState :  function (_s) {
			if (!_s || _s==null || _s=="") { return "None"; }
			else if (_s.search(/Alarm/i)>-1){ return "Error"; }
			return "None";
		},
		alarmTextIcons : function(_s){
			if (!_s || _s==null || _s=="") { return; }
			else if (_s.search(/Alarm/i)>-1){ return "sap-icon://alert"; }
			else if (_s.search(/Event/i)>-1){ return "sap-icon://message-error"; }
			return;
		}
	};

	return Formatter;

}, /* bExport= */ true);
