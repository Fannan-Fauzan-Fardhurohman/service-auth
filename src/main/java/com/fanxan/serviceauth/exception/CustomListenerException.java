package com.fanxan.serviceauth.exception;

import com.fanxan.serviceauth.model.BaseErrorResponse;

public class CustomListenerException extends Exception {
    private final int code;
    private final String errorCode;

    public CustomListenerException(int code, String errorCode, String message) {
        super(message);
        this.code = code;
        this.errorCode = errorCode;
    }

    public CustomListenerException(BaseErrorResponse errorResponse) {
        super(errorResponse.getError());
        this.code = errorResponse.getStatus();
        this.errorCode = errorResponse.getCode();
    }

    public int getCode() {
        return this.code;
    }

    public String getErrorCode() {
        return this.errorCode;
    }
}
