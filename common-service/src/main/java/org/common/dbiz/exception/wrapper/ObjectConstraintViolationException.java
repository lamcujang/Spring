package org.common.dbiz.exception.wrapper;

import javax.validation.ConstraintViolationException;

public class ObjectConstraintViolationException extends ConstraintViolationException {

    private static final long serialVersionUID = 1L;

    public ObjectConstraintViolationException(String message) {

        super(message, null);
    }
}
