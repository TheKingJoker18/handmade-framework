package utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import annotation.ModelAttribute;
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

    public void setSimpleParam(int i, Parameter[] parameters, Object[] values, HttpServletRequest request) throws NullPointerException {
        Param param = parameters[i].getAnnotation(Param.class);
        String paramName = null;
        if (param == null)                                      paramName = parameters[i].getName();
        else                                                    paramName = param.name();
        String paramValue = request.getParameter(paramName);
        if (paramValue == null) {
            throw new NullPointerException("There is an undefined parameter (parameter name : " + paramName + ")");
        } else {
            if (parameters[i].getType() == int.class)           values[i] = Integer.valueOf(paramValue);
            else if (parameters[i].getType() == double.class)   values[i] = Double.valueOf(paramValue);
            else if (parameters[i].getType() == float.class)    values[i] = Float.valueOf(paramValue);
            else                                                values[i] = (String) paramValue;
        }
    }

    public void setModelParam(int i, Parameter[] parameters, Object[] values, HttpServletRequest request) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        ModelAttribute attribute = parameters[i].getAnnotation(ModelAttribute.class);
        String modelName = null;
        if (attribute == null)                                  modelName = parameters[i].getName();
        else                                                    modelName = attribute.name();
        Object model = Reflect.invokeEmptyConstructor(parameters[i].getType().getName());
        String[] fieldNames = Reflect.getFieldsNames(model);
        Field[] fields = model.getClass().getDeclaredFields();
        for (int j = 0; j < fieldNames.length; j++) {
            String inputName = modelName + "." + fieldNames[j];
            String fieldValue = request.getParameter(inputName);
            if (fieldValue == null) {
                throw new NullPointerException("There is an undefined field (field name : " + inputName + ")");
            } else {
                if (fields[j].getType() == int.class)           Reflect.invokeSetterMethod(model, fieldNames[j], int.class, Integer.valueOf(fieldValue));
                else if (fields[j].getType() == double.class)   Reflect.invokeSetterMethod(model, fieldNames[j], double.class, Double.valueOf(fieldValue));
                else if (fields[j].getType() == float.class)    Reflect.invokeSetterMethod(model, fieldNames[j], float.class, Float.valueOf(fieldValue));
                else                                            Reflect.invokeSetterMethod(model, fieldNames[j], String.class, fieldValue);
            }
        }
        values[i] = model;
    }

    public void configParam(Parameter[] parameters, Object[] values, HttpServletRequest request) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getType() == int.class || parameters[i].getType() == double.class || parameters[i].getType() == String.class) {
                setSimpleParam(i, parameters, values, request);
            } else {
                setModelParam(i, parameters, values, request);
            }
        }
    }

    public Object invokeMethod(HttpServletRequest request) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        Method method = Reflect.getMethodByName(Reflect.invokeEmptyConstructor(this.getClassName()), this.getMethodName());
        Parameter[] parameters = method.getParameters();
        if (parameters.length > 0) {
            Object[] values = new Object[parameters.length];
            configParam(parameters, values, request);
            return Reflect.invokeMethod(Reflect.invokeEmptyConstructor(this.getClassName()), this.getMethodName(), values);
        }
        return Reflect.invokeMethod(Reflect.invokeEmptyConstructor(this.getClassName()), this.getMethodName(), null);
    }

    public String execute(HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, ServletException, IOException {
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
