package com.mtattab.c2cServer.util;

import lombok.Data;
import lombok.experimental.UtilityClass;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.security.NoSuchAlgorithmException;

@UtilityClass
@Data
public class EncryptionUtil {


    public static SecretKey generateAes256Key() {
        // Create a KeyGenerator instance for AES
        KeyGenerator keyGenerator = null;
        try {
            keyGenerator = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }

        // Initialize the KeyGenerator with a 256-bit key size
        keyGenerator.init(256);

        // Generate a SecretKey
        return keyGenerator.generateKey();
    }

    public static String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02X", b));
        }
        return result.toString();
    }
}
