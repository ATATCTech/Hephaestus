package com.atatc.hephaestus.component;

import com.atatc.hephaestus.parser.Parser;
import com.atatc.hephaestus.render.HTMLRender;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;

@ComponentConfig(tagName = "html")
public class HTMLBlock extends Component {
    public static HTMLRender<HTMLBlock> HTML_RENDER;
    public static Parser<HTMLBlock> PARSER;

    static {
        HTML_RENDER = htmlBlock -> Jsoup.parse(htmlBlock.getHtml());
        PARSER = HTMLBlock::new;
    }

    protected String html;

    public HTMLBlock() {}

    public HTMLBlock(String html) {
        this.html = html;
    }

    public void setHtml(String html) {
        this.html = html;
    }

    public String getHtml() {
        return html;
    }

    @Override
    public String expr() {
        return "{" + getTagName() + ":" + getHtml() + "}";
    }

    @Override
    public Element toHTML() {
        return HTML_RENDER.render(this);
    }
}
