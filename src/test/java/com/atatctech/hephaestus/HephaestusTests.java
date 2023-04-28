package com.atatctech.hephaestus;

import com.atatctech.hephaestus.component.*;
import com.atatctech.hephaestus.exception.BadFormat;
import org.junit.jupiter.api.Test;

public class HephaestusTests {
    @Test
    void test() throws BadFormat {
        Skeleton skeleton1 = new Skeleton("test");
        skeleton1.setComponent(new Text("test text"));
        skeleton1.setId("skeleton");
        MultiComponent parent = new MultiComponent(skeleton1, new MDBlock(new Text("test text")), new MDBlock(new Text("test text")));
        String hexpr = Hephaestus.reduceRedundancy(parent).expr();
        System.out.println(hexpr);
        System.out.println(Hephaestus.compileComponentTree(Hephaestus.parseExpr(hexpr)));
    }
}
