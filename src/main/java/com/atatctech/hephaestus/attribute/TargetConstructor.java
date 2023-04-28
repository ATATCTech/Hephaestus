package com.atatctech.hephaestus.attribute;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface TargetConstructor<T> {
    @Nullable T cast(@NotNull String v);
}
