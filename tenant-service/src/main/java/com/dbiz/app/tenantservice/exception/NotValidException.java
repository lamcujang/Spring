package com.dbiz.app.tenantservice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@ResponseStatus(value = BAD_REQUEST)
public class NotValidException extends RuntimeException {

    public NotValidException(String message) {
        super(message);
    }
}
