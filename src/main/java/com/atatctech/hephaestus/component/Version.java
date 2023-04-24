package com.atatctech.hephaestus.component;


import com.atatctech.hephaestus.attribute.Attribute;
import com.atatctech.hephaestus.parser.Parser;
import org.jetbrains.annotations.NotNull;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ComponentConfig(tagName = "v")
public class Version extends WrapperComponent {
    public static class Serial implements Serializable {
        protected final @NotNull Object[] args;

        public Serial(Object... args) {
            this.args = args;
        }

        public boolean equals(@NotNull Serial o, boolean sequential) {
            if (args.length != o.args.length) return false;
            List<Object> argsList = Arrays.asList(o.args);
            for (int i = 0; i < args.length; i++) {
                if (sequential) {
                    if (!Objects.equals(args[i], o.args[i])) return false;
                } else if (!argsList.contains(args[i])) return false;
            }
            return true;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o instanceof Serial serial) return equals(serial, true);
            return false;
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(args);
        }
    }

    public static @NotNull Parser<Version> PARSER;

    static {
        PARSER = WrapperComponent.makeParser(Version.class);
    }

    @Attribute
    protected @NotNull Serial serial = new Serial("undefined");

    public Version() {}

    public Version(@NotNull Serial serial) {
        setSerial(serial);
    }

    public Version(@NotNull String serial) {
        setSerial(new Serial(serial));
    }

    public void setSerial(@NotNull Serial serial) {
        this.serial = serial;
    }

    public @NotNull Serial getSerial() {
        return serial;
    }
}
