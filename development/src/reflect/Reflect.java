package reflect;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;

import annotation.ModelField;
import utils.MySession;

public class Reflect {

    public static String capitalizeFirstLetter(String str) {
        char premiereLettre = str.charAt(0);
        String resteChaine = str.substring(1);
        String strMaj = Character.toUpperCase(premiereLettre) + resteChaine;
        return strMaj;
    }

    public static String getClassName(Object obj) {
        return obj.getClass().getName();
    }

    public static String getClassSimpleName(Object obj) {
        return obj.getClass().getSimpleName();
    }

    public static String[] getFieldsNames(Object obj) throws IllegalArgumentException {
        Field[] fields = obj.getClass().getDeclaredFields();
        String[] fieldsNames = new String[fields.length];

        for (int i = 0; i < fields.length; i++) {
            ModelField field = fields[i].getAnnotation(ModelField.class);
            if (field == null)              throw new IllegalArgumentException("There is no @ModelField annotation on the field (field place : no." + (i + 1) + " )");
            else                            fieldsNames[i] = field.name();
        }

        return fieldsNames;
    }

    public static int checkFieldByType(Object obj, Class<?> fieldType) {
        Field[] fields = obj.getClass().getDeclaredFields();
        int id = -1;
        for (int i = 0; i < fields.length; i++) {
            if (fields[i].getType() == fieldType) {
                return i;
            }
        }
        return id;
    }

    public static Object getFieldValueByType(Object obj, Class<?> fieldType) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Field[] fields = obj.getClass().getDeclaredFields();
        int checkId = Reflect.checkFieldByType(obj, fieldType);
        if (checkId > -1) {
            return invokeGetterMethod(obj, fields[checkId].getName());
        }
        return null;
    }

    public static Method getMethodByName(Object obj, String methodName) {
        Class<?> clazz = obj.getClass();
        
        // Vérifier toutes les méthodes publiques de la classe et des superclasses
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (method.getName().equals(methodName)) {
                return method;
            }
        }

        return null;
    }

    public static Method getMethod(Object obj, String methodName, Object[] args) throws NoSuchMethodException, SecurityException {
        if (args == null) {
            return obj.getClass().getMethod(methodName);
        } else {
            Class<?>[] parameterTypes = new Class[args.length];
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Integer)         parameterTypes[i] = int.class;
                else if (args[i] instanceof Double)     parameterTypes[i] = double.class;
                else if (args[i] instanceof Float)      parameterTypes[i] = float.class;
                else if (args[i] instanceof Boolean)    parameterTypes[i] = boolean.class;
                else                                    parameterTypes[i] = args[i].getClass();
            }
            return obj.getClass().getMethod(methodName, parameterTypes);
        }
    }

    public static Object invokeMethod(Object obj, String methodName, Object[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        return Reflect.getMethod(obj, methodName, args).invoke(obj, args);
    }

    public static void invokeSetterMethod(Object obj, String fieldName, Class<?> fieldClass, Object value) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        obj.getClass().getMethod("set" + capitalizeFirstLetter(fieldName), fieldClass).invoke(obj, value);
    }

    public static Object invokeGetterMethod(Object obj, String fieldName) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        Object[] args = null;
        return obj.getClass().getMethod("get" + capitalizeFirstLetter(fieldName)).invoke(obj, args);
    }

    public static Object invokeEmptyConstructor(String className) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        Object obj = clazz.getConstructor().newInstance();
        return obj;
    }

    public static Object invokeControllerConstructor(String className, HttpServletRequest request) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        Object obj = Reflect.invokeEmptyConstructor(className);
        Field[] fields = obj.getClass().getDeclaredFields();
        int checkId = Reflect.checkFieldByType(obj, MySession.class);
        if (checkId > -1) {
            Reflect.invokeSetterMethod(obj, fields[checkId].getName(), fields[checkId].getType(), new MySession(request.getSession()));
        }
        return obj;
    }

}