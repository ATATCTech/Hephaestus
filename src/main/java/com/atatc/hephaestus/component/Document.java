package com.atatc.hephaestus.component;

import com.atatc.hephaestus.parser.Parser;
import com.atatc.hephaestus.parser.WrapperComponentParser;
import com.atatc.hephaestus.render.HTMLRender;
import org.jsoup.nodes.Element;

@ComponentConfig(tagName = "doc")
public class Document extends WrapperComponent {
    public static HTMLRender<Document> HTML_RENDER;
    public static Parser<Document> PARSER;

    static {
        HTML_RENDER = document -> new Element("div").append(document.getHTMLHeader()).appendChild(document.getChildren().toHTML());
        PARSER = WrapperComponentParser.makeParser(Document.class);
    }

    @Attribute
    protected String htmlHeader;

    @Attribute
    protected String name = "untitled";

    public Document() {}

    public Document(MultiComponents body) {
        super(body);
    }

    public Document(Component... body) {
        super(body);
    }

    public void setHTMLHeader(String htmlHeader) {
        if (!htmlHeader.equals(DEFAULT_HEADER)) this.htmlHeader = htmlHeader;
    }

    public String getHTMLHeader() {
        return htmlHeader == null ? DEFAULT_HEADER : htmlHeader;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public Element toHTML() {
        return HTML_RENDER.render(this);
    }

    public static final String DEFAULT_HEADER = "<link rel=\"stylesheet\" href=\"/path/to/styles/default.min.css\"><script src=\"/path/to/highlight.min.js\"></script>";
}
