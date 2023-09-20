package org.kainos.ea.cli;

import com.fasterxml.jackson.annotation.JsonCreator;

public class UserResponse {

    private int userID;
    private String email;
    private String role;

    @JsonCreator
    public UserResponse(int userID, String email, String role) {
        this.userID = userID;
        this.email = email;
        this.role = role;
    }

    public UserResponse(User user) {
        this.userID = user.getId();
        this.email = user.getEmail();
        this.role = user.getRole().toString();
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
