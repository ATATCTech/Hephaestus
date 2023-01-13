package com.atatc.hephaestus.html;

public class HTMLString extends HTMLElement {
    protected final String string;
    public HTMLString(String string) {
        super("");
        this.string = string;
    }

    @Override
    public String toString() {
        return string;
    }
}
