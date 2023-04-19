package com.atatctech.hephaestus;

import com.atatctech.hephaestus.component.*;
import com.atatctech.hephaestus.config.Config;
import com.atatctech.hephaestus.exception.BadFormat;
import com.atatctech.hephaestus.exception.ComponentNotClosed;
import com.atatctech.hephaestus.export.fs.ComponentFile;
import com.atatctech.hephaestus.export.fs.ComponentFolder;
import com.atatctech.hephaestus.parser.Parser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.*;

public final class Hephaestus {
    public static @Nullable Component parseExpr(@NotNull String expr) throws BadFormat {
        if (expr.length() < 2) return null;
        if (Text.wrappedBy(expr, '[', ']')) return MultiComponent.PARSER.parse(expr.substring(1, expr.length() - 1));
        UnsupportedComponent temp = new UnsupportedComponent();
        temp.fullExpr = expr;
        int i = Text.indexOf(expr, ':');
        if (i < 0) {
            temp.tagName = "";
            temp.inner = expr.substring(1, expr.length() - 1);
        } else {
            temp.tagName = expr.substring(1, i);
            temp.inner = expr.substring(i + 1, expr.length() - 1);
        }
        if (Text.wrappedBy(expr, '{', '}')) {
            if (temp.tagName.isEmpty()) return Text.PARSER.parse(temp.inner);
            temp.tagName = temp.tagName.replaceAll(" ", "");
            Parser<?> parser = Config.getInstance().getParser(temp.tagName);
            return parser == null ? temp : parser.parse(temp.inner);
        }
        if (!Text.wrappedBy(expr, '<', '>')) throw new ComponentNotClosed(expr);
        Skeleton skeleton;
        if (temp.tagName.isEmpty()) skeleton = new Skeleton(temp.inner);
        else {
            skeleton = Skeleton.PARSER.parse(temp.inner);
            skeleton.setName(Text.decompile(temp.tagName));
        }
        return skeleton;
    }

    public static @Nullable Component parse(@NotNull String expr) throws BadFormat {
        return parseExpr(clean(expr));
    }

    public static @NotNull String[] listTagNames() {
        return Config.getInstance().listTagNames();
    }

    public static @NotNull String clean(@NotNull String expr) {
        if (expr.isEmpty()) return "";
        int f = 0;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < expr.length(); i++) {
            char bit = expr.charAt(i);
            if (Text.charAtEqualsAny(expr, i, '{', '<')) f = 1;
            else if (f == 1 && Text.charAtEquals(expr, i, ':')) f = 2;
            else if (f == 2 && Text.charAtEquals(expr, i, '(')) f = 3;
            else if (Text.charAtEqualsAny(expr, i, '}', '>')) f = 0;
            if (f != 1 && (bit == '\n' || bit == ' ')) continue;
            builder.append(bit);
        }
        return builder.toString();
    }

    public static @NotNull Component compileComponentTree(@NotNull Component top) {
        Map<String, Component> componentMap = new HashMap<>();
        List<Ref> references = new LinkedList<>();
        top.parallelTraversal((component, depth) -> {
            if (component instanceof Ref ref) references.add(ref);
            else if (component.getId() != null) componentMap.put(component.getId(), component);
            if (component instanceof Compilable compilable) compilable.compile(refs -> references.addAll(Arrays.asList(refs)));
        });
        references.forEach(ref -> ref.referTo(componentMap.get(ref.getId())));
        return top;
    }

    public static boolean export(@NotNull Component component, File to) {
        return component instanceof WrapperComponent wrapperComponent ? new ComponentFolder(wrapperComponent).write(to) : new ComponentFile(component).write(to);
    }

    public static boolean export(@NotNull Component component, String to) {
        return export(component, new File(to));
    }
}
