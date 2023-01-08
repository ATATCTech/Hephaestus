package com.atatc.hephaestus.component;

import com.atatc.hephaestus.parser.Parser;
import com.atatc.hephaestus.render.HTMLRender;
import org.jsoup.nodes.Element;

@ComponentConfig(tagName = "cd")
public class Code extends Component {
    public static HTMLRender<Code> HTML_RENDER;
    public static Parser<Code> PARSER;

    static {
        HTML_RENDER = code -> new Element("pre").appendChild(new Element("code").text(code.getCode()).attr("class", "language-" + code.getLanguage()));
        PARSER = expr -> {
            Code component = new Code();
            AttributesUtils.AttributesAndBody attributesAndBody = AttributesUtils.searchAttributesInExpr(expr);
            String code;
            if (attributesAndBody == null) {
                code = expr;
            } else {
                code = attributesAndBody.bodyExpr();
                AttributesUtils.injectAttributes(component, attributesAndBody.attributesExpr());
            }
            component.setCode(code);
            return component;
        };
    }

    protected String code;

    @Attribute
    protected String language;

    public Code() {}

    public Code(String code) {
        setCode(code);
    }

    public Code(String code, String language) {
        setCode(code);
        setLanguage(language);
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public void setLanguage(String language) {
        if (!language.equals("plaintext")) this.language = language;
    }

    public String getLanguage() {
        return language == null ? "plaintext" : language;
    }

    @Override
    public String expr() {
        return "{" + getTagName() + ":" + AttributesUtils.extractAttributes(this) + getCode() + "}";
    }

    @Override
    public Element toHTML() {
        return HTML_RENDER.render(this);
    }
}
