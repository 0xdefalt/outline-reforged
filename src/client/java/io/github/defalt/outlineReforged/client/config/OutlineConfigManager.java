package io.github.defalt.outlineReforged.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.awt.*;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class OutlineConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("outline-reforged.json");
    private static OutlineConfig outlineConfig = new OutlineConfig();

    private OutlineConfigManager() {
        // TODO: not yet implemented
    }

    public static void load() {
        if (Files.notExists(PATH)) {
            save();
            return;
        }
        try (Reader reader = Files.newBufferedReader(PATH)) {
            OutlineConfig outlineConfig = GSON.fromJson(reader, OutlineConfig.class);
            if (outlineConfig != null) {
                OutlineConfigManager.outlineConfig = sanitize(outlineConfig);
            } else {
                OutlineConfigManager.outlineConfig = new OutlineConfig();
            }
        } catch (Exception exception) {
            outlineConfig = new OutlineConfig();
        }
    }

    public static void save() {
        outlineConfig = sanitize(outlineConfig);
        try {
            Files.createDirectories(PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(PATH)) {
                GSON.toJson(outlineConfig, writer);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static OutlineConfig getOutlineConfig() {
        return outlineConfig;
    }

    public static int getCurrentArgbColor() {
        OutlineConfig current = outlineConfig;
        int alpha = clamp(current.outlineAlpha, 0, 255);
        if (!current.rainbowEnabled) {
            return current.toArgb();
        }
        float cycles = Math.max(0.01F, current.rainbowCyclesPerSecond);
        float seconds = System.nanoTime() / 1_000_000_000.0F;
        float hue = (seconds * cycles) % 1.0F;
        int rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F) & 0x00FFFFFF;
        return (alpha << 24) | rgb;
    }

    private static OutlineConfig sanitize(OutlineConfig outlineConfig) {
        outlineConfig.outlineAlpha = clamp(outlineConfig.outlineAlpha, 0, 255);
        outlineConfig.outlineRed = clamp(outlineConfig.outlineRed, 0, 255);
        outlineConfig.outlineGreen = clamp(outlineConfig.outlineGreen, 0, 255);
        outlineConfig.outlineBlue = clamp(outlineConfig.outlineBlue, 0, 255);
        outlineConfig.outlineThickness = clamp(outlineConfig.outlineThickness, 0.5F, 16.0F);
        outlineConfig.outlineGlow = clamp(outlineConfig.outlineGlow, 0, 10);
        outlineConfig.rainbowCyclesPerSecond = clamp(outlineConfig.rainbowCyclesPerSecond, 0.01F, 10.0F);
        return outlineConfig;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

}