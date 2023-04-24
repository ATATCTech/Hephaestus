package com.atatctech.hephaestus.exception;

import org.jetbrains.annotations.NotNull;

public class BadFormat extends HephaestusException {
    public BadFormat(@NotNull String msg, @NotNull String loc) {
        super(msg + (msg.endsWith(" ") ? "Check here: " : " Check here: ") + (loc.length() < 24 ? loc : loc.substring(0, 10) + "..." + loc.substring(loc.length() - 10)));
    }
}
