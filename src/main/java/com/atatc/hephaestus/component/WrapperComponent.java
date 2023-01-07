package com.atatc.hephaestus.component;

public abstract class WrapperComponent extends Component {
    protected MultiComponents children;

    public WrapperComponent(MultiComponents children) {
        setChildren(children);
    }

    public WrapperComponent(Component... children) {
        setChildren(children);
    }

    public void setChildren(MultiComponents children) {
        this.children = children;
    }

    public void setChildren(Component... children) {
        setChildren(new MultiComponents(children));
    }

    public MultiComponents getChildren() {
        return children;
    }
}
