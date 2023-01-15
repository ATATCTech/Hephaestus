package com.atatc.hephaestus.component;

public class UnsupportedComponent extends Component {
    public String tagName = "undefined";
    public String expr = "";
    public String inner = "";

    @Override
    public String expr() {
        return expr;
    }
}
