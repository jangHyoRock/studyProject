sap.ui.define([], function() {
	"use strict";
	return {
		formatTitleCase: function(sVal) {
			if (!sVal) {
				return;
			}
			return sVal.replace(/\w\S*/g,
				function(txt) {
					return txt.charAt(0).toUpperCase() + txt.substr(1).toLowerCase();
				});
		},

		formatFirstLowerCase: function(sVal) {
			if (!sVal) {
				return;
			}
			return sVal.substring(0, 1).toLowerCase() + sVal.substr(1);
		},
		
		formatDigit: function(sVal) {
			return sVal > 9 ? sVal : "0" + sVal;
		},
		getDateformat: function(d, b) {
			if(b){
				return this.formatDigit(d.getDate())+"-"+this.formatDigit(d.getMonth()+1)+"-"+d.getFullYear()+" "+
					this.formatDigit(d.getHours())+":"+this.formatDigit(d.getMinutes())+":"+this.formatDigit(d.getSeconds());
			}else{
				return d.getFullYear()+"-"+this.formatDigit(d.getMonth()+1)+"-"+this.formatDigit(d.getDate())+" "+
					this.formatDigit(d.getHours())+":"+this.formatDigit(d.getMinutes())+":"+this.formatDigit(d.getSeconds());
			}
		},
		getDateOnlyformat: function(d, b) {
			if(b){
				return this.formatDigit(d.getDate())+"-"+this.formatDigit(d.getMonth()+1)+"-"+d.getFullYear();
			}else{
				return d.getFullYear()+"-"+this.formatDigit(d.getMonth()+1)+"-"+this.formatDigit(d.getDate());
			}
		}
	};
});