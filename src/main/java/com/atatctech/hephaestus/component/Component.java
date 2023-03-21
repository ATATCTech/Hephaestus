package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.Style;
import com.atatctech.hephaestus.attribute.Attribute;
import com.atatctech.hephaestus.attribute.AttributeUtils;
import com.atatctech.hephaestus.function.Consumer;
import org.jetbrains.annotations.NotNull;

/**
 * Component is the base class of all components.
 * To make a new variant of component, inherit directly or indirectly from this class.
 * Annotate the class with `ComponentConfig` to identify the configuration and create a static field named `PARSER`. See documentation.
 * There are some builtin components that do not have `ComponentConfig` annotation because they are specified in the parsing process. However, if custom components are not annotated, they will not be discovered or loaded.
 * Remember to scan the packages where the custom components are using `Config.getInstance().scanPackages()` before `Hephaestus.parseExpr()` is called.
 * If there are attributes need to be written in the expression, annotate the fields with `Attribute`. See documentation.
 */
public abstract class Component {
    @Attribute
    protected String id;
    protected Style style = new Style();

    protected Component() {
    }

    /**
     * Get the `ComponentConfig` annotation of this class.
     * @return the `ComponentConfig` annotation.
     */
    public ComponentConfig getConfig() {
        return getClass().getAnnotation(ComponentConfig.class);
    }

    /**
     * Get the configured custom tag name of this class.
     * @return the tag name, "undefined" if it does not exist
     */
    public String getTagName() {
        ComponentConfig config = getConfig();
        return config == null ? "undefined" : config.tagName();
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setStyle(Style style) {
        this.style = style;
    }

    public Style getStyle() {
        return style;
    }

    /**
     * Exposes an interface capable of traversing the component tree. This method traverses the component tree in order from top right to bottom left (DFS).
     * @param action callback function
     * @param depth indicates the depth of the top (this) component
     */
    public void forEach(Consumer<? super Component> action, int depth) {
        action.accept(this, depth);
    }

    public void forEach(Consumer<? super Component> action) {
        forEach(action, 0);
    }

    /**
     * Exposes an interface capable of traversing the component tree. Different from `Component.forEach()`, this method traverses the component tree horizontally from top to bottom (BFS).
     * It has a better performance compared to `Component.forEach()`.
     * @param action callback function
     * @param depth indicates the depth of the top (this) component
     */
    public void parallelTraversal(Consumer<? super Component> action, int depth) {
        action.accept(this, depth);
    }

    public void parallelTraversal(Consumer<? super Component> action) {
        parallelTraversal(action, 0);
    }

    protected String generateExpr(String inner) {
        return "{" + getTagName() + ":" + AttributeUtils.extractAttributes(this) + inner + "}";
    }

    /**
     * Generate a string expression of the component.
     * @return the expression
     */
    public abstract @NotNull String expr();

    /**
     * Convert the component into a string. By default, it just simply call `expr()`.
     * @return a string
     */
    @Override
    public String toString() {
        return expr();
    }
}
