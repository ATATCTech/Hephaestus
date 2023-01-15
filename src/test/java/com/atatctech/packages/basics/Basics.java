package com.atatctech.packages.basics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.util.*;

public class Basics {
    public static class NativeHandler {
        public static final String SYSTEM_SEPARATOR = System.getProperty("line.separator");
        public static final Properties properties = System.getProperties();
        public static InetAddress localHost = null;

        static {
            try {
                localHost = InetAddress.getLocalHost();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }

        public static String getName() {
            if (localHost != null) {
                return localHost.getHostName();
            }
            return "";
        }

        public static String getIP() {
            if (localHost != null) {
                return localHost.getHostAddress();
            }
            return "";
        }

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
