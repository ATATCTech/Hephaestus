package com.atatctech.hephaestus;

public record Color(String color) {
    public static final Color POSITIVE = new Color("POSITIVE");
    public static final Color NEGATIVE = new Color("NEGATIVE");
    @Override
    public String toString() {
        return color();
    }
}
