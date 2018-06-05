sap.ui.define([
   "sap/ui/core/UIComponent",
   "jquery.sap.global"
], function (UIComponent, jQuery) {
    "use strict";
    return UIComponent.extend("ro.msg.learning.feedback.Component", {
        metadata: {
            manifest: "json"
        },

        init: function () {
            UIComponent.prototype.init.apply(this, arguments);
            this.getModel().setData({feedback: [], text: ""});
            this.getRouter().initialize();
        }
    });
});