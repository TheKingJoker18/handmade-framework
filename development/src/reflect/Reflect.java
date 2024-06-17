package reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflect {

    public static String getClassName(Object obj) {
        return obj.getClass().getName();
    }

    public static String getClassSimpleName(Object obj) {
        return obj.getClass().getSimpleName();
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

    public static Object invokeEmptyConstructor(String className) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        Object obj = clazz.getConstructor().newInstance();
        return obj;
    }

}