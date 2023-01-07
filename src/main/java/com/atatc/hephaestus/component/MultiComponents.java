package com.atatc.hephaestus.component;

import com.atatc.hephaestus.render.HTMLRender;
import org.jsoup.nodes.Element;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

public class MultiComponents extends Component {
    public static HTMLRender<MultiComponents> HTML_RENDER = (multiComponents) -> {
        Element element = new Element("div");
        multiComponents.getComponents().forEach((component) -> element.appendChild(component.toHTML()));
        return element;
    };

    protected List<Component> components;

    public MultiComponents() {
        components = new LinkedList<>();
    }

    public MultiComponents(List<Component> components) {
        setComponents(components);
    }

    public MultiComponents(Component... components) {
        setComponents(components);
    }

    public void setComponents(List<Component> components) {
        this.components = components;
    }

    public void setComponents(Component... components) {
        setComponents(Arrays.asList(components));
    }

    public List<Component> getComponents() {
        return components;
    }

    public Component[] getComponentsArray() {
        return getComponents().toArray(Component[]::new);
    }

    public void append(Component component) {
        getComponents().add(component);
    }

    public void appendAll(Collection<? extends Component> components) {
        getComponents().addAll(components);
    }

    @Override
    public String expr() {
        StringBuilder expr = new StringBuilder("[");
        components.forEach((component) -> expr.append(component.expr()));
        return expr + "]";
    }

    @Override
    public Element toHTML() {
        return HTML_RENDER.render(this);
    }
}
