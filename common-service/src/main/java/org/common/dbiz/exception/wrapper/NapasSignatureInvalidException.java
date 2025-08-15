package org.common.dbiz.exception.wrapper;

public class NapasSignatureInvalidException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public NapasSignatureInvalidException() {
        super();
    }

    public NapasSignatureInvalidException(String message) {
        super(message);
    }

    public NapasSignatureInvalidException(String message, Throwable cause) {
        super(message, cause);
    }
}
