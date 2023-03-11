package com.atatctech.hephaestus.export.fs;

import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.component.WrapperComponent;
import com.atatctech.hephaestus.config.Config;
import com.atatctech.hephaestus.exception.HephaestusException;
import com.atatctech.hephaestus.exception.HephaestusRuntimeException;
import com.atatctech.packages.basics.Basics;

import java.io.File;
import java.io.IOException;

public record ComponentFile(Component component, File file) {
    public ComponentFile {
        if (component instanceof WrapperComponent)
            throw new HephaestusRuntimeException("Wrapper components should be converted into `ComponentFolder`.");
    }

    public boolean write() {
        Transform transform = Transform.DEFAULT_TRANSFORM;
        Class<? extends Component> clz = component.getClass();
        if (clz.isAnnotationPresent(Transform.RequireTransform.class)) transform = Transform.getTransform(clz);
        String suffix = component.getTagName();
        if (suffix.equals("undefined")) suffix = "hexpr";
        suffix = "." + suffix;
        String filename = file.getAbsolutePath();
        return Basics.NativeHandler.writeFile(filename.endsWith(suffix) ? filename : filename + suffix, transform.beforeWrite(component));
    }

    public static ComponentFile read(File file) throws IOException, HephaestusException {
        String filename = file.getName();
        Transform transform = Config.getInstance().getTransform(filename.substring(filename.lastIndexOf(".") + 1));
        return new ComponentFile((transform == null ? Transform.DEFAULT_TRANSFORM : transform).afterRead(Basics.NativeHandler.readFile(file)), file);
    }
}
