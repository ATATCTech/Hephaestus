package com.atatc.hephaestus.skeleton;

import com.atatc.hephaestus.component.Component;
import com.atatc.hephaestus.component.Title;
import com.atatc.hephaestus.component.WrapperComponent;

public class Skeleton {
    static Bone generateSkeleton(Component component, Bone parent) {
        Bone bone = new Bone();
        if (component instanceof WrapperComponent wrapperComponent) {
            // todo: test
            if (!wrapperComponent.contains(Title.class)) return null;
            bone.setComponent(wrapperComponent);
            bone.setName(wrapperComponent.getText());
            parent.appendChild(bone);
            wrapperComponent.forEach((c, depth) -> {
                Bone b = generateSkeleton(c, bone);
                if (b != null) bone.appendChild(b);
                return true;
            });
            return bone;
        }
        return null;
    }
    public static Bone generateSkeleton(Component component) {
        return generateSkeleton(component, null);
    }
}
