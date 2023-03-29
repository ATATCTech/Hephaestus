package com.atatctech.hephaestus.component;


import com.atatctech.hephaestus.parser.Parser;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@ComponentConfig(tagName = "v")
public class Version extends WrapperComponent {
    public static class Serial {
        protected final @NotNull Object[] args;

        public Serial(Object... args) {
            this.args = args;
        }

        public boolean equals(@NotNull Serial serial, boolean sequential) {
            if (args.length != serial.args.length) return false;
            List<Object> argsList = Arrays.asList(serial.args);
            for (int i = 0; i < args.length; i++) {
                if (sequential) if (!Objects.equals(args[i], serial.args[i])) return false;
                else if (!argsList.contains(args[i])) return false;
            }
            return true;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Serial serial)) return false;
            return equals(serial, true);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(args);
        }
    }

    public static Parser<Version> PARSER;

    static {
        PARSER = expr -> null;
    }

    protected Serial serial;

    public Version(Serial serial) {
        setSerial(serial);
    }

    public Version(String serial) {
        setSerial(new Serial(serial));
    }

    public void setSerial(Serial serial) {
        this.serial = serial;
    }

    public Serial getSerial() {
        return serial;
    }

    @Override
    public @NotNull String expr() {
        return "";
    }
}
