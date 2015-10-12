(function($) {

    //to avoid conflicts with jsp syntax, set custom symbols for insertion with underscore
    _.templateSettings = {
        evaluate: /\{\{(.+?)\}\}/g,
        interpolate: /\{\{=(.+?)\}\}/g,
        escape: /\{\{-(.+?)\}\}/g
    };

    //create instance of master view
    var memberListView = new MemberListView();
    //create instance of router
    var membersRouter = new MembersRouter();

    //to update the function that handles UI events on the <select element so that the URL is updated when the select box is used, 
    //enable Backbone’s history support by starting the history service after our app is initialised, to monitor the URL for hash changes
    Backbone.history.start();

}(jQuery));