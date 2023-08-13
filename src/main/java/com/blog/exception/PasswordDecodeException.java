package com.blog.exception;

public class PasswordDecodeException extends Exception {

    public int errorCode;
    public PasswordDecodeException() {
        super();
    }

    public PasswordDecodeException(String message) {
        super(message);
    }

    public PasswordDecodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordDecodeException(Throwable cause) {
        super(cause);
    }
}
