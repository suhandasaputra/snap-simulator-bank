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
public class credittransferinterbank {

    private static Logger log = Logger.getLogger(credittransferinterbank.class);

    public HashMap process(HashMap input) {
        BackendDBProcess dp = new BackendDBProcess();
        HashMap status = dp.credit_transfer_interbank(
                input.get("merchant_id").toString(),
                input.get("source_account_number").toString(),
                input.get("beneficiary_account_number").toString(),
                input.get("beneficiary_name").toString(),
                input.get("beneficiary_email").toString(),
                input.get("beneficiary_address").toString(),
                input.get("beneficiary_bank_code").toString(),
                input.get("beneficiary_bank_name").toString(),
                input.get("amount").toString(),
                input.get("currency").toString(),
                input.get("remark").toString(),
                input.get("transaction_datetime").toString(),
                input.get("reference_number").toString());
        return status;
    }
}
