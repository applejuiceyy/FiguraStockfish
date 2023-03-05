package com.github.applejuiceyy.figurastockfish.client.wrap;

import com.github.applejuiceyy.figurastockfish.stockfish.data.PartialSearchInfo;
import org.moon.figura.lua.LuaWhitelist;

@LuaWhitelist
public class SearchInfoWrapper {
    private final PartialSearchInfo e;

    @LuaWhitelist
    public int getDepth() {
        return e.depth();
    }

    @LuaWhitelist
    public int getNodes() {
        return e.nodes();
    }

    @LuaWhitelist
    public String getPv() {
        return e.pv();
    }

    @LuaWhitelist
    public String getScoreCP() {
        return e.scoreCP();
    }

    @LuaWhitelist
    public String getScoreMate() {
        return e.scoreMate();
    }

    public SearchInfoWrapper(PartialSearchInfo e) {
        this.e = e;
    }
}
