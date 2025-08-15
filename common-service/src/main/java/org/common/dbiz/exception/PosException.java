package org.common.dbiz.exception;

public class PosException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public PosException() {
        super();
    }

    public PosException(String message, Throwable cause) {
        super(message, cause);
    }

    public PosException(String message) {
        super(message);
    }

    public PosException(Throwable cause) {
        super(cause);
    }
}
