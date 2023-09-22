package org.kainos.ea.client;

public class DuplicateRegistrationException extends ValidationFailedException {

    public DuplicateRegistrationException() {
        super("User with this email already exists");
    }

}
