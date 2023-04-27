package com.atatctech.hephaestus;

import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.component.ComponentConfig;
import com.atatctech.hephaestus.config.Config;
import com.atatctech.hephaestus.exception.BadFormat;
import com.atatctech.hephaestus.parser.Parser;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.Test;

public class HephaestusTests {
    @ComponentConfig(tagName = "myc")
    static class MyComponent extends Component {
        public static Parser<MyComponent> PARSER;

        static {
            PARSER = expr -> new MyComponent();
        }

        public MyComponent() {}

        @Override
        public @NotNull String expr() {
            return generateExpr("This is my component.");
        }
    }
    @Test
    void test() throws BadFormat {
        Config.getInstance().scanPackage("com.atatctech.hephaestus");

        Component myComponent = new MyComponent();
        String expr = myComponent.expr();
        System.out.println(expr);
        myComponent = Hephaestus.parseExpr(expr);
        assert myComponent != null;
        System.out.println(myComponent.getTagName());
    }
}
