package de.lsvn.controller;

import java.io.BufferedReader;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

import de.lsvn.dao.UserDao;
import de.lsvn.model.User;
import javax.servlet.http.HttpSession;

public class UpdateUserController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final UserDao dao;

    public UpdateUserController() {
        super();
        dao = new UserDao();
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        int memberId = (Integer) session.getAttribute("memberId");
        
        if((Integer) memberId != null) {
          StringBuffer requestBody = new StringBuffer();
          String line = null;
          try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
              requestBody.append(line);
          } catch (Exception e) { 
              // TODO Auto-generated catch block
              e.printStackTrace();
          }
          try {
              //generate JSON from request body
              JSONObject jsonObject = new JSONObject(requestBody.toString());
              int userid = (Integer) jsonObject.get("userid");
              if (userid == (Integer) memberId) {

                  User user = new User();
                  user.setFirstName(jsonObject.getString("firstname"));
                  user.setLastName(jsonObject.getString("lastname"));
                  user.setEmail(jsonObject.getString("email"));
                  user.setTelephone(jsonObject.getString("telephone"));
                  user.setHandy(jsonObject.getString("handy"));
                  user.setBirthday(jsonObject.getString("birthday"));
                  user.setMembership(jsonObject.getString("membership"));
                  user.setVoting(jsonObject.getBoolean("voting"));
                  user.setLicense(jsonObject.getBoolean("license"));
                  user.setYouth(jsonObject.getBoolean("youth"));
                  user.setAhk(jsonObject.getBoolean("ahk"));
                  user.setStreet(jsonObject.getString("street"));
                  user.setPlz(jsonObject.getString("plz"));
                  user.setCity(jsonObject.getString("city"));
                  user.setCountry(jsonObject.getString("country"));
                  user.setSpecialStatus(jsonObject.getString("specialStatus"));
                  user.setEmailOffice(jsonObject.getString("emailOffice"));
                  user.setPhoneOffice(jsonObject.getString("phoneOffice"));
                  user.setMedFrom(jsonObject.getString("medFrom"));
                  user.setMedTo(jsonObject.getString("medTo"));

                  if (!jsonObject.has("userid")) {
                      dao.addUser(user);
                  } else {
                      try {
                          user.setUserid(userid);
                          dao.updateUser(user);
                      } catch (Exception e) {
                      }
                  }
              } else {

              }
          } catch (Exception e) {
            // crash and burn
            throw new IOException("Error parsing JSON request string");
          }
          
        }
        
    }
}