package com.atatc.hephaestus.render;

import com.atatc.hephaestus.component.Component;
import com.atatc.hephaestus.html.HTMLElement;
import org.jetbrains.annotations.NotNull;

@FunctionalInterface
public interface HTMLRender<C extends Component> {
    @NotNull
    HTMLElement render(C component);
}
