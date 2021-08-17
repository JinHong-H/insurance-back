package cn.wghtstudio.insurance.util;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class PasswordMD5 {
    public static String getPasswordMD5(String password) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(password.getBytes(StandardCharsets.UTF_8));
            byte[] result = messageDigest.digest();

            return new BigInteger(1, result).toString(16);
        } catch (NoSuchAlgorithmException e) {
            return "";
        }
    }
}
