package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.parser.Parser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@ComponentConfig(tagName = "ref")
public class Ref extends Component {
    public static @NotNull Parser<Ref> PARSER;

    static {
        PARSER = Ref::new;
    }
    protected @Nullable Component to;

    public Ref(@Nullable String id) {
        setId(id);
    }

    public void referTo(@Nullable Component real) {
        to = real;
    }

    @Override
    public @NotNull String expr() {
        return to == null ? generateExpr("") : to.expr();
    }
}
