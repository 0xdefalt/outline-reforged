package io.github.defalt.outlineReforged.client.config;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.awt.*;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class OutlineConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("defalt-outline-reforged.json");
    private static final Path LEGACY_PATH = FabricLoader.getInstance().getConfigDir().resolve("outline-reforged.json");
    private static OutlineConfig outlineConfig = new OutlineConfig();

    private OutlineConfigManager() {
        // TODO: not yet implemented
    }

    public static void load() {
        migrateLegacyPathIfNeeded();
        if (Files.notExists(PATH)) {
            save();
            return;
        }
        try (Reader reader = Files.newBufferedReader(PATH)) {
            JsonElement root = JsonParser.parseReader(reader);
            OutlineConfig loadedConfig = GSON.fromJson(root, OutlineConfig.class);
            if (loadedConfig != null) {
                if (root != null && root.isJsonObject()) {
                    applyLegacyMigration(loadedConfig, root.getAsJsonObject());
                }
                outlineConfig = sanitize(loadedConfig);
            } else {
                outlineConfig = new OutlineConfig();
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
        int primary = current.getOutlineArgb();
        int secondary = current.getSecondaryArgb();
        OutlineColorMode mode = current.getColorMode();
        if (mode == OutlineColorMode.STATIC) {
            return primary;
        }
        float cycles = Math.max(0.01F, current.animationCyclesPerSecond);
        double seconds = System.nanoTime() / 1_000_000_000.0;
        return switch (mode) {
            case RAINBOW -> resolveRainbowColor(primary, seconds, cycles);
            case GRADIENT -> lerpArgb(primary, secondary, triangleWave(seconds, cycles));
            case PULSE -> lerpArgb(primary, secondary, pulseBlend(seconds, cycles));
            case BREATHING -> resolveBreathingColor(primary, seconds, cycles);
            case STATIC -> primary;
        };
    }

    private static void migrateLegacyPathIfNeeded() {
        if (Files.exists(PATH) || Files.notExists(LEGACY_PATH)) {
            return;
        }
        try {
            Files.move(LEGACY_PATH, PATH);
        } catch (Exception ignored) {
        }
    }

    private static void applyLegacyMigration(OutlineConfig loadedConfig, JsonObject root) {
        if (!root.has("colorMode") && root.has("rainbowEnabled") && root.get("rainbowEnabled").isJsonPrimitive()) {
            if (root.get("rainbowEnabled").getAsBoolean()) {
                loadedConfig.setColorMode(OutlineColorMode.RAINBOW);
            }
        }
        if (!root.has("animationCyclesPerSecond") && root.has("rainbowCyclesPerSecond") && root.get("rainbowCyclesPerSecond").isJsonPrimitive()) {
            loadedConfig.animationCyclesPerSecond = root.get("rainbowCyclesPerSecond").getAsFloat();
        }
    }

    private static int resolveRainbowColor(int primaryArgb, double seconds, float cycles) {
        int alpha = (primaryArgb >>> 24) & 0xFF;
        float hue = fract((float) (seconds * cycles));
        int rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F) & 0x00FFFFFF;
        return (alpha << 24) | rgb;
    }

    private static int resolveBreathingColor(int primaryArgb, double seconds, float cycles) {
        float breathing = oscillation(seconds, cycles);
        float brightnessFactor = 0.55F + (breathing * 0.45F);
        float alphaFactor = 0.40F + (breathing * 0.60F);
        int alpha = Math.round(((primaryArgb >>> 24) & 0xFF) * alphaFactor);
        int rgb = scaleRgb(primaryArgb & 0x00FFFFFF, brightnessFactor);
        return (alpha << 24) | rgb;
    }

    private static float triangleWave(double seconds, float cycles) {
        float phase = fract((float) (seconds * cycles));
        return phase < 0.5F ? phase * 2.0F : (1.0F - phase) * 2.0F;
    }

    private static float pulseBlend(double seconds, float cycles) {
        float pulse = oscillation(seconds, cycles);
        return pulse * pulse * pulse;
    }

    private static float oscillation(double seconds, float cycles) {
        double angle = seconds * cycles * Math.PI * 2.0;
        return 0.5F + (0.5F * (float) Math.sin(angle));
    }

    private static int scaleRgb(int rgb, float factor) {
        int red = clamp(Math.round(((rgb >>> 16) & 0xFF) * factor), 0, 255);
        int green = clamp(Math.round(((rgb >>> 8) & 0xFF) * factor), 0, 255);
        int blue = clamp(Math.round((rgb & 0xFF) * factor), 0, 255);
        return (red << 16) | (green << 8) | blue;
    }

    private static int lerpArgb(int fromArgb, int toArgb, float factor) {
        float t = clamp(factor, 0.0F, 1.0F);
        int alpha = lerp((fromArgb >>> 24) & 0xFF, (toArgb >>> 24) & 0xFF, t);
        int red = lerp((fromArgb >>> 16) & 0xFF, (toArgb >>> 16) & 0xFF, t);
        int green = lerp((fromArgb >>> 8) & 0xFF, (toArgb >>> 8) & 0xFF, t);
        int blue = lerp(fromArgb & 0xFF, toArgb & 0xFF, t);
        return (alpha << 24) | (red << 16) | (green << 8) | blue;
    }

    private static int lerp(int from, int to, float factor) {
        return Math.round(from + ((to - from) * factor));
    }

    private static float fract(float value) {
        return value - (float) Math.floor(value);
    }

    private static OutlineConfig sanitize(OutlineConfig outlineConfig) {
        outlineConfig.outlineAlpha = clamp(outlineConfig.outlineAlpha, 0, 255);
        outlineConfig.outlineRed = clamp(outlineConfig.outlineRed, 0, 255);
        outlineConfig.outlineGreen = clamp(outlineConfig.outlineGreen, 0, 255);
        outlineConfig.outlineBlue = clamp(outlineConfig.outlineBlue, 0, 255);
        outlineConfig.secondaryAlpha = clamp(outlineConfig.secondaryAlpha, 0, 255);
        outlineConfig.secondaryRed = clamp(outlineConfig.secondaryRed, 0, 255);
        outlineConfig.secondaryGreen = clamp(outlineConfig.secondaryGreen, 0, 255);
        outlineConfig.secondaryBlue = clamp(outlineConfig.secondaryBlue, 0, 255);
        outlineConfig.outlineThickness = clamp(outlineConfig.outlineThickness, 0.5F, 16.0F);
        outlineConfig.outlineGlow = clamp(outlineConfig.outlineGlow, 0, 10);
        outlineConfig.animationCyclesPerSecond = clamp(outlineConfig.animationCyclesPerSecond, 0.01F, 10.0F);
        outlineConfig.colorMode = OutlineColorMode.fromId(outlineConfig.colorMode).id();
        return outlineConfig;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

}