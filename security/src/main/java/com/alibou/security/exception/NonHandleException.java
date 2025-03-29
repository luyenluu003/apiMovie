package com.alibou.security.exception;

public class NonHandleException extends MovieException{
    public NonHandleException(String message) {
        super(message);
    }

    public NonHandleException(){
        super();
    }
}
