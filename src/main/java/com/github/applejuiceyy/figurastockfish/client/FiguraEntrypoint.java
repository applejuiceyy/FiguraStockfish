package com.github.applejuiceyy.figurastockfish.client;

import com.github.applejuiceyy.figurastockfish.client.wrap.*;
import org.moon.figura.avatar.Avatar;

import org.moon.figura.entries.FiguraAPI;
import org.moon.figura.lua.LuaWhitelist;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@LuaWhitelist
public class FiguraEntrypoint implements FiguraAPI {
    @Override
    public FiguraAPI build(Avatar avatar) {
        return new StockfishAPI(
                new Bridger(
                        o -> avatar.luaRuntime.typeManager.javaToLua(o),
                        (c, o) -> avatar.run(c, avatar.tick, o),
                        () -> avatar.isHost
                )
        );
    }

    @Override
    public String getName() {
        return "stockfish";
    }

    @Override
    public Collection<Class<?>> getWhitelistedClasses() {
        return List.of(StockfishAPI.class, CompletableFutureWrapper.class,

                AnalysisTaskWrapper.class, EngineInfoWrapper.class, OptionEntryWrapper.class,
                SearchInfoWrapper.class, SearchResultsWrapper.class, StockfishWrapper.class
        );
    }

    @Override
    public Collection<Class<?>> getDocsClasses() {
        return Collections.emptyList();
    }
}
