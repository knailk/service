package com.audit.request;

public class UserNameEmailRequest {
    String name = "Not Found!";
    String mail = "Not Found!";

    public UserNameEmailRequest(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public UserNameEmailRequest(String name, String mail) {
        this.name = name;
        this.mail = mail;
    }
}
