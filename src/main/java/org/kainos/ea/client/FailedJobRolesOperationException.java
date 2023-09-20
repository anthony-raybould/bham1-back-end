package org.kainos.ea.client;

public class FailedJobRolesOperationException extends  Throwable{

    public FailedJobRolesOperationException(String message, Exception causedBy) {
        super(message);
        super.initCause(causedBy);
    }

}
