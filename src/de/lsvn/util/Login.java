//Servlet  to validate the username and password from the database
package de.lsvn.util;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author henningrichter
 */
public class Login extends HttpServlet{

    private Connection connection;
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        connection = DbUtil.getConnection();
        HttpSession session = request.getSession(true);
        try {int count = 0;
        //convert the request parameters to UTF-8 to allow for special signs
            byte[] usrISO = request.getParameter("usr").getBytes();
            byte[] usrLatin = new String(usrISO, "UTF-8").getBytes("ISO-8859-1");
            String usr = new String(usrLatin, "UTF-8");
            byte[] pwdISO = request.getParameter("pwd").getBytes();
            byte[] pwdLatin = new String(pwdISO, "UTF-8").getBytes("ISO-8859-1");
            String pwd = new String(pwdLatin, "UTF-8");
//            String usr = request.getParameter("usr");
//            String pwd = request.getParameter("pwd");
            String pwdHash = "";
            String familyName = "";
            boolean ms = false;
            int authId = -1;
            Boolean valid = false;
            
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT pwd, AuthentId, Nachname, MS FROM authentifizierung INNER JOIN mitglieder ON authentifizierung.AuthentId=mitglieder.Id where eMail=?");
            preparedStatement.setString(1, usr);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                pwdHash = rs.getString("pwd");
                familyName = rs.getString("Nachname");
                authId = rs.getInt("AuthentId");
                ms = rs.getBoolean("MS");
            }
            
            try {
            	if(pwd.length()>0) valid = PasswordHash.validatePassword(pwd, pwdHash);
            } catch (Exception e) {
                e.printStackTrace();
            }

            if ( valid ) {
                session.setAttribute("memberId", authId);
                session.setAttribute("Nachname", familyName);
                session.setAttribute("MS", ms);
                response.sendRedirect("members.jsp");
            } else 
                response.sendRedirect("newPwdRequest.jsp");
            DbUtil.closeResultSet(rs);
            DbUtil.closePreparedStatement(preparedStatement);
        } catch (SQLException e) {
            System.out.println(e);
        } 
        //gebe DB-Ressource frei (oft erlaubt der Hoster keine permanente Datenbankverbindung)
        finally {
            DbUtil.closeConnection(connection);
        }
    }
}