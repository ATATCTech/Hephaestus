package com.atatc.hephaestus.component;

import com.atatc.hephaestus.Hephaestus;
import com.atatc.hephaestus.function.Consumer;
import com.atatc.hephaestus.parser.Parser;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class WrapperComponent extends Component {
    protected MultiComponents children;

    public WrapperComponent(MultiComponents children) {
        setChildren(children);
    }

    public WrapperComponent(Component... children) {
        setChildren(children);
    }

    public void setChildrenComponent(MultiComponents children) {
        this.children = children;
    }

    public void setChildren(MultiComponents children) {
        setChildrenComponent(children);
    }

    public void setChildren(Component... children) {
        setChildren(new MultiComponents(children));
    }

    public MultiComponents getChildren() {
        return children;
    }

    public void appendChild(Component child) {
        getChildren().add(child);
    }

    public Component child(int index) {
        return getChildren().get(index);
    }

    public void removeChild(Component child) {
        getChildren().remove(child);
    }

    public void removeChild(int index) {
        getChildren().remove(index);
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
    public String expr() {
        return "{" + getTagName() + ":" + AttributesUtils.extractAttributes(this) + getChildren().expr() + "}";
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
                Component children = Hephaestus.parseExpr(body);
                if (children instanceof MultiComponents) component.setChildrenComponent((MultiComponents) children);
                else component.setChildren(children);
                return component;
            } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }
}
