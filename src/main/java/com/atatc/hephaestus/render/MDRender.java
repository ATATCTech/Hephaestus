package com.atatc.hephaestus.render;

import com.atatc.hephaestus.component.Component;
import com.vladsch.flexmark.util.ast.Node;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MDRender<C extends Component> {
    @NotNull
    Node render(C component);
}
