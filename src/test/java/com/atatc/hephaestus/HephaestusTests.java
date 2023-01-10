package com.atatc.hephaestus;


import com.atatc.hephaestus.component.Document;
import com.atatc.hephaestus.component.Title;
import com.atatc.hephaestus.skeleton.Skeleton;
import org.junit.jupiter.api.Test;

public class HephaestusTests {
    @Test
    void test() {
        Title title1 = new Title(2, "t1");
        Title title2 = new Title(3, "t2");
        Title title3 = new Title(1, "t3");
        Title title4 = new Title(2, "t4");
        Document document = new Document(title1, title2, title3, title4);
        System.out.println(Skeleton.generateSkeleton(document));
    }
}
