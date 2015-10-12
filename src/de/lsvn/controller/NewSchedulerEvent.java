package de.lsvn.controller;

import javax.servlet.http.HttpServlet;
import de.lsvn.dao.UserDao;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author henningrichter
 */
public class NewSchedulerEvent extends HttpServlet {

    private static final long serialVersionUID = 1L;
    final UserDao dao;

    public NewSchedulerEvent() {
        super();
        dao = new UserDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        
        //handle new event request
        if ( (Boolean)session.getAttribute("MS") ) {
            dao.setNewSchedulerEvent(request.getParameter("start"), request.getParameter("end"), request.getParameter("resource"), request.getParameter("eventId"));
        }
    }
}