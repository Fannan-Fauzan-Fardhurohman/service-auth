package com.fanxan.serviceauth.model.timezone;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class TimezoneModel {

    private String code;

    private String name;

    private String[] utc;

    private String utcValue;

    private float offset;

}
