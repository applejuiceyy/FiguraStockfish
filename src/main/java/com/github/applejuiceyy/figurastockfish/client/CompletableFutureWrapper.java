package com.github.applejuiceyy.figurastockfish.client;

import net.minecraft.client.MinecraftClient;
import org.jetbrains.annotations.Contract;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.Varargs;
import org.moon.figura.lua.LuaWhitelist;

import java.util.ArrayList;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

@LuaWhitelist
public class CompletableFutureWrapper<T, R> {
    private final CompletableFuture<T> wrapped;
    private final Function<T, R> wrapper;
    private final Bridger b;
    public ArrayList<LuaFunction> subs = new ArrayList<>();

    public CompletableFutureWrapper(CompletableFuture<T> wrapped, Function<T, R> wrapper, Bridger b) {
        this.wrapped = wrapped;
        this.wrapper = wrapper;
        this.b = b;
    }

    @LuaWhitelist
    @Contract("_->this")
    public CompletableFutureWrapper<T, R> andThen(LuaFunction func) {
        wrapped.whenComplete((e, v) -> {
            if (v == null) {
                MinecraftClient.getInstance().execute(() -> b.eventFunction().call(func, wrapper.apply(e)));
            }
        });
        return this;
    }

    @LuaWhitelist
    @Contract("_->this")
    public CompletableFutureWrapper<T, R> onException(LuaFunction func) {
        wrapped.whenComplete((e, v) -> {
            if (v != null) {
                MinecraftClient.getInstance().execute(() -> b.eventFunction().call(func, v.getLocalizedMessage()));
            }
        });
        return this;
    }

    @LuaWhitelist
    public boolean completed() {
        return wrapped.isDone();
    }
}
