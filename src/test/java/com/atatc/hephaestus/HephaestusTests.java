package com.atatc.hephaestus;


import com.atatc.hephaestus.component.Component;
import com.atatc.hephaestus.component.Title;
import com.atatc.hephaestus.exception.BadFormat;
import org.junit.jupiter.api.Test;

public class HephaestusTests {
    @Test
    void test() throws BadFormat {
        Title title = new Title(2, "Test");
        title.setColor(Color.NEGATIVE);
        System.out.println(title.expr());
        Component component = Hephaestus.parseExpr(title.expr());
        System.out.println(component.toHTML());
    }
}
