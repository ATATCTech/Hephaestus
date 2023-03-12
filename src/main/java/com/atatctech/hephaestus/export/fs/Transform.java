package com.atatctech.hephaestus.export.fs;

import com.atatctech.hephaestus.Hephaestus;
import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.exception.HephaestusException;
import com.atatctech.hephaestus.exception.MissingFieldException;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class Transform {
    public static Transform getTransform(Class<?> clz) {
        if (clz.isAnnotationPresent(RequireTransform.class)) {
            try {
                Field field = clz.getField("TRANSFORM");
                field.setAccessible(true);
                return (Transform) field.get(null);
            } catch (NoSuchFieldException e) {
                throw new MissingFieldException(clz, "TRANSFORM");
            } catch (IllegalAccessException ignored) {
            }
        }
        return null;
    }

    public String beforeWrite(Component component) {
        return component.expr();
    }

    public Component afterRead(String content) throws HephaestusException {
        return Hephaestus.parse(content);
    }

    public Component handleFilename(String filename, Component component) {
        return component;
    }

    public String interfereFilename(int index, Component component) {
        return String.valueOf(index);
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface RequireTransform {
    }
}
