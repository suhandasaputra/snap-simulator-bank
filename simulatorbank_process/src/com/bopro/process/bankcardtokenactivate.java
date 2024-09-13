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
public class bankcardtokenactivate {

    private static Logger log = Logger.getLogger(bankcardtokenactivate.class);

    public HashMap process(HashMap input) {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap status = dp.bank_card_token_activate(input.get("card_number").toString(), input.get("merchant_id").toString(), input.get("reference_number").toString(), input.get("token").toString());
        return status;
    }
}
