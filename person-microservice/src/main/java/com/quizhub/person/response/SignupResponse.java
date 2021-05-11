package com.quizhub.person.response;

import java.util.ArrayList;
import java.util.UUID;

public class SignupResponse {

    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private ArrayList<String> roles;

    public SignupResponse() {
    }

    public SignupResponse(String firstName, String lastName, String email, String username, ArrayList<String> roles) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.roles = roles;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArrayList<String> getRoles() {
        return roles;
    }

    public void setRoles(ArrayList<String> roles) {
        this.roles = roles;
    }
}