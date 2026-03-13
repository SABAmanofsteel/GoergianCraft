package com.georgiancraft;

import com.georgiancraft.block.ModBlocks;
import com.georgiancraft.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;

public class georgiancraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.AMIRAN_CHAIN, FlyingItemEntityRenderer::new);
        BlockRenderLayerMap.putBlock(ModBlocks.VAZI, BlockRenderLayer.CUTOUT);
    }

}