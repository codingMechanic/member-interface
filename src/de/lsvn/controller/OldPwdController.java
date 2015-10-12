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

public class OldPwdController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final UserDao dao;
    final PwdValidator pwdValidator;

    public OldPwdController() {
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
        boolean valid = false;

        if (oldPwd != null) {
            String match = "false";
            try {
                valid = PasswordHash.validatePassword(oldPwd, user.getPwd());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if ( valid )
                match = "true";
            response.flushBuffer();
            response.setContentType("text/html");
            PrintWriter out = response.getWriter();
            out.write(match);
            response.flushBuffer();
        }
    }
}