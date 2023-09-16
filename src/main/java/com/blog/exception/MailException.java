package com.blog.exception;
import org.springframework.http.HttpStatus;

public class MailException extends Exception {

    private HttpStatus httpStatus;
    public MailException() {
        super();
    }

    public MailException(String message, HttpStatus httpStatus) {
        super(message);
        this.httpStatus = httpStatus;
    }

    public MailException(String message, Throwable cause) {
        super(message, cause);
    }

    public MailException(Throwable cause) {
        super(cause);
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }
}