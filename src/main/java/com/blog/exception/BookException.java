package com.blog.exception;

import org.springframework.http.HttpStatus;

public class BookException extends Exception {

    private HttpStatus httpStatus;
    public BookException() {
        super();
    }

    public BookException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public BookException(String message, Throwable cause) {
        super(message, cause);
    }

    public BookException(Throwable cause) {
        super(cause);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}