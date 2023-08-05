package com.atatctech.hephaestus.config;

import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.component.ComponentConfig;
import com.atatctech.hephaestus.exception.MissingFieldException;
import com.atatctech.hephaestus.export.fs.Transform;
import com.atatctech.hephaestus.parser.Parser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The global configuration manager.
 */
public final class Config {
    private static final Map<String, Parser<?>> parserMap = new ConcurrentHashMap<>();

    private static final Map<String, Transform> transformMap = new ConcurrentHashMap<>();

    static {
        scanPackages(Component.class.getPackageName());
    }

    private Config() {
    }

    public static void scanPackage(@NotNull String pkg) {
        Reflections reflections = new Reflections(pkg);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(ComponentConfig.class);
        for (Class<?> clz : classes) {
            if (!Component.class.isAssignableFrom(clz)) continue;
            ComponentConfig componentConfig = clz.getAnnotation(ComponentConfig.class);
            try {
                Field field = clz.getDeclaredField("PARSER");
                field.setAccessible(true);
                putParser(componentConfig.tagName(), (Parser<?>) field.get(null));
            } catch (NoSuchFieldException ignored) {
                throw new MissingFieldException(clz, "PARSER");
            } catch (IllegalAccessException ignored) {
            }
            if (clz.isAnnotationPresent(Transform.RequireTransform.class)) {
                Transform transform = Transform.getTransform(clz);
                if (transform != null) putTransform(componentConfig.tagName(), transform);
            }
        }
    }

    public static void scanPackages(@NotNull String @NotNull ... packages) {
        for (String pkg : packages) scanPackage(pkg);
    }

    public static @NotNull String @NotNull [] listTagNames() {
        return parserMap.keySet().toArray(String[]::new);
    }

    public static void putParser(@NotNull String tagName, @NotNull Parser<?> parser) {
        parserMap.put(tagName, parser);
    }

    public static @Nullable Parser<?> getParser(@NotNull String tagName) {
        return parserMap.get(tagName);
    }

    public static void putTransform(@NotNull String tagName, @NotNull Transform transform) {
        transformMap.put(tagName, transform);
    }

    public static @Nullable Transform getTransform(@NotNull String tagName) {
        return transformMap.get(tagName);
    }
}
