package org.kainos.ea.client;

public class FailedToValidateRegisterRequestException extends Exception {

    public FailedToValidateRegisterRequestException(Exception cause) {
        super(cause);
    }

    @Override
    public String getMessage(){
        return "Failed to validate register request";
    }
}
