package com.atatctech.hephaestus.function;

import com.atatctech.hephaestus.component.Component;

@FunctionalInterface
public interface Consumer<C extends Component> {
    boolean accept(C component, int depth);
}
