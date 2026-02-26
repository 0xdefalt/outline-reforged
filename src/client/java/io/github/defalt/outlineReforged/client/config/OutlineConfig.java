package io.github.defalt.outlineReforged.client.config;

public class OutlineConfig {

    public int outlineAlpha = 255;
    public int outlineRed = 0;
    public int outlineGreen = 255;
    public int outlineBlue = 255;
    public float outlineThickness = 2.0F;
    public int outlineGlow = 0;
    public boolean rainbowEnabled = false;
    public float rainbowCyclesPerSecond = 0.15F;

    public int toArgb() {
        return ((outlineAlpha & 0xFF) << 24) | ((outlineRed & 0xFF) << 16) | ((outlineGreen & 0xFF) << 8) | (outlineBlue & 0xFF);
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

}