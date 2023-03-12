package com.atatctech.hephaestus.export.fs;

import com.atatctech.hephaestus.component.Component;

import java.io.File;

public interface FileSystemEntity {
    Component component();
    boolean write(File file);
    boolean write(String filename);
}
