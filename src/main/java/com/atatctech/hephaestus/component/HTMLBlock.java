package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.parser.Parser;

@ComponentConfig(tagName = "html")
public class HTMLBlock extends Component {
    public static Parser<HTMLBlock> PARSER;

    static {
        PARSER = expr -> new HTMLBlock(Text.decompile(expr));
    }

    protected String html;

    public HTMLBlock() {}

    public HTMLBlock(String html) {
        setHTML(html);
    }

    public void setHTML(String html) {
        this.html = html;
    }

    public String getHTML() {
        return html;
    }

    @Override
    public String expr() {
        return "{" + getTagName() + ":" + Text.compile(getHTML()) + "}";
    }
}
