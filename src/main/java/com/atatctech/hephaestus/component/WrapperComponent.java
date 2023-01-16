package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.Hephaestus;
import com.atatctech.hephaestus.attribute.AttributesUtils;
import com.atatctech.hephaestus.function.Consumer;
import com.atatctech.hephaestus.parser.Parser;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class WrapperComponent extends Component {
    @NotNull
    protected MultiComponent children = new MultiComponent();

    public WrapperComponent(@NotNull MultiComponent children) {
        setChildren(children);
    }

    public WrapperComponent() {
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
        return children.get(index);
    }

    public void removeChild(Component child) {
        children.remove(child);
    }

    public void removeChild(int index) {
        children.remove(index);
    }

    public String getText() {
        StringBuilder text = new StringBuilder();
        children.forEach((component, depth) -> {
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
        children.forEach((component, depth) -> {
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
        children.forEach(action, depth + 1);
    }

    @Override
    public String expr() {
        return "{" + getTagName() + ":" + AttributesUtils.extractAttributes(this) + children.expr() + "}";
    }

    public static <C extends WrapperComponent> Parser<C> makeParser(Class<C> clz) {
        Constructor<C> constructor;
        try {
            constructor = clz.getDeclaredConstructor();
        } catch (NoSuchMethodException ignored) {
            return null;
        }
        return expr -> {
            try {
                C component = constructor.newInstance();
                AttributesUtils.AttributesAndBody attributesAndBody = AttributesUtils.searchAttributesInExpr(expr);
                String body;
                if (attributesAndBody == null) body = expr;
                else {
                    body = attributesAndBody.bodyExpr();
                    AttributesUtils.injectAttributes(component, attributesAndBody.attributesExpr());
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
