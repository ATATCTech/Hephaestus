package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.parser.Parser;

@ComponentConfig(tagName = "html")
public class HTMLBlock extends Component {
    public static Parser<HTMLBlock> PARSER;

    static {
        PARSER = expr -> new HTMLBlock(Text.PARSER.parse(expr));
    }

    protected Text html;

    public HTMLBlock() {}

    public HTMLBlock(Text html) {
        setHTML(html);
    }

    public void setHTML(Text html) {
        this.html = html;
    }

    public Text getHTML() {
        return html;
    }

    @Override
    public String expr() {
        return "{" + getTagName() + ":" + getHTML().expr() + "}";
    }
}
