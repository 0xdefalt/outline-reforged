package io.github.defalt.blockselector.client.integration;

import io.github.defalt.blockselector.client.config.BlockSelectorColorMode;
import io.github.defalt.blockselector.client.config.BlockSelectorConfig;
import io.github.defalt.blockselector.client.config.BlockSelectorConfigManager;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class BlockSelectorConfigScreen {

    private BlockSelectorConfigScreen() {
        // TODO: not yet implemented
    }

    public static Screen create(Screen parent) {
        ConfigBuilder configBuilder = ConfigBuilder.create().setParentScreen(parent).setTitle(Component.literal("Defalt's Block Selector"));
        ConfigCategory configCategory = configBuilder.getOrCreateCategory(Component.literal("Outline"));
        ConfigEntryBuilder configEntryBuilder = configBuilder.entryBuilder();
        BlockSelectorConfig blockSelectorConfig = BlockSelectorConfigManager.getBlockSelectorConfig();
        configCategory.addEntry(configEntryBuilder
                .startEnumSelector(Component.literal("Color Mode"), BlockSelectorColorMode.class, blockSelectorConfig.getColorMode())
                .setDefaultValue(BlockSelectorColorMode.STATIC)
                .setSaveConsumer(blockSelectorConfig::setColorMode)
                .build());
        configCategory.addEntry(configEntryBuilder
                .startAlphaColorField(Component.literal("Primary Color"), blockSelectorConfig.getOutlineArgb())
                .setDefaultValue(0xFFFFFFFF)
                .setSaveConsumer(blockSelectorConfig::setOutlineArgb)
                .build());
        configCategory.addEntry(configEntryBuilder
                .startAlphaColorField(Component.literal("Secondary Color (Gradient/Pulse)"), blockSelectorConfig.getSecondaryArgb())
                .setDefaultValue(0xFFFFFFFF)
                .setSaveConsumer(blockSelectorConfig::setSecondaryArgb)
                .build());
        configCategory.addEntry(configEntryBuilder
                .startFloatField(Component.literal("Animation Cycles Per Second"), blockSelectorConfig.animationCyclesPerSecond)
                .setMin(0.01F)
                .setMax(10.0F)
                .setDefaultValue(0.10F)
                .setSaveConsumer(value -> blockSelectorConfig.animationCyclesPerSecond = value)
                .build());
        configCategory.addEntry(configEntryBuilder
                .startFloatField(Component.literal("Outline Thickness"), blockSelectorConfig.outlineThickness)
                .setMin(0.5F)
                .setMax(16.0F)
                .setDefaultValue(2.0F)
                .setSaveConsumer(value -> blockSelectorConfig.outlineThickness = value)
                .build());
        configCategory.addEntry(configEntryBuilder
                .startIntSlider(Component.literal("Outline Glow"), blockSelectorConfig.outlineGlow, 0, 10)
                .setDefaultValue(0)
                .setSaveConsumer(value -> blockSelectorConfig.outlineGlow = value)
                .build());
        configBuilder.setSavingRunnable(BlockSelectorConfigManager::save);
        return configBuilder.build();
    }

}