package org.kainos.ea.model;

import org.junit.jupiter.api.Test;
import org.kainos.ea.cli.Role;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RoleTest {

    @Test
    void equals_shouldReturnTrue_whenSameObject() {
        Role role = new Role(1, "role");
        assertTrue(role.equals(role));
    }

    @Test
    void equals_shouldReturnTrue_whenSameRoleIdAndRoleName() {
        Role role1 = new Role(1, "role");
        Role role2 = new Role(1, "role");
        assertTrue(role1.equals(role2));
    }

    @Test
    void equals_shouldReturnFalse_whenDifferentRoleId() {
        Role role1 = new Role(1, "role");
        Role role2 = new Role(2, "role");
        assertFalse(role1.equals(role2));
    }

    @Test
    void equals_shouldReturnFalse_whenDifferentRoleName() {
        Role role1 = new Role(1, "role");
        Role role2 = new Role(1, "role2");
        assertFalse(role1.equals(role2));
    }

    @Test
    void equals_shouldReturnFalse_whenDifferentObject() {
        Role role1 = new Role(1, "role");
        String role2 = "role";
        assertFalse(role1.equals(role2));
    }

    @Test
    void hashCode_shouldReturnSameHashCode_whenSameObject() {
        Role role = new Role(1, "role");
        assertTrue(role.hashCode() == role.hashCode());
    }

    @Test
    void hashCode_shouldReturnSameHashCode_whenSameRoleIdAndRoleName() {
        Role role1 = new Role(1, "role");
        Role role2 = new Role(1, "role");
        assertTrue(role1.hashCode() == role2.hashCode());
    }

    @Test
    void hashCode_shouldReturnDifferentHashCode_whenDifferentRoleId() {
        Role role1 = new Role(1, "role");
        Role role2 = new Role(2, "role");
        assertFalse(role1.hashCode() == role2.hashCode());
    }

    @Test
    void hashCode_shouldReturnDifferentHashCode_whenDifferentRoleName() {
        Role role1 = new Role(1, "role");
        Role role2 = new Role(1, "role2");
        assertFalse(role1.hashCode() == role2.hashCode());
    }

    @Test
    void hashCode_shouldReturnDifferentHashCode_whenDifferentObject() {
        Role role1 = new Role(1, "role");
        String role2 = "role";
        assertFalse(role1.hashCode() == role2.hashCode());
    }


}
