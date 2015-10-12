<%@ page language="java" contentType="text/html; charset=utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html lang="de">
    <head>
        <meta charset="UTF-8" />
        <title>LSVN Mitgliederverzeichnis - Passwort vergessen</title>
        <link rel="stylesheet" href="css/bootstrap.min.css" />
        <link rel="stylesheet" href="css/jquery-ui-1.10.3.custom.css" />
        <link rel="stylesheet" href="css/styles.css" />
    </head>
    <body>
        <form class="requestNewPwd">
            <input type="hidden" class="token" name="token" value="${token}"/>
        </form>
        <div class="dialog"></div>
        <h4><a href="${loginUrl}" id="index-link" style="padding-left:1em">Weiter zum Login</a></h4>
        <script src="js/libs/jquery.min.js"></script>
        <script src="js/libs/jquery-ui-1.10.3.custom.js"></script>
        <script src="js/general.js"></script>
    </body>
</html>