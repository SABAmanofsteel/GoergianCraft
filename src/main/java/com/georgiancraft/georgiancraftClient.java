package com.georgiancraft;

import com.georgiancraft.block.ModBlocks;
import com.georgiancraft.entity.ModEntities;
import com.georgiancraft.item.ModItems;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.client.render.BlockRenderLayer;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.text.Text;

public class georgiancraftClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        EntityRendererRegistry.register(ModEntities.AMIRAN_CHAIN, FlyingItemEntityRenderer::new);
        BlockRenderLayerMap.putBlock(ModBlocks.VAZI, BlockRenderLayer.CUTOUT);
        ItemTooltipCallback.EVENT.register((stack, tooltipContext, tooltipType, list) -> {
            long valueInStone = 0;

            if (stack.isOf(ModItems.STONE_COIN))    valueInStone = 1L;
            else if (stack.isOf(ModItems.COPPER_COIN))  valueInStone = 8L;
            else if (stack.isOf(ModItems.IRON_COIN))    valueInStone = 64L;
            else if (stack.isOf(ModItems.GOLD_COIN))    valueInStone = 512L;
            else if (stack.isOf(ModItems.DIAMOND_COIN)) valueInStone = 4096L;
            else if (stack.isOf(ModItems.NETHERITE_COIN)) valueInStone = 32768L;
            else return;

            long total = valueInStone * stack.getCount();
            list.add(Text.literal("§7Worth: §f" + total + " stone coins"));
        });
    }

}