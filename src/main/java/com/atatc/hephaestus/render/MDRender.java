package com.atatc.hephaestus.render;

import com.atatc.hephaestus.component.Component;
import com.atatc.hephaestus.markdown.MDElement;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface MDRender<C extends Component> {
    @NotNull
    MDElement render(C component);
}
