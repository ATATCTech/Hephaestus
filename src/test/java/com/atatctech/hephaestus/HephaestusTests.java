package com.atatctech.hephaestus;

import com.atatctech.hephaestus.exception.BadFormat;
import org.junit.jupiter.api.Test;

public class HephaestusTests {
    @Test
    void test() throws BadFormat {
        System.out.println(Hephaestus.parseExpr("<Welcome:(component={md:# Test};)<Introduction to Hephaestus:(component={md: # Test 2});>>"));
    }
}
