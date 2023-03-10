package com.atatctech.hephaestus;

import com.atatctech.hephaestus.component.MDBlock;
import com.atatctech.hephaestus.component.Text;
import com.atatctech.hephaestus.export.fs.ComponentFile;
import org.junit.jupiter.api.Test;

import java.io.File;

public class HephaestusTests {
    @Test
    void test() {
        ComponentFile componentFile = new ComponentFile(new MDBlock(new Text("### Test")), new File("F:\\Test2"));
        System.out.println(componentFile.write());
    }
}
