package com.atatctech.hephaestus.parser;

import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.exception.BadFormat;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Parser<C extends Component> {
    C parse(@NotNull String expr) throws BadFormat;
}
