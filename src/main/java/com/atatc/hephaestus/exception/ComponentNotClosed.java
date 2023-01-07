package com.atatc.hephaestus.exception;

public class ComponentNotClosed extends BadFormat {
    public ComponentNotClosed(String loc) {
        super("Component not closed", loc);
    }
}
