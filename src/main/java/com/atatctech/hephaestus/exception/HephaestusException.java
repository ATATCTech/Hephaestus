package com.atatctech.hephaestus.exception;

import org.jetbrains.annotations.NotNull;

public class HephaestusException extends Exception {
    public HephaestusException() {}
    public HephaestusException(@NotNull String msg) {
        super(msg);
    }
}
