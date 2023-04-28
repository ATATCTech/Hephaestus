package com.atatctech.hephaestus.attribute;

import org.jetbrains.annotations.NotNull;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface Attribute {
    /**
     * Specify the name appears in the expression.
     * @return the name, the name of the field is used instead if empty
     */
    @NotNull String name() default "";

    @NotNull Class<? extends TargetConstructor<?>> targetConstructor() default DefaultTargetConstructor.class;
}
