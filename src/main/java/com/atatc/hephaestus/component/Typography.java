package com.atatc.hephaestus.component;

import com.atatc.hephaestus.Color;
import com.atatc.hephaestus.parser.Parser;
import com.atatc.hephaestus.render.HTMLRender;
import org.jsoup.nodes.Element;

@ComponentConfig(tagName = "typo")
public class Typography extends WrapperComponent {
    public static HTMLRender<Typography> HTML_RENDER;
    public static Parser<Typography> PARSER;

    static {
        HTML_RENDER = typography -> {
            Element element = new Element("p").appendChild(typography.getChildren().toHTML());
            element.attr("style", "color:" + typography.getColor() + ";backgroundColor:" + typography.getBackgroundColor() + ";");
            return element;
        };
        PARSER = WrapperComponent.makeParser(Typography.class);
    }

    @Attribute
    protected Color color;

    @Attribute
    protected Color backgroundColor;

    public Typography() {}

    public Typography(MultiComponents children) {
        super(children);
    }

    public Typography(Component... children) {
        super(new MultiComponents(children));
    }

    public Typography(String text) {
        super(new MultiComponents(new Text(text)));
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
    public Element toHTML() {
        return HTML_RENDER.render(this);
    }
}
