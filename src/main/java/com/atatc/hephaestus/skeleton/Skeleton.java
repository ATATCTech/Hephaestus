package com.atatc.hephaestus.skeleton;

import com.atatc.hephaestus.component.*;
import com.atatc.hephaestus.parser.Parser;
import org.jsoup.nodes.Element;

import java.util.concurrent.atomic.AtomicInteger;

public class Skeleton extends WrapperComponent {
    public static Parser<Skeleton> PARSER = WrapperComponent.makeParser(Skeleton.class);

    protected String name;

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
        StringBuilder expr = new StringBuilder("<" + Text.compile(getName()) + ":[");
        for (Component child : getChildren()) if (child instanceof Skeleton) expr.append(child.expr());
        if (expr.toString().endsWith("[")) return expr.substring(0, expr.length() - 2) + ">";
        return expr + "]>";
    }

    @Override
    public Element toHTML() {
        throw new UnsupportedOperationException();
    }

    public static Skeleton generateSkeleton(Component component, String name) {
        Skeleton skeleton = new Skeleton(name);
        final Skeleton[] currentSkeleton = {skeleton};
        AtomicInteger currentLevel = new AtomicInteger();
        component.forEach((c, depth) -> {
            if (c instanceof Title title) {
                Skeleton b = new Skeleton(title.getText());
                int ld = currentLevel.get() - title.getLevel();
                if (ld >= 0) for (int i = 0; i < ld; i++) currentSkeleton[0] = currentSkeleton[0].getParent();
                currentSkeleton[0].appendChild(b);
                currentSkeleton[0] = b;
                currentLevel.set(title.getLevel());
            }
            return true;
        });
        return skeleton;
    }

    public static Skeleton generateSkeleton(Document document) {
        return generateSkeleton(document, document.getName());
    }
}
