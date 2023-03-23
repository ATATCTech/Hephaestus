package com.atatctech.packages.basics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

public class Basics {
    public static class NativeHandler {
        public static String readFile(String filename) throws IOException {
            return readFile(new File(filename));
        }

        public static String readFile(File file) throws IOException {
            return new String(Files.readAllBytes(file.toPath()));
        }

        public static boolean writeFile(String filename, String content, boolean append) {
            try (FileWriter fileWriter = new FileWriter(filename, append)) {
                fileWriter.write(content);
                return true;
            } catch (IOException ignored) {
                return false;
            }
        }

        public static boolean writeFile(String filename, String content) {
            return writeFile(filename, content, false);
        }

        public static boolean writeFile(File file, String content, boolean append) {
            try (FileWriter fileWriter = new FileWriter(file, append)) {
                fileWriter.write(content);
                return true;
            } catch (IOException ignored) {
                return false;
            }
        }

        public static boolean writeFile(File file, String content) {
            return writeFile(file, content, false);
        }
    }
}
