/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.lsvn.util;

import edu.vt.middleware.password.*;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author henningrichter
 */
public class PwdValidator {
    
    public JSONObject validatePwd(String pwd) {
        
//        Properties props = new Properties();
//        try{
////            InputStream msgProp = new FileInputStream("/messages.properties");
//            InputStream msgProp = PwdValidator.class.getClassLoader().getResourceAsStream("/messages.properties");
//            props.load(msgProp);
//        } catch (Exception e) {
//                e.printStackTrace();
//        }
//        MessageResolver resolver = new MessageResolver(props);
        
// password must be between 8 and 16 chars long
        LengthRule lengthRule = new LengthRule(6, 16);

// control allowed characters
        CharacterCharacteristicsRule charRule = new CharacterCharacteristicsRule();
// require at least 1 digit in passwords
        charRule.getRules().add(new DigitCharacterRule(1));
// require at least 1 non-alphanumeric char
        charRule.getRules().add(new NonAlphanumericCharacterRule(1));
// require at least 1 upper case char
        charRule.getRules().add(new UppercaseCharacterRule(1));
// require at least 1 lower case char
        charRule.getRules().add(new LowercaseCharacterRule(1));
// require at least 3 of the previous rules be met
        charRule.setNumberOfCharacteristics(4);

// group all rules together in a List
        List<Rule> ruleList = new ArrayList<Rule>();
        ruleList.add(lengthRule);
        ruleList.add(charRule);

        PasswordValidator validator = new PasswordValidator(ruleList);
//        PasswordValidator validator = new PasswordValidator(resolver, ruleList);
        PasswordData passwordData = new PasswordData(new Password(pwd));

        JSONObject msg = new JSONObject();
        try {
            msg.put("tooShort", false);
            msg.put("noCaps", false);
            msg.put("noSpecials", false);
            msg.put("noDigits", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        
        RuleResult result = validator.validate(passwordData);
        if (result.isValid()) {
            return msg;
        } else {
            ListIterator<RuleResultDetail> resultDetailIterator = result.getDetails().listIterator();
            while (resultDetailIterator.hasNext()) {
                RuleResultDetail ruleResultDetail = resultDetailIterator.next();
                String errorCodeString = ruleResultDetail.getErrorCode();
                if (errorCodeString.equals("TOO_SHORT")) {
                    try {
                        msg.put("tooShort", true);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else if (errorCodeString.equals("INSUFFICIENT_CHARACTERS")) {
                    if (ruleResultDetail.getParameters().containsValue("digit")) {
                        try {
                            msg.put("noDigits", true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (ruleResultDetail.getParameters().containsValue("non-alphanumeric")) {
                        try {
                            msg.put("noSpecials", true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else if (ruleResultDetail.getParameters().containsValue("uppercase")) {
                        try {
                            msg.put("noCaps", true);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            return msg;
        }
    }
    
}