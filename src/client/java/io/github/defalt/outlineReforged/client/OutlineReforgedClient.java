package io.github.defalt.outlineReforged.client;

import io.github.defalt.outlineReforged.client.config.OutlineConfigManager;
import net.fabricmc.api.ClientModInitializer;

public class OutlineReforgedClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        OutlineConfigManager.load();
    }

}