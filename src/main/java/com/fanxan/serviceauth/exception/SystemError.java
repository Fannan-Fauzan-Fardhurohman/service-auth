package com.fanxan.serviceauth.exception;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SystemError {
    VERIFICATION_EXPIRED("#1000424", HttpStatus.BAD_REQUEST.value()),
    VERIFICATION_PIN_NOT_VALID("#1000424", HttpStatus.BAD_REQUEST.value());

    private final String code;
    private final Integer status;
}