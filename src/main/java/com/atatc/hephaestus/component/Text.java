package com.atatc.hephaestus.component;

import com.atatc.hephaestus.parser.Parser;
import com.atatc.hephaestus.render.HTMLRender;
import org.jsoup.nodes.Element;

import java.util.regex.Pattern;

/**
 * This is the most commonly used thing, and it assumes the role of the equivalent of a string in programming languages.
 * You may also notice that yes, there are static methods related to text processing under this class name.
 */
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

    public static final char[] RESERVED_KEYWORDS = {
            '^',
            ':',
            '{',
            '}',
            '[',
            ']',
            '(',
            ')',
            '<',
            '>',
            '=',
            ';',
    };

    /**
     * To prevent the character from being recognized as a keyword.
     * @param c the character to quote
     * @return quotation string
     */
    public static String quote(char c) {
        return String.valueOf(COMPILER_CHARACTER) + c;
    }

    /**
     * To quote all the occurrences of a specific character.
     * @param s the string object
     * @param c the character that needs to be quoted
     * @return compiled string
     */
    public static String compile(String s, char c) {
        if (s == null) return null;
        return s.replaceAll(Pattern.quote(String.valueOf(c)), quote(c));
    }

    /**
     * To quote all the occurrences of every reserved keyword.
     * @param s the string object
     * @return the compiled string
     */
    public static String compile(String s) {
        for (char c : RESERVED_KEYWORDS) s = compile(s, c);
        return s;
    }

    /**
     * To remove all the quotations of a specific character.
     * @param s the string object
     * @param c the character whose quotations need to be removed
     * @return the decompiled string
     */
    public static String decompile(String s, char c) {
        if (s == null) return null;
        return s.replaceAll(Pattern.quote(quote(c)), String.valueOf(c));
    }

    /**
     * To remove all the quotations of every reserved keyword.
     * @param s the string object
     * @return the decompiled string
     */
    public static String decompile(String s) {
        for (char c : RESERVED_KEYWORDS) s = decompile(s, c);
        return s;
    }

    /**
     * To find the first index of a certain character in the string, excluding the quotations.
     * @param s the string object
     * @param c the character to find
     * @param fromIndex search starts from this index
     * @return the index
     */
    public static int indexOf(String s, char c, int fromIndex) {
        for (int i = fromIndex; i < s.length(); i++) if (charAtEquals(s, i, c)) return i;
        return -1;
    }

    public static int indexOf(String s, char c) {
        return indexOf(s, c, 0);
    }

    /**
     * To find the last index of a certain character in the string, excluding the quotations.
     * @param s the string object
     * @param c the character to find
     * @param fromIndex search starts from this index
     * @return the index
     */
    public static int lastIndexOf(String s, char c, int fromIndex) {
        for (int i = fromIndex; i > 0; i--) if (charAtEquals(s, i, c)) return i;
        return -1;
    }

    public static int lastIndexOf(String s, char c) {
        return lastIndexOf(s, c, s.length() - 1);
    }

    /**
     * To determine whether the character at a certain index in the string equals to a specific character and is not a quotation.
     * @param s the string object
     * @param i the index in the string at which the character needs to be compared
     * @param c the character to compare
     * @return true: equal; false: unequal
     */
    public static boolean charAtEquals(String s, int i, char c) {
        boolean e = s.charAt(i) == c;
        if (i > 0) return e && s.charAt(i - 1) != COMPILER_CHARACTER;
        if (c == COMPILER_CHARACTER && s.length() > 1) return e && s.charAt(1) != COMPILER_CHARACTER;
        return e;
    }

    /**
     * To determine whether the string object starts with a certain character and not a quotation.
     * @param s the string object
     * @param c the character to compare
     * @return true: does start with; false: does not start with
     */
    public static boolean startsWith(String s, char c) {
        return charAtEquals(s, 0, c);
    }

    /**
     * To determine whether the string object ends with a certain character and not a quotation.
     * @param s the string object
     * @param c the character to compare
     * @return true: does end with; false: does not end with
     */
    public static boolean endsWith(String s, char c) {
        return charAtEquals(s, s.length() - 1, c);
    }

    public static int[] pairBrackets(String s, char open, char close, int requiredDepth) {
        int depth = 0;
        int startIndex = -1;
        for (int i = 0; i < s.length(); i++) {
            char bit = s.charAt(i);
            if (bit == open && depth++ == requiredDepth) startIndex = i;
            else if (bit == close && --depth == requiredDepth) return new int[]{startIndex, i};
        }
        return new int[]{startIndex, -1};
    }

    public static int[] pairBrackets(String s, char open, char close) {
        return pairBrackets(s, open, close, 0);
    }
}
