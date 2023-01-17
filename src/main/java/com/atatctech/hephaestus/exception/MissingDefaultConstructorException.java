package com.atatctech.hephaestus.exception;

public class MissingDefaultConstructorException extends RuntimeException {
    public MissingDefaultConstructorException(Class<?> clz) {
        super("Missing default constructor in " + clz.getName() + ".");
    }
}
