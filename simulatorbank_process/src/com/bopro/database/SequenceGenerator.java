/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bopro.database;

import java.util.Random;

public class SequenceGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private static final int LENGTH = 10;

    public static String generateSequenceNumber(String lastSequenceNumber) {
        StringBuilder sb = new StringBuilder();

        if (lastSequenceNumber.isEmpty()) {
            // Jika belum ada sequence number sebelumnya, generate secara acak
            Random random = new Random();
            for (int i = 0; i < LENGTH; i++) {
                int index = random.nextInt(CHARACTERS.length());
                char randomChar = CHARACTERS.charAt(index);
                sb.append(randomChar);
            }
        } else {
            // Jika sudah ada sequence number sebelumnya, buat sequence number berikutnya
            char[] sequenceChars = lastSequenceNumber.toCharArray();
            boolean carry = true;
            for (int i = LENGTH - 1; i >= 0; i--) {
                char currentChar = sequenceChars[i];
                if (carry) {
                    if (currentChar == '9') {
                        sequenceChars[i] = 'A';
                        carry = false;
                    } else if (currentChar == 'Z') {
                        sequenceChars[i] = '0';
                    } else {
                        sequenceChars[i] = (char) (currentChar + 1);
                        carry = false;
                    }
                }
                sb.insert(0, sequenceChars[i]);
            }
        }

        return sb.toString();
    }    
}
