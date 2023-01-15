package com.atatctech.hephaestus.parser;

import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.exception.BadFormat;

@FunctionalInterface
public interface Parser<C extends Component> {
    C parse(String expr) throws BadFormat;
}
