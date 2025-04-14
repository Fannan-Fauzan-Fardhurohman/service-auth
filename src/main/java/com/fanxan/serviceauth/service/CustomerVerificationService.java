package com.fanxan.serviceauth.service;

import com.fanxan.serviceauth.config.GsonConfig;
import com.fanxan.serviceauth.exception.SystemError;
import com.fanxan.serviceauth.model.dao.CustomerVerificationPinDao;
import com.fanxan.serviceauth.model.entity.CustomerVerificationPin;
import com.fanxan.serviceauth.service.impl.ICustomerVerificationService;
import com.fanxan.serviceauth.utils.enumeration.DataStatus;
import com.fanxan.serviceauth.utils.DateUtils;
import com.fanxan.serviceauth.utils.ExtraUtils;
import com.fanxan.serviceauth.utils.OTPUtils;
import com.nimbusds.jose.shaded.gson.Gson;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class CustomerVerificationService implements ICustomerVerificationService {
    private final CustomerVerificationPinDao customerVerificationPinDao;


    @Override
    public <T> CustomerVerificationPin createOrUpdate(String event, String deviceId, String type, T param) throws Exception {
        Optional<CustomerVerificationPin> customerVerificationPinOptional = customerVerificationPinDao.findTopByDeviceIdAndAndEventAndCustomerIdIsNull(
                deviceId,
                event
        );
        CustomerVerificationPin customerVerificationPin;
        if (customerVerificationPinOptional.isPresent()) {
            customerVerificationPin = customerVerificationPinOptional.get();
            if (customerVerificationPin.getStatus() == DataStatus.INACTIVE.getValue()) {
                customerVerificationPin.setStatus(DataStatus.ACTIVE.getValue());
                customerVerificationPinDao.save(customerVerificationPin);
            }

        } else {
            customerVerificationPin = new CustomerVerificationPin();
            customerVerificationPin.setDeviceId(deviceId);
            customerVerificationPin.setEvent(event);
        }

        customerVerificationPin.setLastType(type);
        try {
            Gson gson = GsonConfig.createGson();
            customerVerificationPin.setEventData(gson.toJson(param));
        } catch (Exception ex) {
            log.info("Error when set event data " + ex);
            log.info("param error " + param);
        }
        Timestamp generatedTimestamp = DateUtils.toTimestamp(DateUtils.addMinuteToDate(new Date(), 1));
        customerVerificationPin.setExpiredAt(generatedTimestamp);

        String token = generatedNewToken(deviceId);
        customerVerificationPin.setToken(token);
        customerVerificationPin.setTokenHash(ExtraUtils.generateMD5(token));

        String pin = OTPUtils.generateTOTP(deviceId, generatedTimestamp);
        customerVerificationPin.setPin(pin);
        customerVerificationPin.setPinHash(ExtraUtils.generateMD5(pin));

        customerVerificationPin.setStatus(DataStatus.ACTIVE.getValue());

        customerVerificationPinDao.save(customerVerificationPin);

        return customerVerificationPin;
    }

    @Override
    public CustomerVerificationPin resendPin(String deviceId, String eventType, String token) throws Exception {
        return null;
    }

    @Override
    public CustomerVerificationPin findAVerification(String event, String deviceId, String token, String pin) throws Exception {
        return null;
    }

    @Override
    public CustomerVerificationPin findAVerification2(String event, String token, String pin) throws Exception {
// Debug logging
        log.info("Debug Info:");
        log.info("Event: " + event);
        String tokenHash = ExtraUtils.generateMD5(token);
        log.info("Token Hash: " + tokenHash);
        log.info("Status: " + DataStatus.ACTIVE.getValue());
        Optional<CustomerVerificationPin> customerVerificationPinOptional = customerVerificationPinDao.findByEventAndTokenAndStatus(
                event,
                token,
                DataStatus.ACTIVE.getValue()
        );

        boolean verifyTOTP = OTPUtils.verifyTOTP(customerVerificationPinOptional.get().getDeviceId(), pin, customerVerificationPinOptional.get().getExpiredAt());
        log.info("Verified customer verification {}", verifyTOTP);

        // Log the result
        log.info("Query Result Found: " + customerVerificationPinOptional.isPresent());

        if (verifyTOTP) {
            CustomerVerificationPin pins = customerVerificationPinOptional.get();
            log.info("Found Pin Details:");
            log.info("Stored DeviceId: " + pins.getDeviceId());
            log.info("Stored Event: " + pins.getEvent());
            log.info("Stored Token Hash: " + pins.getTokenHash());
            log.info("Stored Status: " + pins.getStatus());
        }


        if (customerVerificationPinOptional.get().getExpiredAt().before(new Date()))
            throw new Exception(SystemError.VERIFICATION_EXPIRED.name());

        if (!customerVerificationPinOptional.get().getPinHash().equals(ExtraUtils.generateMD5(pin)))
            throw new Exception(SystemError.VERIFICATION_PIN_NOT_VALID.name());

        customerVerificationPinOptional.get().setStatus(DataStatus.INACTIVE.getValue());
        customerVerificationPinDao.save(customerVerificationPinOptional.get());

        return customerVerificationPinOptional.get();
    }

    private String generatedNewToken(String deviceId) throws Exception {
        return UUID.randomUUID() + "-" + deviceId;
    }
}
