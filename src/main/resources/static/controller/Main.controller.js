sap.ui.define([
   "sap/ui/core/mvc/Controller",
   "sap/ui/Device",
   "sap/ui/core/format/DateFormat",
   "jquery.sap.global"
], function (Controller, Device, DateFormat, jQuery) {
    "use strict";

    var oInputFormat = DateFormat.getDateTimeInstance({pattern: "yyyy-MM-ddThh:mm:ss.SSSSSS", UTC: true});
    var oOutputFormat = DateFormat.getDateTimeInstance({relative: true, UTC: true});

    return Controller.extend("ro.msg.learning.feedback.controller.Main", {
        onInit: function() {
            var oRouter = sap.ui.core.UIComponent.getRouterFor(this);
            var oModel = this.getOwnerComponent().getModel();
            oRouter.getRoute("main").attachPatternMatched(this.onMatched, this);
            this.connect();

            // refresh the relative times
            jQuery.sap.intervalCall(60000, oModel, oModel.refresh, [true]);
        },

        onSelectChange: function(oEvent) {
            var sTopic = oEvent.getSource().getSelectedKey(),
                oRouter = sap.ui.core.UIComponent.getRouterFor(this);
            oRouter.navTo("main", {topicId: sTopic});
        },

        onMatched: function(oEvent) {
            var oModel = this.getView().getModel();
            jQuery.ajax({
                url: "/rest/feedback",
                method: "GET",
                success: function (aFeedback) {
                    oModel.setProperty("/feedback", aFeedback);
                }
            });
        },

        onSubmit: function() {
            var oData = {
                text: this.getView().getModel().getProperty("/text")
            };
            if (oData.text) {
                this.createFeedback(oData);
            }
        },

        createFeedback: function(oData) {
            var oModel = this.getView().getModel();
            jQuery.ajax({
                url: "/rest/feedback",
                method: "POST",
                data: JSON.stringify(oData),
                contentType: "application/json",
                success: function () {
                    oModel.setProperty("/text", "");
                }
            });
        },

        formatFeedback: function(text, sentiments) {
            var sorted = (sentiments || []).sort(function(a, b) {return a.startIndex - b.startIndex});
            var current = 0;
            var result = "";
            for (var i = 0; i < sorted.length; ++i) {
                if (sorted[i].startIndex > current) {
                    result += jQuery.sap.encodeHTML(text.substring(current, sorted[i].startIndex));
                }
                result += this.formatSentiment(text.substring(sorted[i].startIndex, sorted[i].endIndex), sorted[i]);
                current = sorted[i].endIndex;
            }
            if (current < text.length) {
                result += jQuery.sap.encodeHTML(text.substring(current));
            }
            return result;
        },

        formatSentiment: function(text, sentiment) {
            var result = '<span class="feedback';
            if (sentiment.strong) {
                result += " is-strong";
            }
            if (sentiment.type === "NEGATIVE") {
                result += " is-negative";
            } else if (sentiment.type === "POSITIVE") {
                result += " is-positive";
            }
            result += '">' + jQuery.sap.encodeHTML(text) + "</span>";
            return result;
        },

        connect: function() {
            var socket = new SockJS("/websocket");
            var stompClient = Stomp.over(socket);
            stompClient.connect({}, function () {
                stompClient.subscribe("/topic/feedback", function (message) {
                    this.onFeedbackCreated(JSON.parse(message.body));
                }.bind(this));
                stompClient.subscribe("/topic/sentiment", function (message) {
                    this.onFeedbackUpdated(JSON.parse(message.body));
                }.bind(this));
            }.bind(this));
        },

        onFeedbackCreated: function(oFeedback) {
            this.getView().getModel().getProperty("/feedback").push(oFeedback);
            this.getView().getModel().refresh();
        },

        onFeedbackUpdated: function(oFeedback) {
            var aFeedbacks = this.getView().getModel().getProperty("/feedback");
            for (var i = aFeedbacks.length - 1; i >= 0; --i) {
                if (aFeedbacks[i].id === oFeedback.id) {
                    aFeedbacks[i] = oFeedback;
                }
            }
            this.getView().getModel().refresh();
        },

        formatCreatedAt: function (sDate) {
            return oOutputFormat.format(oInputFormat.parse(sDate));
        }
    });
});