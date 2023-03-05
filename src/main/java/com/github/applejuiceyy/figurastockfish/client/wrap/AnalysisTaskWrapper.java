package com.github.applejuiceyy.figurastockfish.client.wrap;

import com.github.applejuiceyy.figurastockfish.client.Bridger;
import com.github.applejuiceyy.figurastockfish.client.CompletableFutureWrapper;
import com.github.applejuiceyy.figurastockfish.stockfish.data.SearchResults;
import com.github.applejuiceyy.figurastockfish.stockfish.processor.AnalysisTask;
import com.github.applejuiceyy.figurastockfish.stockfish.tree.StockfishError;
import net.minecraft.client.MinecraftClient;
import org.luaj.vm2.LuaFunction;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.moon.figura.lua.LuaWhitelist;

import java.util.function.Function;

@LuaWhitelist
public class AnalysisTaskWrapper {
    private final AnalysisTask wrapped;
    private final Bridger b;

    AnalysisTaskWrapper(AnalysisTask wrapped, Bridger b) {
        this.wrapped = wrapped;
        this.b = b;
    }

    @LuaWhitelist
    public AnalysisTaskWrapper onUpdate(LuaFunction value) {
        wrapped.onUpdate(
                e -> MinecraftClient.getInstance().execute(() -> {
                    b.eventFunction().call(value, new SearchInfoWrapper(e));
                })
        );
        return this;
    }

    @LuaWhitelist
    public CompletableFutureWrapper<SearchResults, SearchResultsWrapper> start(String cmd) {
        return new CompletableFutureWrapper<>(
                this.wrapped.start(cmd),
                SearchResultsWrapper::new,
                b
        );
    }

    @LuaWhitelist
    public Varargs stop() {
        try {
            wrapped.stop();
            return LuaValue.TRUE;
        }
        catch (StockfishError e) {
            return LuaValue.varargsOf(LuaValue.FALSE, LuaValue.valueOf(e.getLocalizedMessage()));
        }
    }
}
