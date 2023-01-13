package com.atatc.hephaestus.component;

import com.atatc.hephaestus.html.HTMLElement;

public class UnsupportedComponent extends Component {
    public String tagName = "undefined";
    public String expr = "";
    public String inner = "";

    @Override
    public String expr() {
        return expr;
    }

    @Override
    public HTMLElement toHTML() {
        throw new UnsupportedOperationException();
    }
}
