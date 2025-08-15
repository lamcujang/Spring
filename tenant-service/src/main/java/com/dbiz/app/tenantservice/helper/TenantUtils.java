package com.dbiz.app.tenantservice.helper;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.UUID;

@Service
public class TenantUtils {

    // Prefixes for database and username generation
    private static final String DB_NAME_PREFIX = "db_";
    private static final String USERNAME_PREFIX = "usr_";
    private static final int DEFAULT_HASH_LENGTH = 12;

    // Characters for password generation
    private static final String PASSWORD_CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*";
    private static final SecureRandom RANDOM = new SecureRandom();


    // Generate a hashed string from tenant name + current time
    public String generateHashedDatabaseName(String tenantName) {
        long timestamp = System.currentTimeMillis();
        String input = tenantName + timestamp;

        try {
            // Use SHA-256 to hash the input string
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            // Return a substring with the desired fixed length (ensure it's not longer than the generated string)
            return DB_NAME_PREFIX + hexString.substring(0, Math.min(DEFAULT_HASH_LENGTH, hexString.length()));

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }

    // Generate Username
    // Generate a hashed string from tenant name + current time
    public String generateUsername(String tenantName) {
        long timestamp = System.currentTimeMillis();
        String input = tenantName + timestamp;

        try {
            // Use SHA-256 to hash the input string
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            // Convert the byte array to a hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }

            // Return a substring with the desired fixed length (ensure it's not longer than the generated string)
            return USERNAME_PREFIX + hexString.substring(0, Math.min(DEFAULT_HASH_LENGTH, hexString.length()));

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating hash", e);
        }
    }

    // Generate Secure Password
    public  String generatePassword(int length) {
        StringBuilder password = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            password.append(PASSWORD_CHARACTERS.charAt(RANDOM.nextInt(PASSWORD_CHARACTERS.length())));
        }
        return password.toString();
    }

    // Alternatively, generate a UUID password
    public  String generatePasswordUsingUUID() {
        return Base64.getEncoder().encodeToString(UUID.randomUUID().toString().getBytes()).substring(0, 12); // Limiting length
    }
}
