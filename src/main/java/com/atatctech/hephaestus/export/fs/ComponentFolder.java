package com.atatctech.hephaestus.export.fs;

public class ComponentFolder {
    protected String src;
    protected String attributes = ".attr";
    public ComponentFolder(String src, String attributes) {
        this.src = src;
        this.attributes = attributes;
    }

}
