package utils;

public class MessageValue {
    String message;
    String value;

    public String getMessage() {
        return this.message;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public String getValue() {
        return this.value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    public MessageValue() {}
    public MessageValue(String message, String value) {
        this.setMessage(message);
        this.setValue(value);
    }

    @Override
    public String toString() {
        return "MessageValue{" +
                "message='" + message + '\'' +
                ", value='" +  value + '\'' +
                '}';
    }
}
