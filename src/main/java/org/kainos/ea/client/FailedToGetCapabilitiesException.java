package org.kainos.ea.client;

public class FailedToGetCapabilitiesException extends Throwable {
    @Override
    public String  getMessage(){
        return "Failed to get capabilities";
    }
}
