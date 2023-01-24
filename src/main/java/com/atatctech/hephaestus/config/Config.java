package com.atatctech.hephaestus.config;

import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.component.ComponentConfig;
import com.atatctech.hephaestus.exception.MissingFieldException;
import com.atatctech.hephaestus.parser.Parser;
import org.reflections.Reflections;

import java.util.*;

/**
 * The global configuration manager.
 */
public final class Config {
    private final static Config instance = new Config();

    public static Config getInstance() {
        return instance;
    }

    private final Map<String, Parser<?>> parserMap = new HashMap<>();

    private Config() {
        scanPackages(Component.class.getPackageName());
    }

    public void scanPackage(String pkg) {
        Reflections reflections = new Reflections(pkg);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(ComponentConfig.class);
        for (Class<?> clz : classes) {
            try {
                if (!Component.class.isAssignableFrom(clz)) continue;
                ComponentConfig componentConfig = clz.getAnnotation(ComponentConfig.class);
                Parser<?> parser = (Parser<?>) clz.getField("PARSER").get(null);
                putParser(componentConfig.tagName(), parser);
            } catch (NoSuchFieldException ignored) {
                throw new MissingFieldException(clz, "PARSER");
            } catch (IllegalAccessException ignored) {}
        }
    }

    public void scanPackages(String... packages) {
        for (String pkg : packages) scanPackage(pkg);
    }

    public String[] listTagNames() {
        return parserMap.keySet().toArray(String[]::new);
    }

    public void putParser(String tagName, Parser<?> parser) {
        parserMap.put(tagName, parser);
    }

    public Parser<?> getParser(String tagName) {
        return parserMap.get(tagName);
    }
}
