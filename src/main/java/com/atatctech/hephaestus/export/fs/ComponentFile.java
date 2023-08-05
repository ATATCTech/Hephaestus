package com.atatctech.hephaestus.export.fs;

import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.component.WrapperComponent;
import com.atatctech.hephaestus.config.Config;
import com.atatctech.hephaestus.exception.HephaestusException;
import com.atatctech.hephaestus.exception.HephaestusRuntimeException;
import com.atatctech.packages.basics.Basics;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public record ComponentFile(@Nullable Component component) implements FileSystemEntity {
    public ComponentFile {
        if (component instanceof WrapperComponent) throw new HephaestusRuntimeException("Wrapper components should be converted into `ComponentFolder`.");
    }

    @Override
    public boolean write(@NotNull File file) {
        return write(file.getAbsolutePath());
    }

    @Override
    public boolean write(@NotNull String filename) {
        if (component == null) return false;
        Class<? extends Component> clz = component.getClass();
        Transform transform = Transform.getTransform(clz);
        String suffix = component.getTagName();
        if (suffix.equals("undefined")) suffix = "hexpr";
        suffix = "." + suffix;
        return Basics.NativeHandler.writeFile(filename.endsWith(suffix) ? filename : filename + suffix, (transform == null ? new Transform() : transform).beforeWrite(component));
    }

    public static @NotNull ComponentFile read(@NotNull File file) throws IOException, HephaestusException {
        return read(file.getAbsolutePath());
    }

    public static @NotNull ComponentFile read(@NotNull String filename) throws IOException, HephaestusException {
        Transform transform = Config.getTransform(filename.substring(filename.lastIndexOf(".") + 1));
        return new ComponentFile((transform == null ? new Transform() : transform).afterRead(Basics.NativeHandler.readFile(filename)));
    }
}
