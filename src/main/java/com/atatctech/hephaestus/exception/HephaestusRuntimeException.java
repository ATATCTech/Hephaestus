package com.atatctech.hephaestus.exception;

import org.jetbrains.annotations.NotNull;

public class HephaestusRuntimeException extends RuntimeException {
    public HephaestusRuntimeException() {}

    public HephaestusRuntimeException(@NotNull String msg) {
        super(msg);
    }
}
