package com.atatc.hephaestus.parser;

import com.atatc.hephaestus.component.Component;
import com.atatc.hephaestus.exception.BadFormat;

@FunctionalInterface
public interface Parser<C extends Component> {
    C parse(String expr) throws BadFormat;
}
