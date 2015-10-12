$(function() {
    var userName = "",
            sessionOwnerId = -1;
    //generate one scale div for each hour below the slider
    for (var i = 4; i < 22; i++) {
        $('.time-range-slider-scale').append('<div class="hour">' + i + '</div>');
    }
    var fullCalendar = $('#calendar').fullCalendar({
        dayClick: function(date) {
            var todaysEvents = getTodaysEvents(date);
            createDialog(date, sessionOwnerId, todaysEvents);
        },
        eventClick: function(calEvent, jsEvent, view) {
            var todaysEvents = getTodaysEvents(calEvent.start);
            createDialog(calEvent, sessionOwnerId, todaysEvents);
        },
        displayEventEnd: true,
        timezone: 'local'
    });
    var view = $('#calendar').fullCalendar('getView');
    function loadEvents() {
        $.post("MsScheduler",
                {
                    start: moment(view.start._d).format('YYYY-MM-DD HH:mm:ss'),
                    end: moment(view.end._d).format('YYYY-MM-DD HH:mm:ss')
                },
        function(data) {
            var userObject = data.pop();
            sessionOwnerId = userObject.memberId;
            userName = userObject.memberName;
            fullCalendar.fullCalendar('removeEvents');
            for (var i = 0; i < data.length; i++) {
                fullCalendar.fullCalendar('renderEvent', {title: data[i].name, start: data[i].start, end: data[i].end, eventId: data[i].id, memberId: data[i].memberId});
            }
        });
    }
    loadEvents();
    var createDialog = function(calEvent, sessionOwnerId, todaysEvents) {

        //abort if existing event has been clicked and the current user is not the event owner
        if (calEvent.eventId && sessionOwnerId !== calEvent.memberId)
            return;

        var startDate = calEvent.start ? calEvent.start._d : new Date(calEvent._d),
                endDate = calEvent.end ? calEvent.end._d : new Date(calEvent._d),
                eventId = calEvent.eventId ? calEvent.eventId : '-1';
        $('.time-range-slider').slider({range: true, animate: "slow", min: 0, max: 72, step: 1, values: [10, 62]});
        $('.dialog-header').text(moment(calEvent._d).format("DD.MM.YYYY"));

        for (var i = 0; i < todaysEvents.length; i++) {
            var eventDurationInHours = (todaysEvents[i].end - todaysEvents[i].start) / 3600000;
            var eventWidth = eventDurationInHours * 5.5 + '%';
            var eventPos = (todaysEvents[i].start.hours() - 4 + todaysEvents[i].start.minutes()/60) * 5.5 + '%';
            $('.time-range-slider-occupied').append('<div class="occupied-timespan" style="width:' + eventWidth + ';left:' + eventPos + '">' + todaysEvents[i].title + '</div>');
        }
        //set values either from slider handle positions or from opened event
        var setValues = function(e, info) {
            if (info) {
                startDate.setHours(4 + Math.floor(info.values[0] * 15 / 60));
                startDate.setMinutes(info.values[0] * 15 % 60);
                endDate.setHours(4 + Math.floor(info.values[1] * 15 / 60));
                endDate.setMinutes(info.values[1] * 15 % 60);
                $('.start-time').text(moment(startDate).format('HH:mm'));
                $('.end-time').text(moment(endDate).format('HH:mm'));
            } else if (startDate && endDate) {
                $('.start-time').text(moment(startDate).format('HH:mm'));
                $('.end-time').text(moment(endDate).format('HH:mm'));
                var sliderStartPos = (moment(startDate).hours() - 4) * 4 + (moment(startDate).minutes() / 15);
                var sliderEndPos = (moment(endDate).hours() - 4) * 4 + (moment(endDate).minutes() / 15);
                $('.time-range-slider').slider({values: [sliderStartPos, sliderEndPos]});
            }
        };
        setValues(calEvent);
        $('.dialog').dialog({
            title: userName,
            width: 900,
            buttons: {
                Reservieren: (function(e) {
                    $(this).dialog('close');
                    $.post("NewSchedulerEvent", {
                        start: moment(startDate).format('YYYY-MM-DD HH:mm:ss'),
                        end: moment(endDate).format('YYYY-MM-DD HH:mm:ss'),
                        resource: sessionOwnerId,
                        eventId: eventId
                    }, loadEvents
                            );
                }),
                Entfernen: (function(e) {
                    $(this).dialog('close');
                    $.post("PurgeSchedulerEvent", {
                        start: moment(startDate).format('YYYY-MM-DD HH:mm:ss'),
                        end: moment(endDate).format('YYYY-MM-DD HH:mm:ss'),
                        resource: sessionOwnerId,
                        eventId: eventId
                    }, loadEvents
                            );
                })}
        })
                .on('slidestop', setValues)
                .on('dialogclose', function() {
                    $('.dialog-header, .time-range-slider-occupied, .time-range-slider-occupied, .start-time, .end-time').empty();
                });
    };
    var getTodaysEvents = function(date) {
        var todaysEvents = [];
        $('#calendar').fullCalendar('clientEvents', function(event) {
            //if event lies between this date's beginning and end
            if (event.start.dayOfYear() === date.dayOfYear()) {
                todaysEvents.push(event);
            }
        });
        return todaysEvents;
    };
});