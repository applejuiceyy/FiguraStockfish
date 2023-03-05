package com.github.applejuiceyy.figurastockfish.stockfish.data;

import org.jetbrains.annotations.Nullable;

public record SearchResults(String bestMove, @Nullable String ponder) {
}
