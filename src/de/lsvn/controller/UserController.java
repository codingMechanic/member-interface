package de.lsvn.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import de.lsvn.dao.UserDao;
import de.lsvn.model.User;
import java.util.ArrayList;
import javax.servlet.http.HttpSession;

public class UserController extends HttpServlet {
    private static final long serialVersionUID = 1L;
    final UserDao dao;

    public UserController() {
        super();
        dao = new UserDao();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        
//    	//write the GET request's parameters of the 
//    	if(request.getParameterMap().size() > 1) {
//	    	User user = new User();
//	        String userid = request.getParameter("userid");
//	        user.setFirstName(request.getParameter("firstName"));
//	        user.setLastName(request.getParameter("lastName"));
//	        user.setEmail(request.getParameter("email"));
//	        user.setTelephone(request.getParameter("telephone"));
//	        user.setHandy(request.getParameter("handy"));
//	        user.setBirthday(request.getParameter("birthday"));
//	        user.setMembership(request.getParameter("membership"));
//	        user.setStreet(request.getParameter("street"));
//	        user.setPlz(request.getParameter("plz"));
//	        user.setCity(request.getParameter("city"));
//	        user.setCountry(request.getParameter("country"));
//	        if(userid == null || userid.isEmpty()) {
//	            dao.addUser(user);
//	        }
//	        else {
//	            user.setUserid(Integer.parseInt(userid));
//	            dao.updateUser(user);
//	        }
//    	} else
        if(session.getAttribute("memberId") != null) {
	    	JSONArray jsonArray = new JSONArray();
	        for (User user : dao.getAllUsers()) {
	            JSONObject jsonObject=new JSONObject();
	                try {
	                    jsonObject.put("userid", user.getUserid());
	                    jsonObject.put("firstname", user.getFirstName());
	                    jsonObject.put("lastname", user.getLastName());
	                    jsonObject.put("email", user.getEmail());
	                    jsonObject.put("telephone", user.getTelephone());
	                    jsonObject.put("handy", user.getHandy());
	                    jsonObject.put("birthday", user.getBirthday());
	                    jsonObject.put("voting", user.isVoting());
	                    jsonObject.put("membership", user.getMembership());
	                    jsonObject.put("license", user.getLicense());
	                    jsonObject.put("youth", user.getYouth());
	                    jsonObject.put("ahk", user.isAhk());
	                    jsonObject.put("street", user.getStreet());
	                    jsonObject.put("plz", user.getPlz());
	                    jsonObject.put("city", user.getCity());
	                    jsonObject.put("country", user.getCountry());
	                    jsonObject.put("special", user.getSpecialStatus());
	                    jsonObject.put("medFrom", user.getMedFrom());
	                    jsonObject.put("medTo", user.getMedTo());
	                    jsonObject.put("ms", user.isMs());
                            if( (Integer)session.getAttribute("memberId") == user.getUserid() )
                                jsonObject.put("owner", true);
	                } catch (JSONException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
                jsonArray.put(jsonObject);
	        }
	        response.setContentType("application/json");
	        PrintWriter out = response.getWriter();
	        out.write(jsonArray.toString());
	        response.flushBuffer();
    	}
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
        HttpSession session = request.getSession(true);
        int memberId = (Integer) session.getAttribute("memberId");
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
              
        if(requestBody.length() > 0) {
              
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
                              e.printStackTrace();
                          }
                      }
                  } else {
                      
                  }
              } catch (Exception e) {
                // crash and burn
                throw new IOException("Error parsing JSON request string");
              }
            
        } else if(session.getAttribute("memberId") != null) {
	    	JSONArray jsonArray = new JSONArray();
	        for (User user : dao.getAllUsers()) {
	            JSONObject jsonObject=new JSONObject();
	                try {
	                    jsonObject.put("userid", user.getUserid());
	                    jsonObject.put("firstname", handleEmptyString(user.getFirstName()));
	                    jsonObject.put("lastname", handleEmptyString(user.getLastName()));
	                    jsonObject.put("email", handleEmptyString(user.getEmail()) );
                            jsonObject.put("emailOffice", handleEmptyString(user.getEmailOffice()) );
	                    jsonObject.put("telephone", handleEmptyString(user.getTelephone()));
	                    jsonObject.put("phoneOffice", handleEmptyString(user.getPhoneOffice()));
	                    jsonObject.put("handy", handleEmptyString(user.getHandy()));
	                    jsonObject.put("birthday", handleEmptyString(user.getBirthday()));
	                    jsonObject.put("membership", handleEmptyString(user.getMembership()));
	                    jsonObject.put("voting", user.isVoting());
	                    jsonObject.put("license", user.getLicense());
	                    jsonObject.put("youth", user.getYouth());
	                    jsonObject.put("ahk", user.isAhk());
	                    jsonObject.put("ms", user.isMs());
	                    jsonObject.put("street", handleEmptyString(user.getStreet()));
	                    jsonObject.put("plz", handleEmptyString(user.getPlz()));
	                    jsonObject.put("city", handleEmptyString(user.getCity()));
	                    jsonObject.put("country", handleEmptyString(user.getCountry()));
	                    jsonObject.put("specialStatus", handleEmptyString(user.getSpecialStatus()));
	                    jsonObject.put("medFrom", handleEmptyString(user.getMedFrom()));
	                    jsonObject.put("medTo", handleEmptyString(user.getMedTo()));
                            if( memberId == user.getUserid() )
                                jsonObject.put("owner", true);
	                } catch (JSONException e) {
	                    // TODO Auto-generated catch block
	                    e.printStackTrace();
	                }
                    jsonArray.put(jsonObject);
	        }
                JSONObject usr = new JSONObject();
                try {
                    ArrayList<String> user = dao.getUsr(memberId);
                    usr.put("usr", user.get(0));
                    usr.put("ms", user.get(1));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                jsonArray.put(usr);
	        response.setContentType("application/json");
	        PrintWriter out = response.getWriter();
	        out.write(jsonArray.toString());
                //call flush() after each writing
                out.flush();
                //to release ressources, call close when done
                out.close();
	        response.flushBuffer();
        }
    }
    
    //if an empty string comes from the database, fill it with something meaningful for presentation
    private String handleEmptyString(String in) {
        if (in != null && in.isEmpty())
            in = "keine Angabe";
        return in;
    }

}