package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.Hephaestus;
import com.atatctech.hephaestus.exception.BadFormat;
import com.atatctech.hephaestus.function.Consumer;
import com.atatctech.hephaestus.parser.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * A collection of components.
 */
public class MultiComponent extends Component implements Collection<Component> {
    public static @NotNull Parser<MultiComponent> PARSER = expr -> {
        char open, close;
        if (Text.wrappedBy(expr, '{', '}')) {
            open = '{';
            close = '}';
        } else if (Text.wrappedBy(expr, '<', '>')) {
            open = '<';
            close = '>';
        } else throw new BadFormat("Unrecognized format.", expr);
        Text.IndexPair indexes = Text.matchBrackets(expr, open, close);
        int endIndex = indexes.end();
        List<Component> components = new LinkedList<>();
        while (indexes.start() >= 0 && endIndex++ >= 0) {
            Component component = Hephaestus.parseExpr(expr.substring(indexes.start(), endIndex));
            if (component == null) continue;
            components.add(component);
            expr = expr.substring(endIndex);
            indexes = Text.matchBrackets(expr, open, close);
            endIndex = indexes.end();
        }
        return new MultiComponent(components);
    };

    protected @NotNull List<Component> components = new LinkedList<>();

    public MultiComponent() {
    }

    public MultiComponent(@NotNull List<Component> components) {
        setComponents(components);
    }

    public MultiComponent(Component ... components) {
        setComponents(components);
    }

    public void setComponents(@NotNull List<Component> components) {
        this.components = components;
    }

    public void setComponents(Component ... components) {
        setComponents(new ArrayList<>(Arrays.asList(components)));
    }

    @Override
    public void forEach(@NotNull Consumer<? super Component> action, int depth) {
        for (Component component : components) component.forEach(action, depth);
    }

    protected void parallelTraversal(@NotNull Consumer<? super Component> action, int depth, @NotNull List<Component> components) {
        List<Component> next = new LinkedList<>();
        for (Component component : components) {
            if (component instanceof WrapperComponent wrapperComponent)
                next.addAll(wrapperComponent.getChildren().components);
            action.accept(component, depth);
        }
        if (!next.isEmpty()) parallelTraversal(action, depth + 1, next);
    }

    @Override
    public void parallelTraversal(@NotNull Consumer<? super Component> action, int depth) {
        parallelTraversal(action, depth, components);
    }

    @Override
    public void parallelTraversal(@NotNull Consumer<? super Component> action) {
        parallelTraversal(action, 0);
    }

    @Override
    public @NotNull String expr() {
        if (components.isEmpty()) return "";
        if (components.size() == 1) return components.get(0).expr();
        StringBuilder expr = new StringBuilder("[");
        components.forEach(component -> expr.append(component.expr()));
        return expr + "]";
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

    public void remove(int index) {
        components.remove(index);
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
