package com.alibou.security.exception;

public abstract class MovieException extends RuntimeException {
    private String code;

    public MovieException(String message, Throwable cause) {
        super(message, cause);
    }

    public MovieException(String code, String message) {
        super(message);
        this.code = code;
    }

    public MovieException(String message) {
        super(message);
        this.code = "";
    }

    public MovieException() {
        super("");
        this.code = "";
    }

    public String getCode() {
        return this.code;
    }
}
