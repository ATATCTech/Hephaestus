package com.atatc.hephaestus.html;

import java.util.LinkedList;
import java.util.List;

public class HTMLElement {
    public final String tagName;
    protected List<HTMLElement> children = new LinkedList<>();
    protected Attributes attributes = new Attributes();

    public HTMLElement(String tagName) {
        this.tagName = tagName;
    }

    public List<HTMLElement> getChildren() {
        return children;
    }

    public HTMLElement appendChild(HTMLElement child) {
        children.add(child);
        return this;
    }

    protected String getChildrenString() {
        StringBuilder stringBuilder = new StringBuilder();
        getChildren().forEach(stringBuilder::append);
        return stringBuilder.toString();
    }

    public Attributes getAttributes() {
        return attributes;
    }

    public HTMLElement attr(String key, String value) {
        attributes.put(key, value);
        return this;
    }

    @Override
    public String toString() {
        return "<" + tagName + attributes + ">" + getChildrenString() + "</" + tagName + ">";
    }
}
