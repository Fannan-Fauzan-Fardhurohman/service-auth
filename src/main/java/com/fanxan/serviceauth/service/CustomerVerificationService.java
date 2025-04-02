package com.fanxan.serviceauth.service;

import com.fanxan.serviceauth.model.dao.CustomerVerificationPinDao;
import com.fanxan.serviceauth.model.entity.CustomerVerificationPin;
import com.fanxan.serviceauth.service.impl.ICustomerVerificationService;
import com.fanxan.serviceauth.utils.DataStatus;
import com.fanxan.serviceauth.utils.ExtraUtils;
import com.fanxan.serviceauth.utils.OTPUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.Optional;

@Service
@AllArgsConstructor
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

//            if (DateUtils.minutesDiff(new Date(), customerVerificationPin.getExpiredAt()) >= 0)
//                return customerVerificationPin;
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
        customerVerificationPin.setTokenHash(StringUtils.generateMD5(token));

//        String pin = StringUtils.generateValidationPIN();

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
        return null;
    }
}
