package com.fanxan.serviceauth.model.timezone;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class TimezoneJsonModel {

    private String value;

    private String abbr;

    private float offset;

    private boolean isdst;

    private String text;

    private List<String> utc;

    private String utcValue;

}
