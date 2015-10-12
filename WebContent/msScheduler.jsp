<%-- 
    Document   : msScheduler
    Created on : May 20, 2014, 6:29:53 PM
    Author     : henningrichter
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>Motorsegler-Buchung</title>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width">
        <link rel="stylesheet" href="css/bootstrap.min.css" />
        <link rel="stylesheet" href="css/jquery-ui-1.10.3.custom.css" />
        <link rel="stylesheet" href="css/styles.css" />
        <link rel='stylesheet' href='css/fullcalendar.css' />
        <link rel='stylesheet' href='css/msscheduler.css' />
    </head>
    <body>
        <div id="calendar"></div>
        <div class="dialog">
            <h4 class="dialog-header"></h4>
            <div class="time-range-slider-cntnr">
                <div class="time-range-slider"></div>
                <div class="time-range-slider-scale clearfix"></div>
                <div class="time-range-slider-occupied clearfix"></div>
            </div>
            <div class="time-cntnr">
                <p class="time-wrapper">Beginn:<span class="start-time"></span></p>
                <p class="time-wrapper">Ende:<span class="end-time"></span></p>
            </div>
        </div>
        
        <script src="js/libs/jquery-1.11.0.js"></script>
        <script src="js/libs/jquery-ui-1.10.3.custom.js"></script>
        <script src='js/libs/moment.min.js'></script>
        <script src='js/libs/fullcalendar.js'></script>
        <script src='js/libs/de.js'></script>
        <script src='js/msscheduler.js'></script>
    </body>
</html>