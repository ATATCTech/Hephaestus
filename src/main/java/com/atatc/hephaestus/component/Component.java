package com.atatc.hephaestus.component;

import com.atatc.hephaestus.Style;
import com.atatc.hephaestus.function.Consumer;
import org.jsoup.nodes.Element;

public abstract class Component {
    protected Style style = new Style();

    public Component() {
    }

    public ComponentConfig getConfig() {
        return getClass().getAnnotation(ComponentConfig.class);
    }

    public String getTagName() {
        ComponentConfig componentConfig = getConfig();
        if (componentConfig == null) return "undefined";
        return componentConfig.tagName();
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

    public abstract Element toHTML();

    @Override
    public String toString() {
        return expr();
    }
}
