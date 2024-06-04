package utils;

import java.lang.reflect.InvocationTargetException;

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

}
