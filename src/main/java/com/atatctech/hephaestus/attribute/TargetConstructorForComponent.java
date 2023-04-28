package com.atatctech.hephaestus.attribute;

import com.atatctech.hephaestus.Hephaestus;
import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.exception.BadFormat;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TargetConstructorForComponent implements TargetConstructor<Component> {
    @Override
    public @Nullable Component cast(@NotNull String v) {
        try {
            return Hephaestus.parseExpr(v);
        } catch (BadFormat ignored) {
            return null;
        }
    }
}
