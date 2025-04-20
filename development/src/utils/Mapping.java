package utils;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.HashSet;
import java.util.Set;

import javax.management.relation.RoleNotFoundException;
import javax.naming.AuthenticationException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import annotation.Authentified;
import annotation.ModelAttribute;
import annotation.Param;
import annotation.Restapi;
import annotation.Role;
import reflect.Reflect;

public class Mapping {
    private String className;
    private Set<VerbAction> ls_verbAction;
    private VerbAction action;
    private FormValidation formValidation;

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

    public FormValidation getFormValidation() {
        return this.formValidation;
    }
    public void setFormValidation(FormValidation formValidation) {
        this.formValidation = formValidation;
    }

    public Mapping() {
        this.setFormValidation(new FormValidation());
    }
    public Mapping(String className, Set<VerbAction> ls_verbAction) {
        this.setClassName(className);
        this.setLs_verbAction(ls_verbAction);
        this.setFormValidation(new FormValidation());
    }
    public Mapping(String className, String methodName, String verb) {
        this.setClassName(className);
        Set<VerbAction> ls_verbAction = new HashSet<VerbAction>();
        ls_verbAction.add(new VerbAction(methodName, verb));
        this.setLs_verbAction(ls_verbAction);
        this.setFormValidation(new FormValidation());
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

        // Adding the value of the parameter in the formValidation in case of error in other field like in setModelParam
        formValidation.addError(paramName, null, paramValue);
    }

    public void setModelParam(Object controller, Method method, int i, Parameter[] parameters, Object[] values, HttpServletRequest request) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, ServletException, IOException {
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
                    // Sprint 13
                    // throw new IllegalArgumentException("Invalid value for field " + inputName + " : " + error_message);

                    // Sprint 14
                    // Add the error message and the old value in the FormValidation
                    formValidation.addError(inputName, error_message, fieldValue);

                // if the Parameter is okay, adding its value in the formValidation in case of error in other field
                } else {
                    formValidation.addError(inputName, null, fieldValue);
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

    public void configParam(Object controller, Method method, Parameter[] parameters, Object[] values, HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, NullPointerException, IOException, ServletException {
        for (int i = 0; i < parameters.length; i++) {
            if (parameters[i].getType() == int.class || parameters[i].getType() == double.class || parameters[i].getType() == String.class || parameters[i].getType() == MySession.class || parameters[i].getType() == FileUpload.class) {
                setSimpleParam(controller, method, i, parameters, values, request);
            } else {
                setModelParam(controller, method, i, parameters, values, request);
            }
        }

        // Verifying if there is any error
        // if (!value_controller.isEmpty()) {
        if (formValidation.hasErrors()) {
            // Sprint 13
            // throw new IllegalArgumentException("Invalid values for the fields : " + formValidation.toString());

            // Sprint 14
            request.setAttribute("formValidation", formValidation);
            
            String form_url = request.getParameter("form_url");
            System.out.println("form_url = " + form_url);

            RequestDispatcher dispatcher = request.getRequestDispatcher(form_url);
            dispatcher.forward(request, response);
        }
    }

    public void verifyMethodPermission(Method method, HttpServletRequest request) throws AuthenticationException, RoleNotFoundException {
        Authentified authentified = method.getAnnotation(Authentified.class);
        Role role = method.getAnnotation(Role.class);

        // Verify authentification
        if (authentified != null) {
            MySession mySession = new MySession(request.getSession());
            if (mySession.get(authentified.session_name()) == null) {
                throw new AuthenticationException("There is no connected user");
            }

            // Verify role
            if (role != null) {
                if (mySession.get(role.session_name()) == null) {
                    throw new RoleNotFoundException("The user has no role");
                
                } else {
                    String user_role = (String) mySession.get(role.session_name());
                    String[] authorized_roles = role.authorized_roles();
                    boolean check = false;
                    for (int i = 0; i < authorized_roles.length; i++) {
                        if (user_role.compareTo(authorized_roles[i]) == 0) {
                            check = true;
                            break;
                        }
                    }

                    if (!check) {
                        throw new RoleNotFoundException("The user role doesn't match with any required role(s)... The actual user role is \"" + user_role + "\"");
                    }
                }
            }

        } else if (authentified == null && role != null) {
            throw new AuthenticationException("The method " + method.getName() + " has an annotation Role but no annotation Authentified");
        }
    }

    public Object invokeMethod(HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, NullPointerException, IOException, ServletException, AuthenticationException, RoleNotFoundException {
        Object controller = Reflect.invokeControllerConstructor(this.getClassName(), request);
        Method method = Reflect.getMethodByName(controller, action.getMethodName());
        // Check if the method has an Authentified and a Role annotations
        verifyMethodPermission(method, request);

        // Configure its parameters
        Parameter[] parameters = method.getParameters();
        if (parameters.length > 0) {
            Object[] values = new Object[parameters.length];
            configParam(controller, method, parameters, values, request, response);
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

    public String execute_html(HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, ServletException, IOException, AuthenticationException, NullPointerException, RoleNotFoundException {
        String print = "";
        print += "<hr/>";
        print += "<h2> Listes des Controllers trouves: </h2>";
        print += "<p>Class: " + this.getClassName() + "</p>";
        print += "<p>Method: " + action.getMethodName() + "</p>";
        Object result = this.invokeMethod(request, response);
        print += "<p>---------------------------------------------------------------</p>";
        if (result instanceof String) {
            print += "<p>Result: " + (String) result + "</p>";
                
        } else if (result instanceof ModelView) {
            if (formValidation.hasErrors()) {
                // Reducing the dispatcher.forward to once each time if there is some error(s) to avoid multiple forward
                return "";
            } else {
                ModelView mv = (ModelView)result;
                mv.prepareModelView(request, response);
            }

        } else {
            throw new ServletException("The returned object Class is not known");
        }
        print += "<p>---------------------------------------------------------------</p>";
        return print;
    }

    public String execute_json(HttpServletRequest request, HttpServletResponse response) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException, ServletException, NullPointerException, IOException, AuthenticationException, RoleNotFoundException {
        String json = "data: ";
        Object result = this.invokeMethod(request, response);
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
