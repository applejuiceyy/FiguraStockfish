package com.github.applejuiceyy.figurastockfish.stockfish.data;

import java.util.HashMap;

public record EngineInfo(String name, String author, HashMap<String, OptionEntry> options) {
    public record OptionEntry(String type, String defaultValue, String min, String max) { }
}
