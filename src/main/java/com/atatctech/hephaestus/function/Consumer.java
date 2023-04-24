package com.atatctech.hephaestus.function;

import com.atatctech.hephaestus.component.Component;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface Consumer<C extends Component> {
    void accept(@NotNull C component, int depth);
}
