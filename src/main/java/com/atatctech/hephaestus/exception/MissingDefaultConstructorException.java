package com.atatctech.hephaestus.exception;

import org.jetbrains.annotations.NotNull;

public class MissingDefaultConstructorException extends RuntimeException {
    public MissingDefaultConstructorException(@NotNull Class<?> clz) {
        super("Missing default constructor in " + clz.getName() + ".");
    }
}
