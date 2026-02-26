package io.github.defalt.outlineReforged.client.integration;

import io.github.defalt.outlineReforged.client.config.OutlineConfig;
import io.github.defalt.outlineReforged.client.config.OutlineConfigManager;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class OutlineReforgedConfigScreen {

    private OutlineReforgedConfigScreen() {
    }

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Defalt's Outline Reforged"));
        ConfigCategory category = builder.getOrCreateCategory(Text.literal("Outline"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        OutlineConfig config = OutlineConfigManager.getConfig();
        category.addEntry(entryBuilder
                .startAlphaColorField(Text.literal("Outline Color"), config.getOutlineArgb())
                .setDefaultValue(0xFF00FFFF)
                .setSaveConsumer(config::setOutlineArgb)
                .build());
        category.addEntry(entryBuilder
                .startFloatField(Text.literal("Outline Thickness"), config.outlineThickness)
                .setMin(0.5F)
                .setMax(16.0F)
                .setDefaultValue(2.0F)
                .setSaveConsumer(value -> config.outlineThickness = value)
                .build());
        category.addEntry(entryBuilder
                .startIntSlider(Text.literal("Outline Glow"), config.outlineGlow, 0, 10)
                .setDefaultValue(0)
                .setSaveConsumer(value -> config.outlineGlow = value)
                .build());
        category.addEntry(entryBuilder
                .startBooleanToggle(Text.literal("Rainbow"), config.rainbowEnabled)
                .setDefaultValue(false)
                .setSaveConsumer(value -> config.rainbowEnabled = value)
                .build());
        category.addEntry(entryBuilder
                .startFloatField(Text.literal("Rainbow Cycles Per Second"), config.rainbowCyclesPerSecond)
                .setMin(0.01F)
                .setMax(10.0F)
                .setDefaultValue(0.15F)
                .setSaveConsumer(value -> config.rainbowCyclesPerSecond = value)
                .build());
        builder.setSavingRunnable(OutlineConfigManager::save);
        return builder.build();
    }

}