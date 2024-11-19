package com.josefy.nnpda.infrastructure.utils;

import org.springframework.stereotype.Component;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Component
public class Sha256Provider implements IHashProvider {
    public String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedHash = digest.digest(
                    input.getBytes(StandardCharsets.UTF_8)
            );
            return bytesToHex(encodedHash);
        } catch (NoSuchAlgorithmException e) {
            // This should not happen as SHA-256 is a standard algorithm
            throw new RuntimeException("SHA-256 algorithm not found.", e);
        }
    }
    public boolean isHmacValid(String input, String key, String hash) {
        return hmac(input, key).equals(hash);
    }

    @Override
    public boolean isHmacValid(InputStream input, String key, String hash) throws IOException {
    }

    public String hmac(String input, String key) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKey);
            byte[] derivedBytes = mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(derivedBytes);
        } catch (Exception e) {
            throw new RuntimeException("Error while hashing with HMAC SHA-256.", e);
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = String.format("%02x", b);
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
