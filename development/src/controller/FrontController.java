package controller;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;

import annotation.AnnotationController;
import annotation.Get;
import utils.*;

@AnnotationController(name = "big_controller")
public class FrontController extends HttpServlet {

    private String controllerPackage;
    private ControllerScanner scanner;
    private List<Class<?>> controllers;
    private HashMap<String, Mapping> methodList;
    
    public void init(ServletConfig config) throws ServletException {
        try {
            super.init(config);

            // Récupérer le paramètre d'initialisation depuis ServletConfig
            controllerPackage = config.getInitParameter("base_package");

            if (controllerPackage == null) {
                throw new ServletException("Base package is not specified in web.xml");
            }

            // System.out.println("Controller package: " + controllerPackage); // Debug

            this.scanner = new ControllerScanner();
            this.controllers = scanner.findControllers(controllerPackage);

            // System.out.println("Found controllers: " + controllers); // Debug

            this.methodList = new HashMap<>();
            initMethodList();

        } catch (Exception e) {
            e.printStackTrace();
            throw new ServletException("Initialization failed", e);
        }
    }

    public String processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, ClassNotFoundException {
        // Get the context path and request URI
        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();

        // Remove the context path from the request URI
        String relativeURI = requestURI.substring(contextPath.length());

        String print = "<h1>Code 200</h1>";
        print += "<p>Vous avez entre avec succes dans ce site :) </p>";
        print += "<p>Page URL: <b>" + relativeURI + "</b> </p>";

        if (methodList != null) {
            print += printMethodList();
        } else {
            print += "methodList is null"; // Debug
        }

        Mapping mapping = methodList.get(relativeURI);
        if (mapping != null) {
            print += mapping.execute(request, response);

        } else {
            if (relativeURI.compareTo("/") != 0) {
                throw new ServletException("The URL is not associated with an method");
            }
        }
        return print;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        String print = "";

        try {
            print = processRequest(req, res);
            print += "<h2>Request method: GET</h2>";
            out.println(print);

        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();
        String print = "";

        try {
            print = processRequest(req, res);
            print += "<h2>Request method: POST</h2>";
            out.println(print);
            
        } catch (Exception e) {
            throw new ServletException(e);
        }
    }

    public void initMethodList() throws ServletException {
        if (this.controllers != null) {
            for (Class<?> controller : this.controllers) {
                System.out.println("Scanning controller: " + controller.getName()); // Debug
                findMethodsAnnoted(controller);
            }
        } else {
            System.out.println("No controllers found"); // Debug
        }
    }

    public String printMethodList() {
        String print = "";
        for (String key : methodList.keySet()) {
            Mapping mapping = methodList.get(key);
            print += "Mapping - Path: \"" + key + "\", Class: \"" + mapping.getClassName() + "\", Method: \"" + mapping.getMethodName() + "\"<br>";
        }
        return print;
    }

    public void findMethodsAnnoted(Class<?> clazz) throws ServletException {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Get.class)) {
                Get getAnnotation = method.getAnnotation(Get.class);
                Mapping map = new Mapping(method.getName(), clazz.getName());
                Mapping m = methodList.get(getAnnotation.value());
                if (m == null) {
                    methodList.put(getAnnotation.value(), map);
                    // System.out.println("Method: " + method.getName() + ", Path: " + getAnnotation.value()); // Debug
                } else {
                    throw new ServletException("An URL of mapping must be unique, but \"" + getAnnotation.value() + "\" is duplicated");
                }
            }
        }
    }

}
