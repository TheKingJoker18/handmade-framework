package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

public class FrontController extends HttpServlet {

    public void processRequest(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try {
            out.println("<h1>Code 200</h1>");
            out.println("<p>Vous avez entre avec succes dans ce site :) </p>");
            out.println("<p>Page URL: <b>" + req.getRequestURL() + "</b> </p>");
            
        } catch (Exception e) {
            out.println("<h1>Code 400</h1>");
            out.println("<p>An error has occurred: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }

    }
    
    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try {
            processRequest(req, res);
            out.println("<h2>Request method: GET</h2>");

        } catch (Exception e) {
            out.println("<h1>Code 400</h1>");
            out.println("<p>An error has occurred: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try {
            processRequest(req, res);
            out.println("<h2>Request method: POST</h2>");
            
        } catch (Exception e) {
            out.println("<h1>Code 400</h1>");
            out.println("<p>An error has occurred: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }
    }

}