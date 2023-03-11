package com.atatctech.hephaestus;

import com.atatctech.hephaestus.component.HTMLBlock;
import com.atatctech.hephaestus.component.Text;
import com.atatctech.hephaestus.exception.HephaestusException;
import com.atatctech.hephaestus.export.fs.ComponentFile;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class HephaestusTests {
    @Test
    void test() throws HephaestusException, IOException {
        ComponentFile componentFile = new ComponentFile(new HTMLBlock(new Text("### Test")), new File("F:\\Test2"));
        System.out.println(componentFile.write());
        System.out.println(ComponentFile.read(new File("F:\\Test2.html")).component());
    }
}
