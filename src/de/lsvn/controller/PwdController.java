package de.lsvn.controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.lsvn.dao.UserDao;
import de.lsvn.model.User;
import de.lsvn.util.PasswordHash;
import de.lsvn.util.PwdValidator;
import java.io.PrintWriter;
import org.json.JSONObject;

public class PwdController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final UserDao dao;
    final PwdValidator pwdValidator;

    public PwdController() {
        super();
        dao = new UserDao();
        pwdValidator = new PwdValidator();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        int memberId = (Integer) session.getAttribute("memberId");
        User user = dao.getUserById(memberId);
        String oldPwd = request.getParameter("oldPwd");
        String newPwd = request.getParameter("newPwd");
        Boolean valid = false;

        if (oldPwd != null && newPwd != null) {
            try {
                valid = PasswordHash.validatePassword(oldPwd, user.getPwd());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ( valid ) {
                JSONObject validated = pwdValidator.validatePwd(newPwd);
                response.flushBuffer();
                response.setContentType("application/json");
	        PrintWriter out = response.getWriter();
	        out.write(validated.toString());
	        response.flushBuffer();
            }
        }
        else if (newPwd != null && request.getParameter("newPwdRpt") != null) {
            String match = "false";
            if (newPwd.equals(request.getParameter("newPwdRpt")))
                match = "true";
            response.flushBuffer();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.write(match);
            response.flushBuffer();
        }
    }
}