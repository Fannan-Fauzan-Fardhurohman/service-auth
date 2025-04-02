package com.fanxan.serviceauth.utils;

import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;

public class ExtraUtils {
    public static String generateMD5(String content) throws Exception {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(content.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toLowerCase();
    }
}
