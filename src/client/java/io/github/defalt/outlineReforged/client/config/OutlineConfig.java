package io.github.defalt.outlineReforged.client.config;

public class OutlineConfig {

    public int outlineAlpha = 255;
    public int outlineRed = 0;
    public int outlineGreen = 255;
    public int outlineBlue = 255;
    public int fillAlpha = 40;
    public int fillRed = 0;
    public int fillGreen = 255;
    public int fillBlue = 255;
    public float outlineThickness = 2.0F;
    public int outlineGlow = 0;
    public boolean rainbowEnabled = false;
    public float rainbowCyclesPerSecond = 0.15F;

    public int toArgb() {
        return ((outlineAlpha & 0xFF) << 24) | ((outlineRed & 0xFF) << 16) | ((outlineGreen & 0xFF) << 8) | (outlineBlue & 0xFF);
    }

    public int fillToArgb() {
        return ((fillAlpha & 0xFF) << 24) | ((fillRed & 0xFF) << 16) | ((fillGreen & 0xFF) << 8) | (fillBlue & 0xFF);
    }

}