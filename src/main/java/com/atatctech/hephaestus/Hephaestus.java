package com.atatctech.hephaestus;

import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.component.MultiComponent;
import com.atatctech.hephaestus.component.Text;
import com.atatctech.hephaestus.component.UnsupportedComponent;
import com.atatctech.hephaestus.config.Config;
import com.atatctech.hephaestus.exception.BadFormat;
import com.atatctech.hephaestus.exception.ComponentNotClosed;
import com.atatctech.hephaestus.parser.Parser;
import com.atatctech.hephaestus.component.Skeleton;
import org.jetbrains.annotations.NotNull;

public final class Hephaestus {
    public static Component parseExpr(@NotNull String expr) throws BadFormat {
        if (expr.isEmpty()) return null;
        if (Text.wrappedBy(expr, '[', ']')) return MultiComponent.PARSER.parse(expr.substring(1, expr.length() - 1));
        UnsupportedComponent temp = new UnsupportedComponent();
        temp.expr = expr;
        int i = Text.indexOf(expr, ':');
        if (i < 0) return Text.PARSER.parse(expr.substring(1, expr.length() - 1));
        temp.tagName = expr.substring(1, i);
        temp.inner = expr.substring(i + 1, expr.length() - 1);
        if (Text.wrappedBy(expr, '<', '>')) {
            Skeleton skeleton = Skeleton.PARSER.parse(temp.inner);
            skeleton.setName(Text.decompile(temp.tagName));
            return skeleton;
        }
        temp.tagName = temp.tagName.replaceAll(" ", "");
        if (!Text.wrappedBy(expr, '{', '}')) throw new ComponentNotClosed(expr);
        Parser<?> parser = Config.getInstance().getParser(temp.tagName);
        if (parser == null) return temp;
        return parser.parse(temp.inner);
    }
}
