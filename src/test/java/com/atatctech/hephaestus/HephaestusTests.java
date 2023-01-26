package com.atatctech.hephaestus;

import com.atatctech.hephaestus.component.Skeleton;
import com.atatctech.hephaestus.exception.BadFormat;
import org.junit.jupiter.api.Test;

public class HephaestusTests {
    @Test
    void test() throws BadFormat {
        System.out.println(new Skeleton("skeleton2"));
        String a = Hephaestus.clean("""
                 < skeleton :(component={md: {
                ### Test
                
                This is a test.
                }};)[<skeleton2> <skeleton3>]>
                """);
        System.out.println(a);
        System.out.println(Hephaestus.parse(a));
    }
}
