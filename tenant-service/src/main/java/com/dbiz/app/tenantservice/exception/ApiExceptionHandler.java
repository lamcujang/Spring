//package com.dbiz.app.tenantservice.exception;
//
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//
//import com.dbiz.app.tenantservice.exception.payload.ExceptionMsg;
//import com.dbiz.app.tenantservice.exception.wrapper.UserObjectNotFoundException;
//import com.dbiz.app.tenantservice.exception.wrapper.VerificationTokenNotFoundException;
//import org.common.dbiz.payload.GlobalReponse;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.http.converter.HttpMessageNotReadableException;
//import org.springframework.validation.BindException;
//import org.springframework.web.bind.MethodArgumentNotValidException;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//
//import com.selimhorri.app.exception.wrapper.AddressNotFoundException;
//import com.selimhorri.app.exception.wrapper.CredentialNotFoundException;
//
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//
//@ControllerAdvice
//@Slf4j
//@RequiredArgsConstructor
//public class ApiExceptionHandler {
//
//    @ExceptionHandler(value = {
//            MethodArgumentNotValidException.class,
//            HttpMessageNotReadableException.class
//    })
//    public <T extends BindException> ResponseEntity<ExceptionMsg> handleValidationException(final T e) {
//
//        log.info("**ApiExceptionHandler controller, handle validation exception*\n");
//        final var badRequest = HttpStatus.BAD_REQUEST;
//        e.printStackTrace();
//        return new ResponseEntity<>(
//                ExceptionMsg.builder()
//                        .msg("*" + e.getBindingResult().getFieldError().getDefaultMessage() + "!**")
//                        .status(badRequest.value())
//                        .timestamp(ZonedDateTime
//                                .now(ZoneId.systemDefault()))
//                        .build(), badRequest);
//    }
//
//    @ExceptionHandler(value = {
//            UserObjectNotFoundException.class,
//            CredentialNotFoundException.class,
//            VerificationTokenNotFoundException.class,
//            AddressNotFoundException.class,
//            Exception.class
//    })
//    public <T extends RuntimeException> ResponseEntity<GlobalReponse> handleApiRequestException(final T e) {
//        e.printStackTrace();
//        log.info("**ApiExceptionHandler controller, handle API request*\n");
//        final var badRequest = HttpStatus.BAD_REQUEST;
//
//        return new ResponseEntity<>(
//                GlobalReponse.builder()
//                        .errors("#### " + e.getMessage() + "! ####")
//                        .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
////					.timestamp(ZonedDateTime
////							.now(ZoneId.systemDefault()))
//                        .build(), HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//
//
//
//}