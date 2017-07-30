package ua.skillsup.service.components;

import ua.skillsup.annotation.CustomDateFormat;
import ua.skillsup.annotation.JsonValue;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

public class JsonEncoder {
    public String toJson(Object o) {
        return o != null ? processObject(o, new StringBuilder()) : "";
    }

    private String processObject(Object o, StringBuilder builder) {
        builder.append("{");

        boolean fieldProcessed = false;
        for (Field field : o.getClass().getDeclaredFields()) {
            if (fieldProcessed) {
                builder.append(",");
            }

            fieldProcessed = appendField(o, field, builder);
        }

        builder.append("}");
        return builder.toString();
    }

    private boolean appendField(Object o, Field field, StringBuilder builder) {
        boolean fieldProcessed = false;

        boolean isAccessModified = !field.isAccessible();
        if (isAccessModified) {
            field.setAccessible(true);
        }

        Object result;
        try {
            result = field.get(o);
        } catch (IllegalAccessException e) {
            result = "UNKNOWN";
        }

        if (isAccessModified) {
            field.setAccessible(false);
        }

        if (result != null) {
            appendFieldName(field, builder);
            appendFieldValue(result, field, builder);
            fieldProcessed = true;
        }

        return fieldProcessed;
    }

    private void appendFieldValue(Object result, Field field, StringBuilder builder) {
        Class<?> fieldType = field.getType();
        if (fieldType.isArray()) {
            // Array
            appendArray(result, builder);
        } else if (fieldType.equals(String.class)) {
            // String
            builder.append("\"")
                    .append(result)
                    .append("\"");
        } else if (fieldType.isAssignableFrom(LocalDate.class)) {
            // LocalDate
            Optional<String> dateformat =
                    field.isAnnotationPresent(CustomDateFormat.class) ?
                            Optional.of(field.getAnnotation(CustomDateFormat.class).format()) :
                            Optional.empty();

            appendLocalDate((LocalDate) result, dateformat, builder);
        } else if (!fieldType.isPrimitive()) {
            // Objects
            processObject(result, builder);
        } else {
            // Everything else?
            builder.append(result);
        }
    }

    private void appendFieldName(Field field, StringBuilder builder) {
        builder.append("\"")
                .append(
                        field.isAnnotationPresent(JsonValue.class) ?
                                field.getAnnotation(JsonValue.class).name() :
                                field.getName())
                .append("\":");
    }

    private void appendLocalDate(LocalDate date, Optional<String> dateFormat, StringBuilder builder) {
        builder.append(
                dateFormat.map(s -> DateTimeFormatter.ofPattern(s).format(date)).orElseGet(date::toString)
        );
    }

    private void appendArray(Object o, StringBuilder builder) {
        builder.append("[")
                .append("some array processing")
                .append("]");
    }


}
