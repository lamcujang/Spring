package org.common.dbiz.exception.wrapper;

public class NapasOrderNotFoundException extends RuntimeException{


    private static final long serialVersionUID = 1L;

    public NapasOrderNotFoundException() {
        super();
    }

    public NapasOrderNotFoundException(String message) {
        super(message);
    }

    public NapasOrderNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
