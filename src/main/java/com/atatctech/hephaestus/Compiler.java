package com.atatctech.hephaestus;


import com.atatctech.hephaestus.component.Ref;

@FunctionalInterface
public interface Compiler {
    void compile(Ref... refs);
}
