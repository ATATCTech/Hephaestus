package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.parser.Parser;

@ComponentConfig(tagName = "md")
public class MDBlock extends Component {
    public static Parser<MDBlock> PARSER;

    static {
        PARSER = expr -> new MDBlock(Text.PARSER.parse(expr));
    }

    protected Text markdown;

    public MDBlock() {}

    public MDBlock(Text markdown) {
        setMarkdown(markdown);
    }

    public void setMarkdown(Text html) {
        this.markdown = html;
    }

    public Text getMarkdown() {
        return markdown;
    }

    @Override
    public String expr() {
        return "{" + getTagName() + ":" + getMarkdown().expr() + "}";
    }
}
