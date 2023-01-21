package com.atatctech.hephaestus;

import com.atatctech.hephaestus.component.Skeleton;
import org.junit.jupiter.api.Test;

public class HephaestusTests {
    @Test
    void test() {
        Skeleton skeleton1 = new Skeleton("S1");
        Skeleton skeleton2 = new Skeleton("S2");
        Skeleton skeleton3 = new Skeleton("S3");
        Skeleton skeleton4 = new Skeleton("S4");
        Skeleton skeleton5 = new Skeleton("S5");

        skeleton1.appendChild(skeleton2);
        skeleton1.appendChild(skeleton3);
        skeleton2.appendChild(skeleton4);
        skeleton3.appendChild(skeleton5);

        skeleton1.forEach((component, depth) -> {
            System.out.println(depth);
            return true;
        });

        skeleton1.parallelTraversal((component, depth) -> {
            System.out.println(depth);
            return true;
        });
    }
}
