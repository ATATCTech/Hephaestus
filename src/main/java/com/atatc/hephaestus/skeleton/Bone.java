package com.atatc.hephaestus.skeleton;

import com.atatc.hephaestus.component.Component;
import com.atatc.hephaestus.exception.BadFormat;
import com.atatc.hephaestus.parser.Parser;
import org.jsoup.nodes.Element;

import java.util.LinkedList;
import java.util.List;

public class Bone extends Component {
    public static Parser<Bone> PARSER = new Parser<Bone>() {
        @Override
        public Bone parse(String expr) throws BadFormat {
            // todo
            return null;
        }
    };
    protected String name;

    protected Component component;

    protected Bone parent;

    protected List<Bone> children = new LinkedList<>();

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

    public void setComponent(Component component) {
        this.component = component;
    }

    public Component getComponent() {
        return component;
    }

    public void setParent(Bone parent) {
        if (!parent.getChildren().contains(this)) parent.appendChild(this);
        else this.parent = parent;
    }

    public Bone getParent() {
        return parent;
    }

    public void appendChild(Bone bone) {
        bone.parent = this;
        getChildren().add(bone);
    }

    public List<Bone> getChildren() {
        return children;
    }

    @Override
    public String expr() {
        StringBuilder expr = new StringBuilder("<" + getName() + ":");
        for (Bone child : getChildren()) {
            expr.append(child.expr());
        }
        if (expr.toString().endsWith(":")) return expr.substring(0, expr.length() - 1);
        return expr + ">";
    }

    @Override
    public Element toHTML() {
        throw new UnsupportedOperationException();
    }
}
