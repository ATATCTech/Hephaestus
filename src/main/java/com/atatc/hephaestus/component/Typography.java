package com.atatc.hephaestus.component;

import com.atatc.hephaestus.Color;
import com.atatc.hephaestus.html.HTMLElement;
import com.atatc.hephaestus.parser.Parser;
import com.atatc.hephaestus.render.HTMLRender;

@ComponentConfig(tagName = "typo")
public class Typography extends WrapperComponent {
    public static HTMLRender<Typography> HTML_RENDER;
    public static Parser<Typography> PARSER;

    static {
        HTML_RENDER = typography -> new HTMLElement("p").appendChild(typography.getChildren().toHTML()).attr("style", "color:" + typography.getColor() + ";backgroundColor:" + typography.getBackgroundColor() + ";");
        PARSER = WrapperComponent.makeParser(Typography.class);
    }

    @Attribute
    protected Color color;

    @Attribute
    protected Color backgroundColor;

    public Typography() {}

    public Typography(MultiComponent children) {
        super(children);
    }

    public Typography(Component... children) {
        super(new MultiComponent(children));
    }

    public Typography(String text) {
        super(new MultiComponent(new Text(text)));
    }

    public void setColor(Color color) {
        if (color != Color.POSITIVE) this.color = color;
    }

    public Color getColor() {
        return color == null ? Color.POSITIVE : color;
    }

    public void setBackgroundColor(Color backgroundColor) {
        if (backgroundColor != Color.NEGATIVE) this.backgroundColor = backgroundColor;
    }

    public Color getBackgroundColor() {
        return backgroundColor == null ? Color.NEGATIVE : backgroundColor;
    }

    @Override
    public HTMLElement toHTML() {
        return HTML_RENDER.render(this);
    }
}
