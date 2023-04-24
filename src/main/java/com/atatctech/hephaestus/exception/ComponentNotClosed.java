package com.atatctech.hephaestus.exception;

import org.jetbrains.annotations.NotNull;

public class ComponentNotClosed extends BadFormat {
    public ComponentNotClosed(@NotNull String loc) {
        super("Component not closed.", loc);
    }
}
