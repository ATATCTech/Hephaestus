package com.atatctech.hephaestus.parser;

import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.exception.BadFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface Parser<C extends Component> {
    @Nullable C parse(@NotNull String expr) throws BadFormat;
}
