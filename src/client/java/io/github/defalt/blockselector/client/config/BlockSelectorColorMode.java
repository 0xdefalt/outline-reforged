package io.github.defalt.blockselector.client.config;

import java.util.Locale;

public enum BlockSelectorColorMode {

    STATIC("static", "Static"),
    RAINBOW("rainbow", "Rainbow"),
    GRADIENT("gradient", "Gradient (2 Colors)"),
    PULSE("pulse", "Pulse"),
    BREATHING("breathing", "Breathing");

    private final String id;
    private final String displayName;

    BlockSelectorColorMode(String id, String displayName) {
        this.id = id;
        this.displayName = displayName;
    }

    public String id() {
        return id;
    }

    public String displayName() {
        return displayName;
    }

    public static BlockSelectorColorMode fromId(String id) {
        if (id == null || id.isBlank()) {
            return STATIC;
        }
        String normalizedId = id.toLowerCase(Locale.ROOT);
        for (BlockSelectorColorMode mode : values()) {
            if (mode.id.equals(normalizedId)) {
                return mode;
            }
        }
        return STATIC;
    }

    @Override
    public String toString() {
        return displayName;
    }

}