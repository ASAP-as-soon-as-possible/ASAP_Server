package com.asap.server.common.utils;

import com.asap.server.service.vo.PasswordInfoVo;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordEncryptionUtil {
    private static final int SALT_SIZE = 16;
    private static final MessageDigest sha256;

    static {
        try {
            sha256 = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    public static PasswordInfoVo encryptPassword(String password) {
        String salt = getSalt();
        String passwordWithSalt = password + salt;

        sha256.update(passwordWithSalt.getBytes());
        String encryptedPassword = byteToString(sha256.digest());
        return new PasswordInfoVo(encryptedPassword, salt);
    }

    public static String encryptPassword(String password, String salt) {
        String passwordWithSalt = password + salt;

        sha256.update(passwordWithSalt.getBytes());
        return byteToString(sha256.digest());
    }

    private static String getSalt() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] temp = new byte[SALT_SIZE];
        secureRandom.nextBytes(temp);
        return byteToString(temp);
    }

    private static String byteToString(byte[] temp) {
        StringBuilder sb = new StringBuilder();
        for (byte t : temp) {
            sb.append(String.format("%02x", t));
        }
        return sb.toString();
    }
}
