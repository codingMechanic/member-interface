//bean file to map the parameters from "loginbean.jsp"
package de.lsvn.util;

public class LoginBean {

    String usr = "";
    String pwd = "";

    public String getUsr() {
        return usr;
    }

    public void setUsr(String userName) {
        this.usr = userName;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String password) {
        this.pwd = password;
    }
}