package com.fanxan.serviceauth.model.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class CustomerLoginRegisterPayload {

    @NotNull
    private String phoneNumber;

    private String fullName;

    private String deviceId;

    private String typeOTP;

    @NotNull
    private String phoneCountryCode;

    @NotNull
    private String email;
}
