package de.lsvn.model;

public class User {

    private int userid;
    private String firstName;
    private String lastName;
    private String email;
    private String telephone;
    private String handy;
    private String birthday;
    private String membership;
    private boolean youth;
    private String emailOffice;
    private String phoneOffice;
    private String plz;
    private String city;
    private String street;
    private String country;
    private boolean voting;
    private boolean ahk;
    private String specialStatus;
    private String medFrom;
    private String medTo;
    private boolean license;
    private String pwd;
    private boolean ms;

    public boolean isMs() {
        return ms;
    }

    public void setMs(boolean ms) {
        this.ms = ms;
    }

    public boolean isVoting() {
        return voting;
    }

    public void setVoting(boolean voting) {
        this.voting = voting;
    }

    public boolean isAhk() {
        return ahk;
    }

    public void setAhk(boolean ahk) {
        this.ahk = ahk;
    }

    public String getEmailOffice() {
        return emailOffice;
    }

    public void setEmailOffice(String emailOffice) {
        this.emailOffice = emailOffice;
    }

    public String getPhoneOffice() {
        return phoneOffice;
    }

    public void setPhoneOffice(String phoneOffice) {
        this.phoneOffice = phoneOffice;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPlz() {
        return plz;
    }

    public void setPlz(String plz) {
        this.plz = plz;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getMembership() {
        return membership;
    }

    public void setMembership(String membership) {
        this.membership = membership;
    }

    public boolean getYouth() {
        return youth;
    }

    public void setYouth(boolean youth) {
        this.youth = youth;
    }
    
    public int getUserid() {
        return userid;
    }
    
    public void setUserid(int userid) {
        this.userid = userid;
    }
    
    public String getFirstName() {
        return firstName;
    }
    
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
    
    public String getLastName() {
        return lastName;
    }
    
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
    
    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getTelephone() {
        return telephone;
    }
    
    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }
    
    public String getHandy() {
        return handy;
    }
    
    public void setHandy(String handy) {
        this.handy = handy;
    }

    public String getSpecialStatus() {
        return specialStatus;
    }

    public void setSpecialStatus(String specialStatus) {
        this.specialStatus = specialStatus;
    }

    public String getMedFrom() {
        return medFrom;
    }

    public void setMedFrom(String medFrom) {
        this.medFrom = medFrom;
    }

    public String getMedTo() {
        return medTo;
    }

    public void setMedTo(String medTo) {
        this.medTo = medTo;
    }

    public boolean getLicense() {
        return license;
    }

    public void setLicense(boolean license) {
        this.license = license;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    
    @Override
    public String toString() {
        return "User [userid=" + userid + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email + ", telephone=" + telephone + "]";
    }    
}