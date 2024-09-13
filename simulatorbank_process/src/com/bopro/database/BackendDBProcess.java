/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bopro.database;

import com.bo.function.Formatdate;
import com.bo.function.StringFunction;
import com.bopro.singleton.DatasourceEntryBackend;
import com.bo.parameter.FieldParameterMatapos;
import static com.bopro.database.SequenceGenerator.generateSequenceNumber;
import static com.bopro.database.TokenGenerator.generateToken;
import static com.bopro.database.TokenGenerator.verifyToken;
import static com.bopro.database.TripleDES.decrypt;
import static com.bopro.database.TripleDES.encrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.apache.log4j.Logger;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class BackendDBProcess {

    private static final Logger log = Logger.getLogger(BackendDBProcess.class);

    private void clearStatment(PreparedStatement stat) {
//        log.info("stat 2 : " + stat);
        if (stat != null) {
            try {
//                log.info("stat A");
                stat.clearBatch();
//                log.info("stat B");
                stat.clearParameters();
//                log.info("stat C");
                stat.close();
//                log.info("stat D");
                stat = null;
//                log.info("stat E");
            } catch (SQLException ex) {
//                log.error("clearStatment : " +ex.getMessage());
//                ex.printStackTrace();
            }
        }
    }

    private void clearDBConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException ex) {
//                log.error("clearDBConnection : "+ex.getMessage());
            }
        }
    }

    private void clearResultset(ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
                rs = null;
            } catch (SQLException ex) {
//                log.error("clearResultset : "+ex.getMessage());
            }
        }
    }

    private void clearAllConnStatRS(Connection conn, PreparedStatement stat, ResultSet rs) {
        clearResultset(rs);
        clearStatment(stat);
        clearDBConnection(conn);
    }

    public HashMap balance_inquiry(String account_number, String reference_number, String bank_card_token) {
        HashMap result = null;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        List b = new ArrayList();
        try {

            if (!"".equals(account_number)) {
                result = new HashMap();
                conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
                stat = conn.prepareStatement("select a.*, b.name from account_internal a \n"
                        + "left join customer b on a.customer_id = b.customer_id \n"
                        + "where a.account_number = ?");
                stat.setString(1, account_number);
                rs = stat.executeQuery();
                if (rs.next()) {

                    result.put("account_number", rs.getString("account_number"));
                    result.put("customer_id", rs.getString("customer_id"));
                    result.put("customer_name", rs.getString("name"));

                    HashMap cash = new HashMap();
                    cash.put("balance_type", "cash");
                    cash.put("amount", rs.getString("amount_cash"));
                    cash.put("currency_amount", rs.getString("currency"));
                    cash.put("float_amount", rs.getString("float_amount_cash"));
                    cash.put("currency_float_amount", rs.getString("currency"));
                    cash.put("hold_amount", rs.getString("hold_amount_cash"));
                    cash.put("currency_hold_amount", rs.getString("currency"));
                    cash.put("available_balance", rs.getString("available_balance_cash"));
                    cash.put("currency_available_balance", rs.getString("currency"));
                    cash.put("ledger_balance", rs.getString("ledger_balance_cash"));
                    cash.put("currency_ledger_balance", rs.getString("currency"));
                    cash.put("current_multilateral_limit", rs.getString("current_multilateral_limit_cash"));
                    cash.put("currency_current_multilateral_limit", rs.getString("currency"));
//                cash.put("registration_status", rs.getBoolean("account_bind_status"));
                    cash.put("account_status", rs.getString("account_status_code"));
                    cash.put("account_status_desc", rs.getString("account_status_desc"));

                    HashMap coins = new HashMap();
                    coins.put("balance_type", "coins");
                    coins.put("amount", rs.getString("amount_coins"));
                    coins.put("currency_amount", rs.getString("currency"));
                    coins.put("float_amount", rs.getString("float_amount_coins"));
                    coins.put("currency_float_amount", rs.getString("currency"));
                    coins.put("hold_amount", rs.getString("hold_amount_coins"));
                    coins.put("currency_hold_amount", rs.getString("currency"));
                    coins.put("available_balance", rs.getString("available_balance_coins"));
                    coins.put("currency_available_balance", rs.getString("currency"));
                    coins.put("ledger_balance", rs.getString("ledger_balance_coins"));
                    coins.put("currency_ledger_balance", rs.getString("currency"));
                    coins.put("current_multilateral_limit", rs.getString("current_multilateral_limit_coins"));
                    coins.put("currency_current_multilateral_limit", rs.getString("currency"));
//                coins.put("registration_status", rs.getBoolean("account_bind_status"));
                    coins.put("account_status_code", rs.getString("account_status_code"));
                    coins.put("account_status_desc", rs.getString("account_status_desc"));

                    b.add(cash);
                    b.add(coins);

                    result.put("account_info", b);
                    result.put("reference_number", reference_number);
                    result.put("resp_code", "00");
                    result.put("resp_desc", "successfully");

                } else {
                    result.put("reference_number", reference_number);
                    result.put(FieldParameterMatapos.resp_code, "02");
                    result.put(FieldParameterMatapos.resp_desc, "not found");
                }
            } else {
                result = new HashMap();
                conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
                stat = conn.prepareStatement("select a.bank_card_token, b.card_number, c.*, d.customer_id, d.name from card_binding a "
                        + "left join card b on a.card_number = b.card_number "
                        + "left join account_internal c on b.account_number = c.account_number "
                        + "left join customer d on c.customer_id = d.customer_id "
                        + "where a.bank_card_token = ?");
                stat.setString(1, bank_card_token);
                rs = stat.executeQuery();
                if (rs.next()) {

                    result.put("account_number", rs.getString("account_number"));
                    result.put("customer_id", rs.getString("customer_id"));
                    result.put("customer_name", rs.getString("name"));

                    HashMap cash = new HashMap();
                    cash.put("balance_type", "cash");
                    cash.put("amount", rs.getString("amount_cash"));
                    cash.put("currency_amount", rs.getString("currency"));
                    cash.put("float_amount", rs.getString("float_amount_cash"));
                    cash.put("currency_float_amount", rs.getString("currency"));
                    cash.put("hold_amount", rs.getString("hold_amount_cash"));
                    cash.put("currency_hold_amount", rs.getString("currency"));
                    cash.put("available_balance", rs.getString("available_balance_cash"));
                    cash.put("currency_available_balance", rs.getString("currency"));
                    cash.put("ledger_balance", rs.getString("ledger_balance_cash"));
                    cash.put("currency_ledger_balance", rs.getString("currency"));
                    cash.put("current_multilateral_limit", rs.getString("current_multilateral_limit_cash"));
                    cash.put("currency_current_multilateral_limit", rs.getString("currency"));
//                cash.put("registration_status", rs.getBoolean("account_bind_status"));
                    cash.put("account_status", rs.getString("account_status_code"));
                    cash.put("account_status_desc", rs.getString("account_status_desc"));

                    HashMap coins = new HashMap();
                    coins.put("balance_type", "coins");
                    coins.put("amount", rs.getString("amount_coins"));
                    coins.put("currency_amount", rs.getString("currency"));
                    coins.put("float_amount", rs.getString("float_amount_coins"));
                    coins.put("currency_float_amount", rs.getString("currency"));
                    coins.put("hold_amount", rs.getString("hold_amount_coins"));
                    coins.put("currency_hold_amount", rs.getString("currency"));
                    coins.put("available_balance", rs.getString("available_balance_coins"));
                    coins.put("currency_available_balance", rs.getString("currency"));
                    coins.put("ledger_balance", rs.getString("ledger_balance_coins"));
                    coins.put("currency_ledger_balance", rs.getString("currency"));
                    coins.put("current_multilateral_limit", rs.getString("current_multilateral_limit_coins"));
                    coins.put("currency_current_multilateral_limit", rs.getString("currency"));
//                coins.put("registration_status", rs.getBoolean("account_bind_status"));
                    coins.put("account_status_code", rs.getString("account_status_code"));
                    coins.put("account_status_desc", rs.getString("account_status_desc"));

                    b.add(cash);
                    b.add(coins);

                    result.put("account_info", b);
                    result.put("reference_number", reference_number);
                    result.put("resp_code", "00");
                    result.put("resp_desc", "successfully");

                } else {
                    result.put("reference_number", reference_number);
                    result.put(FieldParameterMatapos.resp_code, "02");
                    result.put(FieldParameterMatapos.resp_desc, "not found");
                }
            }
        } catch (SQLException e) {
            result.put("reference_number", reference_number);
            result.put(FieldParameterMatapos.resp_code, "01");
            result.put(FieldParameterMatapos.resp_desc, "Error : " + e.getMessage());
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap bank_statement(String account_number, String from_datetime, String to_datetime, String reference_number) {
        HashMap result = null;
        Connection conn = null;
        PreparedStatement stat = null;
        PreparedStatement stat1 = null;
        ResultSet rs = null;
        ResultSet rs1 = null;
        List l = new ArrayList();
        List b = new ArrayList();

        try {
            result = new HashMap();
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();

            stat1 = conn.prepareStatement("SELECT "
                    + "(SELECT start_amount FROM public.transaction "
                    + "WHERE source_account = a.account_number AND transaction_datetime >= ? "
                    + "AND transaction_datetime <= ? ORDER BY record_id ASC LIMIT 1) AS start_amount, "
                    + "(SELECT end_amount FROM public.transaction "
                    + "WHERE source_account = a.account_number AND transaction_datetime >= ? "
                    + "AND transaction_datetime <= ? ORDER BY record_id DESC LIMIT 1) AS end_amount, "
                    + "(SELECT transaction_datetime FROM public.transaction "
                    + "WHERE source_account = a.account_number AND transaction_datetime >= ? "
                    + "AND transaction_datetime <= ? ORDER BY record_id DESC LIMIT 1) AS transaction_datetime, "
                    + "a.amount_cash FROM public.account_internal a WHERE a.account_number = ?");
            stat1.setTimestamp(1, Formatdate.formatdate1(from_datetime));
            stat1.setTimestamp(2, Formatdate.formatdate1(to_datetime));
            stat1.setTimestamp(3, Formatdate.formatdate1(from_datetime));
            stat1.setTimestamp(4, Formatdate.formatdate1(to_datetime));
            stat1.setTimestamp(5, Formatdate.formatdate1(from_datetime));
            stat1.setTimestamp(6, Formatdate.formatdate1(to_datetime));
            stat1.setString(7, account_number);
            rs1 = stat1.executeQuery();

            if (rs1.next()) {
                HashMap ac = new HashMap();
                ac.put("starting_balance", rs1.getString("start_amount"));
                ac.put("current_balance", rs1.getString("amount_cash"));
                ac.put("ending_balance", rs1.getString("end_amount"));

                b.add(ac);
                ac = null;
            }

            stat = conn.prepareStatement("SELECT * FROM transaction WHERE source_account = ? AND transaction_datetime >= ? AND transaction_datetime <= ? ");
            stat.setString(1, account_number);
            stat.setTimestamp(2, Formatdate.formatdate1(from_datetime));
            stat.setTimestamp(3, Formatdate.formatdate1(to_datetime));
            rs = stat.executeQuery();

            int debit = 0;
            int credit = 0;
            long amount_debit = 0;
            long amount_credit = 0;
            String currency_credit = "";
            String currency_debit = "";
            while (rs.next()) {
                HashMap ab = new HashMap();
                ab.put("transaction_id", rs.getString("reference_number"));
                ab.put("transaction_category", rs.getString("transaction_category"));
                ab.put("transaction_datetime", rs.getString("transaction_datetime"));
                ab.put("start_amount", rs.getString("start_amount"));
                ab.put("currency_start_amount", rs.getString("currency"));
                ab.put("amount", rs.getString("amount"));
                ab.put("origin_amount", rs.getString("origin_amount"));
                ab.put("currency_amount", rs.getString("currency"));
                ab.put("amount", rs.getString("amount"));
                ab.put("currency_amount", rs.getString("currency"));
                ab.put("end_amount", rs.getString("end_amount"));
                ab.put("currency_end_amount", rs.getString("currency"));
                ab.put("description", rs.getString("description"));

                if ("C".equals(rs.getString("transaction_category"))) {
                    credit += 1;
                    amount_credit += Long.valueOf(rs.getString("amount"));
                    currency_credit = rs.getString("currency");

                } else if ("D".equals(rs.getString("transaction_category"))) {
                    debit += 1;
                    amount_debit += Long.valueOf(rs.getString("amount"));
                    currency_debit = rs.getString("currency");
                }
                ab.put("status_transaction", rs.getString("status_transaction"));

                l.add(ab);
                ab = null;

            }

            result.put("balance", b);

            result.put("detail_data", l);
            result.put("has_more", "N");
            result.put("last_record_date_time", rs1.getString("transaction_datetime"));

            HashMap total_credit_entries = new HashMap();
            total_credit_entries.put("number_of_entries_credit", credit);
            total_credit_entries.put("total_amount_credit", amount_credit);
            total_credit_entries.put("currency_credit", currency_credit);

            HashMap total_debit_entries = new HashMap();
            total_debit_entries.put("number_of_entries_debit", debit);
            total_debit_entries.put("total_amount_debit", amount_debit);
            total_debit_entries.put("currency_debit", currency_debit);

            result.put("total_credit_entries", total_credit_entries);
            result.put("total_debit_entries", total_debit_entries);

            result.put("reference_number", reference_number);
            result.put("resp_code", "00");
            result.put("resp_desc", "success");

        } catch (SQLException e) {
            result.put("reference_number", reference_number);
            result.put(FieldParameterMatapos.resp_code, "01");
            result.put(FieldParameterMatapos.resp_desc, "Error : " + e.getMessage());
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap otp(String account_number, String reference_number) {
        HashMap result = null;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        PreparedStatement stat1 = null;
        try {
            result = new HashMap();
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select a.account_number, a.customer_id, b.name, b.email, phone_number from account_internal a left join customer b on a.customer_id = b.customer_id where a.account_number = ?");
            stat.setString(1, account_number);
            rs = stat.executeQuery();
            if (rs.next()) {

                String secretKey = String.valueOf(rs.getInt("customer_id")) + rs.getString("email");
                String data = reference_number;
                String token = generateToken(secretKey, data);

                stat1 = conn.prepareStatement("insert into token (customer_id, charge_token, otp, status, expired_time) values (?, ?, ?, ?, ?)");
                stat1.setInt(1, rs.getInt("customer_id"));
                stat1.setString(2, token);
                stat1.setString(3, StringFunction.generateOTP(6));
                stat1.setString(4, "sent");
                stat1.setInt(5, 60);
                stat1.executeUpdate();
                stat1.close();

                result.put("charge_token", token);
                result.put("status_otp", "sent");
                result.put("resp_code", "00");
                result.put("resp_desc", "success");
            } else {
                result.put("resp_code", "02");
                result.put("resp_desc", "not found");
            }
        } catch (SQLException e) {
            result.put(FieldParameterMatapos.resp_code, "01");
            result.put(FieldParameterMatapos.resp_desc, "Error : " + e.getMessage());
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap account_inquiry_internal(String beneficiary_account_number, String reference_number) {
        HashMap result = null;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {

            result = new HashMap();
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select a.*, b.name from account_internal a left join customer b "
                    + "on a.customer_id = b.customer_id where a.account_number = ?");
            stat.setString(1, beneficiary_account_number);
            rs = stat.executeQuery();
            if (rs.next()) {

                result.put("account_number", rs.getString("account_number"));
                result.put("name", rs.getString("name"));
                result.put("account_status_code", rs.getString("account_status_code"));
                result.put("account_status_desc", rs.getString("account_status_desc"));
                result.put("account_type", rs.getString("account_type"));
                result.put("currency", rs.getString("currency"));

                result.put("reference_number", reference_number);
                result.put("resp_code", "00");
                result.put("resp_desc", "successfully");

            } else {
                result.put("reference_number", reference_number);
                result.put(FieldParameterMatapos.resp_code, "02");
                result.put(FieldParameterMatapos.resp_desc, "not found");
            }

        } catch (SQLException e) {
            result.put("reference_number", reference_number);
            result.put(FieldParameterMatapos.resp_code, "01");
            result.put(FieldParameterMatapos.resp_desc, "Error : " + e.getMessage());
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap account_inquiry_eksternal(String beneficiary_account_number, String reference_number, String bank_code) {
        HashMap result = null;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            result = new HashMap();
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select a.*, b.bank_name from account_eksternal a left join bank b "
                    + "on a.bank_code = b.bank_code where a.account_number = ?");
            stat.setString(1, beneficiary_account_number);
            rs = stat.executeQuery();
            if (rs.next()) {

                result.put("account_number", rs.getString("account_number"));
                result.put("name", rs.getString("name"));
                result.put("bank_code", rs.getString("bank_code"));
                result.put("bank_name", rs.getString("bank_name"));

                result.put("reference_number", reference_number);
                result.put("resp_code", "00");
                result.put("resp_desc", "successfully");

            } else {
                result.put("reference_number", reference_number);
                result.put(FieldParameterMatapos.resp_code, "02");
                result.put(FieldParameterMatapos.resp_desc, "not found");
            }

        } catch (SQLException e) {
            result.put("reference_number", reference_number);
            result.put(FieldParameterMatapos.resp_code, "01");
            result.put(FieldParameterMatapos.resp_desc, "Error : " + e.getMessage());
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap card_registration(String card_number, String merchant_id, String reference_number, String type, String transaction_datetime) {
        HashMap result = null;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        Timestamp transactionTimestamp = null;
        long binding_id = 0;
        String email = "";

        try {
            // Validate and parse transaction_datetime
            try {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime parsedDateTime = LocalDateTime.parse(transaction_datetime, inputFormatter);
                String formattedDateTime = parsedDateTime.format(outputFormatter);
                transactionTimestamp = Timestamp.valueOf(formattedDateTime);
            } catch (DateTimeParseException | IllegalArgumentException e) {
                throw new SQLException("Invalid datetime format. Must be yyyy-MM-dd HH:mm:ss.");
            }

            result = new HashMap();
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            conn.setAutoCommit(false); // Start transaction

            stat = conn.prepareStatement("select a.customer_id, a.email, b.account_number, c.card_number from customer a \n"
                    + "left join account_internal b on a.customer_id = b.customer_id \n"
                    + "left join card c on b.account_number = c.account_number \n"
                    + "where c.card_number = ?");
            stat.setString(1, card_number);
            rs = stat.executeQuery();
            if (rs.next()) {
                email = rs.getString("email");
                stat.close();
                rs.close();

                stat = conn.prepareStatement("select * from card_binding where card_number = ? and merchant_id = ? and bind_status = ?");
                stat.setString(1, card_number);
                stat.setString(2, merchant_id);
                stat.setBoolean(3, true);
                rs = stat.executeQuery();
                if (rs.next()) {
                    result.put("reference_number", reference_number);
                    result.put("resp_code", "03");
                    result.put("resp_desc", "card already subscribe");
                } else {

                    stat.close();
                    rs.close();

                    stat = conn.prepareStatement("select * from card_binding where card_number = ? and merchant_id = ? and bind_status = ? and token_status = ?");
                    stat.setString(1, card_number);
                    stat.setString(2, merchant_id);
                    stat.setBoolean(3, false);
                    stat.setBoolean(4, true);
                    rs = stat.executeQuery();
                    if (rs.next()) {
                        result.put("reference_number", reference_number);
                        result.put("resp_code", "04");
                        result.put("resp_desc", "you have requested a token before, please activate the token");
                    } else {
                        stat = conn.prepareStatement("select max(binding_id) as binding_id from card_binding");
                        rs = stat.executeQuery();
                        if (rs.next()) {
                            binding_id = rs.getLong("binding_id") + 1;
                        } else {
                            binding_id = 1;
                        }

                        String g_token = generateToken(merchant_id + binding_id, card_number);
                        stat = conn.prepareStatement("insert into card_binding (binding_id, card_number, bind_status, bank_card_token, merchant_id, binding_datetime, token_status) values (?, ?,?,?,?,?,?)");
                        stat.setLong(1, binding_id);
                        stat.setString(2, card_number);
                        stat.setBoolean(3, false);
                        stat.setString(4, g_token);
                        stat.setString(5, merchant_id);
                        stat.setTimestamp(6, transactionTimestamp);
                        stat.setBoolean(7, true);
                        stat.executeUpdate();
                        stat.close();

                        conn.commit(); // Commit transaction

                        result.put("card_number", card_number);
                        result.put("merchant_id", merchant_id);
                        result.put("bank_card_token", g_token);
                        result.put("reference_number", reference_number);
                        result.put("email", email);
                        result.put("type", type);
                        result.put("binding_datetime", transaction_datetime);
                        result.put("resp_code", "00");
                        result.put("resp_desc", "successfully");
                    }
                }
            } else {
                result.put("reference_number", reference_number);
                result.put(FieldParameterMatapos.resp_code, "02");
                result.put(FieldParameterMatapos.resp_desc, "card not found");
            }
        } catch (SQLException e) {
            result.put("reference_number", reference_number);
            result.put(FieldParameterMatapos.resp_code, "01");
            result.put(FieldParameterMatapos.resp_desc, "Error : " + e.getMessage());
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap bank_card_token_activate(String card_number, String merchant_id, String reference_number, String token) {
        HashMap result = null;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        long binding_id = 0;
        Timestamp waktu = Timestamp.valueOf(LocalDateTime.now());
        Boolean bind_status = false;
        Boolean token_status = false;
        try {
            result = new HashMap();
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            conn.setAutoCommit(false); // Start transaction
            stat = conn.prepareStatement("select * from card_binding where card_number = ? and merchant_id = ? and bank_card_token = ?");
            stat.setString(1, card_number);
            stat.setString(2, merchant_id);
            stat.setString(3, token);
            rs = stat.executeQuery();
            if (rs.next()) {
                bind_status = rs.getBoolean("bind_status");
                token_status = rs.getBoolean("token_status");
                binding_id = rs.getLong("binding_id");

                boolean validate = verifyToken(merchant_id + String.valueOf(binding_id), card_number, token);
                if (validate) {

                    if (bind_status) {
                        result.put("reference_number", reference_number);
                        result.put(FieldParameterMatapos.resp_code, "00");
                        result.put(FieldParameterMatapos.resp_desc, "the token has been previously activated");
                    } else {

                        if (token_status) {
                            stat.close();
                            rs.close();
                            stat = conn.prepareStatement("update card_binding set bind_status = ?, activation_datetime = ? where card_number = ? and merchant_id = ? and binding_id = ? and bank_card_token = ?");
                            stat.setBoolean(1, true);
                            stat.setTimestamp(2, waktu);
                            stat.setString(3, card_number);
                            stat.setString(4, merchant_id);
                            stat.setLong(5, binding_id);
                            stat.setString(6, token);

                            stat.executeUpdate();
                            stat.close();

                            conn.commit();

                            result.put("reference_number", reference_number);
                            result.put("activation_datetime", waktu);
                            result.put("resp_code", "00");
                            result.put("resp_desc", "successfully");
                        } else {
                            result.put("reference_number", reference_number);
                            result.put(FieldParameterMatapos.resp_code, "04");
                            result.put(FieldParameterMatapos.resp_desc, "token not active, please create new token");
                        }

                    }

                } else {
                    result.put("reference_number", reference_number);
                    result.put(FieldParameterMatapos.resp_code, "03");
                    result.put(FieldParameterMatapos.resp_desc, "activation failed");
                }
            } else {
                result.put("reference_number", reference_number);
                result.put(FieldParameterMatapos.resp_code, "02");
                result.put(FieldParameterMatapos.resp_desc, "card binding not found");
            }
        } catch (SQLException e) {
            result.put("reference_number", reference_number);
            result.put(FieldParameterMatapos.resp_code, "01");
            result.put(FieldParameterMatapos.resp_desc, "Error : " + e.getMessage());
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap<String, Object> credit_transfer_intrabank(String merchant_id, String source_account_number, String beneficiary_account_number, String amount,
            String currency, String transaction_datetime, String reference_number, String beneficiary_email, String description) {
        HashMap<String, Object> result = new HashMap<>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        long source_amount_cash_before;
        long source_available_balance_cash_before;
        long destination_amount_cash_before;
        long destination_available_balance_cash_before;

        long source_amount_cash_after;
        long source_available_balance_cash_after;
        long destination_amount_cash_after;
        long destination_available_balance_cash_after;
        String running_numbers = "";
        Timestamp transactionTimestamp = null;

        try {
            // Validate and parse transaction_datetime
            try {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime parsedDateTime = LocalDateTime.parse(transaction_datetime, inputFormatter);
                String formattedDateTime = parsedDateTime.format(outputFormatter);
                transactionTimestamp = Timestamp.valueOf(formattedDateTime);
            } catch (DateTimeParseException | IllegalArgumentException e) {
                throw new SQLException("Invalid datetime format. Must be yyyy-MM-dd HH:mm:ss.");
            }

            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            conn.setAutoCommit(false); // Start transaction

            stat = conn.prepareStatement("select * from account_internal where account_number = ?");
            stat.setString(1, source_account_number);
            rs = stat.executeQuery();
            if (rs.next()) {
                source_amount_cash_before = rs.getLong("amount_cash");
                source_available_balance_cash_before = rs.getLong("available_balance_cash");
                rs.close();
                stat.close();

                stat = conn.prepareStatement("select * from account_internal where account_number = ?");
                stat.setString(1, beneficiary_account_number);
                rs = stat.executeQuery();
                if (rs.next()) {
                    destination_amount_cash_before = rs.getLong("amount_cash");
                    destination_available_balance_cash_before = rs.getLong("available_balance_cash");
                    rs.close();
                    stat.close();

                    long transferAmount = Long.parseLong(amount);
                    source_amount_cash_after = source_amount_cash_before - transferAmount;
                    source_available_balance_cash_after = source_available_balance_cash_before - transferAmount;
                    destination_amount_cash_after = destination_amount_cash_before + transferAmount;
                    destination_available_balance_cash_after = destination_available_balance_cash_before + transferAmount;

                    stat = conn.prepareStatement(
                            "update account_internal set amount_cash = ?, available_balance_cash = ? where account_number = ?");
                    stat.setLong(1, source_amount_cash_after);
                    stat.setLong(2, source_available_balance_cash_after);
                    stat.setString(3, source_account_number);
                    stat.executeUpdate();
                    stat.close();

                    stat = conn.prepareStatement(
                            "update account_internal set amount_cash = ?, available_balance_cash = ? where account_number = ?");
                    stat.setLong(1, destination_amount_cash_after);
                    stat.setLong(2, destination_available_balance_cash_after);
                    stat.setString(3, beneficiary_account_number);
                    stat.executeUpdate();
                    stat.close();

                    stat = conn.prepareStatement("select * from internal_key");
                    rs = stat.executeQuery();
                    if (rs.next()) {
                        running_numbers = rs.getString("running_numbers");
                    }
                    rs.close();
                    stat.close();

                    String sequenceNumber = generateSequenceNumber(running_numbers);

                    stat = conn.prepareStatement(
                            "insert into transaction (reference_number, internal_key, source_account, dest_account, transaction_datetime, "
                            + "transaction_category, start_amount, currency, amount, end_amount, description, status_transaction, "
                            + "origin_amount, source_bank, dest_bank, transaction_type, dest_email, dest_bank_name, resp_code) "
                            + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); "
                            + "update internal_key set running_numbers = ?");
                    stat.setString(1, reference_number);
                    stat.setString(2, sequenceNumber);
                    stat.setString(3, source_account_number);
                    stat.setString(4, beneficiary_account_number);
                    stat.setTimestamp(5, transactionTimestamp); // Use transactionTimestamp
                    stat.setString(6, "D");
                    stat.setLong(7, source_amount_cash_before);
                    stat.setString(8, currency);
                    stat.setLong(9, transferAmount);
                    stat.setLong(10, source_amount_cash_after);
                    stat.setString(11, description);
                    stat.setString(12, "Success");
                    stat.setLong(13, transferAmount);
                    stat.setString(14, "147");
                    stat.setString(15, "147");
                    stat.setString(16, "TRF_INTRABANK");
                    stat.setString(17, beneficiary_email);
                    stat.setString(18, "Bank Muamalat");
                    stat.setString(19, "00");
                    stat.setString(20, sequenceNumber);
                    stat.executeUpdate();
                    stat.close();

                    conn.commit(); // Commit transaction

                    result.put("amount", amount);
                    result.put("currency", currency);
                    result.put("beneficiary_account_number", beneficiary_account_number);
                    result.put("source_account_number", source_account_number);
                    result.put("reference_number", reference_number);
                    result.put("transaction_datetime", transactionTimestamp.toString()); // Add transaction_datetime to result
                    result.put("resp_code", "00");
                    result.put("resp_desc", "successfully");
                } else {
                    conn.rollback(); // Rollback transaction
                    result.put("reference_number", reference_number);
                    result.put("resp_code", "03");
                    result.put("resp_desc", "destination account not found");
                }
            } else {
                result.put("reference_number", reference_number);
                result.put("resp_code", "02");
                result.put("resp_desc", "source account not found");
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            result.put("reference_number", reference_number);
            result.put("resp_code", "01");
            result.put("resp_desc", "Error : " + e.getMessage());
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap<String, Object> credit_transfer_interbank(
            String merchant_id,
            String source_account_number,
            String beneficiary_account_number,
            String beneficiary_name,
            String beneficiary_email,
            String beneficiary_address,
            String beneficiary_bank_code,
            String beneficiary_bank_name,
            String amount,
            String currency,
            String description,
            String transaction_datetime,
            String reference_number) {
        HashMap<String, Object> result = new HashMap<>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        long source_amount_cash_before;
        long source_available_balance_cash_before;
        long destination_amount_cash_before;

        long source_amount_cash_after;
        long source_available_balance_cash_after;
        long destination_amount_cash_after;

        String running_numbers = "";

        try {

            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            conn.setAutoCommit(false); // Start transaction
            stat = conn.prepareStatement("select * from account_internal where account_number = ?");
            stat.setString(1, source_account_number);
            rs = stat.executeQuery();
            if (rs.next()) {

                source_amount_cash_before = rs.getInt("amount_cash");
                source_available_balance_cash_before = Integer.parseInt(rs.getString("available_balance_cash"));

                stat = conn.prepareStatement("select * from account_eksternal where account_number = ?");
                stat.setString(1, beneficiary_account_number);
                rs = stat.executeQuery();
                if (rs.next()) {
                    destination_amount_cash_before = Long.parseLong(rs.getString("balance"));
                    source_amount_cash_after = source_amount_cash_before - Long.parseLong(amount);
                    source_available_balance_cash_after = source_available_balance_cash_before - Long.parseLong(amount);
                    destination_amount_cash_after = destination_amount_cash_before + Long.parseLong(amount);

                    stat.close();
                    rs.close();

                    stat = conn.prepareStatement("update account_internal set amount_cash=?, available_balance_cash=? where account_number = ?; update account_eksternal set balance=? where account_number = ?");
                    stat.setLong(1, source_amount_cash_after);
                    stat.setLong(2, source_available_balance_cash_after);
                    stat.setString(3, source_account_number);
                    stat.setLong(4, destination_amount_cash_after);
                    stat.setString(5, beneficiary_account_number);
                    stat.executeUpdate();

                    stat.close();

                    stat = conn.prepareStatement("select * from internal_key");
                    rs = stat.executeQuery();
                    if (rs.next()) {
                        running_numbers = rs.getString("running_numbers");
                    }

                    String sequenceNumber = generateSequenceNumber(running_numbers);

                    stat = conn.prepareStatement("insert into transaction (reference_number, internal_key, source_account, dest_account, transaction_category, start_amount, currency, amount, end_amount, description, status_transaction, origin_amount, source_bank, dest_bank, transaction_type, dest_name, dest_address, dest_email, dest_bank_name, resp_code) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); update internal_key set running_numbers = ?");
                    stat.setString(1, reference_number);
                    stat.setString(2, sequenceNumber);
                    stat.setString(3, source_account_number);
                    stat.setString(4, beneficiary_account_number);
                    stat.setString(5, "D");
                    stat.setLong(6, source_amount_cash_before);
                    stat.setString(7, currency);
                    stat.setLong(8, Long.parseLong(amount));
                    stat.setLong(9, source_amount_cash_after);
                    stat.setString(10, description);
                    stat.setString(11, "Success");
                    stat.setLong(12, Long.parseLong(amount));
                    stat.setString(13, "147");
                    stat.setString(14, beneficiary_bank_code);
                    stat.setString(15, "TRF_INTERBANK");
                    stat.setString(16, beneficiary_name);
                    stat.setString(17, beneficiary_address);
                    stat.setString(18, beneficiary_email);
                    stat.setString(19, beneficiary_bank_name);
                    stat.setString(20, "00");
                    stat.setString(21, sequenceNumber);
                    stat.executeUpdate();

                    stat.close();
                    conn.commit(); // Commit transaction

                    result.put("amount", amount);
                    result.put("currency", currency);
                    result.put("beneficiary_account_number", beneficiary_account_number);
                    result.put("beneficiary_bank_code", beneficiary_bank_code);
                    result.put("source_account_number", source_account_number);
                    result.put("trace_number", sequenceNumber);
                    result.put("reference_number", reference_number);
                    result.put("resp_code", "00");
                    result.put("resp_desc", "successfully");
                } else {
                    result.put("reference_number", reference_number);
                    result.put(FieldParameterMatapos.resp_code, "03");
                    result.put(FieldParameterMatapos.resp_desc, "destination account not found");
                }
            } else {
                result.put("reference_number", reference_number);
                result.put(FieldParameterMatapos.resp_code, "02");
                result.put(FieldParameterMatapos.resp_desc, "source account not found");
            }
        } catch (SQLException e) {
            result.put("reference_number", reference_number);
            result.put(FieldParameterMatapos.resp_code, "01");
            result.put(FieldParameterMatapos.resp_desc, "Error : " + e.getMessage());
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap<String, Object> credit_transfer_rtgs(
            String merchant_id,
            String source_account_number,
            String beneficiary_account_number,
            String beneficiary_account_name,
            String beneficiary_email,
            String beneficiary_address,
            String beneficiary_bank_code,
            String beneficiary_bank_name,
            String beneficiary_customer_residence,
            String beneficiary_customer_type,
            String amount,
            String currency,
            String beneficiary_poscode,
            String beneficiary_phone,
            String remark,
            String sender_customer_residence,
            String sender_customer_type,
            String sender_phone,
            String transaction_datetime,
            String reference_number) {
        HashMap<String, Object> result = new HashMap<>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        long source_amount_cash_before;
        long source_available_balance_cash_before;
        long destination_amount_cash_before;

        long source_amount_cash_after;
        long source_available_balance_cash_after;
        long destination_amount_cash_after;

        String running_numbers = "";

        Timestamp transactionTimestamp = null;

        String beneficiary_account_type = "";

        try {

            try {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime parsedDateTime = LocalDateTime.parse(transaction_datetime, inputFormatter);
                String formattedDateTime = parsedDateTime.format(outputFormatter);
                transactionTimestamp = Timestamp.valueOf(formattedDateTime);
            } catch (DateTimeParseException | IllegalArgumentException e) {
                throw new SQLException("Invalid datetime format. Must be yyyy-MM-dd HH:mm:ss.");
            }

            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            conn.setAutoCommit(false); // Start transaction
            stat = conn.prepareStatement("select * from account_internal where account_number = ?");
            stat.setString(1, source_account_number);
            rs = stat.executeQuery();
            if (rs.next()) {

                source_amount_cash_before = rs.getInt("amount_cash");
                source_available_balance_cash_before = Integer.parseInt(rs.getString("available_balance_cash"));

                stat = conn.prepareStatement("select * from account_eksternal where account_number = ?");
                stat.setString(1, beneficiary_account_number);
                rs = stat.executeQuery();
                if (rs.next()) {

                    beneficiary_account_type = rs.getString("account_type");
                    destination_amount_cash_before = Long.parseLong(rs.getString("balance"));
                    source_amount_cash_after = source_amount_cash_before - Long.parseLong(amount);
                    source_available_balance_cash_after = source_available_balance_cash_before - Long.parseLong(amount);
                    destination_amount_cash_after = destination_amount_cash_before + Long.parseLong(amount);

                    stat.close();
                    rs.close();

                    stat = conn.prepareStatement("update account_internal set amount_cash=?, available_balance_cash=? where account_number = ?; update account_eksternal set balance=? where account_number = ?");
                    stat.setLong(1, source_amount_cash_after);
                    stat.setLong(2, source_available_balance_cash_after);
                    stat.setString(3, source_account_number);
                    stat.setLong(4, destination_amount_cash_after);
                    stat.setString(5, beneficiary_account_number);
                    stat.executeUpdate();

                    stat.close();

                    stat = conn.prepareStatement("select * from internal_key");
                    rs = stat.executeQuery();
                    if (rs.next()) {
                        running_numbers = rs.getString("running_numbers");
                    }

                    String sequenceNumber = generateSequenceNumber(running_numbers);

                    stat = conn.prepareStatement("insert into transaction (reference_number, internal_key, source_account, dest_account, transaction_category, start_amount, currency, amount, end_amount, description, status_transaction, origin_amount, source_bank, dest_bank, transaction_type, dest_name, dest_address, dest_customer_residence, dest_customer_type, dest_email, dest_poscode, dest_phone, sender_customer_residence, sender_customer_type, sender_phone, dest_bank_name, transaction_datetime,resp_code) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); update internal_key set running_numbers = ?");
                    stat.setString(1, reference_number);
                    stat.setString(2, sequenceNumber);
                    stat.setString(3, source_account_number);
                    stat.setString(4, beneficiary_account_number);
                    stat.setString(5, "D");
                    stat.setLong(6, source_amount_cash_before);
                    stat.setString(7, currency);
                    stat.setLong(8, Long.parseLong(amount));
                    stat.setLong(9, source_amount_cash_after);
                    stat.setString(10, remark);
                    stat.setString(11, "Success");
                    stat.setLong(12, Long.parseLong(amount));
                    stat.setString(13, "147");
                    stat.setString(14, beneficiary_bank_code);
                    stat.setString(15, "RTGS");

                    stat.setString(16, beneficiary_account_name);
                    stat.setString(17, beneficiary_address);
                    stat.setString(18, beneficiary_customer_residence);
                    stat.setString(19, beneficiary_customer_type);
                    stat.setString(20, beneficiary_email);
                    stat.setString(21, beneficiary_poscode);
                    stat.setString(22, beneficiary_phone);
                    stat.setString(23, sender_customer_residence);
                    stat.setString(24, sender_customer_type);
                    stat.setString(25, sender_phone);
                    stat.setString(26, beneficiary_bank_name);
                    stat.setTimestamp(27, transactionTimestamp); // Use transactionTimestamp
                    stat.setString(28, "00");
                    stat.setString(29, sequenceNumber);
                    stat.executeUpdate();

                    stat.close();
                    conn.commit(); // Commit transaction

                    result.put("reference_number", reference_number);
                    result.put("amount", amount);
                    result.put("currency", currency);
                    result.put("beneficiary_account_name", beneficiary_account_name);
                    result.put("beneficiary_account_number", beneficiary_account_number);
                    result.put("beneficiary_account_type", beneficiary_account_type);
                    result.put("beneficiary_bank_code", beneficiary_bank_code);
                    result.put("source_account_number", source_account_number);
                    result.put("trace_number", sequenceNumber);
                    result.put("transaction_datetime", transactionTimestamp.toString()); // Add transaction_datetime to result
                    result.put("reference_number", reference_number);
                    result.put("resp_code", "00");
                    result.put("resp_desc", "successfully");
                } else {
                    result.put(FieldParameterMatapos.resp_code, "03");
                    result.put(FieldParameterMatapos.resp_desc, "destination account not found");
                }
            } else {
                result.put("reference_number", reference_number);
                result.put(FieldParameterMatapos.resp_code, "02");
                result.put(FieldParameterMatapos.resp_desc, "source account not found");
            }
        } catch (SQLException e) {
            result.put("reference_number", reference_number);
            result.put(FieldParameterMatapos.resp_code, "01");
            result.put(FieldParameterMatapos.resp_desc, "Error : " + e.getMessage());
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap<String, Object> credit_transfer_skn(
            String merchant_id,
            String source_account_number,
            String beneficiary_account_number,
            String beneficiary_account_name,
            String beneficiary_email,
            String beneficiary_address,
            String beneficiary_bank_code,
            String beneficiary_bank_name,
            String beneficiary_customer_residence,
            String beneficiary_customer_type,
            String amount,
            String currency,
            String beneficiary_poscode,
            String beneficiary_phone,
            String remark,
            String sender_customer_residence,
            String sender_customer_type,
            String sender_phone,
            String transaction_datetime,
            String reference_number) {
        HashMap<String, Object> result = new HashMap<>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        long source_amount_cash_before;
        long source_available_balance_cash_before;
        long destination_amount_cash_before;

        long source_amount_cash_after;
        long source_available_balance_cash_after;
        long destination_amount_cash_after;

        String running_numbers = "";

        Timestamp transactionTimestamp = null;

        String beneficiary_account_type = "";

        try {

            try {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime parsedDateTime = LocalDateTime.parse(transaction_datetime, inputFormatter);
                String formattedDateTime = parsedDateTime.format(outputFormatter);
                transactionTimestamp = Timestamp.valueOf(formattedDateTime);
            } catch (DateTimeParseException | IllegalArgumentException e) {
                throw new SQLException("Invalid datetime format. Must be yyyy-MM-dd HH:mm:ss.");
            }

            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            conn.setAutoCommit(false); // Start transaction
            stat = conn.prepareStatement("select * from account_internal where account_number = ?");
            stat.setString(1, source_account_number);
            rs = stat.executeQuery();
            if (rs.next()) {

                source_amount_cash_before = rs.getInt("amount_cash");
                source_available_balance_cash_before = Integer.parseInt(rs.getString("available_balance_cash"));

                stat = conn.prepareStatement("select * from account_eksternal where account_number = ?");
                stat.setString(1, beneficiary_account_number);
                rs = stat.executeQuery();
                if (rs.next()) {

                    beneficiary_account_type = rs.getString("account_type");
                    destination_amount_cash_before = Long.parseLong(rs.getString("balance"));
                    source_amount_cash_after = source_amount_cash_before - Long.parseLong(amount);
                    source_available_balance_cash_after = source_available_balance_cash_before - Long.parseLong(amount);
                    destination_amount_cash_after = destination_amount_cash_before + Long.parseLong(amount);

                    stat.close();
                    rs.close();

                    stat = conn.prepareStatement("update account_internal set amount_cash=?, available_balance_cash=? where account_number = ?; update account_eksternal set balance=? where account_number = ?");
                    stat.setLong(1, source_amount_cash_after);
                    stat.setLong(2, source_available_balance_cash_after);
                    stat.setString(3, source_account_number);
                    stat.setLong(4, destination_amount_cash_after);
                    stat.setString(5, beneficiary_account_number);
                    stat.executeUpdate();

                    stat.close();

                    stat = conn.prepareStatement("select * from internal_key");
                    rs = stat.executeQuery();
                    if (rs.next()) {
                        running_numbers = rs.getString("running_numbers");
                    }

                    String sequenceNumber = generateSequenceNumber(running_numbers);

                    stat = conn.prepareStatement("insert into transaction (reference_number, internal_key, source_account, dest_account, transaction_category, start_amount, currency, amount, end_amount, description, status_transaction, origin_amount, source_bank, dest_bank, transaction_type, dest_name, dest_address, dest_customer_residence, dest_customer_type, dest_email, dest_poscode, dest_phone, sender_customer_residence, sender_customer_type, sender_phone, dest_bank_name, transaction_datetime,resp_code) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?); update internal_key set running_numbers = ?");
                    stat.setString(1, reference_number);
                    stat.setString(2, sequenceNumber);
                    stat.setString(3, source_account_number);
                    stat.setString(4, beneficiary_account_number);
                    stat.setString(5, "D");
                    stat.setLong(6, source_amount_cash_before);
                    stat.setString(7, currency);
                    stat.setLong(8, Long.parseLong(amount));
                    stat.setLong(9, source_amount_cash_after);
                    stat.setString(10, remark);
                    stat.setString(11, "Success");
                    stat.setLong(12, Long.parseLong(amount));
                    stat.setString(13, "147");
                    stat.setString(14, beneficiary_bank_code);
                    stat.setString(15, "SKN");

                    stat.setString(16, beneficiary_account_name);
                    stat.setString(17, beneficiary_address);
                    stat.setString(18, beneficiary_customer_residence);
                    stat.setString(19, beneficiary_customer_type);
                    stat.setString(20, beneficiary_email);
                    stat.setString(21, beneficiary_poscode);
                    stat.setString(22, beneficiary_phone);
                    stat.setString(23, sender_customer_residence);
                    stat.setString(24, sender_customer_type);
                    stat.setString(25, sender_phone);
                    stat.setString(26, beneficiary_bank_name);
                    stat.setTimestamp(27, transactionTimestamp); // Use transactionTimestamp
                    stat.setString(28, "00");
                    stat.setString(29, sequenceNumber);
                    stat.executeUpdate();

                    stat.close();
                    conn.commit(); // Commit transaction

                    result.put("reference_number", reference_number);
                    result.put("amount", amount);
                    result.put("currency", currency);
                    result.put("beneficiary_account_name", beneficiary_account_name);
                    result.put("beneficiary_account_number", beneficiary_account_number);
                    result.put("beneficiary_account_type", beneficiary_account_type);
                    result.put("beneficiary_bank_code", beneficiary_bank_code);
                    result.put("source_account_number", source_account_number);
                    result.put("trace_number", sequenceNumber);
                    result.put("transaction_datetime", transactionTimestamp.toString()); // Add transaction_datetime to result
                    result.put("reference_number", reference_number);
                    result.put("resp_code", "00");
                    result.put("resp_desc", "successfully");
                } else {
                    result.put(FieldParameterMatapos.resp_code, "03");
                    result.put(FieldParameterMatapos.resp_desc, "destination account not found");
                }
            } else {
                result.put("reference_number", reference_number);
                result.put(FieldParameterMatapos.resp_code, "02");
                result.put(FieldParameterMatapos.resp_desc, "source account not found");
            }
        } catch (SQLException e) {
            result.put("reference_number", reference_number);
            result.put(FieldParameterMatapos.resp_code, "01");
            result.put(FieldParameterMatapos.resp_desc, "Error : " + e.getMessage());
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap transaction_inquiry(String merchant_id, String amount, String currency, String original_reference_number, String reference_number, String transaction_datetime) {
        HashMap result = null;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {
            result = new HashMap();
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select * from transaction where reference_number = ? and amount = ? and transaction_datetime = ?");
            stat.setString(1, original_reference_number);
            stat.setLong(2, Long.parseLong(amount));
            stat.setTimestamp(3, Formatdate.formatdate1(transaction_datetime));
            rs = stat.executeQuery();
            if (rs.next()) {

                result.put("amount", rs.getString("amount"));
                result.put("currency", rs.getString("currency"));
                result.put("transaction_datetime", transaction_datetime);
                result.put("original_transaction_datetime", rs.getString("transaction_datetime"));
                result.put("beneficiary_account_number", rs.getString("dest_account"));
                result.put("beneficiary_bank_code", rs.getString("dest_bank"));
                result.put("origin_resp_code", rs.getString("resp_code"));
                result.put("origin_resp_desc", rs.getString("status_transaction"));
                result.put("source_account_number", rs.getString("source_account"));
                result.put("transaction_id", rs.getString("internal_key"));
                result.put("original_reference_number", original_reference_number);
                result.put("reference_number", reference_number);
                result.put("resp_code", "00");
                result.put("resp_desc", "successfully");

            } else {
                result.put("reference_number", reference_number);
                result.put(FieldParameterMatapos.resp_code, "02");
                result.put(FieldParameterMatapos.resp_desc, "transaction not found");
            }

        } catch (SQLException e) {
            result.put("reference_number", reference_number);
            result.put(FieldParameterMatapos.resp_code, "01");
            result.put(FieldParameterMatapos.resp_desc, "Error : " + e.getMessage());
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap card_unbinding(String card_number, String merchant_id, String reference_number, String type) {
        HashMap result = null;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        long binding_id = 0;
        Timestamp waktu = Timestamp.valueOf(LocalDateTime.now());

        try {
            result = new HashMap();
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select a.customer_id, a.email, b.account_number, c.card_number from customer a \n"
                    + "left join account_internal b on a.customer_id = b.customer_id \n"
                    + "left join card c on b.account_number = c.account_number \n"
                    + "where c.card_number = ?");
            stat.setString(1, card_number);
            rs = stat.executeQuery();
            if (rs.next()) {

                stat.close();
                rs.close();

                stat = conn.prepareStatement("select * from card_binding where card_number = ? and merchant_id = ? and bind_status = ?");
                stat.setString(1, card_number);
                stat.setString(2, merchant_id);
                stat.setBoolean(3, true);
                rs = stat.executeQuery();
                if (rs.next()) {
                    binding_id = rs.getLong("binding_id");

                    stat.close();
                    rs.close();

                    stat = conn.prepareStatement("update card_binding set bind_status = ?, unbinding_datetime = ?, token_status = ? where card_number = ? and merchant_id = ? and binding_id =?");
                    stat.setBoolean(1, false);
                    stat.setTimestamp(2, waktu);
                    stat.setBoolean(3, false);
                    stat.setString(4, card_number);
                    stat.setString(5, merchant_id);
                    stat.setLong(6, binding_id);

                    stat.executeUpdate();
                    stat.close();

                    conn.commit();
                    result.put("card_number", card_number);
                    result.put("merchant_id", merchant_id);
                    result.put("reference_number", reference_number);
                    result.put("card_number", card_number);
                    result.put("transaction_datetime", waktu);
                    result.put("resp_code", "00");
                    result.put("resp_desc", "successfully");
                } else {
                    result.put("reference_number", reference_number);
                    result.put("resp_code", "03");
                    result.put("resp_desc", "card not registered");
                }
            } else {
                result.put("reference_number", reference_number);
                result.put(FieldParameterMatapos.resp_code, "02");
                result.put(FieldParameterMatapos.resp_desc, "card not found");
            }
        } catch (SQLException e) {
            result.put("reference_number", reference_number);
            result.put(FieldParameterMatapos.resp_code, "01");
            result.put(FieldParameterMatapos.resp_desc, "Error : " + e.getMessage());
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }

    public HashMap customer_validation(String msisdn, String pin, String reference_number) throws Exception {
        HashMap result = null;
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;
        try {

            String encryptedText = encrypt(pin);

            result = new HashMap();
            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            stat = conn.prepareStatement("select a.customer_id, a.name, a.phone_number, a.email, b.account_number, c.card_number, c.pin from customer a "
                    + "left join account_internal b on a.customer_id = b.customer_id "
                    + "left join card c on b.account_number = c.account_number "
                    + "where a.phone_number = ?");
            stat.setString(1, msisdn);
            rs = stat.executeQuery();
            if (rs.next()) {
                String pindb = rs.getString("pin");
                if (encryptedText.equals(pindb)) {
                    result.put("account_number", rs.getString("account_number"));
                    result.put("name", rs.getString("name"));
                    result.put("email", rs.getString("email"));
                    result.put("phone_number", rs.getString("phone_number"));
                    result.put("reference_number", reference_number);
                    result.put("status", "customer valid");
                    result.put("resp_code", "00");
                    result.put("resp_desc", "successfully");
                } else {
                    result.put("reference_number", reference_number);
                    result.put("resp_code", "03");
                    result.put("resp_desc", "pin not match");
                }
            } else {
                result.put("reference_number", reference_number);
                result.put(FieldParameterMatapos.resp_code, "02");
                result.put(FieldParameterMatapos.resp_desc, "not found");
            }
        } catch (SQLException e) {
            result.put("reference_number", reference_number);
            result.put(FieldParameterMatapos.resp_code, "01");
            result.put(FieldParameterMatapos.resp_desc, "Error : " + e.getMessage());
            System.out.println(e);
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }
    
    
    public HashMap<String, Object> direct_debit_transfer(String merchant_id, String source_card_token, String beneficiary_card_token, 
            String description, String amount, String currency, String transaction_datetime, String reference_number) {
        HashMap<String, Object> result = new HashMap<>();
        Connection conn = null;
        PreparedStatement stat = null;
        ResultSet rs = null;

        long source_amount_cash_before;
        long source_available_balance_cash_before;
        long destination_amount_cash_before;
        long destination_available_balance_cash_before;

        long source_amount_cash_after;
        long source_available_balance_cash_after;
        long destination_amount_cash_after;
        long destination_available_balance_cash_after;
        String running_numbers = "";
        Timestamp transactionTimestamp = null;

        try { 
            try {
                DateTimeFormatter inputFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
                DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                LocalDateTime parsedDateTime = LocalDateTime.parse(transaction_datetime, inputFormatter);
                String formattedDateTime = parsedDateTime.format(outputFormatter);
                transactionTimestamp = Timestamp.valueOf(formattedDateTime);
            } catch (DateTimeParseException | IllegalArgumentException e) {
                throw new SQLException("Invalid datetime format. Must be yyyy-MM-dd HH:mm:ss.");
            }

            conn = DatasourceEntryBackend.getInstance().getBackendDS().getConnection();
            conn.setAutoCommit(false); // Start transaction

            stat = conn.prepareStatement("select * from account_internal where account_number = ?");
            stat.setString(1, source_card_token);
            rs = stat.executeQuery();
            if (rs.next()) {
                source_amount_cash_before = rs.getLong("amount_cash");
                source_available_balance_cash_before = rs.getLong("available_balance_cash");
                rs.close();
                stat.close();

                stat = conn.prepareStatement("select * from account_internal where account_number = ?");
                stat.setString(1, beneficiary_card_token);
                rs = stat.executeQuery();
                if (rs.next()) {
                    destination_amount_cash_before = rs.getLong("amount_cash");
                    destination_available_balance_cash_before = rs.getLong("available_balance_cash");
                    rs.close();
                    stat.close();

                    long transferAmount = Long.parseLong(amount);
                    source_amount_cash_after = source_amount_cash_before - transferAmount;
                    source_available_balance_cash_after = source_available_balance_cash_before - transferAmount;
                    destination_amount_cash_after = destination_amount_cash_before + transferAmount;
                    destination_available_balance_cash_after = destination_available_balance_cash_before + transferAmount;

                    stat = conn.prepareStatement(
                            "update account_internal set amount_cash = ?, available_balance_cash = ? where account_number = ?");
                    stat.setLong(1, source_amount_cash_after);
                    stat.setLong(2, source_available_balance_cash_after);
                    stat.setString(3, source_card_token);
                    stat.executeUpdate();
                    stat.close();

                    stat = conn.prepareStatement(
                            "update account_internal set amount_cash = ?, available_balance_cash = ? where account_number = ?");
                    stat.setLong(1, destination_amount_cash_after);
                    stat.setLong(2, destination_available_balance_cash_after);
                    stat.setString(3, beneficiary_card_token);
                    stat.executeUpdate();
                    stat.close();

                    stat = conn.prepareStatement("select * from internal_key");
                    rs = stat.executeQuery();
                    if (rs.next()) {
                        running_numbers = rs.getString("running_numbers");
                    }
                    rs.close();
                    stat.close();

                    String sequenceNumber = generateSequenceNumber(running_numbers);

                    stat = conn.prepareStatement(
                            "insert into transaction (reference_number, internal_key, source_account, dest_account, transaction_datetime, "
                            + "transaction_category, start_amount, currency, amount, end_amount, description, status_transaction, "
                            + "origin_amount, source_bank, dest_bank, transaction_type, dest_email, dest_bank_name, resp_code) "
                            + "values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?); "
                            + "update internal_key set running_numbers = ?");
                    stat.setString(1, reference_number);
                    stat.setString(2, sequenceNumber);
                    stat.setString(3, source_card_token);
                    stat.setString(4, beneficiary_card_token);
                    stat.setTimestamp(5, transactionTimestamp); // Use transactionTimestamp
                    stat.setString(6, "D");
                    stat.setLong(7, source_amount_cash_before);
                    stat.setString(8, currency);
                    stat.setLong(9, transferAmount);
                    stat.setLong(10, source_amount_cash_after);
                    stat.setString(11, description);
                    stat.setString(12, "Success");
                    stat.setLong(13, transferAmount);
                    stat.setString(14, "147");
                    stat.setString(15, "147");
                    stat.setString(16, "TRF_INTRABANK");
//                    stat.setString(17, beneficiary_email);
                    stat.setString(18, "Bank Muamalat");
                    stat.setString(19, "00");
                    stat.setString(20, sequenceNumber);
                    stat.executeUpdate();
                    stat.close();

                    conn.commit(); // Commit transaction

                    result.put("amount", amount);
                    result.put("currency", currency);
                    result.put("beneficiary_account_number", beneficiary_card_token);
                    result.put("source_account_number", source_card_token);
                    result.put("reference_number", reference_number);
                    result.put("transaction_datetime", transactionTimestamp.toString()); // Add transaction_datetime to result
                    result.put("resp_code", "00");
                    result.put("resp_desc", "successfully");
                } else {
                    conn.rollback(); // Rollback transaction
                    result.put("reference_number", reference_number);
                    result.put("resp_code", "03");
                    result.put("resp_desc", "destination account not found");
                }
            } else {
                result.put("reference_number", reference_number);
                result.put("resp_code", "02");
                result.put("resp_desc", "source account not found");
            }
        } catch (SQLException e) {
            try {
                if (conn != null) {
                    conn.rollback();
                }
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            result.put("reference_number", reference_number);
            result.put("resp_code", "01");
            result.put("resp_desc", "Error : " + e.getMessage());
            e.printStackTrace();
        } finally {
            clearAllConnStatRS(conn, stat, rs);
        }
        return result;
    }
}
