package com.atatc.hephaestus.component;

import com.atatc.hephaestus.parser.Parser;
import com.atatc.hephaestus.parser.WrapperComponentParser;
import com.atatc.hephaestus.render.HTMLRender;
import org.jsoup.nodes.Element;

@ComponentConfig(tagName = "tt")
public class Title extends Typography {
    public static HTMLRender<Title> HTML_RENDER;
    public static Parser<Title> PARSER;

    static {
        HTML_RENDER = title -> new Element("h" + title.getLevel()).appendChild(title.getChildren().toHTML());
        PARSER = WrapperComponentParser.makeParser(Title.class);
    }

    @Attribute
    protected Integer level;

    public Title() {}

    public Title(int level, MultiComponents children) {
        super(children);
        setLevel(level);
    }

    public Title(int level, Component... children) {
        super(children);
        setLevel(level);
    }

    public Title(int level, String text) {
        super(text);
        setLevel(level);
    }

    public Title(int level, Text text) {
        super(text);
        setLevel(level);
    }

    public Title(MultiComponents children) {
        super(children);
    }

    public Title(Component... children) {
        super(children);
    }

    public Title(String text) {
        super(text);
    }

    public Title(Text text) {
        super(text);
    }

    public void setLevel(int level) {
        if (level < 1 || level > 5) throw new IllegalArgumentException("`level` must be between 1 and 5.");
        if (level != 1) this.level = level;
    }

    public int getLevel() {
        return level == null ? 1 : level;
    }

    @Override
    public Element toHTML() {
        return HTML_RENDER.render(this);
    }
}
