package com.atatctech.hephaestus;

import com.atatctech.hephaestus.component.HTMLBlock;
import com.atatctech.hephaestus.component.MDBlock;
import com.atatctech.hephaestus.component.Skeleton;
import com.atatctech.hephaestus.component.Text;
import com.atatctech.hephaestus.exception.HephaestusException;
import com.atatctech.hephaestus.export.fs.ComponentFolder;
import com.atatctech.hephaestus.export.fs.FileSystemEntity;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class HephaestusTests {
    @Test
    void test() throws HephaestusException, IOException, ClassNotFoundException {
        Skeleton skeleton1 = new Skeleton("test1");
        skeleton1.setComponent(new HTMLBlock(new Text("<h3>Test</h3>")));
        Skeleton skeleton2 = new Skeleton("test2");
        skeleton2.setComponent(new MDBlock(new Text("### Test")));
        skeleton1.appendChild(skeleton2);
        FileSystemEntity fileSystemEntity = new ComponentFolder(skeleton1);
        System.out.println(fileSystemEntity.write("F:\\Test2"));
        System.out.println(ComponentFolder.read("F:\\Test2").component());
    }
}
