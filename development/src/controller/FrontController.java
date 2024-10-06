package controller;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import javax.servlet.*;
import javax.servlet.http.*;

import annotation.AnnotationController;
import annotation.Post;
import annotation.Url;
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

    public String executeControllerMethod(Mapping mapping, HttpServletRequest request, HttpServletResponse response, String relativeURI) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, ServletException, IOException {
        String print = "";
        response.setContentType("text/html");
        print = "<h1>Code 200</h1>";
        print += "<p>Vous avez entre avec succes dans ce site :) </p>";
        print += "<p>Page URL: <b>" + relativeURI + "</b> </p>";

        if (methodList != null) {
            print += printMethodList();
        } else {
            print += "methodList is null"; // Debug
        }

        if (mapping != null) {
            String method = request.getMethod();
            if (!method.equalsIgnoreCase(mapping.getVerb())) {
                throw new ServletException("The method used by the user('" + method + "') and the VERB('" + mapping.getVerb() + "') doesn't match");
            }

            if (mapping.checkIfMethodHaveRestapiAnnotation(request)) {
                response.setContentType("text/json");
                print = mapping.execute_json(request, response);
    
            } else {
                print += mapping.execute_html(request, response);

                if (method.equalsIgnoreCase("GET")) {
                    print += "<h2>Request method: GET</h2>";
                } else if (method.equalsIgnoreCase("POST")) {
                    print += "<h2>Request method: POST</h2>";
                }
            }

        } else {
            if (relativeURI.compareTo("/") != 0) {
                throw new ServletException("The URL is not associated with an method");
            }
        }

        return print;
    }

    public String processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, ClassNotFoundException {
        String print = "";

        // Get the context path and request URI
        String contextPath = request.getContextPath();
        String requestURI = request.getRequestURI();

        // Remove the context path from the request URI
        String relativeURI = requestURI.substring(contextPath.length());

        Mapping mapping = methodList.get(relativeURI);
        print = executeControllerMethod(mapping, request, response, relativeURI);

        return print;
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        PrintWriter out = res.getWriter();
        String print = "";

        try {
            print = processRequest(req, res);
            out.println(print);

        } catch (Exception e) {
            res.setContentType("text/html");
            print = "<h1>Code 400</h1>";
            print += "<hr/>";
            print += "<p>There was an error processing the request: <b>ETU002556 <br/> " + e.getMessage() + " </b> <p>";
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
            res.setContentType("text/html");
            print = "<h1>Code 400</h1>";
            print += "<hr/>";
            print += "<p>There was an error processing the request: <b>ETU002556 <br/> " + e.getMessage() + " </b> <p>";
            e.printStackTrace();
            out.println(print);
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
            print += "Mapping - Path: \"" + key + "\", Class: \"" + mapping.getClassName() + "\", Method: \"" + mapping.getMethodName() + "\", VERB: \"" + mapping.getVerb() + "\"<br>";
        }
        return print;
    }

    public void findMethodsAnnoted(Class<?> clazz) throws ServletException {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Url.class)) {
                Url url_annotation = method.getAnnotation(Url.class);
                Post post_annotation = method.getAnnotation(Post.class);
                String verb = (post_annotation == null) ? "GET" : "POST";
                Mapping map = new Mapping(method.getName(), clazz.getName(), verb);
                Mapping m = methodList.get(url_annotation.value());
                if (m == null) {
                    methodList.put(url_annotation.value(), map);
                    // System.out.println("Method: " + method.getName() + ", Path: " + url_annotation.value()); // Debug
                } else {
                    throw new ServletException("An URL of mapping must be unique, but \"" + url_annotation.value() + "\" is duplicated");
                }
            }
        }
    }

}
