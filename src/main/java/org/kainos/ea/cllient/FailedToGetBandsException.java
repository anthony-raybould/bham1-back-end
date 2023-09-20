package org.kainos.ea.cllient;

public class FailedToGetBandsException extends Throwable{
    @Override
    public String getMessage(){
        return  "Failed too get bands";
    }
}
