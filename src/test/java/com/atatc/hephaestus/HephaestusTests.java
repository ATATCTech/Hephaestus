package com.atatc.hephaestus;


import com.atatc.hephaestus.component.Document;
import com.atatc.hephaestus.component.Text;
import com.atatc.hephaestus.component.Title;
import com.atatc.hephaestus.component.Typography;
import com.atatc.hephaestus.exception.BadFormat;
import com.atatc.hephaestus.skeleton.Skeleton;
import com.atatc.hephaestus.skeleton.Body;
import com.atatc.packages.log.Log;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

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
        Body body = Hephaestus.parse(expr);
        assert body != null;
        System.out.println(body.skeleton());
        System.out.println(body.component());
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

    @Test
    void test3() {
        System.out.println(Arrays.toString(Text.pairBrackets("{}", '{', '}')));
    }
}
