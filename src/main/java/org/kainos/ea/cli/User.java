package org.kainos.ea.cli;

import java.security.Principal;

public class User implements Principal {

    private final int id;
    private final String email;
    private final Role role;

    public User(int id, String email, Role role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String getName() {
        return getEmail();
    }
}
