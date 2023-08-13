package com.blog.exception;

import org.springframework.http.HttpStatus;

public class FileException extends Exception {
    private HttpStatus httpStatus;
    public FileException() {
        super();
    }

    public FileException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public FileException(String message, Throwable cause) {
        super(message, cause);
    }

    public FileException(Throwable cause) {
        super(cause);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

}

