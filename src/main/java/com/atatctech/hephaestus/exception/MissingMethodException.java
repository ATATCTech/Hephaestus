package com.atatctech.hephaestus.exception;

import org.jetbrains.annotations.NotNull;

public class MissingMethodException extends HephaestusRuntimeException {
    public MissingMethodException(@NotNull Class<?> clz, @NotNull String methodName) {
        super("Missing method " + methodName + " in " + clz.getName() + ".");
    }
}
