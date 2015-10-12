package de.lsvn.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import de.lsvn.util.PwdValidator;

public class ForgotPwdController extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	private PwdValidator pwdValidator;
	
	public ForgotPwdController() {
        super();
        pwdValidator = new PwdValidator();
	}

	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String newPwd = request.getParameter("newPwd");
        String newPwdRpt = request.getParameter("newPwdRpt");

        if (newPwd != null && newPwdRpt == null) {
            JSONObject validated = pwdValidator.validatePwd(newPwd);
            response.flushBuffer();
            response.setContentType("application/json");
	        PrintWriter out = response.getWriter();
	        out.write(validated.toString());
	        response.flushBuffer();
        } else if (newPwd != null && newPwdRpt != null) {
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
