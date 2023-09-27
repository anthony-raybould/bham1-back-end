package org.kainos.ea.client;

public class UpdateJobRoleIDDoesNotExistException extends Throwable {
    @Override
    public String getMessage(){
        return "Provided job role is does not exist";
    }
}
