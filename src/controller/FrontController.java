package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;

import utils.*;

public class FrontController extends HttpServlet {

    private String controllerPackage;
    private ControllerScanner scanner;
    private List<Class<?>> controllers;

    public void init(ServletConfig config) throws ServletException {
        try {
            super.init(config);

            ServletContext context = config.getServletContext();
            controllerPackage = context.getInitParameter("base_package");

            this.scanner = new ControllerScanner();
            this.controllers = scanner.findControllers(controllerPackage);
        } 
        
        catch (Exception e) {
            e.printStackTrace();
        } 
    }

    public void processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException  { 
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try {
            out.println("<h1>Code 200</h1>");
            out.println("<p>Vous avez entre avec succes dans ce site :) </p>");
            out.println("<p>Page URL: <b>" + req.getRequestURL() + "</b> </p>");

            out.println("<hr/>");
            out.println("<h2> Listes des Controllers trouves: </h2>");
            for (Class<?> controller : this.controllers) {
                out.println("Found controller: " + controller.getName() + "<br>");
            }
            out.println("<hr/>");
        } 
        
        catch (Exception e) {
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