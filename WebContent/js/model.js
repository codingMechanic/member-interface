var defaultPlaceholder = "keine Angabe";
    
//define member model
var Member = Backbone.Model.extend({
	defaults : {
		street : defaultPlaceholder,
		birthday : defaultPlaceholder,
		membership : defaultPlaceholder,
		email : defaultPlaceholder,
		firstname : defaultPlaceholder,
		lastname : defaultPlaceholder,
		plz : defaultPlaceholder,
		handy : defaultPlaceholder,
		telephone : defaultPlaceholder,
		country : defaultPlaceholder,
		city : defaultPlaceholder,
		phoneOffice : defaultPlaceholder,
		voting : defaultPlaceholder,
		license : defaultPlaceholder,
		youth : defaultPlaceholder,
		specialStatus : defaultPlaceholder,
		emailOffice : defaultPlaceholder,
		medFrom : defaultPlaceholder,
		medTo : defaultPlaceholder,
		ahk : defaultPlaceholder,
		userid : defaultPlaceholder,
		excluded : false,
		owner : false
	}
});

//define member collection
var MemberList = Backbone.Collection.extend({
    model: Member,
    url: 'UserController'
});