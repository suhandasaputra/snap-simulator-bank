/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.boserverpos.servlet;

import com.bopro.singleton.DatasourceEntryBackend;
import java.io.IOException;
import java.io.PrintWriter;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import org.apache.log4j.Logger;

/**
 *
 * author MATAJARI MITRA SOLUSI
 */
public class DataSourceLookup extends HttpServlet {

    private static Logger log = Logger.getLogger(DataSourceLookup.class);

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
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();
        try {
            /* TODO output your page here
             out.println("<html>");
             out.println("<head>");
             out.println("<title>Servlet DataSourceLookup</title>");
             out.println("</head>");
             out.println("<body>");
             out.println("<h1>Servlet DataSourceLookup at " + request.getContextPath () + "</h1>");
             out.println("</body>");
             out.println("</html>");
             */
        } finally {
            out.close();
        }
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

    public DataSourceLookup() {
        super();
        System.out.println("=-=-=-=-=-=-=-=-=-=-= DatasourceLookup started =-=-=-=-=-=-=-=-=-=-=");
        log.info("=-=-=-=-=-=-=-=-=-=-= DatasourceLookup started =-=-=-=-=-=-=-=-=-=-=");
        DatasourceEntryBackend.getInstance().setBackendDS(getDataSource("jdbc/simulator_bank"));
    }

    private DataSource getDataSource(String dsName) {
        InitialContext ctx = null;
        DataSource datasource = null;
        try {
            ctx = new InitialContext();
            log.debug("env" + ctx.getEnvironment());
        } catch (NamingException e1) {
            log.error(e1.getMessage());
        }
        if (datasource == null) {
            try {
                log.debug("get data source from java:comp/env/" + dsName);
                datasource = (DataSource) ctx.lookup("java:comp/env/" + dsName);
            } catch (Exception e) {
//                log.error(e.getMessage());
            }
        }
        if (datasource == null) {
            try {
                log.debug("get data source from java:/" + dsName);
                datasource = (DataSource) ctx.lookup("java:/" + dsName);
            } catch (Exception e) {
//                log.error(e.getMessage());
            }
        }
        if (datasource == null) {
            try {
                datasource = (DataSource) ctx.lookup(dsName);
            } catch (Exception e) {
                log.error(e.getMessage());
            }
        }
//        log.info("datasource " + dsName + " : " + datasource);
        return datasource;
    }
}
