/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boserverpos.scheduler;

import com.bopro.database.BackendDBProcess;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import org.apache.log4j.Logger;

/**
 *
 * @author herrysuganda
 */
public class ReportScheduler extends Thread {

    private static Logger log = Logger.getLogger(ReportScheduler.class);

    BackendDBProcess dp = new BackendDBProcess();

    @Override
    public void run() {
        while (true) {
            try {
                GregorianCalendar cal = new GregorianCalendar();
                if (cal.get(GregorianCalendar.HOUR_OF_DAY) == 11) {
                    System.out.println("generate data running !");
                    log.info("generate data running !");
//                    dp.generateabsence();

                }
                cal = null;
                Thread.sleep(1000 * 60 * 60);
            } catch (InterruptedException ex) {
                java.util.logging.Logger.getLogger(ReportScheduler.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
