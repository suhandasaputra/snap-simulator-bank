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
public class transactioninquiry {
    
    private static Logger log = Logger.getLogger(transactioninquiry.class);
    
    public HashMap process(HashMap input) {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap status = dp.transaction_inquiry(
                input.get("merchant_id").toString(),
                input.get("amount").toString(),
                input.get("currency").toString(),
                input.get("original_reference_number").toString(),
                input.get("reference_number").toString(),
                input.get("transaction_datetime").toString()
        );
        return status;
    }
}
