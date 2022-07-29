package com.audit.request;

public class UserNameRequest {
    String name;
    public UserNameRequest(){}

    public String getName() {
        return name;
    }

    public UserNameRequest(String name) {
        this.name = name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
