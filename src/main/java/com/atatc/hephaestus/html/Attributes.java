package com.atatc.hephaestus.html;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Attributes {
    @NotNull
    protected HashMap<String, Object> map = new HashMap<>();

    public Attributes() {}

    public void put(String key, String value) {
        map.put(key, value);
    }

    public void put(String key, boolean value) {
        map.put(key, value);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(" ");
        for (Map.Entry<String, Object> entry : map.entrySet()) stringBuilder.append(entry.getKey()).append("=").append(entry.getValue() instanceof Boolean ? entry.toString() : "\"" + entry + "\"");
        return stringBuilder.length() < 2 ? "" : stringBuilder.toString();
    }
}
