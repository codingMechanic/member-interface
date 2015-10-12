package de.lsvn.util;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.Message.RecipientType;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import de.lsvn.dao.UserDao;
import de.lsvn.model.User;

public class EmailDispatcher {

	
    
    public static void sendHtmlEmail(String host, String port,
            final String userName, final String password,
            String subject, String message, String emailFlag, String recipient, HttpServletRequest request, UserDao dao) throws AddressException,
            MessagingException {
 
        // sets SMTP server properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", port);
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.starttls.enable", "true");
 
        // creates a new session with an authenticator
        Authenticator auth = new Authenticator() {
            public PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(userName, password);
            }
        };
 
        Session session = Session.getInstance(properties, auth);

        HttpSession serverSession = request.getSession(true);
        
        int memberId = (Integer) serverSession.getAttribute("memberId");
        User user = dao.getUserById(memberId);
        String ownEMailAddress = user.getEmail();
 
        // creates a new e-mail message
        Message msg = new MimeMessage(session);
 
        msg.setFrom(new InternetAddress(userName));
        
        List<InternetAddress> toAddressesList = new ArrayList<InternetAddress>();

        List<User> users = dao.getAllUsers();
        if(emailFlag != null){
            if(emailFlag.equals("live")) {
            	for(User singleUser : users) {
                	if(singleUser.getEmail() != "keine Angabe" && singleUser.getEmail() != null) {
                		toAddressesList.add(new InternetAddress(singleUser.getEmail()));
                	}
                }
            } else {
            	toAddressesList.add(new InternetAddress(ownEMailAddress));
            }
        } else if (recipient != null) {
        	toAddressesList.add(new InternetAddress(recipient));
        }
    
        InternetAddress[] toAddresses = toAddressesList.toArray(new InternetAddress[toAddressesList.size()]);
        
        msg.setRecipients(Message.RecipientType.TO, toAddresses);
        
        Address[] internetAddressTest = msg.getRecipients(RecipientType.TO);
        
        msg.setSubject(subject);
        msg.setSentDate(new Date());
        // set plain text message
        msg.setContent(message, "text/html; charset=utf-8");
 
        // sends the e-mail
        Transport.send(msg);
 
    }
}
