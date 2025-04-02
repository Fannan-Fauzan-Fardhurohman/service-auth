package com.fanxan.serviceauth.utils;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum DataStatus {

    DELETED(0),

    ACTIVE(1),
    INACTIVE(2),

    SUCCESS(11),
    FAILURE(12),
    CANCEL(13),

    ACCEPTED(21),
    REJECTED(22),
    EXPIRED(23),
    PENDING(24),
    UNPAID(25),


    INVITED(8),
    SUSPENDED(9),

    NEED_ACTIVATION(30),


    // STRIPE_ACCOUNT_STATUS
    RESTRICTED(31),
    RESTRICTED_SOON(32),
    //PENDING(33),
    ENABLED(34),
    COMPLETE(35),
    //REJECTED(36),
    DISABLED(37),
    IN_COMPLETE(38),
    IN_ACTIVE(39);

    private final int value;

    public static String getStatus(int status) {
        for (DataStatus dataStatus : DataStatus.values()) {
            if (dataStatus.getValue() == status) {
                return dataStatus.name();
            }
        }
        throw new IllegalArgumentException("Invalid status: " + status);
    }

}
