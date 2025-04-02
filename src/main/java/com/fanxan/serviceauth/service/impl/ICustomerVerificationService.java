package com.fanxan.serviceauth.service.impl;

import com.fanxan.serviceauth.model.entity.CustomerVerificationPin;

public interface ICustomerVerificationService {

    <T> CustomerVerificationPin createOrUpdate(String event, String deviceId, String type, T param) throws Exception;

    CustomerVerificationPin resendPin(String deviceId, String eventType, String token) throws Exception;

    CustomerVerificationPin findAVerification(String event, String deviceId, String token, String pin) throws Exception;

    CustomerVerificationPin findAVerification2(String event, String token, String pin) throws Exception;
}
