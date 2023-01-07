package com.atatc.hephaestus.config;

import com.atatc.hephaestus.Hephaestus;
import com.atatc.hephaestus.component.Component;
import com.atatc.hephaestus.component.MultiComponents;
import com.atatc.hephaestus.component.Text;
import com.atatc.hephaestus.exception.ComponentNotClosed;
import com.atatc.hephaestus.parser.Parser;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public final class Parsers {
    private final static Parsers instance = new Parsers();

    public static Parsers getInstance() {
        return instance;
    }

    private final Map<String, Parser<?>> parserMap = new HashMap<>();

    private Parsers() {
        put("mc", (expr) -> {
            if (!Text.startsWith(expr, '{') || !Text.endsWith(expr, '}')) throw new ComponentNotClosed(expr);
            int startIndex = 0;
            boolean open = true;
            List<Component> components = new LinkedList<>();
            for (int i = 1; i < expr.length(); i++) {
                char bit = expr.charAt(i);
                if (open && bit == '}' && expr.charAt(i - 1) != Text.COMPILER_CHARACTER) {
                    open = false;
                    components.add(Hephaestus.parseExpr(expr.substring(startIndex, i + 1)));
                }
                else if (!open && bit == '{' && expr.charAt(i - 1) != Text.COMPILER_CHARACTER) {
                    open = true;
                    startIndex = i;
                }
            }
            return new MultiComponents(components);
        });
        put("text", (expr) -> new Text(Text.decompile(expr, ":")));
    }

    public void put(String componentName, Parser<?> parser) {
        parserMap.put(componentName, parser);
    }

    public Parser<?> get(String componentName) {
        return parserMap.get(componentName);
    }
}
