package org.kainos.ea.client;

public class FailedToUpdateJobRoleException extends Throwable {
    @Override
    public String getMessage() {
        return "Failed to update job role";
    }
}
