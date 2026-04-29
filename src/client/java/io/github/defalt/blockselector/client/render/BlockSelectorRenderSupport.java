package io.github.defalt.blockselector.client.render;

import io.github.defalt.blockselector.client.config.BlockSelectorConfig;
import io.github.defalt.blockselector.client.config.BlockSelectorConfigManager;

public final class BlockSelectorRenderSupport {

    private BlockSelectorRenderSupport() {
        // TODO: not yet implemented
    }

    public static int resolveConfiguredColor() {
        BlockSelectorConfig blockSelectorConfig = BlockSelectorConfigManager.getBlockSelectorConfig();
        int argb = BlockSelectorConfigManager.getCurrentArgbColor();
        int alpha = Math.min(255, ((argb >>> 24) & 0xFF) + blockSelectorConfig.outlineGlow * 12);
        return (alpha << 24) | (argb & 0x00FFFFFF);
    }

    public static float resolveConfiguredLineWidth() {
        BlockSelectorConfig config = BlockSelectorConfigManager.getBlockSelectorConfig();
        return Math.max(0.5F, config.outlineThickness) + (config.outlineGlow * 0.15F);
    }

}