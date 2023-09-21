package org.kainos.ea.cli;

public class Role {

    private final int roleId;

    private final String roleName;

    public Role(int roleId, String roleName) {
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public int getRoleId() {
        return roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    @Override
    public String toString() {
        return roleName;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Role) {
            Role role = (Role) obj;
            return role.getRoleId() == roleId && role.getRoleName().equals(roleName);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return roleId;
    }

}
