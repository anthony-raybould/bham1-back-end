package org.kainos.ea.client;

public class FailedToValidateTokenException extends Exception {
    @Override
    public String getMessage(){
        return "Failed to validate token";
    }
}
