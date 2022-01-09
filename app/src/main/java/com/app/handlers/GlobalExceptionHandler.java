package com.app.handlers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

@ControllerAdvice

// When an exception is received on Controller Level,
// a well documented Exception Dictionary is represented instead of entire error

public class GlobalExceptionHandler {
    /*
        @ExceptionHandler({ Exception.class })

        public ResponseEntity<?> handleAuthenticationException(Exception exception, WebRequest request) {
            ExceptionDetails details = new ExceptionDetails(HttpStatus.UNAUTHORIZED.value(),
                    exception.getMessage().split("Exception")[0], request.getDescription(false).split("uri=")[1]);
            System.out.println("AuthenticationException : {\n" + details + "\n}");

            return new ResponseEntity<>(details, HttpStatus.UNAUTHORIZED);
        }
    */
    // Any other exception is sent here
    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception exception, WebRequest request) {

        return new ResponseEntity<>("Exception", HttpStatus.BAD_REQUEST);
    }

    // For Specific Exceptions
    // @ExceptionHandler(BlablaException.class)
    // public ResponseEntity<?> handlerBlablaException(Exception e , WebRequest
    // request){
    // }

}