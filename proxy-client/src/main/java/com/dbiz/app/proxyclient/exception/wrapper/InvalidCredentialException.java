package com.dbiz.app.proxyclient.exception.wrapper;

public class InvalidCredentialException  extends RuntimeException{

    public InvalidCredentialException() {
        super();
    }

    public InvalidCredentialException(String message) {
        super(message);
    }
}
