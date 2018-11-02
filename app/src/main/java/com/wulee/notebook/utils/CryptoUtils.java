package com.wulee.notebook.utils;

import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
/**
 * Created by dell on 2018/4/5.
 */

public class CryptoUtils {
    private static final String ALGORITHM = "AES";

    public CryptoUtils()
    {
        // pass
    }

    /**
     * Encrypts the given plain text
     *
     * @param plainTextString The plain text to encrypt
     */
    public String encrypt(String plainTextString, String keyString) throws Exception
    {
        byte[] key = keyString.getBytes();
        byte[] plainText = plainTextString.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);

        byte[] result = cipher.doFinal(plainText);
        return new String(result, StandardCharsets.UTF_8);
    }

    /**
     * Decrypts the given byte array
     *
     * @param cipherTextString The data to decrypt
     */
    public String decrypt(String cipherTextString, String keyString) throws Exception
    {
        byte[] key = keyString.getBytes();
        byte[] cipherText = cipherTextString.getBytes();
        SecretKeySpec secretKey = new SecretKeySpec(key, ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);

        byte[] result = cipher.doFinal(cipherText);
        return new String(result, StandardCharsets.UTF_8);
    }
}