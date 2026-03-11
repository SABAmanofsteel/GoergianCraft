package com.georgiancraft.entity;

import com.georgiancraft.georgiancraft;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class ModEntities {

    public static final EntityType<AmiranChainEntity> AMIRAN_CHAIN =
            Registry.register(
                    Registries.ENTITY_TYPE,
                    RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(georgiancraft.MOD_ID, "amiran_chain")),
                    EntityType.Builder.<AmiranChainEntity>create(AmiranChainEntity::new, SpawnGroup.MISC)
                            .dimensions(0.25f, 0.25f)
                            .build(RegistryKey.of(RegistryKeys.ENTITY_TYPE, Identifier.of(georgiancraft.MOD_ID, "amiran_chain")))
            );

    public static void registerModEntities() {
        georgiancraft.LOGGER.info("Registering Mod Entities for " + georgiancraft.MOD_ID);
    }
}