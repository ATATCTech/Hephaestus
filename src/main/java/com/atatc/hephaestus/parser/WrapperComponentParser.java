package com.atatc.hephaestus.parser;

import com.atatc.hephaestus.Hephaestus;
import com.atatc.hephaestus.component.AttributesUtils;
import com.atatc.hephaestus.component.Component;
import com.atatc.hephaestus.component.MultiComponents;
import com.atatc.hephaestus.component.WrapperComponent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public final class WrapperComponentParser {
    public static <C extends WrapperComponent> Parser<C> makeParser(Class<C> clz) {
        Constructor<C> constructor;
        try {
            constructor = clz.getDeclaredConstructor();
        } catch (NoSuchMethodException ignored) {
            return null;
        }
        return expr -> {
            try {
                C component = constructor.newInstance();
                AttributesUtils.AttributesAndBody attributesAndBody = AttributesUtils.searchAttributesInExpr(expr);
                String body;
                if (attributesAndBody == null) {
                    body = expr;
                } else {
                    body = attributesAndBody.bodyExpr();
                    AttributesUtils.injectAttributes(component, attributesAndBody.attributesExpr());
                }
                Component children = Hephaestus.parseExpr(body);
                if (children instanceof MultiComponents) component.setChildrenComponent((MultiComponents) children);
                else component.setChildren(children);
                return component;
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
