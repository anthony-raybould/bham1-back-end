package org.kainos.ea.client;

public class FailedToLoginException extends Exception {
    @Override
    public String getMessage(){
        return "Failed to log user in";
    }
}
