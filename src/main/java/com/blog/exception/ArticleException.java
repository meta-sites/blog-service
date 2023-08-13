package com.blog.exception;

import org.springframework.http.HttpStatus;

public class ArticleException extends Exception {

    private HttpStatus httpStatus;
    public ArticleException() {
        super();
    }

    public ArticleException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public ArticleException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArticleException(Throwable cause) {
        super(cause);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}