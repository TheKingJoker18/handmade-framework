package utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import annotation.ModelAttribute;
import annotation.Param;
import annotation.Restapi;
import reflect.Reflect;

public class Mapping {
    private String className;
    private Set<VerbAction> ls_verbAction;
    private VerbAction action;

    public String getClassName() {
        return className;
    }
    public void setClassName(String className) {
        this.className = className;
    }

    public Set<VerbAction> getLs_verbAction() {
        return this.ls_verbAction;
    }
    public void setLs_verbAction(Set<VerbAction> ls_verbAction) {
        this.ls_verbAction = ls_verbAction;
    }
    public void putVerbAction(VerbAction verbAction) {
        Set<VerbAction> ls_verbAction = this.getLs_verbAction();
        ls_verbAction.add(verbAction);
        this.setLs_verbAction(ls_verbAction);
    }

    public VerbAction getAction() {
        return this.action;
    }
    public void setAction(VerbAction action) {
        this.action = action;
    }

    public Mapping() {}
    public Mapping(String className, Set<VerbAction> ls_verbAction) {
        this.setClassName(className);
        this.setLs_verbAction(ls_verbAction);
    }
    public Mapping(String className, String methodName, String verb) {
        this.setClassName(className);
        Set<VerbAction> ls_verbAction = new HashSet<VerbAction>();
        ls_verbAction.add(new VerbAction(methodName, verb));
        this.setLs_verbAction(ls_verbAction);
    }

    @Override
    public String toString() {
        String string = "Mapping{"; 
        string += ", className='" + className + '\'';
        for (VerbAction verbAction : this.getLs_verbAction()) {
            string += verbAction.toString();
        }
        string += '}';
        return string;
    }

    public boolean checkIfVerbExists(String verb) {
        for (VerbAction verbAction : this.getLs_verbAction()) {
            if (verbAction.getVerb().equalsIgnoreCase(verb)) return true;
        }
        return false;
    }

    public VerbAction getVerbActionByVerb(String verb) {
        for (VerbAction verbAction : this.getLs_verbAction()) {
            if (verbAction.getVerb().equalsIgnoreCase(verb)) return verbAction;
        }
        return null;
    }

    public void setSimpleParam(Object controller, Method method, int i, Parameter[] parameters, Object[] values, HttpServletRequest request) throws NullPointerException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException, IOException, ServletException {
        Param param = parameters[i].getAnnotation(Param.class);
        String paramName = null;
        if (parameters[i].getType() != MySession.class && param == null)    throw new IllegalArgumentException("There is no @Param annotation on the parameter (parameter place : no." + (i + 1) + " ) ");
        else if (parameters[i].getType() != MySession.class)                paramName = param.name();
        String paramValue = request.getParameter(paramName);
        if (parameters[i].getType() != MySession.class && parameters[i].getType() != FileUpload.class && paramValue == null) {
            throw new NullPointerException("There is an undefined parameter (parameter name : " + paramName + ")");
        } else {
            if (parameters[i].getType() == int.class)                       values[i] = Integer.valueOf(paramValue);
            else if (parameters[i].getType() == double.class)               values[i] = Double.valueOf(paramValue);
            else if (parameters[i].getType() == float.class)                values[i] = Float.valueOf(paramValue);
            else if (parameters[i].getType() == java.sql.Date.class)        values[i] = java.sql.Date.valueOf(paramValue);
            else if (parameters[i].getType() == String.class)               values[i] = (String) paramValue;
            else if (parameters[i].getType() == FileUpload.class) {
                values[i] = FileUpload.getFileUploadedfromFilePart(request.getPart(paramName));

            } else if (parameters[i].getType() == MySession.class) {
                if (Reflect.getFieldValueByType(controller, MySession.class) != null) {
                    values[i] = (MySession) Reflect.getFieldValueByType(controller, MySession.class);
                    System.out.println("Field Utilisation...");
                } else {
                    values[i] = new MySession(request.getSession());
                    System.out.println("Parameter Utilisation...");
                }
            }
            else                                                            throw new IllegalStateException("The type " + parameters[i].getType().getSimpleName() + " of the Attribute no." + (i + 1) + " " + paramName + " of the Method " + method.getName() + " of the Controller " + controller.getClass().getSimpleName() + " is not supported");
        }
    }

