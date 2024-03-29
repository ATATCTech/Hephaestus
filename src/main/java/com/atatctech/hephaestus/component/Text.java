package com.atatctech.hephaestus.component;

import com.atatctech.hephaestus.exception.HephaestusRuntimeException;
import com.atatctech.hephaestus.format.Format;
import com.atatctech.hephaestus.parser.Parser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.regex.Pattern;

/**
 * This is the most commonly used thing, and it assumes the role of the equivalent of a string in programming languages.
 * Basically, it is used for string scape.
 * You may also notice that yes, there are static methods related to text processing under this class.
 */
@ComponentConfig(tagName = "t")
public class Text extends Component {
    public static @NotNull Parser<Text> PARSER = expr -> new Text(Text.decompile(expr));

    protected @NotNull String text = "";

    protected @NotNull Format format = Format.UNKNOWN;

    public Text() {
    }

    public Text(@NotNull String text) {
        setText(text);
    }

    public void setText(@NotNull String text) {
        this.text = text;
    }

    public @NotNull String getText() {
        return text;
    }

    public void assertFormat(@NotNull Format format) {
        this.format = format;
    }

    public @NotNull Format detectFormat() {
        if (getText().matches("<[a-z][\\s\\S]*>")) return Format.HTML;
        return Format.MARKDOWN;
    }

    public @NotNull Format getFormat() {
        return format;
    }

    @Override
    public @NotNull String expr() {
        return "{" + compile(getText()) + "}";
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
     * Prevent {@param c} from being recognized as a keyword.
     * @param c the character to quote
     * @return quotation string
     */
    @Contract(pure = true)
    public static @NotNull String quote(char c) {
        return String.valueOf(COMPILER_CHARACTER) + c;
    }

    /**
     * Quote all the occurrences of {@param c}.
     * @param s the string object
     * @param c the character that needs to be quoted
     * @return compiled string
     */
    public static @NotNull String compile(@NotNull String s, char c) {
        return s.replaceAll(Pattern.quote(String.valueOf(c)), quote(c));
    }

    /**
     * Quote all the occurrences of every reserved keyword.
     * @param s the string object
     * @return the compiled string
     */
    public static @NotNull String compile(@NotNull String s) {
        for (char c : RESERVED_KEYWORDS) s = compile(s, c);
        return s;
    }

    /**
     * Remove all the quotations of {@param c}.
     * @param s the string object
     * @param c the character whose quotations need to be removed
     * @return the decompiled string
     */
    public static @NotNull String decompile(@NotNull String s, char c) {
        return s.replaceAll(Pattern.quote(quote(c)), String.valueOf(c));
    }

    /**
     * Remove all the quotations of every reserved keyword.
     * @param s the string object
     * @return the decompiled string
     */
    public static @NotNull String decompile(@NotNull String s) {
        for (char c : RESERVED_KEYWORDS) s = decompile(s, c);
        return s;
    }

    /**
     * Find the first index of {@param c} in {@param s}, excluding the quotations.
     * @param s the string object
     * @param c the character to find
     * @param fromIndex search starts from this index
     * @return the index
     */
    public static int indexOf(@NotNull String s, char c, int fromIndex) {
        for (int i = fromIndex; i < s.length(); i++) if (charAtEquals(s, i, c)) return i;
        return -1;
    }

    public static int indexOf(@NotNull String s, char c) {
        return indexOf(s, c, 0);
    }

    /**
     * Find the last index of {@param c} in {@param s}, excluding the quotations.
     * @param s the string object
     * @param c the character to find
     * @param fromIndex search starts from this index
     * @return the index
     */
    public static int lastIndexOf(@NotNull String s, char c, int fromIndex) {
        for (int i = fromIndex; i > 0; i--) if (charAtEquals(s, i, c)) return i;
        return -1;
    }

    public static int lastIndexOf(@NotNull String s, char c) {
        return lastIndexOf(s, c, s.length() - 1);
    }

    /**
     * Determine whether the character at {@param i} in {@param s} equals to {@param c} and is not a quotation.
     * @param s the string object
     * @param i the index in the string at which the character needs to be compared
     * @param c the character to compare
     * @return true: equal; false: unequal
     */
    public static boolean charAtEquals(@NotNull String s, int i, char c) {
        boolean e = s.charAt(i) == c;
        if (i > 0) return e && s.charAt(i - 1) != COMPILER_CHARACTER;
        if (c == COMPILER_CHARACTER && s.length() > 1) return e && s.charAt(1) != COMPILER_CHARACTER;
        return e;
    }

    public static boolean charAtEqualsAny(@NotNull String s, int i, char @NotNull ... cs) {
        char bit = s.charAt(i);
        for (char c : cs) {
            if (bit != c) continue;
            if (i > 0) return s.charAt(i - 1) != COMPILER_CHARACTER;
            if (c == COMPILER_CHARACTER && s.length() > 1) return s.charAt(1) != COMPILER_CHARACTER;
            return true;
        }
        return false;
    }

    /**
     * Determine whether {@param s} starts with {@param c} and the occurrence is not a quotation.
     * @param s the string object
     * @param c expected starting character
     * @return true: does start with; false: does not start with
     */
    public static boolean startsWith(@NotNull String s, char c) {
        return !s.isEmpty() && charAtEquals(s, 0, c);
    }

    /**
     * Determine whether {@param s} ends with {@param c} and not a quotation.
     * @param s the string object
     * @param c expected ending character
     * @return true: does end with; false: does not end with
     */
    public static boolean endsWith(@NotNull String s, char c) {
        return !s.isEmpty() && charAtEquals(s, s.length() - 1, c);
    }

    /**
     * Determine if {@param s} starts with {@param start} and ends with {@param end}.
     * @param s the string object
     * @param start expected starting character
     * @param end expected ending character
     * @return true: is wrapped by; false: is not wrapped by
     */
    public static boolean wrappedBy(@NotNull String s, char start, char end) {
        return startsWith(s, start) && endsWith(s, end);
    }

    public static boolean wrappedBy(@NotNull String s, char boarder) {
        return wrappedBy(s, boarder, boarder);
    }

    public record IndexPair(int start, int end) {}

    /**
     * Match brackets at a same level.
     * @param s the string object
     * @param open the left bracket
     * @param close the right bracket
     * @param requiredDepth the target level
     * @return indexes of the left bracket and the right bracket
     */
    @Contract("_, _, _, _ -> new")
    public static @NotNull IndexPair matchBrackets(@NotNull String s, char open, char close, int requiredDepth) {
        int depth = 0;
        int startIndex = -1;
        for (int i = 0; i < s.length(); i++) {
            if (Text.charAtEquals(s, i, open) && depth++ == requiredDepth) startIndex = i;
            else if (Text.charAtEquals(s, i, close) && --depth == requiredDepth) return new IndexPair(startIndex, i);
        }
        return new IndexPair(startIndex, -1);
    }

    public static @NotNull IndexPair matchBrackets(@NotNull String s, char open, char close) {
        return matchBrackets(s, open, close, 0);
    }

    /**
     * Get the other bracket.
     * @param b the left bracket or the right one
     * @return the other one
     */
    public static char pairBracket(char b) {
        return switch (b) {
            case '(' -> ')';
            case '[' -> ']';
            case '{' -> '}';
            case '<' -> '>';
            case ')' -> '(';
            case ']' -> '[';
            case '}' -> '{';
            case '>' -> '<';
            default -> throw new HephaestusRuntimeException("Not a bracket: " + b + ".");
        };
    }
}
