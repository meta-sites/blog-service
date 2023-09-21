package com.blog.exception;

import org.springframework.http.HttpStatus;

public class UnAuthorizeException extends Exception {

    private HttpStatus httpStatus;
    public int errorCode;

    public UnAuthorizeException() {
        super();
    }

    public UnAuthorizeException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public UnAuthorizeException(String message) {
        super(message);
    }

    public UnAuthorizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public UnAuthorizeException(Throwable cause) {
        super(cause);
    }
}