var MembersRouter = Backbone.Router.extend({
	routes : {
		//routes specify the element by its id in the key before the slash
		//routes can contain parameter parts, :param, which are passed to the callback function
		"filter/:membership" : "urlFilter"
	},
	urlFilter : function(membership) {
		memberListView.filterType = membership;
		memberListView.trigger("change:filterType");
	}
});