package com.vox.post.exception;

public class ApiUnauthorizedException extends RuntimeException {
    public ApiUnauthorizedException(String s) {
        super(s);
    }

    public ApiUnauthorizedException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
