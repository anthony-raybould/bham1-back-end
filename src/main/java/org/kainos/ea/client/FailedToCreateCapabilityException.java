package org.kainos.ea.client;

public class FailedToCreateCapabilityException extends Throwable {

    @Override
    public String getMessage(){
        return  "Failed to create capability";
    }
}
