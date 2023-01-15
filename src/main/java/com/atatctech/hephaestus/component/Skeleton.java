package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.parser.Parser;

public class Skeleton extends WrapperComponent {
    public static Parser<Skeleton> PARSER = WrapperComponent.makeParser(Skeleton.class);

    protected String name;

    @Attribute
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
        String expr = "<" + Text.compile(getName()) + ":" + AttributesUtils.extractAttributes(this) + getChildren().expr();
        return (expr.endsWith(":") ? expr.substring(0, expr.length() - 1) : expr) + ">";
    }
}
