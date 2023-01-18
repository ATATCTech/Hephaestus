package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.parser.Parser;

@ComponentConfig(tagName = "md")
public class MDBlock extends Component {
    public static Parser<MDBlock> PARSER;

    static {
        PARSER = expr -> new MDBlock(Text.decompile(expr));
    }

    protected String markdown;

    public MDBlock() {}

    public MDBlock(String markdown) {
        setMarkdown(markdown);
    }

    public void setMarkdown(String html) {
        this.markdown = html;
    }

    public String getMarkdown() {
        return markdown;
    }

    @Override
    public String expr() {
        return "{" + getTagName() + ":" + Text.compile(getMarkdown()) + "}";
    }
}
