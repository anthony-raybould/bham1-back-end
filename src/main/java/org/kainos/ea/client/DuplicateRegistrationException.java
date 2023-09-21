package org.kainos.ea.client;

public class DuplicateRegistrationException extends Exception {
    @Override
    public String getMessage(){
        return "User with this email already exists";
    }
}
