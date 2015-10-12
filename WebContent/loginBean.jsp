<!--webpage to set the parameters used by JSP Bean and forward to the Servlet page-->
<%@ page language="Java" import="java.sql.*" %>  
<html> 
    <head><title>DataBase Search</title></head>  
    <body>
        <jsp:useBean id="db" scope="request" class="de.lsvn.util.LoginBean" >
            <jsp:setProperty name="db" property="usr" value="<%=request.getParameter("usr")%>"/>
            <jsp:setProperty name="db" property="pwd" value="<%=request.getParameter("pwd")%>"/>
        </jsp:useBean>
    </body>
</html>