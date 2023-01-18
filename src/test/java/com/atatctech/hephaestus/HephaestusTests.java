package com.atatctech.hephaestus;

import com.atatctech.hephaestus.component.HTMLBlock;
import com.atatctech.hephaestus.exception.BadFormat;
import com.atatctech.hephaestus.component.Skeleton;
import org.junit.jupiter.api.Test;

public class HephaestusTests {
    @Test
    void test() throws BadFormat {
        Skeleton skeleton = new Skeleton("column a");
        skeleton.setComponent(new HTMLBlock("};)"));
        System.out.println(skeleton.expr());
        System.out.println(Hephaestus.parseExpr(skeleton.expr()));
    }
}
