package io.github.defalt.blockselector.client;

import io.github.defalt.blockselector.client.config.BlockSelectorConfigManager;
import net.fabricmc.api.ClientModInitializer;

public class BlockSelectorClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        BlockSelectorConfigManager.load();
    }

}