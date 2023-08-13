package com.blog.exception;

import org.springframework.http.HttpStatus;

public class TokenException extends Exception {

    private HttpStatus httpStatus;
    public TokenException() {
        super();
    }

    public TokenException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public TokenException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenException(Throwable cause) {
        super(cause);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}