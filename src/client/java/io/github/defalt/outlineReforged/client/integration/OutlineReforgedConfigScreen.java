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
        // TODO: not yet implemented
    }

    public static Screen create(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("Defalt's Outline Reforged"));
        ConfigCategory category = builder.getOrCreateCategory(Text.literal("Outline"));
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();
        OutlineConfig config = OutlineConfigManager.getConfig();
        category.addEntry(entryBuilder
                .startIntSlider(Text.literal("Alpha"), config.outlineAlpha, 0, 255)
                .setDefaultValue(255)
                .setSaveConsumer(value -> config.outlineAlpha = value)
                .build());
        category.addEntry(entryBuilder
                .startIntSlider(Text.literal("Red"), config.outlineRed, 0, 255)
                .setDefaultValue(0)
                .setSaveConsumer(value -> config.outlineRed = value)
                .build());
        category.addEntry(entryBuilder
                .startIntSlider(Text.literal("Green"), config.outlineGreen, 0, 255)
                .setDefaultValue(0)
                .setSaveConsumer(value -> config.outlineGreen = value)
                .build());
        category.addEntry(entryBuilder
                .startIntSlider(Text.literal("Blue"), config.outlineBlue, 0, 255)
                .setDefaultValue(0)
                .setSaveConsumer(value -> config.outlineBlue = value)
                .build());
        category.addEntry(entryBuilder
                .startIntSlider(Text.literal("Fill Alpha"), config.fillAlpha, 0, 255)
                .setDefaultValue(40)
                .setSaveConsumer(value -> config.fillAlpha = value)
                .build());
        category.addEntry(entryBuilder
                .startIntSlider(Text.literal("Fill Red"), config.fillRed, 0, 255)
                .setDefaultValue(0)
                .setSaveConsumer(value -> config.fillRed = value)
                .build());
        category.addEntry(entryBuilder
                .startIntSlider(Text.literal("Fill Green"), config.fillGreen, 0, 255)
                .setDefaultValue(255)
                .setSaveConsumer(value -> config.fillGreen = value)
                .build());
        category.addEntry(entryBuilder
                .startIntSlider(Text.literal("Fill Blue"), config.fillBlue, 0, 255)
                .setDefaultValue(255)
                .setSaveConsumer(value -> config.fillBlue = value)
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