package com.atatctech.hephaestus.export.fs;

import com.atatctech.hephaestus.attribute.AttributeUtils;
import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.component.WrapperComponent;
import com.atatctech.hephaestus.exception.HephaestusException;
import com.atatctech.hephaestus.exception.HephaestusRuntimeException;
import com.atatctech.hephaestus.exception.MissingDefaultConstructorException;
import com.atatctech.hephaestus.utils.Utils;
import com.atatctech.packages.basics.Basics;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class ComponentFolder implements FileSystemEntity {
    protected final @NotNull WrapperComponent component;

    protected final File wrapperFile;

    public ComponentFolder(@NotNull WrapperComponent component, File wrapperFile) {
        this.component = component;
        this.wrapperFile = wrapperFile;
    }

    public ComponentFolder(@NotNull WrapperComponent component) {
        this.component = component;
        wrapperFile = new File(".wrapper");
    }

    @Override
    public @NotNull WrapperComponent component() {
        return component;
    }

    public boolean writeWrapper(String dirPath) {
        return Basics.NativeHandler.writeFile(dirPath + "/" + wrapperFile.getName(), component.getClass().getName() + "@" + AttributeUtils.extractAttributes(component));
    }

    protected static boolean writeSub(@NotNull Component component, @NotNull String dirPath, int index, @NotNull String prefix) {
        Transform transform = Transform.getTransform(component.getClass());
        return (component instanceof WrapperComponent wrapperComponent ? new ComponentFolder(wrapperComponent) : new ComponentFile(component)).write(dirPath + "/" + prefix + (transform == null ? new Transform() : transform).interfereFilename(index, component));
    }

    protected static boolean writeSub(@NotNull Component component, @NotNull String dirPath, int index) {
        return writeSub(component, dirPath, index, "");
    }

    protected boolean write(@NotNull File dir, @NotNull String dirPath) {
        if (dir.exists()) {
            if (!dir.isDirectory()) throw new HephaestusRuntimeException("Target must be a directory.");
        } else if (!dir.mkdirs()) return false;
        if (!writeWrapper(dirPath)) return false;
        int i = 0;
        for (Field field : Utils.getDeclaredFields(component.getClass())) {
            if (field.isAnnotationPresent(HiddenComponent.class)) {
                try {
                    field.setAccessible(true);
                    Component hiddenComponent = (Component) field.get(component);
                    if (hiddenComponent != null && !writeSub(hiddenComponent, dirPath, i++, "hidden")) return false;
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        i = 0;
        for (Component child : component.getChildren())
            if (!writeSub(child, dirPath, i++)) return false;
        return true;
    }

    @Override
    public boolean write(@NotNull File dir) {
        return write(dir, dir.getAbsolutePath());
    }

    @Override
    public boolean write(@NotNull String dirPath) {
        return write(new File(dirPath), dirPath);
    }

    @Contract("_, _, _ -> new")
    protected static @NotNull ComponentFolder read(@NotNull File dir, @NotNull String dirPath, @NotNull File wrapperFile) throws IOException, ClassNotFoundException, HephaestusException {
        String wrapper = Basics.NativeHandler.readFile(dirPath + "/" + wrapperFile.getName());
        int separatorIndex = wrapper.indexOf("@");
        String className = wrapper.substring(0, separatorIndex);
        Class<?> clz = Class.forName(className);
        if (!WrapperComponent.class.isAssignableFrom(clz))
            throw new HephaestusRuntimeException("Not a subclass of `WrapperComponent`: `" + className + "`.");
        try {
            Constructor<?> constructor = clz.getDeclaredConstructor();
            constructor.setAccessible(true);
            WrapperComponent wrapperComponent = (WrapperComponent) constructor.newInstance();
            AttributeUtils.injectAttributes(wrapperComponent, wrapper.substring(separatorIndex + 1));
            File[] files = dir.listFiles();
            if (files == null) throw new FileNotFoundException("Expecting at least one file under `" + dirPath + "`.");
            for (File file : files) {
                if (file.getName().equals(wrapperFile.getName()) || file.getName().startsWith("hidden")) continue;
                if (file.isDirectory()) {
                    WrapperComponent component = ComponentFolder.read(file, wrapperFile).component();
                    Transform transform = Transform.getTransform(component.getClass());
                    wrapperComponent.appendChild((transform == null ? new Transform() : transform).handleFilename(file.getName(), component));
                } else {
                    Component component = ComponentFile.read(file).component();
                    if (component != null) wrapperComponent.appendChild(component);
                }
            }
            Transform transform = Transform.getTransform(wrapperComponent.getClass());
            return new ComponentFolder((transform == null ? new Transform() : transform).handleFilename(dir.getName(), wrapperComponent), wrapperFile);
        } catch (NoSuchMethodException ignored) {
            throw new MissingDefaultConstructorException(clz);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Contract("_, _ -> new")
    public static @NotNull ComponentFolder read(@NotNull File dir, @NotNull File wrapperFile) throws IOException, ClassNotFoundException, HephaestusException {
        return read(dir, dir.getAbsolutePath(), wrapperFile);
    }

    @Contract("_ -> new")
    public static @NotNull ComponentFolder read(@NotNull File dir) throws IOException, ClassNotFoundException, HephaestusException {
        return read(dir, new File(".wrapper"));
    }

    @Contract("_, _ -> new")
    public static @NotNull ComponentFolder read(@NotNull String dirPath, @NotNull File wrapperFile) throws IOException, ClassNotFoundException, HephaestusException {
        return read(new File(dirPath), dirPath, wrapperFile);
    }

    @Contract("_ -> new")
    public static @NotNull ComponentFolder read(@NotNull String dirPath) throws IOException, ClassNotFoundException, HephaestusException {
        return read(dirPath, new File(".wrapper"));
    }
}
