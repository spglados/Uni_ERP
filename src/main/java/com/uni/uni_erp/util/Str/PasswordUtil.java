package com.uni.uni_erp.util.Str;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

public class PasswordUtil {

    private static String saltGenerator() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hashGenerator(String password) throws NoSuchAlgorithmException {
        String salt = saltGenerator();
        MessageDigest md = MessageDigest.getInstance("SHA-512");

        md.update(salt.getBytes());
        byte[] hashedPassword = md.digest(password.getBytes());

        return salt + ":" + Base64.getEncoder().encodeToString(hashedPassword);
    }


    public static boolean verify(String password, String storedSaltedHash) throws NoSuchAlgorithmException {
        String parts[] = storedSaltedHash.split(":");
        String salt = parts[0];
        String storedHash = parts[1];

        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(salt.getBytes());

        byte[] hashedPassword = md.digest(password.getBytes());
        byte[] storedHashedPassword = Base64.getDecoder().decode(storedHash);

        return MessageDigest.isEqual(hashedPassword, storedHashedPassword);
    }
}
