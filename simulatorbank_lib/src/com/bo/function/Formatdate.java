/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.bo.function;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author suhan
 */
public class Formatdate {
    
    public static Timestamp formatdate1(String a) {
        String dateTimeStringfrom = a;
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStringfrom, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
        String formattedDateTime = dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        Timestamp timestamp = Timestamp.valueOf(formattedDateTime);
        return timestamp;
    }
    
}
