package com.github.applejuiceyy.figurastockfish.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.text.Text;
import org.moon.figura.config.ConfigType;
import org.moon.figura.config.Configs;
import org.moon.figura.utils.FiguraText;

@Environment(EnvType.CLIENT)
public class FiguraStockfishClient implements ClientModInitializer {
    static ConfigType.StringConfig path;
    @Override
    public void onInitializeClient() {
        try {
            Class.forName("org.moon.figura.config.Configs", true, this.getClass().getClassLoader());
        } catch (ClassNotFoundException ignore) { /* shouldn't happen */ }

        ConfigType.Category category = new ConfigType.Category("Figura Stockfish"){{
            this.name = Text.translatable("figurastockfish.config.category");
            this.tooltip = Text.translatable("figurastockfish.config.category.tooltip");
        }};

        String os = System.getProperty("os.name");
        String dpath = os.toLowerCase().contains("windows") ? "stockfish.exe" : "stockfish";
        path = new ConfigType.StringConfig("Path", category, dpath){{
            this.name = Text.translatable("figurastockfish.config.path");
            this.tooltip = Text.translatable("figurastockfish.config.path.tooltip");
        }};
    }
}
