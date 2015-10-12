//Servlet  to validate the username and password from the database
package de.lsvn.util;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import javax.crypto.Cipher;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.lsvn.controller.NewPwdMailDispatcher;

/**
 *
 * @author henningrichter
 */
public class NewPwd extends HttpServlet{

    private Connection connection;
    private int userId;
    
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,IOException {
        connection = DbUtil.getConnection();
        HttpSession session = request.getSession(true);
        try {
        	int count = 0;
        //convert the request parameters to UTF-8 to allow for special signs
            byte[] usrISO = request.getParameter("usr").getBytes();
            byte[] usrLatin = new String(usrISO, "UTF-8").getBytes("ISO-8859-1");
            String usr = new String(usrLatin, "UTF-8");
            String firstName = "";
            
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT Id, Vorname FROM mitglieder where eMail=?");
            preparedStatement.setString(1, usr);
            ResultSet rs = preparedStatement.executeQuery();
            if (rs.first()) {
            	userId = rs.getInt("Id");
            	firstName = rs.getString("Vorname");
            	session.setAttribute("memberId", userId);
            	Date date = new Date();
            	long timestamp = date.getTime();
            	String rndmString = HashGen.generateRndmPwd();
            	try {
					String hashedRndmString = PasswordHash.createHash(rndmString);
	            	PreparedStatement prpdInsertStatement = connection.prepareStatement("INSERT INTO 47_lsvn.password_change_requests (Time,UserID,Token) VALUES (?,?,?)");
	            	prpdInsertStatement.setLong(1, timestamp);
	            	prpdInsertStatement.setInt(2, userId);
	            	prpdInsertStatement.setString(3, hashedRndmString);
	            	prpdInsertStatement.execute();
	            	NewPwdMailDispatcher.dispatchNewPwdEmail(request, usr, firstName, rndmString);
	            	request.setAttribute("message", "Ein Link zur Vergabe eines neuen Passworts wurde an Deine eMail-Adresse gesendet.");
	            	ServletContext servletContext = getServletContext();
	            	RequestDispatcher dispatcher = servletContext.getRequestDispatcher("/index.jsp");
	            	dispatcher.forward(request, response);
				} catch (NoSuchAlgorithmException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvalidKeySpecException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
            else 
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