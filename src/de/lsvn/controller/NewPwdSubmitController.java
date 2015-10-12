package de.lsvn.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.lsvn.dao.UserDao;
import de.lsvn.util.PasswordHash;

public class NewPwdSubmitController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final UserDao dao;

    public NewPwdSubmitController() {
        super();
        dao = new UserDao();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String token = request.getParameter("token");
        String newPwd = request.getParameter("newPwd");
        
        String newPwdHash = "";
        
        try {
            newPwdHash = PasswordHash.createHash(newPwd);
            
            int usr = dao.validateToken(token);
            
            if (usr >= 0 && dao.resetUserPwd(usr, newPwdHash)) {
            	request.setAttribute("message", "Das Passwort wurde erfolgreich geändert.");
            	request.removeAttribute("token");
            } else {
            	request.setAttribute("message", "Es ist ein Fehler aufgetreten. Bitte setze Dich mit dem <a href=\"mailto:info@codingmechanic.de\">Admin</a> in Verbindung.");
            	request.removeAttribute("token");
            }
        	ServletContext servletContext = getServletContext();
        	RequestDispatcher dispatcher = servletContext.getRequestDispatcher("/index.jsp");
        	dispatcher.forward(request, response);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}