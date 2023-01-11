package com.atatc.hephaestus.skeleton;

import com.atatc.hephaestus.component.Component;
import com.atatc.hephaestus.component.Text;
import com.atatc.hephaestus.component.WrapperComponent;
import com.atatc.hephaestus.parser.Parser;
import org.jsoup.nodes.Element;

// Fixme: MultiComponent is not capable to parse `<>`.
public class Bone extends WrapperComponent {
    public static Parser<Bone> PARSER = WrapperComponent.makeParser(Bone.class);

    protected String name;

    protected Bone parent;

    public Bone() {}

    public Bone(String name) {
        setName(name);
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setParent(Bone parent) {
        if (!parent.getChildren().contains(this)) parent.appendChild(this);
        else this.parent = parent;
    }

    public Bone getParent() {
        return parent;
    }

    @Override
    public void appendChild(Component child) {
        if (child instanceof Bone bone) {
            super.appendChild(child);
            bone.setParent(this);
        } else throw new UnsupportedOperationException();
    }

    @Override
    public String expr() {
        StringBuilder expr = new StringBuilder("<" + Text.compile(getName()) + ":[");
        for (Component child : getChildren()) if (child instanceof Bone) expr.append(child.expr());
        if (expr.toString().endsWith("[")) return expr.substring(0, expr.length() - 2) + ">";
        return expr + "]>";
    }

    @Override
    public Element toHTML() {
        throw new UnsupportedOperationException();
    }
}
