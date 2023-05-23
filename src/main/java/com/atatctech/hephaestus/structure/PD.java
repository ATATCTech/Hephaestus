package com.atatctech.hephaestus.structure;

import com.atatctech.hephaestus.component.Component;
import com.atatctech.hephaestus.component.MultiComponent;
import com.atatctech.hephaestus.component.WrapperComponent;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Pattern descriptor.
 */
public final class PD {
    /**
     * Check if the component is one of the listed.
     * Note that the tag names do not include the subclasses. The tag names must be exactly the same.
     * @param tagNames acceptable tag names
     * @return constraint chain
     */
    public static @NotNull ConstraintChain is(@NotNull String... tagNames) {
        return new ConstraintChain(component -> {
            for (String tagName : tagNames) if (component.getTagName().equals(tagName)) return true;
            return false;
        });
    }

    /**
     * Check if the component is one of the listed.
     * Note that the component classes do include the subclasses.
     * @param componentClasses acceptable component classes
     * @return constraint chain
     */
    public static @NotNull ConstraintChain is(@NotNull Class<?>... componentClasses) {
        return new ConstraintChain(component -> {
            for (Class<?> componentClass : componentClasses) if (componentClass.isInstance(component)) return true;
            return false;
        });
    }

    /**
     * Switch to the children's level. This will change the level of the chain.
     * Note that this can only be applied to a `WrapperComponent`.
     * @return constraint chain
     */
    public static @NotNull ConstraintChain children() {
        return new ConstraintChain(component -> component instanceof WrapperComponent) {
            @Override
            public boolean conforms(@NotNull Component component) {
                Constraint nc = getNextChain();
                if (nc == null) return true;
                return component instanceof WrapperComponent wrapperComponent && nc.conforms(wrapperComponent.getChildren());
            }
        };
    }

    /**
     * For each component in the `MultiComponent`. This will change the level of the chain.
     * Note that this can only be applied to a `MultiComponent`.
     * @param fromDepth start at depth (inclusive)
     * @param toDepth end at depth (exclusive)
     * @return constraint chain
     */
    public static @NotNull ConstraintChain forEach(int fromDepth, int toDepth) {
        return new ConstraintChain(component -> component instanceof MultiComponent) {
            @Override
            public boolean conforms(@NotNull Component component) {
                Constraint nc = getNextChain();
                if (nc == null) return true;
                AtomicBoolean r = new AtomicBoolean(true);
                component.parallelTraversal((child, depth) -> {
                    if (depth < fromDepth || (toDepth >= 0 && depth >= toDepth)) return;
                    if (!nc.conforms(child)) r.set(false);
                });
                return r.get();
            }
        };
    }

    public static @NotNull ConstraintChain forEach(int toDepth) {
        return forEach(0, toDepth);
    }

    public static @NotNull ConstraintChain forEach() {
        return forEach(0, 1);
    }

    /**
     * Check if the `MultiComponent` has the required number of satisfaction.
     * Note that this can only be applied to a `MultiComponent`.
     * @param least at least (inclusive)
     * @param most at most (inclusive)
     * @param satisfaction constraint chain to be satisfied
     * @return constraint chain
     */
    private static @NotNull ConstraintChain has(int least, int most, @NotNull ConstraintChain satisfaction) {
        return new ConstraintChain(component -> {
            if (!(component instanceof MultiComponent multiComponent)) return false;
            int nLeast = least < 0 ? multiComponent.size() : least;
            int nMost = most < 0 ? multiComponent.size() : most;
            AtomicInteger counter = new AtomicInteger();
            forEach().and(satisfaction.and(new ConstraintChain(ignored -> {
                counter.getAndIncrement();
                return true;
            }))).conforms(component);
            int i = counter.get();
            return i >= nLeast && i <= nMost;
        });
    }

    /**
     * Check if the `MultiComponent` has the required number of acceptable components.
     * Note that the tag names do not include the subclasses. The tag names must be exactly the same.
     * @param least at least (inclusive)
     * @param most at most (inclusive)
     * @param tagNames acceptable tag names
     * @return constraint chain
     */
    public static @NotNull ConstraintChain has(int least, int most, @NotNull String... tagNames) {
        return has(least, most, is(tagNames));
    }

    /**
     * Check if the `MultiComponent` has the required number of acceptable components.
     * Note that the component classes do include the subclasses.
     * @param least at least (inclusive)
     * @param most at most (inclusive)
     * @param componentClasses acceptable component classes
     * @return constraint chain
     */
    public static @NotNull ConstraintChain has(int least, int most, @NotNull Class<?>... componentClasses) {
        return has(least, most, is(componentClasses));
    }

    /**
     * Check if the `MultiComponent` has more than {@param than} acceptable components.
     * Note that the tag names do not include the subclasses. The tag names must be exactly the same.
     * @param than expect more than this number (exclusive)
     * @param tagNames acceptable tag names
     * @return constraint chain
     */
    public static @NotNull ConstraintChain more(int than, @NotNull String... tagNames) {
        return has(than + 1, -1, tagNames);
    }

    /**
     * Check if the `MultiComponent` has more than {@param than} acceptable components.
     * Note that the component classes do include the subclasses.
     * @param than expect more than this number (exclusive)
     * @param componentClasses acceptable component classes
     * @return constraint chain
     */
    public static @NotNull ConstraintChain more(int than, @NotNull Class<?>... componentClasses) {
        return has(than + 1, -1, componentClasses);
    }

    /**
     * Check if the `MultiComponent` has at least one acceptable components.
     * Note that the tag names do not include the subclasses. The tag names must be exactly the same.
     * @param tagNames acceptable tag names
     * @return constraint chain
     */
    public static @NotNull ConstraintChain includes(@NotNull String... tagNames) {
        return more(0, tagNames);
    }

    /**
     * Check if the `MultiComponent` has at least one acceptable components.
     * Note that the component classes do include the subclasses.
     * @param componentClasses acceptable component classes
     * @return constraint chain
     */
    public static @NotNull ConstraintChain includes(@NotNull Class<?>... componentClasses) {
        return more(0, componentClasses);
    }

    /**
     * Check if all the components in the `MultiComponent` are acceptable.
     * Note that the tag names do not include the subclasses. The tag names must be exactly the same.
     * @param tagNames acceptable tag names
     * @return constraint chain
     */
    public static @NotNull ConstraintChain all(@NotNull String... tagNames) {
        return forEach().and(is(tagNames));
    }

    /**
     * Check if all the components in the `MultiComponent` are acceptable.
     * Note that the component classes do include the subclasses.
     * @param componentClasses acceptable component classes
     * @return constraint chain
     */
    public static @NotNull ConstraintChain all(@NotNull Class<?>... componentClasses) {
        return forEach().and(is(componentClasses));
    }
}
