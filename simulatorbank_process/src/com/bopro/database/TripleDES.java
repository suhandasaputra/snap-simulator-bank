/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bopro.database;


import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import java.util.Base64;

public class TripleDES {

    private static final String ALGORITHM = "DESede";
    private static final String TRANSFORMATION = "DESede/ECB/PKCS5Padding";
    private static final String key = "suhandasaputra1234567890";

    public static String encrypt(String plaintext) throws Exception {
        SecretKey secretKey = generateKey(key);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        byte[] encryptedTextBytes = cipher.doFinal(plaintext.getBytes("UTF-8"));
        return Base64.getEncoder().encodeToString(encryptedTextBytes);
    }

    public static String decrypt(String encryptedText) throws Exception {
        SecretKey secretKey = generateKey(key);
        Cipher cipher = Cipher.getInstance(TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        byte[] decryptedTextBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        return new String(decryptedTextBytes, "UTF-8");
    }

    private static SecretKey generateKey(String key) throws Exception {
        DESedeKeySpec keySpec = new DESedeKeySpec(key.getBytes("UTF-8"));
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance(ALGORITHM);
        return keyFactory.generateSecret(keySpec);
    }

    public static void main(String[] args) {
        try {
            String plaintext = "123456";
            
            String encryptedText = encrypt(plaintext);
            System.out.println("Encrypted: " + encryptedText);
            
            String decryptedText = decrypt(encryptedText);
            System.out.println("Decrypted: " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
