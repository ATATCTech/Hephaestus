package com.atatctech.hephaestus.exception;

public class BadFormat extends HephaestusException {
    public BadFormat(String msg, String loc) {
        super(msg + (msg.endsWith(" ") ? "Check here: " : " Check here: ") + (loc.length() < 24 ? loc : loc.substring(0, 10) + "..." + loc.substring(loc.length() - 10)));
    }
}
