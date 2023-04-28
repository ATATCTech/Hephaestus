package com.atatctech.hephaestus.attribute;

import org.jetbrains.annotations.NotNull;

public class DefaultTargetConstructor implements TargetConstructor<String> {
    @Override
    public @NotNull String cast(@NotNull String v) {
        return v;
    }
}
