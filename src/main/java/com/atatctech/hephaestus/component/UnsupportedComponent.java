package com.atatctech.hephaestus.component;

import org.jetbrains.annotations.NotNull;

public class UnsupportedComponent extends Component {
    @NotNull
    public String tagName = "undefined";
    @NotNull
    public String fullExpr = "";
    @NotNull
    public String inner = "";

    @Override
    public @NotNull String expr() {
        return fullExpr;
    }
}
