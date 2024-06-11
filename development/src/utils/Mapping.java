package utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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

    public Object invokeMethod() throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, ClassNotFoundException {
        return Reflect.invokeMethod(Reflect.invokeEmptyConstructor(this.getClassName()), this.getMethodName(), null);
    }

    public String execute(HttpServletRequest request, HttpServletResponse response) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, InstantiationException, ClassNotFoundException, ServletException, IOException {
        String print = "";
        print += "<hr/>";
        print += "<h2> Listes des Controllers trouves: </h2>";
        print += "<p>Class: " + this.getClassName() + "</p>";
        print += "<p>Method: " + this.getMethodName() + "</p>";
        Object result = this.invokeMethod();
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
