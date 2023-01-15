package com.atatctech.hephaestus;


import com.atatctech.hephaestus.component.HTMLBlock;
import com.atatctech.hephaestus.component.Text;
import com.atatctech.hephaestus.exception.BadFormat;
import com.atatctech.hephaestus.component.Skeleton;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

public class HephaestusTests {
    @Test
    void test() throws BadFormat {
        Skeleton skeleton = new Skeleton("column a");
        skeleton.setComponent(new HTMLBlock("<h1>Title 1</h1>"));
        System.out.println(skeleton.expr());
        System.out.println(Hephaestus.parseExpr(skeleton.expr()));
    }

    @Test
    void test3() {
        System.out.println(Arrays.toString(Text.pairBrackets("{}", '{', '}')));
    }
}
