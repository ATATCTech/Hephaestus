package com.atatc.hephaestus.exception;

public class MissingFieldException extends HephaestusRuntimeException {
    public MissingFieldException(Class<?> clz, String fieldName) {
        super("Missing field " + fieldName + " from " + clz.getName() + ".");
    }
}
