package io.github.defalt.outlineReforged.client.integration;

import io.github.defalt.outlineReforged.client.config.OutlineColorMode;
import io.github.defalt.outlineReforged.client.config.OutlineConfig;
import io.github.defalt.outlineReforged.client.config.OutlineConfigManager;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class OutlineReforgedConfigScreen {

    private OutlineReforgedConfigScreen() {
        // TODO: not yet implemented
    }

    public static Screen create(Screen parent) {
        ConfigBuilder configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(Text.literal("Defalt's Outline Reforged"));
        ConfigCategory configCategory = configBuilder.getOrCreateCategory(Text.literal("Outline"));
        ConfigEntryBuilder configEntryBuilder = configBuilder.entryBuilder();
        OutlineConfig outlineConfig = OutlineConfigManager.getOutlineConfig();
        configCategory.addEntry(configEntryBuilder
                .startEnumSelector(Text.literal("Color Mode"), OutlineColorMode.class, outlineConfig.getColorMode())
                .setDefaultValue(OutlineColorMode.STATIC)
                .setSaveConsumer(outlineConfig::setColorMode)
                .build());
        configCategory.addEntry(configEntryBuilder
                .startAlphaColorField(Text.literal("Primary Color"), outlineConfig.getOutlineArgb())
                .setDefaultValue(0xFFFFFFFF)
                .setSaveConsumer(outlineConfig::setOutlineArgb)
                .build());
        configCategory.addEntry(configEntryBuilder
                .startAlphaColorField(Text.literal("Secondary Color (Gradient/Pulse)"), outlineConfig.getSecondaryArgb())
                .setDefaultValue(0xFFFFFFFF)
                .setSaveConsumer(outlineConfig::setSecondaryArgb)
                .build());
        configCategory.addEntry(configEntryBuilder
                .startFloatField(Text.literal("Animation Cycles Per Second"), outlineConfig.animationCyclesPerSecond)
                .setMin(0.01F)
                .setMax(10.0F)
                .setDefaultValue(0.10F)
                .setSaveConsumer(value -> outlineConfig.animationCyclesPerSecond = value)
                .build());
        configCategory.addEntry(configEntryBuilder
                .startFloatField(Text.literal("Outline Thickness"), outlineConfig.outlineThickness)
                .setMin(0.5F)
                .setMax(16.0F)
                .setDefaultValue(2.0F)
                .setSaveConsumer(value -> outlineConfig.outlineThickness = value)
                .build());
        configCategory.addEntry(configEntryBuilder
                .startIntSlider(Text.literal("Outline Glow"), outlineConfig.outlineGlow, 0, 10)
                .setDefaultValue(0)
                .setSaveConsumer(value -> outlineConfig.outlineGlow = value)
                .build());
        configBuilder.setSavingRunnable(OutlineConfigManager::save);
        return configBuilder.build();
    }

}