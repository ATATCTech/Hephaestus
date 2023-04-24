package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.Compilable;
import com.atatctech.hephaestus.Compiler;
import com.atatctech.hephaestus.attribute.Attribute;
import com.atatctech.hephaestus.attribute.AttributeUtils;
import com.atatctech.hephaestus.export.fs.HiddenComponent;
import com.atatctech.hephaestus.export.fs.Transform;
import com.atatctech.hephaestus.parser.Parser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * A component used to integrate pages and display hierarchical relationships.
 */
@Transform.RequireTransform
public class Skeleton extends WrapperComponent implements Compilable {
    public static @NotNull Parser<Skeleton> PARSER = WrapperComponent.makeParser(Skeleton.class);
    public static final Transform TRANSFORM;

    static {
        TRANSFORM = new Transform() {
            @Override
            public @NotNull String interfereFilename(int index, Component component) {
                return ((Skeleton) component).getName();
            }

            @Override
            public @NotNull WrapperComponent handleFilename(String filename, WrapperComponent wrapperComponent) {
                Skeleton skeleton = (Skeleton) wrapperComponent;
                skeleton.setName(filename);
                return skeleton;
            }
        };
    }

    protected @NotNull String name = "unnamed";

    @Attribute
    @HiddenComponent
    protected @Nullable Component component;

    protected @Nullable Skeleton parent;

    public Skeleton() {}

    public Skeleton(@NotNull String name) {
        setName(name);
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setComponent(@Nullable Component component) {
        this.component = component;
    }

    public @Nullable Component getComponent() {
        return component;
    }

    public void setParent(@Nullable Skeleton parent) {
        if (parent == null || parent.getChildren().contains(this)) this.parent = parent;
        else parent.appendChild(this);
    }

    public @Nullable Skeleton getParent() {
        return parent;
    }

    @Override
    public void appendChild(@NotNull Component child) {
        if (child instanceof Skeleton skeleton) {
            super.appendChild(child);
            skeleton.setParent(this);
        } else throw new UnsupportedOperationException();
    }

    @Override
    public @NotNull String expr() {
        String expr = "<" + Text.compile(getName()) + ":" + AttributeUtils.extractAttributes(this) + getChildren().expr();
        return (expr.endsWith(":") ? expr.substring(0, expr.length() - 1) : expr) + ">";
    }

    @Override
    public void compile(@NotNull Compiler compiler) {
        if (getComponent() instanceof Ref ref) compiler.compile(ref);
    }
}
