package org.kainos.ea.client;

public class FailedToDeleteCapabilityException extends Throwable{
    @Override
    public String getMessage(){
        return "Failed to delete capability";
    }
}
