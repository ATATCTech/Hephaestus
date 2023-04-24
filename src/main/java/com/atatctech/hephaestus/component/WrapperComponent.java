package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.Hephaestus;
import com.atatctech.hephaestus.attribute.AttributeUtils;
import com.atatctech.hephaestus.exception.MissingDefaultConstructorException;
import com.atatctech.hephaestus.function.Consumer;
import com.atatctech.hephaestus.parser.Parser;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public abstract class WrapperComponent extends Component {
    protected @NotNull MultiComponent children = new MultiComponent();

    protected WrapperComponent(@NotNull MultiComponent children) {
        setChildren(children);
    }

    protected WrapperComponent() {
    }

    public void setChildren(@NotNull MultiComponent children) {
        this.children = children;
    }

    public @NotNull MultiComponent getChildren() {
        return children;
    }

    public void appendChild(@NotNull Component child) {
        children.add(child);
    }

    public @NotNull Component child(int index) {
        return getChildren().get(index);
    }

    public void removeChild(@NotNull Component child) {
        children.remove(child);
    }

    public void removeChild(int index) {
        children.remove(index);
    }

    public @NotNull String getText() {
        StringBuilder text = new StringBuilder();
        getChildren().forEach((component, depth) -> {
            if (component instanceof Text t) text.append(t.getText());
        });
        return text.toString();
    }

    public boolean contains(Class<? extends Component> classOfComponent) {
        for (Component child : getChildren()) if (child.getClass().isAssignableFrom(classOfComponent)) return true;
        return false;
    }

    @Override
    public void forEach(@NotNull Consumer<? super Component> action, int depth) {
        super.forEach(action, depth);
        getChildren().forEach(action, depth + 1);
    }

    @Override
    public void parallelTraversal(@NotNull Consumer<? super Component> action, int depth) {
        super.parallelTraversal(action, depth);
        getChildren().parallelTraversal(action, depth + 1);
    }

    @Override
    public @NotNull String expr() {
        return generateExpr(getChildren().expr());
    }

    public static <C extends WrapperComponent> @NotNull Parser<C> makeParser(@NotNull Class<C> clz) {
        Constructor<C> constructor;
        try {
            constructor = clz.getDeclaredConstructor();
            constructor.setAccessible(true);
        } catch (NoSuchMethodException ignored) {
            throw new MissingDefaultConstructorException(clz);
        }
        return expr -> {
            try {
                C component = constructor.newInstance();
                AttributeUtils.AttributesAndBody attributesAndBody = AttributeUtils.searchAttributesInExpr(expr);
                String body;
                if (attributesAndBody == null) body = expr;
                else {
                    body = attributesAndBody.bodyExpr();
                    AttributeUtils.injectAttributes(component, attributesAndBody.attributesExpr());
                }
                Component bodyComponent = Hephaestus.parseExpr(body);
                if (bodyComponent != null) component.setChildren(bodyComponent instanceof MultiComponent children ? children : new MultiComponent(bodyComponent));
                return component;
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
