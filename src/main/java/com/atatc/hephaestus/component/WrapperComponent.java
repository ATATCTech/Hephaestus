package com.atatc.hephaestus.component;

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

    @Override
    public String expr() {
        return "{" + getTagName() + ":" + AttributesUtils.extractAttributes(this) + getChildren().expr() + "}";
    }
}
