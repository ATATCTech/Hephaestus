package com.atatc.hephaestus.exception;

import java.lang.annotation.Annotation;

public class MissingAnnotationException extends HephaestusRuntimeException {
    public MissingAnnotationException(Class<?> clz, Class<? extends Annotation> annotation) {
        super("Missing annotation " + annotation.getName() + " from " + clz.getName() + ".");
    }
}
