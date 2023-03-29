package com.atatctech.hephaestus.component;

import org.jetbrains.annotations.NotNull;

public class UnsupportedComponent extends Component {
    public @NotNull String tagName = "undefined";
    public @NotNull String fullExpr = "";
    public @NotNull String inner = "";

    @Override
    public @NotNull String expr() {
        return fullExpr;
    }
}
