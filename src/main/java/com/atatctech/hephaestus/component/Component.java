package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.Style;
import com.atatctech.hephaestus.function.Consumer;

public abstract class Component {
    protected Style style = new Style();

    protected Component() {
    }

    public ComponentConfig getConfig() {
        return getClass().getAnnotation(ComponentConfig.class);
    }

    public String getTagName() {
        ComponentConfig config = getConfig();
        if (config == null) return "undefined";
        return config.tagName();
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Style getStyle() {
        return style;
    }

    protected void forEach(Consumer<? super Component> action, int depth) {
        action.accept(this, depth);
    }

    public void forEach(Consumer<? super Component> action) {
        forEach(action, 0);
    }

    public abstract String expr();

    @Override
    public String toString() {
        return expr();
    }
}
