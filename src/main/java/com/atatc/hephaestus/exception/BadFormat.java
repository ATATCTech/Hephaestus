package com.atatc.hephaestus.exception;

public class BadFormat extends Exception {
    public BadFormat(String msg, String loc) {
        super(msg + " Check here: " + (loc.length() < 24 ? loc : loc.substring(0, 10) + "..." + loc.substring(loc.length() - 10)));
    }
}
