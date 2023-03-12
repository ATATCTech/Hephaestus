package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.Compilable;
import com.atatctech.hephaestus.Compiler;
import com.atatctech.hephaestus.attribute.Attribute;
import com.atatctech.hephaestus.attribute.AttributeUtils;
import com.atatctech.hephaestus.export.fs.HiddenComponent;
import com.atatctech.hephaestus.export.fs.Transform;
import com.atatctech.hephaestus.parser.Parser;

/**
 * A component used to integrate pages and display hierarchical relationships.
 */
@Transform.RequireTransform
public class Skeleton extends WrapperComponent implements Compilable {
    public static Parser<Skeleton> PARSER = WrapperComponent.makeParser(Skeleton.class);
    public static final Transform TRANSFORM;

    static {
        TRANSFORM = new Transform() {
            @Override
            public String interfereFilename(int index, Component component) {
                return ((Skeleton) component).getName();
            }

            @Override
            public Component handleFilename(String filename, Component component) {
                Skeleton skeleton = (Skeleton) component;
                skeleton.setName(filename);
                return skeleton;
            }
        };
    }

    protected String name;

    @Attribute
    @HiddenComponent
    protected Component component;

    protected Skeleton parent;

    public Skeleton() {}

    public Skeleton(String name) {
        setName(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }

    public void setParent(Skeleton parent) {
        if (!parent.getChildren().contains(this)) parent.appendChild(this);
        else this.parent = parent;
    }

    public Skeleton getParent() {
        return parent;
    }

    @Override
    public void appendChild(Component child) {
        if (child instanceof Skeleton skeleton) {
            super.appendChild(child);
            skeleton.setParent(this);
        } else throw new UnsupportedOperationException();
    }

    @Override
    public String expr() {
        String expr = "<" + Text.compile(getName()) + ":" + AttributeUtils.extractAttributes(this) + getChildren().expr();
        return (expr.endsWith(":") ? expr.substring(0, expr.length() - 1) : expr) + ">";
    }

    @Override
    public void compile(Compiler compiler) {
        if (getComponent() instanceof Ref ref) compiler.compile(ref);
    }
}
