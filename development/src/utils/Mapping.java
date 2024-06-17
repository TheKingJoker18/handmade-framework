package utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import annotation.Param;
import reflect.Reflect;

public class Mapping {

    private String methodName;
    private String className;

    public Mapping() {
    }

    public Mapping(String methodName, String className) {
        this.methodName = methodName;
        this.className = className;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    @Override
    public String toString() {
        return "Mapping{" +
                "methodName='" + methodName + '\'' +
                ", className='" + className + '\'' +
                '}';
    }

    public Object invokeMethod(HttpServletRequest request) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, ClassNotFoundException {
        Method method = Reflect.getMethodByName(Reflect.invokeEmptyConstructor(this.getClassName()), this.getMethodName());
        Parameter[] parameters = method.getParameters();
        if (parameters.length > 0) {
            Object[] values = new Object[parameters.length];
            for (int i = 0; i < parameters.length; i++) {
                Param param = parameters[i].getAnnotation(Param.class);
                if (param == null) {
                    throw new NullPointerException("There is no annotation @Param on this parameter");
                } else {
                    String paramName = param.name();
                    if (parameters[i].getType() == int.class)           values[i] = Integer.valueOf(request.getParameter(paramName));
                    else if (parameters[i].getType() == double.class)   values[i] = Double.valueOf(request.getParameter(paramName));
                    else if (parameters[i].getType() == float.class)    values[i] = Float.valueOf(request.getParameter(paramName));
                    else                                                values[i] = (String) request.getParameter(paramName);
                }
            }
            return Reflect.invokeMethod(Reflect.invokeEmptyConstructor(this.getClassName()), this.getMethodName(), values);
        }
        return Reflect.invokeMethod(Reflect.invokeEmptyConstructor(this.getClassName()), this.getMethodName(), null);
    }

    public String execute(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, ClassNotFoundException, ServletException, IOException {
        String print = "";
        print += "<hr/>";
        print += "<h2> Listes des Controllers trouves: </h2>";
        print += "<p>Class: " + this.getClassName() + "</p>";
        print += "<p>Method: " + this.getMethodName() + "</p>";
        Object result = this.invokeMethod(request);
        print += "<p>---------------------------------------------------------------</p>";
        if (result instanceof String) {
            print += "<p>Result: " + (String) result + "</p>";
                
        } else if (result instanceof ModelView) {
            ModelView mv = (ModelView)result;
            mv.prepareModelView(request, response);

        } else {
            throw new ServletException("The returned object Class is not known");
        }
        print += "<p>---------------------------------------------------------------</p>";
        return print;
    }

}
