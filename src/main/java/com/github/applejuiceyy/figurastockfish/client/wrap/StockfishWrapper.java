package com.github.applejuiceyy.figurastockfish.client.wrap;

import com.github.applejuiceyy.figurastockfish.client.Bridger;
import com.github.applejuiceyy.figurastockfish.client.CompletableFutureWrapper;
import com.github.applejuiceyy.figurastockfish.stockfish.StockfishInstance;
import org.luaj.vm2.Varargs;
import org.moon.figura.avatar.Avatar;
import org.moon.figura.lua.FiguraAPI;
import org.moon.figura.lua.LuaWhitelist;

import java.util.Collection;
import java.util.function.Function;

@LuaWhitelist
public class StockfishWrapper implements FiguraAPI {
    private final StockfishInstance inst;
    private final Bridger b;

    @Override
    public FiguraAPI build(Avatar avatar) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Collection<Class<?>> getWhitelistedClasses() {
        return null;
    }

    public StockfishWrapper(StockfishInstance inst, Bridger b) {
        this.inst = inst;
        this.b = b;
    }

    @LuaWhitelist
    public CompletableFutureWrapper<Void, Void> setFEN(String fen) {
        return new CompletableFutureWrapper<>(inst.setFEN(fen), Function.identity(), b);
    }

    @LuaWhitelist
    public CompletableFutureWrapper<Void, Void> setFEN(String fen, String moves) {
        return new CompletableFutureWrapper<>(inst.setFEN(fen, moves), Function.identity(), b);
    }

    @LuaWhitelist
    public EngineInfoWrapper getInfo() {
        return new EngineInfoWrapper(this.inst.info, b);
    }

    @LuaWhitelist
    public AnalysisTaskWrapper calculate() {
        return new AnalysisTaskWrapper(inst.calculate(), b);
    }

    @LuaWhitelist
    public void setLevel(int level) {
        inst.setOption("Skill Level", String.valueOf(level));
    }

    @LuaWhitelist
    public void close() {
        inst.close();
    }
}
