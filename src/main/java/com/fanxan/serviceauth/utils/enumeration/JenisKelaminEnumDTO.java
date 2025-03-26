package com.fanxan.serviceauth.utils.enumeration;

import com.fasterxml.jackson.annotation.JsonCreator;


public enum JenisKelaminEnumDTO {
    L, P;

    @JsonCreator
    public static JenisKelaminEnumDTO create(String value) {
        return JenisKelaminEnumDTO.valueOf(value.toUpperCase());
    }
}
