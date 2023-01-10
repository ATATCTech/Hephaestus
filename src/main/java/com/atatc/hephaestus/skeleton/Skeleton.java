package com.atatc.hephaestus.skeleton;

import com.atatc.hephaestus.component.Component;
import com.atatc.hephaestus.component.Document;
import com.atatc.hephaestus.component.Title;

import java.util.concurrent.atomic.AtomicInteger;

public record Skeleton(Bone bone, Component component) {

    public static Bone generateSkeleton(Component component, String name) {
        Bone bone = new Bone(name);
        final Bone[] currentBone = {bone};
        AtomicInteger currentLevel = new AtomicInteger();
        component.forEach((c, depth) -> {
            if (c instanceof Title title) {
                Bone b = new Bone(title.getText());
                int ld = currentLevel.get() - title.getLevel();
                if (ld >= 0) for (int i = 0; i < ld; i++) currentBone[0] = currentBone[0].getParent();
                currentBone[0].appendChild(b);
                currentBone[0] = b;
                currentLevel.set(title.getLevel());
            }
            return true;
        });
        return bone;
    }

    public static Bone generateSkeleton(Document document) {
        return generateSkeleton(document, document.getName());
    }
}
