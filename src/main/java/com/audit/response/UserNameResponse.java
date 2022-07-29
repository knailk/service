package com.audit.response;

public class UserNameResponse {
    protected int id;
    protected String firstName;
    protected String lastName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getName() {
        return lastName + " " + firstName;
    }

}
