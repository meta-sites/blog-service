package com.blog.exception;

import org.springframework.http.HttpStatus;

public class UserException extends Exception {

    private HttpStatus httpStatus;
    public UserException() {
        super();
    }

    public UserException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public UserException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserException(Throwable cause) {
        super(cause);
    }
}
