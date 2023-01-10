package com.atatc.hephaestus.function;

import com.atatc.hephaestus.component.Component;

@FunctionalInterface
public interface Consumer<C extends Component> {
    boolean accept(C component, int depth);
}
