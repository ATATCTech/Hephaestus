package com.atatctech.hephaestus;

import org.jetbrains.annotations.NotNull;

public interface Compilable {
    void compile(@NotNull Compiler compiler);
}
