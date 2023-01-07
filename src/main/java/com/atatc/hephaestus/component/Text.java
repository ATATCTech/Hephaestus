package com.atatc.hephaestus.component;

import com.atatc.hephaestus.render.HTMLRender;
import org.jsoup.nodes.Element;

import java.util.regex.Pattern;

public class Text extends Component {
    protected HTMLRender<Text> HTML_RENDER = (text) -> new Element("p").text(text.getText());

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

    public String getCompiledText() {
        // No need to compile other patterns.
        return compile(getText(), ":");
    }

    @Override
    public String expr() {
        return "{" + getText() + "}";
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
    };

    public static String quote(String s) {
        return COMPILER_CHARACTER + s;
    }

    public static String compile(String s, String p) {
        return s.replaceAll(Pattern.quote(p), quote(p));
    }

    public static String compile(String s) {
        for (String p : RESERVED_PATTERNS) {
            s = compile(s, p);
        }
        return s;
    }

    public static String decompile(String s, String p) {
        return s.replaceAll(Pattern.quote(quote(p)), p);
    }

    public static String decompile(String s) {
        for (String p : RESERVED_PATTERNS) {
            s = decompile(s, p);
        }
        return s;
    }

    public static int indexOf(String s, char c) {
        if (s.charAt(0) == c) return 0;
        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i) == c && s.charAt(i - 1) != COMPILER_CHARACTER) return i;
        }
        return -1;
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
