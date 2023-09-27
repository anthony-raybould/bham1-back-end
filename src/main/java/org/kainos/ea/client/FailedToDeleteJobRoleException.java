package org.kainos.ea.client;

public class FailedToDeleteJobRoleException extends Exception {
    @Override
    public String getMessage() {
        return "Failed to delete job role";
    }
}
