package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.parser.Parser;

@ComponentConfig(tagName = "ref")
public class Ref extends Component {
    public static Parser<Ref> PARSER;

    static {
        PARSER = Ref::new;
    }
    protected Component to;

    public Ref(String id) {
        setId(id);
    }

    public void referTo(Component real) {
        to = real;
    }

    @Override
    public String expr() {
        return to == null ? "{" + getTagName() + ":" + getId() + "}" : to.expr();
    }
}
