sap.ui.define(["sap/viz/ui5/data/FlattenedDataset", "sap/viz/ui5/controls/common/feeds/FeedItem", "sap/m/FlexItemData"], 
		function(FlattenedDataset, FeedItem, FlexItemData) {
	"use strict";
	return {
		/**
		 * set property VizFrame chart.
		 * oVizFrame 작성은 Trend.controller.js 참고
		 * Chart Property Reference: https://sapui5.hana.ondemand.com/docs/vizdocs/index.html
		 */
		setVizFrame: function(oVizFrame, v, vizFrame) {
			if(undefined==vizFrame||null==vizFrame){ vizFrame = v.byId(oVizFrame.id); }
			vizFrame.destroyDataset();
			vizFrame.destroyFeeds();
			vizFrame.setVizType(oVizFrame.type);
			for (var i = 0; i < oVizFrame.feedItems.length; i++) {vizFrame.addFeed(new FeedItem(oVizFrame.feedItems[i]));}
			vizFrame.setDataset(new FlattenedDataset(oVizFrame.dataset));
			vizFrame.setVizProperties(oVizFrame.properties);
		},
		resetSlider: function(oVizFrame, v, oModel, flag) {
			var oslider = flag?v:v.byId(oVizFrame.sliderId);
			oslider._getRangeSlider().setRange([0,100]);
			oslider.setVizType(oVizFrame.sliderType);
			oslider.setValueAxisVisible(false);
			for (var i = 0; i < oVizFrame.sliderFeedItems.length; i++) {
				oslider.addFeed(new FeedItem(oVizFrame.sliderFeedItems[i]));
			}
			oslider.setDataset(new FlattenedDataset(oVizFrame.silderDataset));
			oslider.setModel(oModel);
		},
		setSlider: function(oVizFrame, v, flag) {
			var oslider = flag?v:v.byId(oVizFrame.sliderId);
			oslider.destroyDataset();
			oslider.removeAllFeeds();
			oslider.setVizType(oVizFrame.sliderType);
			oslider.setValueAxisVisible(false);
			for (var i = 0; i < oVizFrame.sliderFeedItems.length; i++) {
				oslider.addFeed(new FeedItem(oVizFrame.sliderFeedItems[i]));
			}
			oslider.setDataset(new FlattenedDataset(oVizFrame.silderDataset));
		}
	};
});