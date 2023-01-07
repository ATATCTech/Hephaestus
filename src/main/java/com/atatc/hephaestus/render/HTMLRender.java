package com.atatc.hephaestus.render;

import com.atatc.hephaestus.component.Component;
import org.jetbrains.annotations.NotNull;
import org.jsoup.nodes.Element;

@FunctionalInterface
public interface HTMLRender<C extends Component> {
    @NotNull
    Element render(C component);
}
