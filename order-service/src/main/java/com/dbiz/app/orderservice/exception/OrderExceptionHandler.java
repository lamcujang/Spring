package com.dbiz.app.orderservice.exception;

import lombok.extern.slf4j.Slf4j;
import org.common.dbiz.dto.paymentDto.napas.NapasResponseDto;
import org.common.dbiz.exception.wrapper.NapasOrderNotFoundException;
import org.common.dbiz.exception.wrapper.NapasSignatureInvalidException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class OrderExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * Handles global exceptions and returns a ResponseEntity with an ErrorResponse.
     *
     * @param    global exception to handle.
     * @return A ResponseEntity containing the error response.
     */

//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<Object> handleGlobalException(RuntimeException runtimeException, WebRequest request) {
//        log.error("GlobalExceptionHandler controller, handle global exception\n");
//        String error = runtimeException.getMessage();
//        log.info("Log 1: " + runtimeException.getMessage());
//        runtimeException.printStackTrace();
//        return this.handleExceptionInternal(runtimeException, error, null, HttpStatus.INTERNAL_SERVER_ERROR, request);
//    }


    @ExceptionHandler(NapasSignatureInvalidException.class)
    public ResponseEntity<Object> handleNapasErrorException(NapasSignatureInvalidException napasErrorException) {
        log.error("NapasErrorException controller, handle NapasErrorException\n");
        String error = napasErrorException.getMessage();
        log.info("Log 2: " + napasErrorException.getMessage());
        return ResponseEntity.status(HttpStatus.MULTIPLE_CHOICES)
                .body(NapasResponseDto.builder()
                        .code(String.valueOf(HttpStatus.MULTIPLE_CHOICES.value()))
                        .message(napasErrorException.getMessage())
                        .build());
//        return NapasResponseDto.builder()
//                .code("vai")
//                .message(error)
//                .build();
    }


    @ExceptionHandler(NapasOrderNotFoundException.class)
    public ResponseEntity<Object> handleNapasErrorException(NapasOrderNotFoundException napasErrorException) {
        log.error("NapasErrorException controller, handle NapasErrorException\n");
        log.info("Log 2: " + napasErrorException.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(NapasResponseDto.builder()
                        .code(String.valueOf(HttpStatus.NOT_FOUND.value()))
                        .message(napasErrorException.getMessage())
                        .build());
    }

}
