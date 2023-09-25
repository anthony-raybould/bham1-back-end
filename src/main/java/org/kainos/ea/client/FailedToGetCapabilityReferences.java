package org.kainos.ea.client;

public class FailedToGetCapabilityReferences extends Throwable {
    @Override
    public String getMessage(){
        return "Failed to get capability reference";
    }
}
