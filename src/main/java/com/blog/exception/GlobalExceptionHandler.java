package com.blog.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception ex) throws Exception {
//        throw ex;
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageHandler(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(PasswordDecodeException.class)
    public ResponseEntity<String> handlePasswordDecodeException(PasswordDecodeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageHandler(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<String> handleDataBaseException(DataBaseException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(messageHandler(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
    }

    @ExceptionHandler(UserException.class)
    public ResponseEntity<String> handleUserException(UserException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageHandler(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<String> handleAuthenticationException(AuthenticationException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(messageHandler(ex.getMessage(), HttpStatus.BAD_REQUEST));
    }

    @ExceptionHandler(UnAuthorizeException.class)
    public ResponseEntity<String> handleUnAuthorizeException(UnAuthorizeException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(messageHandler(ex.getMessage(), HttpStatus.UNAUTHORIZED));
    }

    @ExceptionHandler(ArticleException.class)
    public ResponseEntity<String> handleArticleException(ArticleException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        return ResponseEntity.status(httpStatus).body(messageHandler(ex.getMessage(), httpStatus));
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<String> handleTokenException(TokenException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        return ResponseEntity.status(httpStatus).body(messageHandler(ex.getMessage(), httpStatus));
    }

    @ExceptionHandler(BookException.class)
    public ResponseEntity<String> handleBookException(BookException ex) {
        HttpStatus httpStatus = ex.getHttpStatus();
        return ResponseEntity.status(httpStatus).body(messageHandler(ex.getMessage(), httpStatus));
    }


    private String messageHandler(String msg, HttpStatus code) {
        String errMsg = "\"Message\": \"".concat(msg).concat("\"");
        String errCode = "\"Error\": \"".concat(String.valueOf(code.value())).concat("\"");

        return "{".concat("\n").concat("\t")
                    .concat(errMsg).concat(",").concat("\n").concat("\t")
                    .concat(errCode).concat("\n")
                .concat("}");
    }
}
