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
public class accountinquiryeksternal {

    private static Logger log = Logger.getLogger(accountinquiryeksternal.class);

    public HashMap process(HashMap input) {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap status = dp.account_inquiry_eksternal(input.get("account_number").toString(), input.get("reference_number").toString(), input.get("bank_code").toString());
        return status;
    }
}
