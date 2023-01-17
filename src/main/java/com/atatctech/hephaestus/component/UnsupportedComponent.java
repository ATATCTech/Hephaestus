package com.atatctech.hephaestus.component;

public class UnsupportedComponent extends Component {
    public String tagName = "undefined";
    public String fullExpr = "";
    public String inner = "";

    @Override
    public String expr() {
        return fullExpr;
    }
}
