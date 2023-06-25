package com.atatctech.hephaestus.attribute;

import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.component.Text;
import com.atatctech.hephaestus.exception.HephaestusRuntimeException;
import com.atatctech.hephaestus.utils.Utils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class AttributeUtils {
    public static @NotNull String extractAttributes(@NotNull Component component) {
        StringBuilder attributes = new StringBuilder("(");
        Utils.forEachDeclaredField(component.getClass(), field -> {
            try {
                if (!field.isAnnotationPresent(Attribute.class)) return;
                field.setAccessible(true);
                Object val = field.get(component);
                if (val == null) return;
                attributes.append(getAttributeName(field.getAnnotation(Attribute.class), field)).append("=").append(val).append(";");
            } catch (IllegalAccessException ignored) {}
        });
        if (attributes.length() == 1) return "";
        return attributes + ")";
    }

    public record AttributesAndBody(@NotNull String attributesExpr, @NotNull String bodyExpr) {}

    public static @NotNull AttributesAndBody searchAttributesInExpr(@NotNull String expr) {
        if (!Text.startsWith(expr, '(')) return new AttributesAndBody("", expr);
        int endIndex = Text.indexOf(expr, ')', 1) + 1;
        if (endIndex < 1) return new AttributesAndBody("", expr);
        return new AttributesAndBody(expr.substring(0, endIndex), expr.substring(endIndex));
    }

    public static void injectField(@NotNull Field field, @NotNull Object instance, @NotNull String value, @NotNull Class<? extends TargetConstructor<?>> targetConstructor) throws IllegalAccessException, NoSuchMethodException, InvocationTargetException, InstantiationException {
        field.setAccessible(true);
        Method cast = targetConstructor.getDeclaredMethod("cast", String.class);
        if (!field.getType().isAssignableFrom(cast.getReturnType())) throw new HephaestusRuntimeException("Target constructor does not return the required type.");
        cast.setAccessible(true);
        field.set(instance, cast.invoke(targetConstructor.getDeclaredConstructor().newInstance(), value));
    }

    public static @NotNull String getAttributeName(@NotNull Attribute annotation, @NotNull Field field) {
        return annotation.name().isEmpty() ? field.getName() : annotation.name();
    }

    public static @Nullable String getAttribute(@NotNull String attributesExpr, @NotNull String attributeName) {
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

    public static void injectAttributes(@NotNull Component component, @NotNull String attributesExpr) {
        Utils.forEachDeclaredField(component.getClass(), field -> {
            try {
                if (!field.isAnnotationPresent(Attribute.class)) return;
                Attribute annotation = field.getAnnotation(Attribute.class);
                String val = getAttribute(attributesExpr, getAttributeName(annotation, field));
                if (val == null) return;
                injectField(field, component, val, annotation.targetConstructor());
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException |
                     InstantiationException ignored) {}
        });
    }
}
