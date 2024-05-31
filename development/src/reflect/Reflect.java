package reflect;

import java.lang.reflect.InvocationTargetException;

public class Reflect {

    public static String getClassName(Object obj) {
        return obj.getClass().getName();
    }

    public static String getClassSimpleName(Object obj) {
        return obj.getClass().getSimpleName();
    }

    public static Object invokeMethod(Object obj, String methodName, Object[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException {
        if (args == null)   return obj.getClass().getMethod(methodName).invoke(obj, args);
        else                return obj.getClass().getMethod(methodName, args.getClass()).invoke(obj, args);
    }

    public static Object invokeEmptyConstructor(String className) throws InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException, NoSuchMethodException, SecurityException, ClassNotFoundException {
        Class<?> clazz = Class.forName(className);
        Object obj = clazz.getConstructor().newInstance();
        return obj;
    }

}