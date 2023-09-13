package org.kainos.ea.cli;

public enum Role {

    ADMIN(1, "Admin"),
    EMPLOYEE(2, "Employee");

    private final int roleId;
    private final String roleName;

    Role(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    @Override
    public String toString() {
        return roleName;
    }

    public static Role fromRoleId(int roleId) {
        switch (roleId) {
            case 1:
                return ADMIN;
            case 2:
                return EMPLOYEE;
        }
        return null;
    }

}
