package utils;

import java.util.Objects;

public class VerbAction {
    private String methodName;
    private String verb;

    public String getMethodName() {
        return this.methodName;
    }
    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getVerb() {
        return this.verb;
    }
    public void setVerb(String verb) {
        this.verb = verb;
    }

    public VerbAction() {}
    public VerbAction(String methodName, String verb) {
        this.setMethodName(methodName);
        this.setVerb(verb);
    }

    public String toString() {
        return "VerbAction{" +
                ", methodName='" + methodName + '\'' +
                ", verb='" + verb + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VerbAction)) return false;
        VerbAction that = (VerbAction) o;
        return getMethodName().equals(that.getMethodName()) && getVerb().equals(that.getVerb());
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.getMethodName(), this.getVerb());
    }
}
