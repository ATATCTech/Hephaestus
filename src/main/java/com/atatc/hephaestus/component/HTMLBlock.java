package com.atatc.hephaestus.component;

import com.atatc.hephaestus.html.HTMLElement;
import com.atatc.hephaestus.html.HTMLString;
import com.atatc.hephaestus.parser.Parser;
import com.atatc.hephaestus.render.HTMLRender;

@ComponentConfig(tagName = "html")
public class HTMLBlock extends Component {
    public static HTMLRender<HTMLBlock> HTML_RENDER;
    public static Parser<HTMLBlock> PARSER;

    static {
        HTML_RENDER = htmlBlock -> new HTMLString(htmlBlock.getHTML());
        PARSER = HTMLBlock::new;
    }

    protected String html;

    public HTMLBlock() {}

    public HTMLBlock(String html) {
        this.html = html;
    }

    public void setHTML(String html) {
        this.html = html;
    }

    public String getHTML() {
        return html;
    }

    @Override
    public String expr() {
        return "{" + getTagName() + ":" + getHTML() + "}";
    }

    @Override
    public HTMLElement toHTML() {
        return HTML_RENDER.render(this);
    }
}
