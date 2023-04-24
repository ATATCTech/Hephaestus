package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.Hephaestus;
import com.atatctech.hephaestus.export.fs.Transform;
import com.atatctech.hephaestus.parser.Parser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Transform.RequireTransform
@ComponentConfig(tagName = "md")
public class MDBlock extends Component {
    public static @NotNull Parser<MDBlock> PARSER;

    public static final Transform TRANSFORM;

    static {
        PARSER = expr -> new MDBlock((Text) Hephaestus.parseExpr(expr));
        TRANSFORM = new Transform() {
            @Override
            public @NotNull String beforeWrite(@NotNull Component component) {
                if (component instanceof MDBlock mdBlock) return mdBlock.getMarkdown().getText();
                return super.beforeWrite(component);
            }

            @Override
            public MDBlock afterRead(@NotNull String content) {
                return new MDBlock(new Text(content));
            }
        };
    }

    protected @Nullable Text markdown;

    public MDBlock() {}

    public MDBlock(@Nullable Text markdown) {
        setMarkdown(markdown);
    }

    public void setMarkdown(@Nullable Text markdown) {
        this.markdown = markdown;
    }

    public @NotNull Text getMarkdown() {
        return markdown == null ? new Text() : markdown;
    }

    @Override
    public @NotNull String expr() {
        return generateExpr(getMarkdown().expr());
    }
}
