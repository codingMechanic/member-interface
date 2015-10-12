package de.lsvn.controller;

import javax.servlet.http.HttpServlet;
import de.lsvn.dao.UserDao;
import de.lsvn.model.Event;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author henningrichter
 */
public class MsScheduler extends HttpServlet {

    private static final long serialVersionUID = 1L;
    final UserDao dao;

    public MsScheduler() {
        super();
        dao = new UserDao();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(true);
                
        //handle page load request
        if ( (Boolean)session.getAttribute("MS") ) {
            JSONArray jsonArray = new JSONArray();
            
            for (Event event : dao.getSchedulerEvents(request.getParameter("start"), request.getParameter("end"))) {
                JSONObject jsonObject = new JSONObject();
                try {
                    jsonObject.put("id", event.getId());
                    jsonObject.put("start", event.getStart());
                    jsonObject.put("end", event.getEnd());
                    jsonObject.put("name", event.getName());
                    jsonObject.put("memberId", event.getMitgliederId());
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                jsonArray.put(jsonObject);
            }
            
            //add user id and family name for automatic setting of a new event's name in the scheduler
            JSONObject usrJsonObject = new JSONObject();
            try {
                usrJsonObject.put("memberId", session.getAttribute("memberId"));
                usrJsonObject.put("memberName", session.getAttribute("Nachname"));
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            jsonArray.put(usrJsonObject);
            
            response.setContentType("application/json");
            PrintWriter out = response.getWriter();
            out.write(jsonArray.toString());
            response.flushBuffer();
            
        }
    }
}