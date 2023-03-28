package com.atatctech.hephaestus.export.fs;

import com.atatctech.hephaestus.Hephaestus;
import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.component.WrapperComponent;
import com.atatctech.hephaestus.exception.HephaestusException;
import com.atatctech.hephaestus.exception.MissingFieldException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Field;

/**
 * This is a callback class for `FileSystemEntity`.
 * You might want to customize outputting behaviors via overriding these methods.
 * To apply a `Transform` to a class, you need to define a static field in the class, named "TRANSFORM". Make sure the package where the component class is has been scanned into config and the class is annotated with `@RequireTransform`.
 */
public class Transform {
    /**
     * Acquire defined `Transform` from a class by reflection.
     * @param clz target class
     * @return the `Transform` object (if exists)
     */
    public static @Nullable Transform getTransform(Class<?> clz) {
        if (clz.isAnnotationPresent(RequireTransform.class)) {
            try {
                Field field = clz.getField("TRANSFORM");
                field.setAccessible(true);
                return (Transform) field.get(null);
            } catch (NoSuchFieldException e) {
                throw new MissingFieldException(clz, "TRANSFORM");
            } catch (IllegalAccessException ignored) {
            }
        }
        return null;
    }

    /**
     * This callback method is called right before writing IO operations.
     * As an example, `MDBlock` outputs simply in form of Markdown instead of Hexpr.
     * *It only effects `ComponentFile`.*
     * @param component component to write
     * @return string representation
     */
    public @NotNull String beforeWrite(Component component) {
        return component.expr();
    }

    /**
     * This callback method is called right after reading IO operations.
     * As an example, `MDBlock` requires special postprocess that is not what `Hephaestus.parse()` can do.
     * *It only effects `ComponentFile`.*
     * @param content string content from file
     * @return component
     * @throws HephaestusException if parsing fails
     */
    public @Nullable Component afterRead(String content) throws HephaestusException {
        return Hephaestus.parse(content);
    }

    /**
     * This callback method is called as the last step after reading operation.
     * As an example, `Skeleton` sets its name depending on the filename.
     * *It only effects `ComponentFolder`.*
     * @param filename original filename (considered as no suffix)
     * @param wrapperComponent current component
     * @return final component
     */
    public @NotNull WrapperComponent handleFilename(String filename, WrapperComponent wrapperComponent) {
        return wrapperComponent;
    }

    /**
     * This callback method is called as the last step before writing operation.
     * As an example, `Skeleton` sets the folder's name (filename) according to its own name.
     * *It only effects when the component is a child of a `WrapperComponent`.*
     * @param index index of component inside the folder
     * @param component component to write
     * @return final filename
     */
    public @NotNull String interfereFilename(int index, Component component) {
        return String.valueOf(index);
    }

    /**
     * Classes without this annotation are ignored even though they have a defined field for `Transform`.
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    public @interface RequireTransform {
    }
}
