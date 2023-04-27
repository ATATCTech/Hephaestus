package com.atatctech.hephaestus.utils;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Consumer;

public final class Utils {
    /**
     * Get the complementary set of all declared fields in {@param clz} and every super class.
     * @param clz target class
     * @return all declared fields
     */
    public static @NotNull Set<Field> getDeclaredFields(@NotNull Class<?> clz) {
        Set<Field> fields = new HashSet<>(Arrays.asList(clz.getDeclaredFields()));
        Class<?> superClz = clz.getSuperclass();
        if (superClz == null) return fields;
        Set<Field> superFields = getDeclaredFields(superClz);
        fields.addAll(superFields);
        return fields;
    }

    public static void forEachDeclaredField(@NotNull Class<?> clz, @NotNull Consumer<Field> action) {
        for (Field field : clz.getDeclaredFields()) action.accept(field);
        Class<?> superClass = clz.getSuperclass();
        if (superClass != null) forEachDeclaredField(superClass, action);
    }
}
