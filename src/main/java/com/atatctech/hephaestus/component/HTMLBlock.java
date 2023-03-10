package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.Hephaestus;
import com.atatctech.hephaestus.export.fs.ComponentFile;
import com.atatctech.hephaestus.parser.Parser;

@ComponentFile.RequireTransform
@ComponentConfig(tagName = "html")
public class HTMLBlock extends Component {
    public static Parser<HTMLBlock> PARSER;

    public static final ComponentFile.Transform TRANSFORM;

    static {
        PARSER = expr -> new HTMLBlock((Text) Hephaestus.parseExpr(expr));
        TRANSFORM = new ComponentFile.Transform() {
            @Override
            public String beforeWrite(Component component) {
                if (component instanceof HTMLBlock htmlBlock) return htmlBlock.getHTML().getText();
                return ComponentFile.DEFAULT_TRANSFORM.beforeWrite(component);
            }

            @Override
            public Component afterRead(String content) {
                return new HTMLBlock(new Text(content));
            }
        };
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
        return generateExpr(getHTML().expr());
    }
}
