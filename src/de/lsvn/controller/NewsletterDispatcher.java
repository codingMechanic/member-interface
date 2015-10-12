package de.lsvn.controller;

import java.io.IOException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.lsvn.dao.UserDao;
import de.lsvn.util.EmailDispatcher;

public class NewsletterDispatcher extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final UserDao dao;
    Locale locDE = new Locale("de", "DE");

    public NewsletterDispatcher() {
        super();
        dao = new UserDao();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String emailFlag = request.getParameter("emails");
        String contentString = request.getParameter("content");
        byte[] bytes = contentString.getBytes( Charset.forName("UTF-8"));
        String encodedContentString = new String( bytes, Charset.forName("UTF-8") );
        
        
        // SMTP server information
        String host = "smtp.strato.de";
        String port = "587";
        String mailFrom = "info@segelflug-northeim.de";
        String password = ";<%4BQ;1sL";
 
        String subject = "LSVN-Newsletter";
        int serverPort = request.getServerPort();

     // if port is default or 0, just use the default port.
     String appUrl = request.getScheme() + "://" + request.getServerName();

     // if it's not the default port, append the port to your url
     if(serverPort != 80 || serverPort != 443 || serverPort != 0) {
         appUrl += ":" + new Integer(serverPort).toString();
     }
        String documentRoot = appUrl + request.getContextPath();
        Calendar cal = Calendar.getInstance();
        DateFormat dfDE = DateFormat.getDateInstance(DateFormat.SHORT, locDE );
 
        // message contains HTML markups
        String message = "<table align='center' cellspacing='0' cellpadding='0' border='0' width='100%'><tr><td>";
        message += "<table align='center' cellspacing='0' cellpadding='0' border='0'><tr width='900'><td width='900' height='184'>";
        message += "<img display='block' src='" + documentRoot + "/img/bg-header.jpg' alt='header-image' width='900' height='184' /></td></tr>";
        message += "<tr width='900'><td align='center' cellpadding='30' width='900' height='80' style='font-weight: bold; font-size: 130%;'>" + dfDE.format(cal.getTime()) + "</td></tr>";
        message += "<tr width='900'><td align='left' cellpadding='30' width='900' >"
        		+ encodedContentString
        		+ "</td></tr>";
        message += "<tr width='900'><td align='center' cellpadding='30' width='900' height='50' ></td></tr>";
        message += "<tr width='900'><td align='center' cellpadding='30' width='900' style='font-size: 80%; color: #ffffff; background-color: #3a9afa;'>"
        		+ "Luftsportverein Northeim | 1. Vorsitzender: Ralf Teichert | Tel:+49 (0)5551 - 2570 | info@segelflug-northeim.de | Webdesign: www.codingmechanic.de"
        		+ "</td></tr></table>";
        message += "</td></tr></table>";
 
        try {
        	EmailDispatcher.sendHtmlEmail(host, port, mailFrom, password,
                    subject, message, emailFlag, null, request, dao);
            System.out.println("Email sent.");
        } catch (Exception ex) {
            System.out.println("Failed to send email.");
            ex.printStackTrace();
        }
    }
    
}