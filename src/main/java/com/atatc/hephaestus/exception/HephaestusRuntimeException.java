package com.atatc.hephaestus.exception;

public class HephaestusRuntimeException extends RuntimeException {
    public HephaestusRuntimeException() {}

    public HephaestusRuntimeException(String msg) {
        super(msg);
    }
}
