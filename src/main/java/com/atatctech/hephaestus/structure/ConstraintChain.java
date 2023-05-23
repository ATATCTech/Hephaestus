package com.atatctech.hephaestus.structure;

import com.atatctech.hephaestus.component.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConstraintChain implements Constraint {
    protected final Constraint base;
    protected @Nullable ConstraintChain nextChain;

    public ConstraintChain() {
        this.base = component -> true;
    }

    public ConstraintChain(@NotNull Constraint base) {
        this.base = base;
    }

    public @NotNull Constraint getBase() {
        return base;
    }

    public void setNextChain(@Nullable ConstraintChain nextChain) {
        this.nextChain = nextChain;
    }

    public @Nullable ConstraintChain getNextChain() {
        return nextChain;
    }

    /**
     * Add a constraint chain to current level, preventing level-changing in customized constraint chains.
     * @param next the appendix constraint chain
     */
    public void addNext(@NotNull ConstraintChain next) {
        ConstraintChain nextChain = getNextChain();
        if (nextChain == null) setNextChain(new ConstraintChain(next));
        else nextChain.addNext(next);
    }

    public @NotNull ConstraintChain and(@NotNull ConstraintChain next) {
        addNext(next);
        return this;
    }

    @Override
    public boolean conforms(@NotNull Component component) {
        if (!getBase().conforms(component)) return false;
        Constraint next = getNextChain();
        return next == null || next.conforms(component);
    }
}
