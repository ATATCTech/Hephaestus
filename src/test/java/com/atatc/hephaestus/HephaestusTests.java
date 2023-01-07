package com.atatc.hephaestus;


import com.atatc.hephaestus.component.MultiComponents;
import com.atatc.hephaestus.component.Text;
import com.atatc.hephaestus.exception.BadFormat;
import org.junit.jupiter.api.Test;

public class HephaestusTests {
    @Test
    void test() throws BadFormat {
        Text text = new Text("{text^}");
        Text text2 = new Text("text2");
        MultiComponents multiComponents = new MultiComponents(text, text2);
        System.out.println(multiComponents.expr());
        System.out.println(multiComponents.toHTML());

        String compiled = Text.compile("{text^}");
        System.out.println(compiled);
        System.out.println(Text.decompile(compiled));

        System.out.println((Hephaestus.parseExpr(multiComponents.expr())).toHTML());
    }
}
