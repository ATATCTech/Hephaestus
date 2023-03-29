package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.Hephaestus;
import com.atatctech.hephaestus.export.fs.Transform;
import com.atatctech.hephaestus.parser.Parser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Transform.RequireTransform
@ComponentConfig(tagName = "html")
public class HTMLBlock extends Component {
    public static Parser<HTMLBlock> PARSER;

    public static final Transform TRANSFORM;

    static {
        PARSER = expr -> new HTMLBlock((Text) Hephaestus.parseExpr(expr));
        TRANSFORM = new Transform() {
            @Override
            public @NotNull String beforeWrite(@NotNull Component component) {
                if (component instanceof HTMLBlock htmlBlock) return htmlBlock.getHTML().getText();
                return super.beforeWrite(component);
            }

            @Override
            public HTMLBlock afterRead(String content) {
                return new HTMLBlock(new Text(content));
            }
        };
    }

    protected @Nullable Text html;

    public HTMLBlock() {}

    public HTMLBlock(@Nullable Text html) {
        setHTML(html);
    }

    public void setHTML(@Nullable Text html) {
        this.html = html;
    }

    public @NotNull Text getHTML() {
        return html == null ? new Text() : html;
    }

    @Override
    public @NotNull String expr() {
        return generateExpr(getHTML().expr());
    }
}
