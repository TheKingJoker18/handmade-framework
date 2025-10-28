package controller;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;

import javax.management.relation.RoleNotFoundException;
import javax.naming.AuthenticationException;
import javax.servlet.*;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.*;

import annotation.AnnotationController;
import exception.MyException;
import utils.*;

@MultipartConfig
@AnnotationController(name = "big_controller")
public class FrontController extends HttpServlet {
    private String controllerPackage;
    private String base_url;
    private ControllerScanner scanner;
    private List<Class<?>> controllers;
    private HashMap<String, Mapping> methodList;

    public String getControllerPackage() {
        return this.controllerPackage;
    }
    public void setControllerPackage(String controllerPackage) {
        this.controllerPackage = controllerPackage;
    }
    
    public String getBase_url() {
        return this.base_url;
    }
    public void setBase_url(String base_url) {
        this.base_url = base_url;
    }

    public ControllerScanner getScanner() {
        return this.scanner;
    }
    public void setScanner(ControllerScanner scanner) {
        this.scanner = scanner;
    }

    public List<Class<?>> getControllers() {
        return this.controllers;
    }
    public void setControllers(List<Class<?>> controllers) {
        this.controllers = controllers;
    }

    public HashMap<String, Mapping> getMethodList() {
        return this.methodList;
    }
    public void setMethodList(HashMap<String, Mapping> methodList) {
        this.methodList = methodList;
    }

    public FrontController() {}
    
    public void init(ServletConfig config) throws ServletException {
        try {
            super.init(config);

            // Récupérer le paramètre d'initialisation de controllerPackage depuis ServletConfig
            this.controllerPackage = config.getInitParameter("base_package");

            if (this.controllerPackage == null) {
                throw new ServletException("Base package is not specified in web.xml");
            }

            // System.out.println("Controller package: " + this.controllerPackage); // Debug

            // Récupérer le paramètre d'initialisation de base_url depuis ServletConfig
            this.base_url = config.getInitParameter("base_url");

            if (this.base_url == null) {
                throw new ServletException("Base url is not specified in web.xml");
            }

            // System.out.println("Base url: " + this.controllerPackage); // Debug

            this.scanner = new ControllerScanner();
            this.controllers = scanner.findControllers(this.controllerPackage);

            // System.out.println("Found controllers: " + controllers); // Debug

            this.methodList = new HashMap<>();
            FrontControllerMethod.initMethodList(this);

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Initialization failed", e);
        }
    }

    public String processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, ClassNotFoundException, MyException, AuthenticationException, NullPointerException, RoleNotFoundException {
        String print = "";

        // Get the context path and request URI
        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();

        // Remove the context path from the request URI
        String relativeURI = requestURI.substring(contextPath.length());

        Mapping mapping = methodList.get(relativeURI);
        print = FrontControllerMethod.executeControllerMethod(mapping, request, response, relativeURI, this);

        return print;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        String print = "";

        try {
            print = processRequest(req, res);
            out.println(print);

        } catch (Exception e) {
            int code = (e instanceof MyException) ? ((MyException)e).getErrorCode() : 400;
            res.setContentType("text/html");
            print = "<h1>Code " + code + "</h1>";
            print += "<hr/>";
            print += "<p>There was an error processing the request: <b>ETU002556 <br/> " + e.getMessage() + " </b> <p>";
            print += "<p> <a href=\"" + this.getBase_url() + "\"><button>Go back to Home Page</button></a> </p>";
            e.printStackTrace();
            out.println(print);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        PrintWriter out = res.getWriter();
        String print = "";

        try {
            print = processRequest(req, res);
            out.println(print);
            
        } catch (Exception e) {
            int code = (e instanceof MyException) ? ((MyException)e).getErrorCode() : 400;
            res.setContentType("text/html");
            print = "<h1>Code " + code + "</h1>";
            print += "<hr/>";
            print += "<p>There was an error processing the request: <b>ETU002556 <br/> " + e.getMessage() + " </b> <p>";
            print += "<p> <a href=\"" + this.getBase_url() + "\"><button>Go back to Home Page</button></a> </p>";
            e.printStackTrace();
            out.println(print);
        }
    }

}
