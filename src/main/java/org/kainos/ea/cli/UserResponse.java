package org.kainos.ea.cli;

import com.fasterxml.jackson.annotation.JsonCreator;

public class UserResponse {

    private int userID;
    private String email;
    private RoleResponse role;

    @JsonCreator
    public UserResponse(int userID, String email, Role role) {
        this.userID = userID;
        this.email = email;
        this.role = new RoleResponse(role);
    }

    public UserResponse(User user) {
        this.userID = user.getId();
        this.email = user.getEmail();
        this.role = new RoleResponse(user.getRole());
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

    public RoleResponse getRole() {
        return role;
    }

    public void setRole(RoleResponse role) {
        this.role = role;
    }

}
