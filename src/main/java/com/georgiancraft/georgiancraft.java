package com.georgiancraft;

import com.georgiancraft.block.ModBlocks;
import com.georgiancraft.entity.ModEntities;
import com.georgiancraft.item.ModItems;
import com.georgiancraft.sound.ModSounds;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents;

public class georgiancraft implements ModInitializer {
	public static final String MOD_ID = "georgiancraft";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		ModSounds.registerModSounds();
		ModEntities.registerModEntities();
		ModBlocks.registerModBlocks();
		ModItems.registerModItems();
		CauldronBrewing.register();
		georgiancraftProtection.register();

	}
}