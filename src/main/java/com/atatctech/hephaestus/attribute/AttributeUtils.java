package com.atatctech.hephaestus.attribute;

import com.atatctech.hephaestus.Color;
import com.atatctech.hephaestus.Hephaestus;
import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.component.Text;
import com.atatctech.hephaestus.exception.BadFormat;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public final class AttributeUtils {
    public static Set<Field> getDeclaredFields(Class<?> clz) {
        if (clz == null) return null;
        Set<Field> fields = new HashSet<>(Arrays.asList(clz.getDeclaredFields()));
        Set<Field> superFields = getDeclaredFields(clz.getSuperclass());
        if (superFields != null) fields.addAll(superFields);
        return fields;
    }

    public static String extractAttributes(Component component) {
        Set<Field> fields = getDeclaredFields(component.getClass());
        StringBuilder attributes = new StringBuilder("(");
        for (Field field : fields) {
            try {
                if (!field.isAnnotationPresent(Attribute.class)) continue;
                field.setAccessible(true);
                Object val = field.get(component);
                if (val == null) continue;
                attributes.append(field.getName()).append("=").append(val).append(";");
            } catch (IllegalAccessException ignored) {}
        }
        if (attributes.length() == 1) return "";
        return attributes + ")";
    }

    public record AttributesAndBody(String attributesExpr, String bodyExpr) {}

    public static AttributesAndBody searchAttributesInExpr(String expr) {
        if (!Text.startsWith(expr, '(')) return null;
        int endIndex = Text.indexOf(expr, ')', 1) + 1;
        if (endIndex < 1) return null;
        return new AttributesAndBody(expr.substring(0, endIndex), expr.substring(endIndex));
    }

    public static void injectField(Field field, Object instance, String value) throws IllegalAccessException, BadFormat {
        Class<?> t = field.getType();
        if (t == String.class) field.set(instance, value);
        else if (t == Integer.class) field.set(instance, Integer.valueOf(value));
        else if (t == Float.class) field.set(instance, Float.valueOf(value));
        else if (t == Long.class) field.set(instance, Long.valueOf(value));
        else if (t == Double.class) field.set(instance, Double.valueOf(value));
        else if (t == Color.class) field.set(instance, new Color(value));
        else if (t == Component.class) field.set(instance, Hephaestus.parseExpr(value));
        else field.set(instance, t.cast(value));
    }

    public static String getAttribute(String attributesExpr, String attributeName) {
        if (attributesExpr.length() < attributeName.length()) return null;
        int startIndex = attributesExpr.indexOf(attributeName);
        if (startIndex < 0) return null;
        startIndex += attributeName.length();
        if (!Text.charAtEquals(attributesExpr, startIndex, '=')) return getAttribute(attributesExpr.substring(startIndex), attributeName);
        startIndex += 1;
        int endIndex = Text.indexOf(attributesExpr, ';', startIndex);
        if (endIndex < 0) return attributesExpr.substring(startIndex);
        return attributesExpr.substring(startIndex, endIndex);
    }

    public static void injectAttributes(Component component, String attributesExpr) {
        Set<Field> fields = getDeclaredFields(component.getClass());
        for (Field field : fields) {
            if (!field.isAnnotationPresent(Attribute.class)) continue;
            try {
                String val = getAttribute(attributesExpr, field.getName());
                if (val == null) continue;
                injectField(field, component, val);
            } catch (IllegalAccessException | BadFormat ignored) {}
        }
    }
}
