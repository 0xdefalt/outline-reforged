package io.github.defalt.outlineReforged.client.render;

import io.github.defalt.outlineReforged.client.config.OutlineConfig;
import io.github.defalt.outlineReforged.client.config.OutlineConfigManager;

public final class OutlineRenderSupport {

    private OutlineRenderSupport() {
        // TODO: not yet implemented
    }

    public static int resolveConfiguredColor() {
        OutlineConfig outlineConfig = OutlineConfigManager.getOutlineConfig();
        int argb = OutlineConfigManager.getCurrentArgbColor();
        int alpha = Math.min(255, ((argb >>> 24) & 0xFF) + outlineConfig.outlineGlow * 12);
        return (alpha << 24) | (argb & 0x00FFFFFF);
    }

    public static float resolveConfiguredLineWidth() {
        OutlineConfig config = OutlineConfigManager.getOutlineConfig();
        return Math.max(0.5F, config.outlineThickness) + (config.outlineGlow * 0.15F);
    }

}