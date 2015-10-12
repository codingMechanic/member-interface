package de.lsvn.controller;

import javax.servlet.http.HttpServletRequest;

import de.lsvn.dao.UserDao;
import de.lsvn.util.EmailDispatcher;
import de.lsvn.util.LinkRootHelper;

public class NewPwdMailDispatcher {
    final static UserDao dao = new UserDao();

    public NewPwdMailDispatcher() {
        super();
    }
    
    public static void dispatchNewPwdEmail(HttpServletRequest request, String usr, String firstName, String rndmString) {
        
        // SMTP server information
        String host = "smtp.strato.de";
        String port = "587";
        String mailFrom = "info@segelflug-northeim.de";
        String password = ";<%4BQ;1sL";
 
        String subject = "Neues Passwort für Deinen LSVN-Account";
 
        // message contains HTML markups
     
        String message = "<h3>Hallo " + firstName + "!</h3><h4>Klicke bitte hier, um ein neues Passwort für Dein Konto des LSVN-Mitgliederverzeichnisses einzugeben:</h4><a href=\"" + LinkRootHelper.getLinkRoot(request) + "/NewPwdInput?Token=" + rndmString + "\" >Neues Passwort vergeben</a><p>Solltest Du kein neues Passwort angefordert haben, setze Dich bitte mit dem <a href=\"mailto:info@codingmechanic.de\">Administrator</a> in Verbindung</p>";
 
        try {
        	EmailDispatcher.sendHtmlEmail(host, port, mailFrom, password,
                    subject, message, null, usr, request, dao);
            System.out.println("Email sent.");
        } catch (Exception ex) {
            System.out.println("Failed to send email.");
            ex.printStackTrace();
        }
    }
    
}