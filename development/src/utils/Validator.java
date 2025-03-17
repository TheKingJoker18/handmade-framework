package utils;

import java.lang.reflect.Field;

import annotation.Email;
import annotation.Length;
import annotation.NotNull;
import annotation.Range;

public class Validator {

    public Validator() {}

    public static String check(Field field, String value) {
        String error = "";

        error += (notNullCheck(field, value) != null) ? " " + notNullCheck(field, value) : "";

        error += (emailCheck(field, value) != null) ? " " + emailCheck(field, value) : "";

        error += (rangeCheck(field, value) != null) ? " " + rangeCheck(field, value) : "";

        error += (lengthCheck(field, value) != null) ? " " + lengthCheck(field, value) : "";

        if (error.trim().isEmpty()) {
            error = null;
        }


        return error;
    }

    public static String notNullCheck(Field field, String value) {
        String error = null;

        NotNull not_null_annotation = field.getAnnotation(NotNull.class);

        if (not_null_annotation!= null) {
            if (value.trim().isEmpty()) {
                error = not_null_annotation.message();
            }
        }

        return error;
    }

    public static String emailCheck(Field field, String value) {
        String error = null;

        Email email_annotation = field.getAnnotation(Email.class);

        if (email_annotation != null) {
            if (!value.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                error = email_annotation.message();
            }
        }

        return error;
    }

    public static String rangeCheck(Field field, String value) {
        String error = null;

        Range range_annotation = field.getAnnotation(Range.class);

        if (range_annotation != null) {
            double numeric_value = Double.valueOf(value);

            if (range_annotation.min() != null) {
                double min = Double.parseDouble(range_annotation.min());
                if (numeric_value < min) {
                    error = range_annotation.message();
                }
            }
            
            if (range_annotation.max() != null && error != null) {
                double max = Double.parseDouble(range_annotation.max());
                if (max < numeric_value) {
                    error = range_annotation.message();
                }
            }
        }

        if (error != null) {
            if (range_annotation.min() != null) {
                error = error.replace("{min}", range_annotation.min());
            }

            if (range_annotation.max() != null) {
                error = error.replace("{max}", range_annotation.max());
            }
        }

        return error;
    }

    public static String lengthCheck(Field field, String value) {
        String error = null;

        Length length_annotation = field.getAnnotation(Length.class);

        if (length_annotation != null) {
            if (value.length() > length_annotation.value()) {
                error = length_annotation.message()
                    .replace("{value}", Integer.toString(length_annotation.value()));
            }
        }

        return error;
    }
}
