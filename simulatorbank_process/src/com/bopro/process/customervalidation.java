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
public class customervalidation {

    private static Logger log = Logger.getLogger(customervalidation.class);

    public HashMap process(HashMap input) throws Exception {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap status = dp.customer_validation(input.get("msisdn").toString(), input.get("pin").toString(), input.get("reference_number").toString());
        return status;
    }
}
