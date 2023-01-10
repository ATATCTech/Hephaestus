package com.atatc.hephaestus;


import com.atatc.hephaestus.component.Document;
import com.atatc.hephaestus.component.Title;
import com.atatc.hephaestus.component.Typography;
import com.atatc.hephaestus.exception.BadFormat;
import com.atatc.hephaestus.skeleton.Skeleton;
import org.junit.jupiter.api.Test;

public class HephaestusTests {
    @Test
    void test() throws BadFormat {
        Title title1 = new Title(2, "t1");
        Title title2 = new Title(3, "t2");
        Title title3 = new Title(1, "t3");
        Title title4 = new Title(2, "t4");
        Document document = new Document(title1, title2, title3, title4);
        String expr = Skeleton.generateSkeleton(document).expr();
        System.out.println(expr);
        Skeleton skeleton = Hephaestus.parse(expr);
        System.out.println(skeleton.bone());
        System.out.println(skeleton.component());
    }

    @Test
    void test2() throws BadFormat {
        Title title = new Title(2, "t1");
        title.appendChild(new Typography("typo"));
        Document document = new Document(title);
        String expr = document.expr();
        System.out.println(expr);
        System.out.println(Hephaestus.parseExpr(expr));
    }
}
