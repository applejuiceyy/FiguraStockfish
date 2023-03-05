package com.github.applejuiceyy.figurastockfish.stockfish.processor;


import com.github.applejuiceyy.figurastockfish.stockfish.StringReader;

public interface StockfishProcessor<T> {
    boolean shouldSendReadyOk();
    boolean feed(StringReader reader);
    T build();
}
