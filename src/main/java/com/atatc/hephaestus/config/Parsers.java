package com.atatc.hephaestus.config;

import com.atatc.hephaestus.component.*;
import com.atatc.hephaestus.exception.MissingFieldException;
import com.atatc.hephaestus.parser.Parser;
import org.reflections.Reflections;

import java.util.*;

public final class Parsers {
    private final static Parsers instance = new Parsers();

    public static Parsers getInstance() {
        return instance;
    }

    private final Map<String, Parser<?>> parserMap = new HashMap<>();

    private Parsers() {
//        put("mc", (expr) -> {
//            if (!Text.startsWith(expr, '{') || !Text.endsWith(expr, '}')) throw new ComponentNotClosed(expr);
//            int startIndex = 0;
//            boolean open = true;
//            List<Component> components = new LinkedList<>();
//            for (int i = 1; i < expr.length(); i++) {
//                if (open && Text.charAtEquals(expr, i, '}')) {
//                    open = false;
//                    components.add(Hephaestus.parseExpr(expr.substring(startIndex, i + 1)));
//                }
//                else if (!open && Text.charAtEquals(expr, i, '{')) {
//                    open = true;
//                    startIndex = i;
//                }
//            }
//            return new MultiComponents(components);
//        });
//        put("text", (expr) -> new Text(Text.decompile(expr, ":")));
//        put("typo", WrapperComponentParser.makeParser(Typography.class));
//        put("title", WrapperComponentParser.makeParser(Title.class));
        scanPackage("com.atatc.hephaestus.component");
    }

    public void scanPackage(String pkg) {
        Reflections reflections = new Reflections(pkg);
        Set<Class<?>> classes = reflections.getTypesAnnotatedWith(ComponentConfig.class);
        for (Class<?> clz : classes) {
            try {
                if (!Component.class.isAssignableFrom(clz)) continue;
                ComponentConfig componentConfig = clz.getAnnotation(ComponentConfig.class);
                Parser<?> parser = (Parser<?>) clz.getField("PARSER").get(null);
                put(componentConfig.tagName(), parser);
            } catch (NoSuchFieldException ignored) {
                throw new MissingFieldException(clz, "PARSER");
            } catch (IllegalAccessException ignored) {}
        }
    }

    public void put(String componentName, Parser<?> parser) {
        parserMap.put(componentName, parser);
    }

    public Parser<?> get(String componentName) {
        return parserMap.get(componentName);
    }
}
