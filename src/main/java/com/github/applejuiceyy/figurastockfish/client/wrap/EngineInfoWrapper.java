package com.github.applejuiceyy.figurastockfish.client.wrap;

import com.github.applejuiceyy.figurastockfish.client.Bridger;
import com.github.applejuiceyy.figurastockfish.stockfish.data.EngineInfo;
import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.Varargs;
import org.moon.figura.lua.LuaWhitelist;

import java.util.function.Function;

@LuaWhitelist
public class EngineInfoWrapper {
    private final EngineInfo info;
    private final Bridger b;

    public EngineInfoWrapper(EngineInfo info, Bridger b) {
        this.info = info;
        this.b = b;
    }

    @LuaWhitelist
    public String getName() {
        return info.name();
    }

    @LuaWhitelist
    public String getAuthors() {
        return info.author();
    }

    @LuaWhitelist
    public LuaTable getOptions() {
        LuaTable table = new LuaTable();
        info.options().forEach((s, o) -> table.set(LuaValue.valueOf(s), b.j2l().apply(new OptionEntryWrapper(o)).arg1()));
        return table;
    }
}
