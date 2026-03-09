package io.github.defalt.outlineReforged.client.config;

public class OutlineConfig {

    public int outlineAlpha = 0;
    public int outlineRed = 0;
    public int outlineGreen = 0;
    public int outlineBlue = 0;
    public float outlineThickness = 2.0F;
    public int outlineGlow = 0;
    public boolean rainbowEnabled = false;
    public float rainbowCyclesPerSecond = 0.10F;

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