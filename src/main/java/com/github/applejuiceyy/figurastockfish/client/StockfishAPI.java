package com.github.applejuiceyy.figurastockfish.client;

import com.github.applejuiceyy.figurastockfish.client.wrap.StockfishWrapper;
import com.github.applejuiceyy.figurastockfish.stockfish.StockfishInstance;
import net.fabricmc.loader.api.FabricLoader;
import org.moon.figura.avatar.Avatar;
import org.moon.figura.lua.FiguraAPI;
import org.moon.figura.lua.LuaWhitelist;

import java.util.Collection;

@LuaWhitelist
public class StockfishAPI implements FiguraAPI {
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



    StockfishAPI(Bridger b) {
        this.b = b;
    }

    @LuaWhitelist
    public CompletableFutureWrapper<StockfishInstance, StockfishWrapper> newEngine() {
        return new CompletableFutureWrapper<>(
                StockfishInstance.bind(FabricLoader.getInstance().getGameDir().resolve("stockfish.exe").toString()),
                e -> new StockfishWrapper(e, b),
                b
        );
    }
}
