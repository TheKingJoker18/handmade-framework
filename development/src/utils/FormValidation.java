package utils;

import java.util.HashMap;

public class FormValidation {
    HashMap<String, MessageValue> errors;

    // Setters and Getters
    public void setErrors(HashMap<String, MessageValue> errors) {
        this.errors = errors;
    }
    public HashMap<String, MessageValue> getErrors() {
        return this.errors;
    }

    // Constructors
    public FormValidation() {
        this.setErrors(new HashMap<String, MessageValue>());
    }
    public FormValidation(HashMap<String, MessageValue> errors) {
        this.setErrors(errors);
    }

    // Methods
    public boolean hasErrors() {
        for (MessageValue mv : errors.values()) {
            if (mv.getMessage() != null && !mv.getMessage().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void addError(String fieldName, String message, String oldValue) {
        errors.put(fieldName, new MessageValue(message, oldValue));
    }

    // Optional methods for specific validation checks or error formatting
    public String getErrorMessage(String fieldName) {
        MessageValue error = errors.get(fieldName);
        return (error != null) ? error.getMessage() : null;
    }

    public String getOldValue(String fieldName) {
        MessageValue error = errors.get(fieldName);
        return (error != null) ? error.getValue() : null;
    }

    // Override
    @Override
    public String toString() {
        return "FormValidation{" +
                "errors=" + this.getErrors().toString() +
                "}";
    }
}