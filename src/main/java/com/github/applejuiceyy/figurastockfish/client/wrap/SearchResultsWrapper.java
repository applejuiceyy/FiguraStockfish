package com.github.applejuiceyy.figurastockfish.client.wrap;

import com.github.applejuiceyy.figurastockfish.stockfish.data.SearchResults;
import org.moon.figura.lua.LuaWhitelist;

@LuaWhitelist
public class SearchResultsWrapper {
    private final SearchResults searchResults;

    public SearchResultsWrapper(SearchResults searchResults) {
        this.searchResults = searchResults;
    }

    @LuaWhitelist
    public String getBestMove() {
        return searchResults.bestMove();
    }

    @LuaWhitelist
    public String getPonder() {
        return searchResults.ponder();
    }
}
