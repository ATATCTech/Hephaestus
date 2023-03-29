package com.atatctech.hephaestus.export.fs;

import com.atatctech.hephaestus.component.Component;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public interface FileSystemEntity {
    @NotNull Component component();
    boolean write(@NotNull File file);
    boolean write(@NotNull String filename);
}
