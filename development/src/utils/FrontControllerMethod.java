package utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import javax.management.relation.RoleNotFoundException;
import javax.naming.AuthenticationException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import annotation.AnnotationController;
import annotation.Post;
import annotation.Url;
import controller.FrontController;
import exception.MyException;

public class FrontControllerMethod {

    public static void findMethodsAnnoted(Class<?> clazz, FrontController frontController) throws ServletException {
        HashMap<String, Mapping> methodList = frontController.getMethodList();
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method : methods) {
            if (method.isAnnotationPresent(Url.class)) {
                Url url_annotation = method.getAnnotation(Url.class);
                Post post_annotation = method.getAnnotation(Post.class);

                String verb = (post_annotation == null) ? "GET" : "POST";

                // Fetching the AnnotationController from the controller to concatenate it with the url of the method to separate the methods of each controller
                AnnotationController annotation_controller = clazz.getAnnotation(AnnotationController.class);
                String url_path = annotation_controller.name() + url_annotation.value();

                Mapping m = methodList.get(url_path);
                if (m == null) {
                    Mapping map = new Mapping(clazz.getName(), method.getName(), verb);
                    methodList.put(url_path, map);
                    // System.out.println("Method: " + method.getName() + ", Path: " + url_path); // Debug
                } else {
                    VerbAction verbAction = m.getVerbActionByVerb(verb);
                    if (verbAction == null) {
                        verbAction = new VerbAction(method.getName(), verb);
                        m.putVerbAction(verbAction);
                        methodList.put(url_path, m);
                    } else {
                        System.out.println("An URL of mapping with the verb \"" + verb + "\" must be unique, but \"" + url_annotation.value() + "\" is duplicated (referenced method: '" + method.getName() + "')");
                    }
                }
            }
        }
        frontController.setMethodList(methodList);
    }

    public static void initMethodList(FrontController frontController) throws ServletException {
        List<Class<?>> controllers = frontController.getControllers();
        if (controllers != null) {
            for (Class<?> controller : controllers) {
                System.out.println("Scanning controller: " + controller.getName()); // Debug
                findMethodsAnnoted(controller, frontController);
            }
        } else {
            System.out.println("No controllers found"); // Debug
        }
    }

    public static String printMethodList(FrontController frontController) {
        HashMap<String, Mapping> methodList = frontController.getMethodList();
        String print = "<table border=\"1\">";
        print += "<tr> <th>Path</th> <th>Class</th> <th>Verb</th> <th>Method</th> </tr>";
        for (String key : methodList.keySet()) {
            Mapping mapping = methodList.get(key);
            print += "<tr> <td>" + key + "</td> <td>" + mapping.getClassName() + "</td>";
            VerbAction[] ls_verbAction = mapping.getLs_verbAction().toArray(new VerbAction[0]);
            String verb_td = "<td>";
            String method_td = "<td>";
            for (VerbAction verbAction : ls_verbAction) {
                verb_td += "<br/> " + verbAction.getVerb() + "<br/>";
                method_td += "<br/> " + verbAction.getMethodName() + "<br/>";
            }
            verb_td += "</td>";
            method_td += "</td>";
            print += verb_td + method_td;
            print += "</tr>";
        }
        print += "</table>";
        return print;
    }

    public static String executeControllerMethod(Mapping mapping, HttpServletRequest request, HttpServletResponse response, String relativeURI, FrontController frontController) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, ServletException, IOException, MyException, AuthenticationException, NullPointerException, RoleNotFoundException {
        String print = "";
        response.setContentType("text/html");
        print = "<h1>Code 200</h1>";
        print += "<p>Vous avez entre avec succes dans ce site :) </p>";
        print += "<p>Page URL: <b>" + relativeURI + "</b> </p>";

        HashMap<String, Mapping> methodList = frontController.getMethodList();
        if (methodList != null) {
            print += printMethodList(frontController);
        } else {
            print += "methodList is null"; // Debug
        }

        if (mapping != null) {
            String method = request.getMethod();
            mapping.setAction(mapping.getVerbActionByVerb(method));
            if (!mapping.checkIfVerbExists(method)) {
                throw new MyException(500, "The VERB used by the user('" + method + "') hasn't been defined");
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
                throw new MyException(404, "The URL is not associated with an method... The actual url: " + request.getRequestURI());
            }
        }

        return print;
    }

}
