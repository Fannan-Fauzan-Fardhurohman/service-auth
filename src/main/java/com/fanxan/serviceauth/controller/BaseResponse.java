package com.fanxan.serviceauth.controller;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;

@Value
@RequiredArgsConstructor
class BaseResponse<T> {

    String message;
    String status;
    int httpCode;
    T data;

    BaseResponse() {
        message = "";
        status = "";
        httpCode = 0;
        data = null;
    }

    BaseResponse(String message, HttpStatus httpStatus, T data) {
        this.message = message;
        status = httpStatus.getReasonPhrase();
        httpCode = httpStatus.value();
        this.data = data;
    }

    static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(
            HttpConstant.SUCCESS,
            HttpStatus.OK,
            data
        );
    }
}
