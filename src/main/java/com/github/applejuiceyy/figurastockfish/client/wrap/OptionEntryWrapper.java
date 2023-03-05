package com.github.applejuiceyy.figurastockfish.client.wrap;

import com.github.applejuiceyy.figurastockfish.stockfish.data.EngineInfo;
import org.moon.figura.lua.LuaWhitelist;

@LuaWhitelist
public class OptionEntryWrapper {
    private final EngineInfo.OptionEntry o;

    public String getType() {
        return o.type();
    }

    public String getDefaultValue() {
        return o.defaultValue();
    }

    public String getMin() {
        return o.min();
    }

    public String getMax() {
        return o.max();
    }

    public OptionEntryWrapper(EngineInfo.OptionEntry o) {
        this.o = o;
    }
}
