package io.github.defalt.blockselector.client.config;

import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;

import java.awt.*;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;

public final class BlockSelectorConfigManager {

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final Path PATH = FabricLoader.getInstance().getConfigDir().resolve("defalt-block-selector.json");
    private static final Path LEGACY_SELECTOR_PATH = FabricLoader.getInstance().getConfigDir().resolve("defalt-selector-outline.json");
    private static final Path LEGACY_REFORGED_PATH = FabricLoader.getInstance().getConfigDir().resolve("defalt-outline-reforged.json");
    private static final Path OLDEST_LEGACY_PATH = FabricLoader.getInstance().getConfigDir().resolve("outline-reforged.json");
    private static BlockSelectorConfig blockSelectorConfig = new BlockSelectorConfig();

    private BlockSelectorConfigManager() {
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
            BlockSelectorConfig loadedConfig = GSON.fromJson(root, BlockSelectorConfig.class);
            if (loadedConfig != null) {
                if (root != null && root.isJsonObject()) {
                    applyLegacyMigration(loadedConfig, root.getAsJsonObject());
                }
                blockSelectorConfig = sanitize(loadedConfig);
            } else {
                blockSelectorConfig = new BlockSelectorConfig();
            }
        } catch (Exception exception) {
            blockSelectorConfig = new BlockSelectorConfig();
        }
    }

    public static void save() {
        blockSelectorConfig = sanitize(blockSelectorConfig);
        try {
            Files.createDirectories(PATH.getParent());
            try (Writer writer = Files.newBufferedWriter(PATH)) {
                GSON.toJson(blockSelectorConfig, writer);
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public static BlockSelectorConfig getBlockSelectorConfig() {
        return blockSelectorConfig;
    }

    public static int getCurrentArgbColor() {
        BlockSelectorConfig current = blockSelectorConfig;
        int primary = current.getOutlineArgb();
        int secondary = current.getSecondaryArgb();
        BlockSelectorColorMode mode = current.getColorMode();
        if (mode == BlockSelectorColorMode.STATIC) {
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
        if (Files.exists(PATH)) {
            return;
        }
        Path source = Files.exists(LEGACY_SELECTOR_PATH) ? LEGACY_SELECTOR_PATH : Files.exists(LEGACY_REFORGED_PATH) ? LEGACY_REFORGED_PATH : OLDEST_LEGACY_PATH;
        if (Files.notExists(source)) {
            return;
        }
        try {
            Files.move(source, PATH);
        } catch (Exception ignored) {
        }
    }

    private static void applyLegacyMigration(BlockSelectorConfig loadedConfig, JsonObject root) {
        if (!root.has("colorMode") && root.has("rainbowEnabled") && root.get("rainbowEnabled").isJsonPrimitive()) {
            if (root.get("rainbowEnabled").getAsBoolean()) {
                loadedConfig.setColorMode(BlockSelectorColorMode.RAINBOW);
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

    private static BlockSelectorConfig sanitize(BlockSelectorConfig blockSelectorConfig) {
        blockSelectorConfig.outlineAlpha = clamp(blockSelectorConfig.outlineAlpha, 0, 255);
        blockSelectorConfig.outlineRed = clamp(blockSelectorConfig.outlineRed, 0, 255);
        blockSelectorConfig.outlineGreen = clamp(blockSelectorConfig.outlineGreen, 0, 255);
        blockSelectorConfig.outlineBlue = clamp(blockSelectorConfig.outlineBlue, 0, 255);
        blockSelectorConfig.secondaryAlpha = clamp(blockSelectorConfig.secondaryAlpha, 0, 255);
        blockSelectorConfig.secondaryRed = clamp(blockSelectorConfig.secondaryRed, 0, 255);
        blockSelectorConfig.secondaryGreen = clamp(blockSelectorConfig.secondaryGreen, 0, 255);
        blockSelectorConfig.secondaryBlue = clamp(blockSelectorConfig.secondaryBlue, 0, 255);
        blockSelectorConfig.outlineThickness = clamp(blockSelectorConfig.outlineThickness, 0.5F, 16.0F);
        blockSelectorConfig.outlineGlow = clamp(blockSelectorConfig.outlineGlow, 0, 10);
        blockSelectorConfig.animationCyclesPerSecond = clamp(blockSelectorConfig.animationCyclesPerSecond, 0.01F, 10.0F);
        blockSelectorConfig.colorMode = BlockSelectorColorMode.fromId(blockSelectorConfig.colorMode).id();
        return blockSelectorConfig;
    }

    private static int clamp(int value, int min, int max) {
        return Math.max(min, Math.min(max, value));
    }

    private static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
    }

}