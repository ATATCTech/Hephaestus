package com.atatc.hephaestus;


import com.atatc.hephaestus.component.Code;
import com.atatc.hephaestus.component.Component;
import com.atatc.hephaestus.component.Document;
import com.atatc.hephaestus.exception.BadFormat;
import org.junit.jupiter.api.Test;

public class HephaestusTests {
    @Test
    void test() throws BadFormat {
        Code code = new Code("String string = \"\";", "java");
        Document document = new Document(code);
        System.out.println(document.expr());
        System.out.println(code.toHTML());
        Component component = Hephaestus.parseExpr(document.expr());
        System.out.println(component.toHTML());
    }
}
