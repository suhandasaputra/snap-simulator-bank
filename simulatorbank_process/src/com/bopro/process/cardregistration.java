/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.process;

import com.bopro.database.BackendDBProcess;
import java.util.HashMap;
import org.apache.log4j.Logger;

/**
 *
 * @author suhanda
 */
public class cardregistration {

    private static Logger log = Logger.getLogger(cardregistration.class);

    public HashMap process(HashMap input) {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap status = dp.card_registration(input.get("card_number").toString(), input.get("merchant_id").toString(), input.get("reference_number").toString(), input.get("type").toString(), input.get("transaction_datetime").toString());
        return status;
    }
}
