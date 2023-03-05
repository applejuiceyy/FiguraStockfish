package com.github.applejuiceyy.figurastockfish.client;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.Varargs;

import java.util.function.Consumer;
import java.util.function.Function;

public record Bridger(Function<Object, Varargs> j2l, IndirectCall<LuaFunction> eventFunction) {
    @FunctionalInterface
    public interface IndirectCall<T> {
        void call(T call, Object... v);
    }
}
