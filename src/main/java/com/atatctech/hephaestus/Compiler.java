package com.atatctech.hephaestus;


import com.atatctech.hephaestus.component.Ref;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Compiler {
    void compile(@NotNull Ref @NotNull ... refs);
}
