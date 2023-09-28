package org.kainos.ea.cli;

import com.fasterxml.jackson.annotation.JsonCreator;

public class RoleResponse {

    private int roleID;
    private String roleName;

    @JsonCreator
    public RoleResponse(int roleID, String roleName) {
        this.roleID = roleID;
        this.roleName = roleName;
    }

    public RoleResponse(Role role) {
        this.roleID = role.getRoleId();
        this.roleName = role.getRoleName();
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
