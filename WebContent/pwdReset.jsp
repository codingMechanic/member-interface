<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <script>
        function validate() {
            var oldPwd = document.form.oldPwd.value;
            var newPwd = document.form.newPwd.value;
            if (oldPwd === "") {
                alert("Bitte bisheriges Passwort eingeben!");
                return false;
            }
            if (newPwd === "") {
                alert("Bitte neues Passwort eingeben!");
                return false;
            }
            return true;
        }
    </script>
    <form name="form" method="post" action="PwdReset" onsubmit="javascript:return validate();">
        <p>altes Passwort <input type="password" name="oldPwd"></p>
        <p>neues Passwort <input type="password" name="newPwd"></p>
        <input type="submit" value="Passwort ändern">
    </form>
    <a class="cancel" id="cancelPwdReset" href="members.jsp">abbrechen</a>
    <script src="js/general.js"/>
</html>