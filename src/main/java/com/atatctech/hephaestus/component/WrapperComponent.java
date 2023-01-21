package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.Hephaestus;
import com.atatctech.hephaestus.attribute.AttributeUtils;
import com.atatctech.hephaestus.exception.MissingDefaultConstructorException;
import com.atatctech.hephaestus.function.Consumer;
import com.atatctech.hephaestus.parser.Parser;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class WrapperComponent extends Component {
    @NotNull
    protected MultiComponent children = new MultiComponent();

    protected WrapperComponent(MultiComponent children) {
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

    public void appendChild(Component child) {
        children.add(child);
    }

    public Component child(int index) {
        return getChildren().get(index);
    }

    public void removeChild(Component child) {
        children.remove(child);
    }

    public void removeChild(int index) {
        children.remove(index);
    }

    public String getText() {
        StringBuilder text = new StringBuilder();
        getChildren().forEach((component, depth) -> {
            if (component instanceof Text t) text.append(t.getText());
            return true;
        });
        return text.toString();
    }

    public boolean contains(Class<? extends Component> classOfComponent) {
        for (Component child : getChildren()) if (child.getClass().isAssignableFrom(classOfComponent)) return true;
        return false;
    }

    public boolean contains(Class<? extends Component> classOfComponent, int depthLimit) {
        AtomicBoolean flag = new AtomicBoolean(false);
        getChildren().forEach((component, depth) -> {
            if (component.getClass().isAssignableFrom(classOfComponent)) {
                flag.set(true);
                return false;
            }
            return depthLimit < 0 || depth < depthLimit;
        });
        return flag.get();
    }

    @Override
    public void forEach(Consumer<? super Component> action, int depth) {
        super.forEach(action, depth);
        getChildren().forEach(action, depth + 1);
    }

    @Override
    public void parallelTraversal(Consumer<? super Component> action, int depth) {
        super.parallelTraversal(action, depth);
        getChildren().parallelTraversal(action, depth + 1);
    }

    @Override
    public String expr() {
        return "{" + getTagName() + ":" + AttributeUtils.extractAttributes(this) + getChildren().expr() + "}";
    }

    public static <C extends WrapperComponent> Parser<C> makeParser(Class<C> clz) {
        Constructor<C> constructor;
        try {
            constructor = clz.getDeclaredConstructor();
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
