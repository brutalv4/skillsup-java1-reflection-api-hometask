package ua.skillsup.service.components;

import ua.skillsup.annotation.CustomDateFormat;
import ua.skillsup.annotation.JsonValue;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class JsonEncoder {
    public String toJson(Object o) {
        StringBuilder builder = new StringBuilder();

        if (o != null) {
            builder.append(processObject(o));
        }

        return builder.toString();
    }

    private String processObject(Object o) {
        StringBuilder builder = new StringBuilder();
        builder.append("{");

        Class<?> clazz = o.getClass();
        boolean isFieldProcessed = false;
        for (Field field : clazz.getDeclaredFields()) {
            if (isFieldProcessed) {
                builder.append(",");
                isFieldProcessed = false;
            }

            String nameField = proccessFieldName(field);

            String valueField;
            try {
                valueField = processFieldValue(field, o);
            } catch (IllegalAccessException e) {
                valueField = "UNKNOWN";
            }

            if (!valueField.isEmpty()) {
                builder
                        .append("\"")
                        .append(nameField)
                        .append("\":")
                        .append(valueField)
                ;

                isFieldProcessed = true;
            }

        }

        builder.append("}");
        return builder.toString();
    }

    private String proccessFieldName(Field field) {
        String fieldName;

        if (field.isAnnotationPresent(JsonValue.class)) {
            fieldName = field.getAnnotation(JsonValue.class).name();
        } else {
            fieldName = field.getName();
        }

        return fieldName;
    }

    private String processFieldValue(Field field, Object o) throws IllegalAccessException {
        Object result;

        StringBuilder builder = new StringBuilder();

        boolean isAccessModified = false;
        if (!field.isAccessible()) {
            field.setAccessible(true);
            isAccessModified = true;
        }

        result = field.get(o);

        if (isAccessModified) {
            field.setAccessible(false);
        }

        if (result != null) {
            Class<?> fieldType = field.getType();
            if (fieldType.isArray()) {
                // Array
                builder.append(processArray(result));
            } else if (fieldType.isAssignableFrom(Number.class)) {
                // Number
                builder.append(result);
            } else if (fieldType.equals(String.class)) {
                // String
                builder.append("\"");
                builder.append(result);
                builder.append("\"");
            } else if (fieldType.isAssignableFrom(LocalDate.class)) {
                // LocalDate
                String dateformat = null;
                if (field.isAnnotationPresent(CustomDateFormat.class)) {
                    dateformat = field.getAnnotation(CustomDateFormat.class).format();
                }

                builder.append(processLocalDate((LocalDate) result, dateformat));
            } else if (!fieldType.isPrimitive()) {
                // Objects
                builder.append(processObject(result));
            } else {
                // Everything else?
                builder.append(result);
            }
        }

        return builder.toString();
    }

    private String processLocalDate(LocalDate date, String dateFormat) {
        String result;
        if (dateFormat != null) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(dateFormat);
            result = formatter.format(date);
        } else {
            result = date.toString();
        }

        return result;
    }

    private String processArray(Object o) {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        builder.append("some array processing");
        builder.append("]");
        return builder.toString();
    }


}
