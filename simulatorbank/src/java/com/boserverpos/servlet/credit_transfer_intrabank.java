/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.boserverpos.servlet;

import com.bo.function.JsonProcess;
import com.bo.function.MessageProcess;
import com.bo.function.impl.MessageProcessImpl;
import com.bo.parameter.RuleNameParameter;
import com.bopro.process.credittransferintrabank;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *
 * @author suhan
 */
@WebServlet(name = "credit_transfer_intrabank", urlPatterns = {"/api/transaction/credit_transfer_intrabank"})
public class credit_transfer_intrabank extends HttpServlet {

    private MessageProcess mp = new MessageProcessImpl();
    private static Logger log = Logger.getLogger(credit_transfer_intrabank.class);

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BufferedReader in = null;
        HashMap input = null;
        String inputString = "";
        String line = "";
        try {
            in = new BufferedReader(new InputStreamReader(request.getInputStream()));
            input = new HashMap();
            while ((line = in.readLine()) != null) {
                inputString += line;
            }

            input = JsonProcess.decodeJson(inputString);

            log.info("\n" + "request message : " + input + " \n");
            System.out.println("\n" + "request message : " + input + " \n");

            input = new credittransferintrabank().process(input);

        } catch (IOException e) {
            input.put(RuleNameParameter.resp_code, "0030");
            input.put(RuleNameParameter.resp_desc, "Error message request");
        }

        response.getOutputStream().write(JsonProcess.generateJson(input).getBytes());
        response.getOutputStream().flush();
        System.out.println(
                "\n" + "response FROM Backend process message TO Web : " + JsonProcess.generateJson(input) + " \n");
        log.info(
                "\n" + "response FROM Backend process message TO Web : " + JsonProcess.generateJson(input) + " \n");
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
