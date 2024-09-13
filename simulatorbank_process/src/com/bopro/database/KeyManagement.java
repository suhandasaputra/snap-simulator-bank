///*
// * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
// * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
// */
//package com.bopro.database;
//
///**
// *
// * @author suhan
// */
//import javax.crypto.KeyGenerator;
//import javax.crypto.SecretKey;
//import javax.crypto.spec.SecretKeySpec;
//import java.util.Base64;
//import java.nio.file.Files;
//import java.nio.file.Paths;
//import java.nio.file.StandardOpenOption;
//
//public class KeyManagement {
//    private static final String ALGORITHM = "DESede";
//    private static final String KEY_FILE = "3des.key";
//
//    public static void generateAndSaveKey() throws Exception {
//        System.out.println("Current Working Directory: " + System.getProperty("user.dir"));
//
//        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
//        keyGenerator.init(168); // Key size for Triple DES
//        SecretKey key = keyGenerator.generateKey();
//        byte[] encodedKey = Base64.getEncoder().encode(key.getEncoded());
//        Files.write(Paths.get(KEY_FILE), encodedKey, StandardOpenOption.CREATE);
//    }
//
//    public static SecretKey loadKey() throws Exception {
//        byte[] encodedKey = Files.readAllBytes(Paths.get(KEY_FILE));
//        byte[] decodedKey = Base64.getDecoder().decode(encodedKey);
//        return new SecretKeySpec(decodedKey, ALGORITHM);
//    }
//
////    public static void main(String[] args) {
////        try {
////            generateAndSaveKey();
////            SecretKey key = loadKey();
////            System.out.println("Key generated and loaded successfully");
////        } catch (Exception e) {
////            e.printStackTrace();
////        }
////    }
//}
