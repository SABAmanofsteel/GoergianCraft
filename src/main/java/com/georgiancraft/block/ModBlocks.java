package com.georgiancraft.block;

import com.georgiancraft.georgiancraft;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.cauldron.CauldronBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;


public class ModBlocks {


    public static final Block WINE_CAULDRON = registerBlock(
            "wine_cauldron",
            new WineCauldronBlock(
                    AbstractBlock.Settings.copy(Blocks.WATER_CAULDRON)
                            .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(georgiancraft.MOD_ID, "wine_cauldron"))),
                    CauldronBehavior.createMap("wine")
            )
    );
    public static final Block KHACHAPURI = registerBlock(
            "khachapuri",
            new KhachapuriBlock(
                    AbstractBlock.Settings.create()
                            .strength(0.5f)
                            .sounds(net.minecraft.sound.BlockSoundGroup.WOOL)
                            .nonOpaque()
                            .pistonBehavior(net.minecraft.block.piston.PistonBehavior.DESTROY)
                            .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(georgiancraft.MOD_ID, "khachapuri")))
            )
    );
    public static final Block GEORGIAN_FLAG = registerBlock(
            "georgian_flag",
            new GeorgianFlagBlock(
                    AbstractBlock.Settings.create()
                            .strength(0.5f)
                            .nonOpaque()
                            .noCollision()
                            .sounds(net.minecraft.sound.BlockSoundGroup.WOOL)
                            .pistonBehavior(net.minecraft.block.piston.PistonBehavior.DESTROY)
                            .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(georgiancraft.MOD_ID, "georgian_flag")))
            )
    );
    public static final Block MEGRULI_FLAG = registerBlock(
            "megruli_flag",
            new GeorgianFlagBlock(
                    AbstractBlock.Settings.create()
                            .strength(0.5f)
                            .nonOpaque()
                            .noCollision()
                            .sounds(net.minecraft.sound.BlockSoundGroup.WOOL)
                            .pistonBehavior(net.minecraft.block.piston.PistonBehavior.DESTROY)
                            .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(georgiancraft.MOD_ID, "megruli_flag")))
            )
    );
    public static final Block VAZI = registerBlock(
            "vazi",
            new VaziBlock(
                    AbstractBlock.Settings.create()
                            .strength(0.2f)
                            .noCollision()
                            .nonOpaque()
                            .ticksRandomly()
                            .sounds(net.minecraft.sound.BlockSoundGroup.GRASS)
                            .pistonBehavior(net.minecraft.block.piston.PistonBehavior.DESTROY)
                            .registryKey(RegistryKey.of(RegistryKeys.BLOCK, Identifier.of(georgiancraft.MOD_ID, "vazi")))
            )
    );




    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(
                Registries.BLOCK,
                Identifier.of(georgiancraft.MOD_ID, name),
                block
        );
    }

    private static void registerBlockItem(String name, Block block) {
        Registry.register(
                Registries.ITEM,
                Identifier.of(georgiancraft.MOD_ID, name),
                new BlockItem(block, new Item.Settings()
                        .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, name))))
        );
    }


    public static void registerModBlocks() {
        georgiancraft.LOGGER.info("Registering Mod Blocks for " + georgiancraft.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register(entries -> {
            entries.add(WINE_CAULDRON);
            entries.add(GEORGIAN_FLAG);
            entries.add(MEGRULI_FLAG);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FOOD_AND_DRINK).register(entries -> {
            entries.add(KHACHAPURI);
        });
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register(entries -> {
            entries.add(VAZI);
        });
    }
}