package com.github.applejuiceyy.figurastockfish.client;

import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.Varargs;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public record Bridger(Function<Object, Varargs> j2l, IndirectCall<LuaFunction> eventFunction, Supplier<Boolean> isHost) {
    @FunctionalInterface
    public interface IndirectCall<T> {
        void call(T call, Object... v);
    }
}
