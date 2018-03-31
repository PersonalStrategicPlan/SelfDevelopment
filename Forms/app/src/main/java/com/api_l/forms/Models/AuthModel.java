package com.api_l.forms.Models;

public class AuthModel {
    private String UserEmail;
    private  String UserPass;

    public AuthModel(String s, String s1) {
        this.setUserEmail(s);
        this.setUserPass(s1);
    }


    public String getUserEmail() {
        return UserEmail;
    }

    public void setUserEmail(String userEmail) {
        UserEmail = userEmail;
    }

    public String getUserPass() {
        return UserPass;
    }

    public void setUserPass(String userPass) {
        UserPass = userPass;
    }
}
