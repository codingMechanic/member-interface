/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lsvn.util;

import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author henningrichter
 */
public class HashGen {
    
    private static final long serialVersionUID = 1L;
    private static final Random RANDOM = new SecureRandom();
    public static final int PWD_LENGTH = 8;
    
    static String generateRndmPwd() {
    	String chars = "abcdefghjkmnpqrstuvwxyzABCDEFGHJKMNPQRSTUVWXYZ23456789+@";
    	String pwd = "";
    	for (int i=0; i<PWD_LENGTH; i++)
        {
            int index = (int)(RANDOM.nextDouble()*chars.length());
            pwd += chars.substring(index, index+1);
        }
    	return pwd;
    }

    public static List<String> generateAuthData() {
    	List<String> authData = new ArrayList<String>();
    	String rndmPwd = generateRndmPwd();
    	authData.add(rndmPwd);
        try {
            String hashValue = PasswordHash.createHash(rndmPwd);
            authData.add(hashValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return authData;
    }

    public static void main(String[] args) {
//        Connection connection = DbUtil.getConnection();
//        Map<String, String> map = new HashMap<String, String>(); 
//        try {
//            PreparedStatement preparedStatement = connection.prepareStatement("SELECT pwd FROM authentifizierung");
//            ResultSet rs = preparedStatement.executeQuery();
//            while (rs.next()) {
//                String hashValue = PasswordHash.createHash(rs.getString("pwd"));
//                map.put(rs.getString("pwd"), hashValue);
//            }
//            DbUtil.closeResultSet(rs);
//            DbUtil.closePreparedStatement(preparedStatement);
//            for (Map.Entry<String, String> entry : map.entrySet()) {
//                PreparedStatement preparedStatement2 = connection.prepareStatement("UPDATE authentifizierung SET pwd=? WHERE pwd=?");
//                preparedStatement2.setString(1, entry.getValue());
//                preparedStatement2.setString(2, entry.getKey());
//                preparedStatement2.executeUpdate();
//                DbUtil.closeStatement(preparedStatement2);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            DbUtil.closeConnection(connection);
//        }
    	String rndmPwd = generateRndmPwd();
        try {
            String hashValue = PasswordHash.createHash(rndmPwd);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}