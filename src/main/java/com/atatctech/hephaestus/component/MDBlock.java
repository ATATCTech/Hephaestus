package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.Hephaestus;
import com.atatctech.hephaestus.export.fs.Transform;
import com.atatctech.hephaestus.parser.Parser;

@Transform.RequireTransform
@ComponentConfig(tagName = "md")
public class MDBlock extends Component {
    public static Parser<MDBlock> PARSER;

    public static final Transform TRANSFORM;

    static {
        PARSER = expr -> new MDBlock((Text) Hephaestus.parseExpr(expr));
        TRANSFORM = new Transform() {
            @Override
            public String beforeWrite(Component component) {
                if (component instanceof MDBlock mdBlock) return mdBlock.getMarkdown().getText();
                return super.beforeWrite(component);
            }

            @Override
            public MDBlock afterRead(String content) {
                return new MDBlock(new Text(content));
            }
        };
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
        return generateExpr(getMarkdown().expr());
    }
}
