package org.kainos.ea.client;

public class JobRoleDoesNotExistException extends Exception {
    private final int id;

    public JobRoleDoesNotExistException(int id) {
        this.id = id;
    }

    @Override
    public String getMessage() {
        return "Job role with ID " + id + " does not exist";
    }
}
