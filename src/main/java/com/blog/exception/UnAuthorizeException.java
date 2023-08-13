package com.blog.exception;

public class UnAuthorizeException extends Exception {

    public int errorCode;
    public UnAuthorizeException() {
        super();
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