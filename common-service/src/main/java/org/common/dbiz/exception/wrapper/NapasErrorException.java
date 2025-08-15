package org.common.dbiz.exception.wrapper;

public class NapasErrorException extends RuntimeException {
    private static final long serialVersionUID = 1L;


    public NapasErrorException() {
        super();
    }

    public NapasErrorException(String message) {
        super(message);
    }

    public NapasErrorException(String message, Throwable cause) {
        super(message, cause);
    }

}
