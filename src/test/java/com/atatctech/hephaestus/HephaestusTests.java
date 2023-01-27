package com.atatctech.hephaestus;

import com.atatctech.hephaestus.component.Ref;
import com.atatctech.hephaestus.component.Skeleton;
import org.junit.jupiter.api.Test;

public class HephaestusTests {
    @Test
    void test() {
        Skeleton top = new Skeleton();
        Skeleton skeleton1 = new Skeleton();
        skeleton1.setId("ID");
        Ref ref = new Ref("ID");
        top.appendChild(skeleton1);
        top.setComponent(ref);
        System.out.println(Hephaestus.compileComponentTree(top));
    }
}
