var memberList = new MemberList({model: Member}),
filteredMemberList = new MemberList({model: Member});
    
//define individual member view
var MemberView = Backbone.View.extend({
	tagName : "div",
	className : "member-container",
	template : $("#memberTemplate").html(),
	editTemplate : _.template($("#memberEditTemplate").html()),
	events : {
		"click button.edit" : "openEdit",
		"change select.membership" : "addType",
		"click button.save" : "saveEdits",
		"click button.cancel" : "cancelEdit",
		"click email-button" : "test",
		"click #msScheduler-link-button" : 'openMSScheduler'
	},
	render : function() {
		var tmpl = _.template(this.template);
		var json = this.model.toJSON();

		this.$el.html(tmpl(json));
		//bereite String des Mitgliedschaftstyps zu class-tauglichem String ohne Leerzeichen auf
		var membershipClass = json.membership.replace(/\s|\.\s/g, '-');
		this.$el.addClass(membershipClass);
		this.$el.find('.vote input')[0].checked = json.voting;
		this.$el.find('.license input')[0].checked = json.license;
		this.$el.find('.youth input')[0].checked = json.youth;
		this.$el.find('.ahk input')[0].checked = json.ahk;
		this.$el.find('.mail, .mail-offc').each(function() {
			//swap mailto-link to "keine Angabe" with plain text "keine Angabe"
			if ($(this).children('.addr').text() === 'keine Angabe')
				$(this).children('.addr').html('keine Angabe');
		});
		return this;
	},
	openEdit : function() {
		var json = this.model.toJSON();
		if (this.model.get('owner')) {
			this.$el.html(this.editTemplate(json));
			this.$el.find('.voting')[0].checked = json.voting;
			this.$el.find('.license')[0].checked = json.license;
			this.$el.find('.youth')[0].checked = json.youth;
			this.$el.find('.ahk')[0].checked = json.ahk;
			this.select = memberListView.createSelect(
					this.$el.find("#membership").val()).addClass("membership")
					.insertAfter(this.$el.find(".name-cntnr"));
			this.$el.find("input[type='hidden']").remove();
			$('.birthday').datepicker({
				changeYear : true,
				dateFormat : "dd.mm.yy"
			});
			$('.medFrom, .medTo').datepicker({
				dateFormat : "dd.mm.yy"
			});
		}
	},
	saveEdits : function(e) {
		e.preventDefault();

		var formData = {},
		//previousAttributes property of models is a data store Backbone maintains to show what an attribute’s previous data was
		prev = this.model.previousAttributes();

		$(e.target).closest("form").find(":input").not('button').each(
				function() {
					if ($(this).attr('type') === 'checkbox')
						formData[$(this).attr("class")] = this.checked;
					else
						formData[$(this).attr("class").replace(
								/ hasDatepicker/, '')] = $(this).val();
				});

		this.model.set(formData);

		this.render();

		var members = memberListView.members;
		_.each(members, function(member) {
			//_.isEqual performs a deep comparison between two objects, to determine if they should be considered equal
			if (_.isEqual(member, prev)) {
				//splice() adds/removes items to/from an array
				members.splice(_.indexOf(members, member), 1, formData);
			}
		});

		this.model.save();
	},
	cancelEdit : function() {
		this.render();
	}

});

//define individual addressee view
var AddresseeView = Backbone.View.extend({
	tagName : "div",
	className : "email-container included clearfix",
	template : $("#eMailDispatchTemplate").html(),
	initialize : function() {
		this.on('exclude', this.exclude);
		this.on('include', this.include);
	},
	include : function() {
		this.model.set({
			excluded : false
		});
		console.log(this.model.get('lastname') + ' included');
	},
	exclude : function() {
		this.model.set({
			excluded : true
		});
		console.log(this.model.get('lastname') + ' excluded');
	},
	render : function() {
		var tmpl = _.template(this.template);
		var json = this.model.toJSON();
		var self = this;

		this.$el.html(tmpl(json));
		//bereite String des Mitgliedschaftstyps zu class-tauglichem String ohne Leerzeichen auf
		var membershipClass = json.membership.replace(/\s|\.\s/g, '-');
		this.$el.find('.membership').addClass(membershipClass);
		if (self.model.get('email') === 'keine Angabe') {
			this.$el.addClass('unspecified');
			this.$el.find('.unspecified-note').text(' - keine email-Adr.');
		}
		//listen to the click on the entfernen-/hinzu-button and trigger corresponding custom event
		this.$el.find('.email-button').click(
				function() {
					self.model.get('excluded') ? self.trigger('include') : self
							.trigger('exclude');
				});
		return this;
	}
});

//define eMail list view
var AddresseeListView = Backbone.View.extend({
	el : $("#emailDispatchCntnr"),
	addresseeCount : 0,

	initialize : function() {
		this.collection = filteredMemberList;
		this.render();
		//listen for the reset event to invoke the render() method
		filteredMemberList.on("reset", this.render, this);
		this.on('click:dispatch', this.dispatch, this);
	},

	events : {
		"click .dispatch" : "dispatch"
	},

	dispatch : function() {
		var addressees = '';
		_.each(
		//filter recipients for included classification and for available email address 
		_.filter(this.collection.models, function(member) {
			return !member.get('excluded')
					&& member.get('email') !== 'keine Angabe';
		}),
		//concatenate filtered recipients for use in email address line
		function(member) {
			addressees += member.get('email') + ',';
			;
		});
		//open email client with the recipients from addressees in the address line
		document.location.href = "mailto:" + addressees;
	},
	render : function() {
		this.addresseeCount = filteredMemberList.length;
		var self = this;
		this.$el.find(".email-container").remove();
		_.each(filteredMemberList.models, function(item, index) {
			self.renderAddressee(item, index);
		}, this);
	},
	renderAddressee : function(item, index) {
		var addresseeView = new AddresseeView({
			model : item
		});
		//distribute the member views on the bootstrap columns
		var colIndex;
		if (index < this.addresseeCount / 2)
			colIndex = 1;
		else if (index >= this.addresseeCount / 2
				&& index <= this.addresseeCount)
			colIndex = 2;
		this.$el.find('.addressee-col-' + colIndex).append(
				addresseeView.render().el);
	}

});

