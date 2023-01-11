package com.atatc.hephaestus.component;

import com.atatc.hephaestus.Hephaestus;
import com.atatc.hephaestus.exception.ComponentNotClosed;
import com.atatc.hephaestus.function.Consumer;
import com.atatc.hephaestus.parser.Parser;
import com.atatc.hephaestus.render.HTMLRender;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;

import java.util.*;

public class MultiComponents extends Component implements Collection<Component> {
    public static HTMLRender<MultiComponents> HTML_RENDER = multiComponents -> {
        Element element = new Element("div").attr("style", "display:inline-block;");
        multiComponents.forEach((component) -> element.appendChild(component.toHTML()));
        return element;
    };
    public static Parser<MultiComponents> PARSER = expr -> {
        if (!Text.startsWith(expr, '{') || !Text.endsWith(expr, '}')) throw new ComponentNotClosed(expr);
        List<Component> components = new LinkedList<>();
        int[] indexes = Text.pairBrackets(expr, '{', '}');
        while (indexes[0] >= 0 && indexes[1] >= 0) {
            components.add(Hephaestus.parseExpr(expr.substring(indexes[0], indexes[1] + 1)));
            expr = expr.substring(indexes[1] + 1);
            indexes = Text.pairBrackets(expr, '{', '}');
        }
        return new MultiComponents(components);
    };

    protected List<Component> components = new LinkedList<>();

    public MultiComponents() {}

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
        setComponents(new ArrayList<>(Arrays.asList(components)));
    }

    @Override
    public void forEach(Consumer<? super Component> action, int depth) {
        for (Component component : components) {
            if (!action.accept(component, depth)) break;
        }
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

    @Override
    public int size() {
        return components.size();
    }

    @Override
    public boolean isEmpty() {
        return components.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return components.contains(o);
    }

    @Override
    public Iterator<Component> iterator() {
        return components.iterator();
    }

    @Override
    public Object @NotNull [] toArray() {
        return components.toArray();
    }

    @Override
    public <T> T @NotNull [] toArray(T @NotNull [] a) {
        return components.toArray(a);
    }

    @Override
    public boolean add(Component component) {
        return components.add(component);
    }

    @Override
    public boolean remove(Object o) {
        return components.remove(o);
    }

    public Component remove(int index) {
        return components.remove(index);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return new HashSet<>(components).containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends Component> c) {
        return components.addAll(c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return components.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return components.retainAll(c);
    }

    @Override
    public void clear() {
        components.clear();
    }

    public Component get(int index) {
        return components.get(index);
    }
}
