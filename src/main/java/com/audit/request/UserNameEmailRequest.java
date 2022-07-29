package com.audit.request;

public class UserNameEmailRequest {
    String name;
    String email;

    public UserNameEmailRequest(){}

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public UserNameEmailRequest(String name, String email) {
        this.name = name;
        this.email = email;
    }
}