//define master view
var MemberListView = Backbone.View.extend({
	el : $("#members"),
	memberCount : 0,
	rowQuantity : 0,
	template : $("#membersRowTemplate").html(),

	initialize : function() {
		_.bindAll(this, 'render', 'buildView');
		filteredMemberList.fetch({
			type : 'POST',
			success : this.buildView
		});
		this.on("change:filterType", this.filterByMembership, this);
		//listen for the reset event to invoke the render() method
		filteredMemberList.on("reset", this.render, this);
	},
	buildView : function() {
		memberList = filteredMemberList.clone();
		this.membershipClasses = this.mapMembershipClasses();
		$("#filter").append(this.createSelect('Alle'));
		this.render();
		$('.owner[value=true]').closest('.member-container').addClass('owner');
		var ownerMember = $.grep(filteredMemberList.models, function(value) {
			if (value.get('owner'))
				return true;
		});
		var ms = false;
		if (ownerMember) {
			var usr = ownerMember[0].get('email');
			$('.user').text(usr);
			if (ownerMember[0].get('ms'))
				ms = true;
		}
		;
		if (ms)
			$('#msScheduler-link-button').removeClass('hidden');
		//create AddresseeListView parallel to MemberListView
		new AddresseeListView();
	},
	render : function() {
		this.memberCount = filteredMemberList.length;
		/*--left-to-right row based alphabetical ordering---*/
		var tmpl = _.template(this.template);
		//derive the number of needed rows from the member count
		this.rowQuantity = Math.ceil(this.memberCount / 3);
		//clear the container and produce as many rows as needed for the specific filter
		this.$el.find('.member-cntnr').empty();
		for (var i = 0; i < this.rowQuantity; i++)
			this.$el.find('.member-cntnr').append(tmpl({
				rowIndex : i
			}));
		/*----*/
		//store a reference to the view to make it accessible within a callback function
		var self = this;
		this.$el.find(".member-container").remove();
		_.each(filteredMemberList.models, function(item, index) {
			self.renderMember(item, index);
		}, this);
		$('.owner[value=true]').closest('.member-container').addClass('owner');
	},
	events : {
		"change #filter select" : "setFilter"
	},
	setFilter : function(e) {
		this.filterType = e.currentTarget.value;
		//trigger custom namespaced event
		this.trigger("change:filterType");
	},
	filterByMembership : function() {
		var filterType = this.filterType;

		if (this.filterType === "Alle") {
			//use reset to replace a collection with a new list of models (or attribute hashes)
			filteredMemberList.reset(memberList.models);
			//to update the URL after something happens, call the router's navigate() method
			membersRouter.navigate("filter/all");
		} else {

			//set the silent option to true so that the reset event is not fired, 
			//so the view is not re-rendered unnecessarily when we reset the collection at the start of this branch
			filteredMemberList.reset(memberList.models, {
				silent : true
			});

			//_.filter looks through each value in the list, returning an array of all the values that pass a truth test
			filtered = _.filter(filteredMemberList.models, function(item) {
				return item.get("membership") === filterType;
			});

			filteredMemberList.reset(filtered);
			membersRouter.navigate("filter/" + filterType);
		}
	},
	renderMember : function(item, index) {
		var memberView = new MemberView({
			model : item
		});
		//distribute the member views to the bootstrap columns
		var colIndex, rowIndex;

		if (index % 3 == 0) {
			colIndex = 1;
			rowIndex = Math.floor(index / 3);
		} else if (index % 3 == 1) {
			colIndex = 2;
			rowIndex = Math.floor(index / 3);
		} else if (index % 3 == 2) {
			colIndex = 3;
			rowIndex = Math.floor(index / 3);
		}

		this.$el.find('.members-row-' + rowIndex + ' .member-col-' + colIndex)
				.append(memberView.render().el);
	},
	getTypes : function() {
		//uniq produces a duplicate-free version of the array, using === to test object equality
		return _.uniq(
		//pluck() method is a simple way to pull all values of a single attribute out of a collection of models
		memberList.pluck("membership"), false, function(membership) {
			return membership;
		});
	},
	createSelect : function(val) {
		var select = $('<select/>', {
			html : '<option>' + val + '</option>'
		});
		for ( var item in this.membershipClasses) {
			var selectOption = $('<option value="' + item + '">' + item
					+ '</option>');
			selectOption.addClass(this.membershipClasses[item]);
			selectOption.appendTo(select);
		}
		return select;
	},
	//generate an object of classNames (values) derived from the type names (keys) by an immediate function call
	mapMembershipClasses : function() {
		var classNames = {};
		_.each(this.getTypes(), function(item) {
			if (item !== 'keine Angabe') {
				var className = item.replace(/\s|\.\s/g, '-');
				classNames[item] = className;
			}
		});
		return classNames;
	}

});