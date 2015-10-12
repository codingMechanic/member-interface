package de.lsvn.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.lsvn.dao.UserDao;
import de.lsvn.util.LinkRootHelper;

public class NewPwdController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final UserDao dao;

    public NewPwdController() {
        super();
        dao = new UserDao();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String token = (String) request.getParameter("Token");
        int usr = dao.validateToken(token);
        if(usr != -1) {
        	ServletContext servletContext = getServletContext();
        	request.setAttribute("token", token);
        	request.setAttribute("loginUrl", LinkRootHelper.getLinkRoot(request));
        	RequestDispatcher dispatcher = servletContext.getRequestDispatcher("/newPwdSubmit.jsp");
        	dispatcher.forward(request, response);
        } else {
        	response.sendRedirect("index.jsp");
        }
    }
}