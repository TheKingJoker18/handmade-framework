package controller;

import java.io.*;
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

    public void initMethodList() {
        try {
            if (this.controllers != null) {
                for (Class<?> controller : this.controllers) {
                    System.out.println("Scanning controller: " + controller.getName()); // Debug
                    findMethodsAnnoted(controller);
                }
            } else {
                System.out.println("No controllers found"); // Debug
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void processRequest(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        res.setContentType("text/html");
        PrintWriter out = res.getWriter();

        try {
            // Get the context path and request URI
            String contextPath = req.getContextPath();
            String requestURI = req.getRequestURI();

            // Remove the context path from the request URI
            String relativeURI = requestURI.substring(contextPath.length());

            out.println("<h1>Code 200</h1>");
            out.println("<p>Vous avez entre avec succes dans ce site :) </p>");
            out.println("<p>Page URL: <b>" + relativeURI + "</b> </p>");

            if (methodList != null) {
                for (String key : methodList.keySet()) {
                    Mapping mapping = methodList.get(key);
                    out.println("Mapping - Path: " + key + ", Class: " + mapping.getClassName() + ", Method: " + mapping.getMethodName() + "<br>");
                }
            } else {
                System.out.println("methodList is null"); // Debug
            }

            Mapping mapping = methodList.get(relativeURI);
            if (mapping != null) {
                out.println("<hr/>");
                out.println("<h2> Listes des Controllers trouves: </h2>");
                out.println("<p>Class: " + mapping.getClassName() + "</p>");
                out.println("<p>Method: " + mapping.getMethodName() + "</p>");
            } else {
                out.println("<p>Aucune méthode associée</p>");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
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

    public void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
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

    public void findMethodsAnnoted(Class<?> clazz) {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Get.class)) {
                Get getAnnotation = method.getAnnotation(Get.class);
                Mapping map = new Mapping(method.getName(), clazz.getName());
                methodList.put(getAnnotation.value(), map);
                // System.out.println("Method: " + method.getName() + ", Path: " + getAnnotation.value()); // Debug
            }
        }
    }
}
