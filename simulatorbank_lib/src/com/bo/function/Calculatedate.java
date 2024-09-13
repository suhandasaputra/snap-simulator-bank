/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bo.function;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;
import org.apache.log4j.Logger;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

/**
 *
 * author MATAJARI MITRA SOLUSI
 */
public class Calculatedate {

    private static Logger log = Logger.getLogger(Calculatedate.class);

    // Create function for finding difference   
    public static HashMap calculatedate(String join_date, String nowdate) {
        // Create an instance of the SimpleDateFormat class  
        SimpleDateFormat obj = new SimpleDateFormat("yyyy-MM-dd");
        long time_difference = 0;
        long days_difference = 0;
        long years_difference = 0;
        long seconds_difference = 0;
        long minutes_difference = 0;

        HashMap list = new HashMap();
        try {
            Date date1 = obj.parse(join_date);
            Date date2 = obj.parse(nowdate);
            time_difference = date2.getTime() - date1.getTime();
            list.put("differenttime", time_difference);
            days_difference = TimeUnit.MILLISECONDS.toDays(time_difference) % 365;
            list.put("differentday", days_difference);
            years_difference = TimeUnit.MILLISECONDS.toDays(time_difference) / 365l;
            list.put("differentyear", years_difference);
            seconds_difference = TimeUnit.MILLISECONDS.toSeconds(time_difference) % 60;
            list.put("differentsecond", seconds_difference);
            minutes_difference = TimeUnit.MILLISECONDS.toMinutes(time_difference) % 60;
            list.put("differentminutes", minutes_difference);
            long hours_difference = TimeUnit.MILLISECONDS.toHours(time_difference) % 24;
            list.put("differenthours", hours_difference);
        } catch (java.text.ParseException excep) {
            System.out.println("error : " + excep);
        }
        return list;
    }
}
