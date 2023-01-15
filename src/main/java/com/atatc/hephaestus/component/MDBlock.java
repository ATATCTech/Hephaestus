package com.atatc.hephaestus.component;

import com.atatc.hephaestus.parser.Parser;

@ComponentConfig(tagName = "md")
public class MDBlock extends Component {
    public static Parser<MDBlock> PARSER;

    static {
        PARSER = MDBlock::new;
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
        return "{" + getTagName() + ":" + getMarkdown() + "}";
    }
}
