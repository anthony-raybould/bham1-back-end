package org.kainos.ea.cli;

public enum SQLErrorCode {

    DUPLICATE_ENTRY(1062);

    private final int errorCode;

    SQLErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

}
