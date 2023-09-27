package org.kainos.ea.client;

import org.checkerframework.checker.optional.qual.OptionalBottom;

public class FailedToGetUserId extends Throwable {
    @Override
    public String getMessage(){
        return "Failed to get userId from db";
    }
}
