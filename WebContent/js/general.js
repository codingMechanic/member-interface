(function($) {

    function validate() {
        var username = document.form.usr.value;
        var password = document.form.pwd.value;
        if (username === "") {
            alert("Enter Username!");
            return false;
        }
        if (password === "") {
            alert("Enter Password!");
            return false;
        }
        return true;
    }

    function requestNewPwd() {
        var username = document.form.usr.value;
        if (username === "") {
            alert("Enter Username!");
            return false;
        }
        return true;
    }

    function openNewPwdDialog(forgotPwd) {
        $('.dialog')
                .dialog({
                    title: "Passwort ändern",
                    width: $('.member-col-2').width(),
                    modal: true,
                    minWidth: 420,
                    position: ['center', 150],
                    open: function() {
                    	var oldPwdMarkup = !forgotPwd ? '<p class="oldPwd-container">altes Passwort <input type="password" id="oldPwd"><span class="check-hook oldPwd"></span></p>' : '',
                    			newPwdInputDisabled = !forgotPwd ? 'disabled' : '',
                    					newPwdAreaInactive = !forgotPwd ? 'inactive' : '';
                        $('.ui-dialog-content').html(
                        		oldPwdMarkup
                                + '<div class="newPwd-area">'
                                + '<p class="newPwd-container"><span class="' + newPwdAreaInactive + ' desc">neues Passwort</span><input type="password" id="newPwd" ' + newPwdInputDisabled + '><span class="check-hook newPwd"></p>'
                                + '<p class="newPwdRepeat-container no-display">neues Passwort wiederholen<input type="password" id="newPwdRepeat"><span class="check-hook newPwd-repeat"></span></p>'
                                + '<p class="' + newPwdAreaInactive + '">Das Passwort muss mindestens enthalten...</p>'
                                + '<div class="container clearfix"><p class="criteria tooShort ' + newPwdAreaInactive + ' desc">... 6 Zeichen</p><div class="check-hook tooShort-img"></div></div>'
                                + '<div class="container clearfix"><p class="criteria noCaps ' + newPwdAreaInactive + ' desc">... 1 Großbuchstabe</p><div class="check-hook noCaps-img"></div></div>'
                                + '<div class="container clearfix"><p class="criteria noSpecials ' + newPwdAreaInactive + ' desc">... 1 Sonderzeichen</p><div class="check-hook noSpecials-img"></div></div>'
                                + '<div class="container clearfix"><p class="criteria noDigits ' + newPwdAreaInactive + ' desc">... 1 Ziffer</p><div class="check-hook noDigits-img"></div></div>'
                                + '</div>'
                                );
                    },
                    buttons: [{text: "OK", click: function() {
                    			if(forgotPwd) {
                    				sendNewForgottenPwd();
                    			} else {
                    				sendNewPwd();
                    			}
                            }, "id": "submitPwd", "class": "submit"},
                        {text: "Abbrechen", click: function() {
                                $('.dialog').dialog('close');
                            }, "id": "cancelPwdReset", "class": "cancel"}]
                })
                .dialog('open');
        $('.submit').button('disable');
        $('.submit').removeClass('ui-state-focus');
        $('#oldPwd').autocomplete({
            source: function(request, response) {
                $.post('OldPwdController',
                        {oldPwd: $('#oldPwd').val()},
                function(match) {
                    if (match == 'true') {
                        $('.newPwd-area .inactive').removeClass('inactive');
                        $('.newPwd-area input').removeAttr('disabled');
                        $('.check-hook.oldPwd').show();
                    } else {
                        $('.newPwd-area .desc').addClass('inactive');
                        $('.newPwd-area input').attr('disabled', 'disabled');
                        $('.check-hook.oldPwd').hide();
                    }
                },
                'text');
            }
        });
        $('#newPwd').autocomplete({
            source: function(request, response) {
            	var action = 'PwdController',
            	parameters = {oldPwd: $('#oldPwd').val(), newPwd: $('#newPwd').val()}
            	if(forgotPwd) {
            		action = 'ForgotPwdController';
            		parameters = {newPwd: $('#newPwd').val()}
            	}
                $.post(action, parameters,
                function(responseJson) {
                    //set visibility of check marks according to specific criteria states
                    responseJson.tooShort ? $('.tooShort-img').hide() : $('.tooShort-img').show();
                    responseJson.noDigits ? $('.noDigits-img').hide() : $('.noDigits-img').show();
                    responseJson.noSpecials ? $('.noSpecials-img').hide() : $('.noSpecials-img').show();
                    responseJson.noCaps ? $('.noCaps-img').hide() : $('.noCaps-img').show();
                    //if all criteria is met, release submit button
                    if (!responseJson.tooShort && !responseJson.noDigits && !responseJson.noSpecials && !responseJson.noCaps) {
                        $('#newPwdRepeat').val('');
                        $('.newPwdRepeat-container, .check-hook.newPwd').show();
                    }
                    else {
                        $('.newPwdRepeat-container, .check-hook.newPwd').hide();
                    }
                },
                        'json');
            }
        });
        $('#newPwdRepeat').autocomplete({
            source: function(request, response) {
            	var action = 'PwdController';
            	if(forgotPwd) {
            		action = 'ForgotPwdController';
            	}
                $.post(action,
                        {newPwd: $('#newPwd').val(), newPwdRpt: $('#newPwdRepeat').val()},
                function(match) {
                    if (match == 'true') {
                        $('.check-hook.newPwd-repeat').show();
                        $('.submit').button('enable');
                    } else {
                        $('.check-hook.newPwd-repeat').hide();
                        $('.submit').button('disable');
                    }
                },
                        'text');
            }
        });
    }
    function openPwdConfirmAlert(response) {
        $('.dialog')
                .dialog('close')
                .dialog({
                    title: "Das Passwort wurde geändert.",
                    open: function() {
                        $('.ui-dialog-content').html('');
                    }
                    , buttons: [{text: "OK", click: function() {
                                $('.dialog').dialog('close');
                                $('#index-link').fadeIn();
                            }, "id": "confirmPwdOk", "class": "confirm"}]
                })
                .dialog('open');
    }
    function openPwdRejectAlert(response) {
        $('.dialog')
                .dialog('close')
                .dialog({
                    title: response,
                    open: function() {
                        $('.ui-dialog-content').html(
                                );
                    }
                    , buttons: []
                })
                .dialog('open');
    }
    function sendNewPwd() {
        $.post('PwdReset', {oldPwd: $('#oldPwd').val(), newPwd: $('#newPwd').val()}, function(response) {
            $('#retryPwdReset, #cancelPwdReset').hide();
            openPwdConfirmAlert(response);
        });
    }
    function sendNewForgottenPwd() {
        $.post('NewPwdSubmit', {token: $('.token').val(), newPwd: $('#newPwd').val()}, function(response) {
            $('#retryPwdReset, #cancelPwdReset').hide();
            openPwdConfirmAlert(response);
        });
    }
    function sendNewsletter(emailFlag, contentString) {
        $.post('SendNewsletter', {emails: emailFlag, content: contentString}, function(response) {
        	$('.newsletter-submit.inactive').removeClass('inactive').addClass('active');
        });
    }

    $('.cancel, .retry, .confirm, .logIn').button();
    
    $('.user').button({icons: {primary: "ui-icon-carat-1-s"}});

    //extend/retrieve links for changing password and logoff
    $('.user').click(function(event) {
        event.stopPropagation();
        $(this).siblings('.user-body').toggle();
    });

    $('#openNewPwdDialog').click(function() {openNewPwdDialog(false)});

    $('#eMailDispatch').click(function() {
        if ($('#emailDispatchCntnr').hasClass('active')) {
            $('#emailDispatchCntnr').slideUp(600).removeClass('active');
        } else {
            $('#emailDispatchCntnr').slideDown(600).addClass('active');
        }
    });

    $('#newsletterDispatch').click(function() {
        if ($('#newsletterCntnr').hasClass('active')) {
            $('#newsletterCntnr').slideUp(600).removeClass('active');
        } else {
            $('#newsletterCntnr').slideDown(600).addClass('active');
        }
    });
    
    $('.newsletter-test-submit').click(function() {sendNewsletter("test", $('#newsletter-input').val());});
    $('.newsletter-cntnr-buttons').on('click', '.newsletter-submit.active', function() {sendNewsletter("live", $('#newsletter-input').val());});
    
    $('#emailDispatchCntnr').on('click', '.exclude-email', function(){
        $(this).closest('.email-container').addClass('excluded').removeClass('included');
        $(this).siblings('.include-email').show();
        $(this).hide();
    });
    
    $('#emailDispatchCntnr').on('click', '.include-email', function(){
        $(this).closest('.email-container').removeClass('excluded').addClass('included');
        $(this).siblings('.exclude-email').show();
        $(this).hide();
    });
    
    if($('.requestNewPwd').length) openNewPwdDialog(true);

}(jQuery));