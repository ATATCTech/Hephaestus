package com.atatc.hephaestus;

import com.atatc.hephaestus.component.Component;
import com.atatc.hephaestus.component.MultiComponents;
import com.atatc.hephaestus.component.Text;
import com.atatc.hephaestus.component.UnsupportedComponent;
import com.atatc.hephaestus.config.Config;
import com.atatc.hephaestus.exception.BadFormat;
import com.atatc.hephaestus.exception.ComponentNotClosed;
import com.atatc.hephaestus.parser.Parser;
import com.atatc.hephaestus.skeleton.Bone;
import com.atatc.hephaestus.skeleton.Skeleton;

public final class Hephaestus {
    public static Component parseExpr(String expr) throws BadFormat {
        if (Text.startsWith(expr, '[')) {
            if (!Text.endsWith(expr, ']')) throw new BadFormat("Component list is not closed.", expr);
            return MultiComponents.PARSER.parse(expr.substring(1, expr.length() - 1));
        }
        if (Text.startsWith(expr, '<') && Text.endsWith(expr, '>')) return Bone.PARSER.parse(expr.substring(1, expr.length() - 1));
        if (!Text.startsWith(expr, '{') || !Text.endsWith(expr, '}')) throw new ComponentNotClosed(expr);
        UnsupportedComponent temp = new UnsupportedComponent();
        temp.expr = expr;
        // Only determine component name, NO ATTRIBUTES OR CONTENT INVOLVED.
        int i = Text.indexOf(expr, ':');
        if (i < 0) return Text.PARSER.parse(expr.substring(1, expr.length() - 1));
        else temp.tagName = expr.substring(1, i).replaceAll(" ", "");
        Parser<?> parser = Config.getInstance().getParser(temp.tagName);
        if (parser == null) return temp;
        return parser.parse(expr.substring(i + 1, expr.length() - 1));
    }

    public static Skeleton parse(String expr) throws BadFormat {
        Bone bone = null;
        if (Text.startsWith(expr, '<')) {
            int endIndex = Text.lastIndexOf(expr, '>') + 1;
            if (endIndex >= 0) {
                bone = Bone.PARSER.parse(expr.substring(0, endIndex));
                expr = expr.substring(endIndex);
            }
        }
        return new Skeleton(bone, parseExpr(expr));
    }
}
