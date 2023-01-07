package com.atatc.hephaestus.component;

import org.jsoup.nodes.Element;

public class UnsupportedComponent extends Component {
    public String tagName = "undefined";
    public String expr = "";

    @Override
    public String expr() {
        return expr;
    }

    @Override
    public Element toHTML() {
        throw new UnsupportedOperationException();
    }
}
