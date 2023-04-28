package com.atatctech.hephaestus.component;


import com.atatctech.hephaestus.attribute.Attribute;
import com.atatctech.hephaestus.parser.Parser;
import org.jetbrains.annotations.NotNull;

@ComponentConfig(tagName = "v")
public class Version extends WrapperComponent {
    public static @NotNull Parser<Version> PARSER;

    static {
        PARSER = WrapperComponent.makeParser(Version.class);
    }

    @Attribute
    protected @NotNull String serial = "undefined";

    public Version() {}

    public Version(@NotNull String serial) {
        setSerial(serial);
    }

    public void setSerial(@NotNull String serial) {
        this.serial = serial;
    }

    public @NotNull String getSerial() {
        return serial;
    }
}
