package com.fanxan.serviceauth.utils;

import lombok.extern.slf4j.Slf4j;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.GeneralSecurityException;
import java.sql.Timestamp;

@Slf4j
public class OTPUtils {
    public OTPUtils() {
    }

    private static final int TIME_STEP_SECONDS = 60;
    private static final int OTP_LENGTH = 6;
    private static final String HMAC_ALGORITHM = "HmacSHA1";

    public static String generateTOTP(String secret) {
        long timeStep = System.currentTimeMillis() / 1000 / TIME_STEP_SECONDS;
        return calculateTOTP(secret, timeStep);
    }


    public static String generateTOTP(String secret, Timestamp timestamp) {
        long timeInSecond = timestamp.getTime() / 1000;
        long timeStep = timeInSecond / TIME_STEP_SECONDS;
        return calculateTOTP(secret, timeStep);
    }

    public static String calculateTOTP(String secret, long timeStep){
        byte[] key = hexStringToByteArray(secret);
        byte[] data = longToBytes(timeStep);

        try {
            Mac mac = Mac.getInstance(HMAC_ALGORITHM);
            SecretKeySpec keySpec = new SecretKeySpec(key, "RAW");
            mac.init(keySpec);

            byte[] hash = mac.doFinal(data);

            int offset = hash[hash.length - 1] & 0xf;
            int binary = ((hash[offset] & 0x7f) << 24) |
                    ((hash[offset + 1] & 0xff) << 16) |
                    ((hash[offset + 2] & 0xff) << 8) |
                    (hash[offset + 3] & 0xff);

            int otp = binary % (int) Math.pow(10, OTP_LENGTH);
            return String.format("%0" + OTP_LENGTH + "d", otp);
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Error generating TOTP", e);
        }
    }

    public static boolean verifyTOTP(String secret, String totpCode, Timestamp timeInSeconds) {
        String generatedTOTP = generateTOTP(secret, timeInSeconds);
        log.info("Generating TOTP and PIN {}, {}", generatedTOTP, totpCode);
        return generatedTOTP.equals(totpCode);
    }
    private static byte[] hexStringToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

    private static byte[] longToBytes(long value) {
        byte[] data = new byte[8];
        for (int i = 7; i >= 0; i--) {
            data[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
        return data;
    }
}
