<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="de">
    <head>
        <meta charset="UTF-8" />
        <title>LSVN Mitgliederverzeichnis</title>
        <link rel="stylesheet" href="css/bootstrap.min.css" />
        <link rel="stylesheet" href="css/jquery-ui-1.10.3.custom.css" />
        <link rel="stylesheet" href="css/styles.css" />
    </head>
    <body>
    	<div class="message">${message}</div>
        <form class="login" name="form" method="post" action="Login" onsubmit="javascript:return validate();">
            <div class="login-header">Mitgliederverzeichnis des LSVN Northeim</div>
            <p>Nutzername:<input type="text" name="usr"></p>
            <p>Passwort:<input type="password" name="pwd"></p>
            <input type="submit" value="Anmelden" class="logIn">
        </form>
        <a href="newPwdRequest.jsp" style="padding-left:1em">Passwort vergessen?</a>
        <script src="js/libs/jquery.min.js"></script>
        <script src="js/libs/jquery-ui-1.10.3.custom.js"></script>
        <script src="js/general.js"></script>
    </body>
</html>