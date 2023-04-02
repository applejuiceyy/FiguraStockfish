package com.github.applejuiceyy.figurastockfish.client;

import com.github.applejuiceyy.figurastockfish.client.wrap.StockfishWrapper;
import com.github.applejuiceyy.figurastockfish.stockfish.StockfishInstance;
import com.github.applejuiceyy.figurastockfish.stockfish.tree.StockfishError;
import net.fabricmc.loader.api.FabricLoader;
import org.moon.figura.avatar.Avatar;

import org.moon.figura.entries.FiguraAPI;
import org.moon.figura.lua.LuaWhitelist;

import java.nio.file.Path;
import java.util.Collection;
import java.util.Collections;
import java.util.concurrent.CompletableFuture;

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

    @Override
    public Collection<Class<?>> getDocsClasses() {
        return Collections.emptyList();
    }


    StockfishAPI(Bridger b) {
        this.b = b;
    }

    @LuaWhitelist
    public CompletableFutureWrapper<StockfishInstance, StockfishWrapper> newEngine() {
        CompletableFuture<StockfishInstance> future;

        if (b.isHost().get()) {
            future = StockfishInstance.bind(FabricLoader.getInstance().getGameDir().resolve(FiguraStockfishClient.path.value).toString());
        }
        else {
            future = CompletableFuture.failedFuture(new StockfishError("Not host"));
        }

        return new CompletableFutureWrapper<>(future, e -> new StockfishWrapper(e, b), b);
    }
}
