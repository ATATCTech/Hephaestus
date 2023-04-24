package com.atatctech.hephaestus.exception;

import org.jetbrains.annotations.NotNull;

public class MissingFieldException extends HephaestusRuntimeException {
    public MissingFieldException(@NotNull Class<?> clz, @NotNull String fieldName) {
        super("Missing field " + fieldName + " in " + clz.getName() + ".");
    }
}