    public void setModelParam(Object controller, Method method, int i, Parameter[] parameters, Object[] values, HttpServletRequest request) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        ModelAttribute attribute = parameters[i].getAnnotation(ModelAttribute.class);
        String modelName = null;
        if (attribute == null)                                              throw new IllegalArgumentException("There is no @ModelAttribute annotation on the model parmeter (parameter place : no." + (i + 1) + " )");
        else                                                                modelName = attribute.name();
        Object model = Reflect.invokeEmptyConstructor(parameters[i].getType().getName());
        String[] fieldNames = Reflect.getFieldsNames(model);
        Field[] fields = model.getClass().getDeclaredFields();
        for (int j = 0; j < fieldNames.length; j++) {
            String inputName = modelName + "." + fieldNames[j];

            String fieldValue = request.getParameter(inputName);
            
            if (fieldValue == null) {
                throw new NullPointerException("There is an undefined field (field name : " + inputName + ")");
            } else {
                // Check the value of the field according to its annotation(s)
                String error_message = Validator.check(fields[j], fieldValue);
                if (error_message != null) {
                    throw new IllegalArgumentException("Invalid value for field " + inputName + " : " + error_message);
                }

                if (fields[j].getType() == int.class)                       Reflect.invokeSetterMethod(model, fieldNames[j], int.class, Integer.valueOf(fieldValue));
                else if (fields[j].getType() == double.class)               Reflect.invokeSetterMethod(model, fieldNames[j], double.class, Double.valueOf(fieldValue));
                else if (fields[j].getType() == float.class)                Reflect.invokeSetterMethod(model, fieldNames[j], float.class, Float.valueOf(fieldValue));
                else if (fields[j].getType() == java.sql.Date.class)        Reflect.invokeSetterMethod(model, fieldNames[j], java.sql.Date.class, java.sql.Date.valueOf(fieldValue));
                else if (fields[j].getType() == String.class)               Reflect.invokeSetterMethod(model, fieldNames[j], String.class, fieldValue);
                else                                                        throw new IllegalStateException("The type " + fields[j].getType().getSimpleName() + " of the Field no." + (j + 1) + " " + fieldNames[j] + " of the Class " + parameters[i].getType().getSimpleName() + " of the Attribute no." + (i + 1) + " " + modelName + " of the Method " + method.getName() + " of the Controller " + controller.getClass().getSimpleName() + " is not supported");
            }
        }
        values[i] = model;
    }

    public void configParam(Object controller, Method method, Parameter[] parameters, Object[] values, HttpServletRequest request) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, NullPointerException, IOException, ServletException {
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getType() == int.class || parameters[i].getType() == double.class || parameters[i].getType() == String.class || parameters[i].getType() == MySession.class || parameters[i].getType() == FileUpload.class) {
                setSimpleParam(controller, method, i, parameters, values, request);
            } else {
                setModelParam(controller, method, i, parameters, values, request);
            }
        }
    }

    public Object invokeMethod(HttpServletRequest request) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, NullPointerException, IOException, ServletException {
        Object controller = Reflect.invokeControllerConstructor(this.getClassName(), request);
        Method method = Reflect.getMethodByName(controller, action.getMethodName());
        Parameter[] parameters = method.getParameters();
        if (parameters.length > 0) {
            Object[] values = new Object[parameters.length];
            configParam(controller, method, parameters, values, request);
            return Reflect.invokeMethod(controller, action.getMethodName(), values);
        }
        return Reflect.invokeMethod(controller, action.getMethodName(), null);
    }

    public boolean checkIfMethodHaveRestapiAnnotation(HttpServletRequest request) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        Object controller = Reflect.invokeControllerConstructor(this.getClassName(), request);
        Method method = Reflect.getMethodByName(controller, action.getMethodName());
        if (method.isAnnotationPresent(Restapi.class)) {
            return true;
        }
        return false;
    }

    public String execute_html(HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, ServletException, IOException {
        String print = "";
        print += "<hr/>";
        print += "<h2> Listes des Controllers trouves: </h2>";
        print += "<p>Class: " + this.getClassName() + "</p>";
        print += "<p>Method: " + action.getMethodName() + "</p>";
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

    public String execute_json(HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, ServletException, NullPointerException, IOException {
        String json = "data: ";
        Object result = this.invokeMethod(request);
        if (result instanceof String) {
            json += "{\"string\":\"" + (String) result + "\"}";
                
        } else if (result instanceof ModelView) {
            ModelView mv = (ModelView)result;
            json += mv.toJson();

        } else {
            throw new ServletException("The returned object Class is not known");
        }
        return json;
    }

}
