sap.ui.define([
    "sap/ui/core/mvc/Controller"
], function(Controller) {
    "use strict";

    return Controller.extend("NewProject.controller.Main", {
        onInit: function (oEvent){
        	 window.header = this;       	
        },
        onContactPress:function(){
        	alert("Contact클릭");
        }
    });
});

