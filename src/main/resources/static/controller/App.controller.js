sap.ui.define([
   "sap/ui/core/mvc/Controller",
   "sap/ui/Device"
], function (Controller, Device) {
    "use strict";
    return Controller.extend("ro.msg.learning.feedback.controller.App", {
        onInit: function() {
            this.getView().addStyleClass(Device.support.touch ? "sapUiSizeCozy" : "sapUiSizeCompact");
        }
    });
});