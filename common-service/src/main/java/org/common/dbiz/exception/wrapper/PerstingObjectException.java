package org.common.dbiz.exception.wrapper;

public class PerstingObjectException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public PerstingObjectException() {
        super();
    }

    public PerstingObjectException(String message, Throwable cause) {
        super(message, cause);
    }

    public PerstingObjectException(String message) {
        super(message);
    }

    public PerstingObjectException(Throwable cause) {
        super(cause);
    }
}
