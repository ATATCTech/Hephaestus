package com.atatctech.hephaestus.structure;

import com.atatctech.hephaestus.component.Component;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Constraint {
    /**
     * Define constraint rule.
     * @param component component to be constrained
     * @return true if the component satisfy the constraint else false
     */
    boolean conforms(@NotNull Component component);
}
