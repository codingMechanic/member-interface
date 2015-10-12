package de.lsvn.controller;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import de.lsvn.dao.UserDao;
import de.lsvn.model.User;
import de.lsvn.util.PasswordHash;

public class PwdSetter extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final UserDao dao;

    public PwdSetter() {
        super();
        dao = new UserDao();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        int memberId = (Integer) session.getAttribute("memberId");
        User user = dao.getUserById(memberId);
        String oldPwd = request.getParameter("oldPwd");
        String newPwd = request.getParameter("newPwd");
        String newPwdHash = "";
        Boolean valid = false;

        if (oldPwd != null && newPwd != null) {
            try {
                valid = PasswordHash.validatePassword(oldPwd, user.getPwd());
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (valid) {
                try {
                    newPwdHash = PasswordHash.createHash(newPwd);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dao.resetUserPwd(memberId, newPwdHash);
                response.flushBuffer();
                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.write("Das Passwort wurde geändert.");
                response.flushBuffer();
            }
        }
    }
}