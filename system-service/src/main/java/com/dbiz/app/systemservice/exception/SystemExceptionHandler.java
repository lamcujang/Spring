package com.dbiz.app.systemservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.napas.NapasResponseDto;
import org.common.dbiz.exception.wrapper.NapasErrorException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class SystemExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles global exceptions and returns a ResponseEntity with an ErrorResponse.
     *
     * @param    global exception to handle.
     * @return A ResponseEntity containing the error response.
     */

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGlobalException(RuntimeException runtimeException, WebRequest request) {
        log.error("GlobalExceptionHandler controller, handle global exception\n");
        String error = runtimeException.getMessage();
        log.info("Log 1: " + runtimeException.getMessage());
        runtimeException.printStackTrace();
        return this.handleExceptionInternal(runtimeException, error, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
    }


//    @ExceptionHandler(NapasErrorException.class)
//    public ResponseEntity<Object> handleGlobalException(NapasErrorException ex) {
//        return ResponseEntity.status(HttpStatus.MULTIPLE_CHOICES)
//                .body(NapasResponseDto.builder()
//                        .code("300")
//                        .message(ex.getMessage())
//                        .build());
//    }
}
