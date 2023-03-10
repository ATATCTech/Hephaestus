package com.atatctech.hephaestus.config;

import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.component.ComponentConfig;
import com.atatctech.hephaestus.exception.MissingFieldException;
import com.atatctech.hephaestus.export.fs.ComponentFile;
import com.atatctech.hephaestus.parser.Parser;
import org.jetbrains.annotations.NotNull;
import org.reflections.Reflections;

import java.lang.reflect.Field;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The global configuration manager.
 */
public final class Config {
    private final static Config instance = new Config();

    public static Config getInstance() {
        return instance;
    }

    private final Map<String, Parser<?>> parserMap = new ConcurrentHashMap<>();

    private final Map<String, ComponentFile.Transform> transformMap = new ConcurrentHashMap<>();

    private Config() {
        scanPackages(Component.class.getPackageName());
    }

    public void scanPackage(String pkg) {
        Reflections reflections = new Reflections(pkg);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(ComponentConfig.class);
        for (Class<?> clz : classes) {
            if (!Component.class.isAssignableFrom(clz)) continue;
            ComponentConfig componentConfig = clz.getAnnotation(ComponentConfig.class);
            try {
                Field field = clz.getField("PARSER");
                field.setAccessible(true);
                putParser(componentConfig.tagName(), (Parser<?>) field.get(null));
            } catch (NoSuchFieldException ignored) {
                throw new MissingFieldException(clz, "PARSER");
            } catch (IllegalAccessException ignored) {
            }
            ComponentFile.Transform transform = ComponentFile.getTransform(clz);
            if (transform != ComponentFile.DEFAULT_TRANSFORM) putTransform(componentConfig.tagName(), transform);
        }
    }

    public void scanPackages(String... packages) {
        for (String pkg : packages) scanPackage(pkg);
    }

    @NotNull
    public String[] listTagNames() {
        return parserMap.keySet().toArray(String[]::new);
    }

    public void putParser(String tagName, Parser<?> parser) {
        parserMap.put(tagName, parser);
    }

    public Parser<?> getParser(String tagName) {
        return parserMap.get(tagName);
    }

    public void putTransform(String tagName, ComponentFile.Transform transform) {
        transformMap.put(tagName, transform);
    }

    public ComponentFile.Transform getTransform(String tagName) {
        return transformMap.get(tagName);
    }
}
