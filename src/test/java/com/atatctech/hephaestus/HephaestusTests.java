package com.atatctech.hephaestus;

import com.atatctech.hephaestus.component.Skeleton;
import com.atatctech.hephaestus.component.Text;
import com.atatctech.hephaestus.exception.BadFormat;
import org.junit.jupiter.api.Test;

public class HephaestusTests {
    @Test
    void test() throws BadFormat {
        Skeleton skeleton = new Skeleton("test");
        skeleton.setComponent(new Text("test text"));
        skeleton.setId("skeleton");
        String hexpr = skeleton.expr();
        System.out.println(hexpr);
        System.out.println(Hephaestus.parseExpr(hexpr));
    }
}
