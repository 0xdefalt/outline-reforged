package io.github.defalt.blockselector.client.config;

public class BlockSelectorConfig {

    public int outlineAlpha = 255;
    public int outlineRed = 255;
    public int outlineGreen = 255;
    public int outlineBlue = 255;
    public int secondaryAlpha = 255;
    public int secondaryRed = 255;
    public int secondaryGreen = 255;
    public int secondaryBlue = 255;
    public float outlineThickness = 2.0F;
    public int outlineGlow = 0;
    public String colorMode = BlockSelectorColorMode.STATIC.id();
    public float animationCyclesPerSecond = 0.10F;

    public int toArgb() {
        return ((outlineAlpha & 0xFF) << 24)
                | ((outlineRed & 0xFF) << 16)
                | ((outlineGreen & 0xFF) << 8)
                | (outlineBlue & 0xFF);
    }

    public int getOutlineArgb() {
        return toArgb();
    }

    public void setOutlineArgb(int argb) {
        outlineAlpha = (argb >>> 24) & 0xFF;
        outlineRed = (argb >>> 16) & 0xFF;
        outlineGreen = (argb >>> 8) & 0xFF;
        outlineBlue = argb & 0xFF;
    }

    public int getSecondaryArgb() {
        return ((secondaryAlpha & 0xFF) << 24)
                | ((secondaryRed & 0xFF) << 16)
                | ((secondaryGreen & 0xFF) << 8)
                | (secondaryBlue & 0xFF);
    }

    public void setSecondaryArgb(int argb) {
        secondaryAlpha = (argb >>> 24) & 0xFF;
        secondaryRed = (argb >>> 16) & 0xFF;
        secondaryGreen = (argb >>> 8) & 0xFF;
        secondaryBlue = argb & 0xFF;
    }

    public BlockSelectorColorMode getColorMode() {
        return BlockSelectorColorMode.fromId(colorMode);
    }

    public void setColorMode(BlockSelectorColorMode mode) {
        colorMode = mode.id();
    }

}