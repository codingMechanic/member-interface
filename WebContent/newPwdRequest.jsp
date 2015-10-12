<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="de">
    <head>
        <meta charset="UTF-8" />
        <title>LSVN Mitgliederverzeichnis - </title>
        <link rel="stylesheet" href="css/bootstrap.min.css" />
        <link rel="stylesheet" href="css/jquery-ui-1.10.3.custom.css" />
        <link rel="stylesheet" href="css/styles.css" />
    </head>
    <body>
    <div class="requestNewPwd-cntnr">
        <form class="requestNewPwd" name="form" method="post" action="NewPwd" >
            <p>Nutzername: <input type="text" name="usr" value="" placeholder="Deine eMail-Adresse"></p>
            <input type="submit" value="Neue Passwortvergabe anfordern" class="requestNewPwd ui-button ui-widget ui-state-default ui-corner-all">
        </form>
        <a class="back-to-index" href="index.jsp"><p class="back-to-index-caption">Erneut versuchen?</p></a>
    </div>
        <script src="js/libs/jquery.min.js"></script>
        <script src="js/libs/jquery-ui-1.10.3.custom.js"></script>
        <script src="js/general.js"></script>
    </body>
</html>