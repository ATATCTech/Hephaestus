package com.atatc.hephaestus.component;

import com.atatc.hephaestus.parser.Parser;
import com.atatc.hephaestus.render.HTMLRender;
import org.jsoup.nodes.Element;

import java.util.regex.Pattern;

public class Text extends Component {
    public static HTMLRender<Text> HTML_RENDER = text -> new Element("b").text(text.getText());
    public static Parser<Text> PARSER = expr -> new Text(Text.decompile(expr));

    protected String text;

    public Text(String text) {
        this.text = text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }

    @Override
    public String expr() {
        return "{" + compile(getText()) + "}";
    }

    @Override
    public Element toHTML() {
        return HTML_RENDER.render(this);
    }

    public static final char COMPILER_CHARACTER = '^';

    public static final String[] RESERVED_PATTERNS = {
            "^",
            ":",
            "{",
            "}",
            "[",
            "]",
            "(",
            ")",
            "=",
            ";",
    };

    public static String quote(String s) {
        return COMPILER_CHARACTER + s;
    }

    public static String compile(String s, String p) {
        if (s == null) return null;
        return s.replaceAll(Pattern.quote(p), quote(p));
    }

    public static String compile(String s) {
        for (String p : RESERVED_PATTERNS) {
            s = compile(s, p);
        }
        return s;
    }

    public static String decompile(String s, String p) {
        if (s == null) return null;
        return s.replaceAll(Pattern.quote(quote(p)), p);
    }

    public static String decompile(String s) {
        for (String p : RESERVED_PATTERNS) {
            s = decompile(s, p);
        }
        return s;
    }

    public static int indexOf(String s, char c, int fromIndex) {
        if (s.charAt(fromIndex) == c) return fromIndex;
        for (int i = fromIndex + 1; i < s.length(); i++) {
            if (s.charAt(i) == c && s.charAt(i - 1) != COMPILER_CHARACTER) return i;
        }
        return -1;
    }

    public static int indexOf(String s, char c) {
        return indexOf(s, c, 0);
    }

    public static boolean charAtEquals(String s, int i, char c) {
        if (i > 0) return s.charAt(i) == c && s.charAt(i - 1) != COMPILER_CHARACTER;
        return s.charAt(i) == c;
    }

    public static boolean startsWith(String s, char c) {
        boolean b = s.charAt(0) == c;
        if (c == COMPILER_CHARACTER && s.length() > 1) return b && s.charAt(1) != COMPILER_CHARACTER;
        return b;
    }

    public static boolean endsWith(String s, char c) {
        boolean b = s.charAt(s.length() - 1) == c;
        if (s.length() < 2) return b;
        return b && s.charAt(s.length() - 2) != COMPILER_CHARACTER;
    }
}
