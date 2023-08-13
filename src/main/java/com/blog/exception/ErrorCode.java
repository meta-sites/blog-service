package com.blog.exception;

public enum ErrorCode {
    SUCCESS(200, "Success"),
    PERMISSION_DENIED(403, "Permission denied"),
    NOT_FOUND(404, "Not found"),
    INVALID_INPUT(400, "Invalid input"),
    DATABASE_ERROR(701, "Database error");

    private final int code;
    private final String message;

    private ErrorCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
