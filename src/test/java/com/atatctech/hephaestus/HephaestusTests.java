package com.atatctech.hephaestus;

import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.exception.BadFormat;
import com.atatctech.hephaestus.structure.PD;
import org.junit.jupiter.api.Test;

public class HephaestusTests {
    @Test
    void test() throws BadFormat {
        Component component = Hephaestus.parse("[<abc:>{text}]");
        assert component != null;
        System.out.println(Hephaestus.satisfies(component, PD.all("sk")));
    }
}
