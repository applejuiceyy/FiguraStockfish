package com.github.applejuiceyy.figurastockfish.stockfish.processor;

import com.github.applejuiceyy.figurastockfish.stockfish.StringReader;

public class ReadyProcessor implements StockfishProcessor<Void> {
    public static final ReadyProcessor INSTANCE = new ReadyProcessor();

    @Override
    public boolean shouldSendReadyOk() {
        return true;
    }

    @Override
    public boolean feed(StringReader reader) {
        return !reader.expect("readyok");
    }

    @Override
    public Void build() {
        return null;
    }
}
