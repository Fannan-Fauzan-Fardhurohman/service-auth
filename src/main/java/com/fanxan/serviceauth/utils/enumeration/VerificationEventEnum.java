package com.fanxan.serviceauth.utils.enumeration;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum VerificationEventEnum {
    MERCHANT_PAYOUT("MERCHANT_PAYOUT"),
    CUSTOMER_REGISTER("CUSTOMER_REGISTER"),
    CUSTOMER_LOGIN("CUSTOMER_LOGIN"),
    CUSTOMER_LOGIN_REGISTER("CUSTOMER_LOGIN_REGISTER"),
    CUSTOMER_CHANGE_PHONE_NUMBER("CUSTOMER_CHANGE_PHONE_NUMBER"),
    CUSTOMER_VERIFICATION_ON_CHECKOUT("CUSTOMER_VERIFICATION_ON_CHECKOUT"),

    MERCHANT_REGISTER("MERCHANT_REGISTER"),

    MERCHANT_LOGIN("MERCHANT_LOGIN");

    private final String code;
}