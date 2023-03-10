package com.atatctech.hephaestus.export.fs;

import com.atatctech.hephaestus.Hephaestus;
import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.component.WrapperComponent;
import com.atatctech.hephaestus.exception.BadFormat;
import com.atatctech.hephaestus.exception.HephaestusException;
import com.atatctech.hephaestus.exception.HephaestusRuntimeException;
import com.atatctech.hephaestus.exception.MissingFieldException;
import com.atatctech.packages.basics.basics.Basics;

import java.io.File;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

public class ComponentFile {
    public static final Transform DEFAULT_TRANSFORM = new Transform() {
        @Override
        public String beforeWrite(Component component) {
            return component.expr();
        }

        @Override
        public Component afterRead(String content) throws BadFormat {
            return Hephaestus.parse(content);
        }
    };

    public static ComponentFile.Transform getTransform(Class<?> clz) {
        if (clz.isAnnotationPresent(RequireTransform.class)) {
            try {
                Field field = clz.getField("TRANSFORM");
                field.setAccessible(true);
                return (ComponentFile.Transform) field.get(null);
            } catch (NoSuchFieldException e) {
                throw new MissingFieldException(clz, "TRANSFORM");
            } catch (IllegalAccessException ignored) {
            }
        }
        return DEFAULT_TRANSFORM;
    }

    protected final Component component;
    protected final File file;
    public ComponentFile(Component component, File file) {
        if (component instanceof WrapperComponent)
            throw new HephaestusRuntimeException("Wrapper components should be converted into `ComponentFolder`.");
        this.component = component;
        this.file = file;
    }

    public boolean write() {
        Transform transform = DEFAULT_TRANSFORM;
        Class<? extends Component> clz = component.getClass();
        if (clz.isAnnotationPresent(RequireTransform.class)) transform = getTransform(clz);
        String suffix = component.getTagName();
        if (suffix.equals("undefined")) suffix = "hexpr";
        suffix = "." + suffix;
        String filename = file.getAbsolutePath();
        return Basics.NativeHandler.writeFile(filename.endsWith(suffix) ? filename : filename + suffix, transform.beforeWrite(component));
    }

    public static abstract class Transform {
        public abstract String beforeWrite(Component component);
        public abstract Component afterRead(String content) throws HephaestusException;
    }

    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface RequireTransform {
    }
}
