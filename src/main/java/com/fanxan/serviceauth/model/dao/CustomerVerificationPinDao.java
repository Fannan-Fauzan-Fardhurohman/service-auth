package com.fanxan.serviceauth.model.dao;


import com.fanxan.serviceauth.model.entity.CustomerVerificationPin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface CustomerVerificationPinDao extends JpaRepository<CustomerVerificationPin, Long>, JpaSpecificationExecutor<CustomerVerificationPin> {

    Optional<CustomerVerificationPin> findTopByDeviceIdAndAndEventAndCustomerIdIsNull(String deviceId, String event);
    // Remove the extra "And"
    Optional<CustomerVerificationPin> findTopByDeviceIdAndEventAndTokenHashAndStatus(
            String deviceId,
            String event,
            String tokenHash,
            Integer status
    );

    Optional<CustomerVerificationPin> findByDeviceIdAndEventAndTokenHashAndStatus(
            String deviceId,
            String event,
            String tokenHash,
            Integer status
    );

    Optional<CustomerVerificationPin> findByEventAndTokenHashAndStatus(
            String event,
            String tokenHash,
            Integer status
    );

    Optional<CustomerVerificationPin> findByEventAndTokenAndStatus(
            String event,
            String token,
            Integer status
    );


    Optional<CustomerVerificationPin> findTopByDeviceIdAndEventAndTokenHash(String deviceId, String event, String tokenHash);

    Optional<CustomerVerificationPin> findTopByDeviceIdAndStatus(String deviceId, int status);

}
