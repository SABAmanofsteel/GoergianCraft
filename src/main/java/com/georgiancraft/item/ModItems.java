package com.georgiancraft.item;

import com.georgiancraft.georgiancraft;
import com.georgiancraft.block.ModBlocks;
import com.llamalad7.mixinextras.lib.apache.commons.ObjectUtils;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ConsumableComponent;
import net.minecraft.component.type.ConsumableComponents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.consume.ApplyEffectsConsumeEffect;
import net.minecraft.item.consume.ConsumeEffect;
import net.minecraft.item.equipment.ArmorMaterial;
import net.minecraft.item.equipment.EquipmentAsset;
import net.minecraft.item.equipment.EquipmentAssetKeys;
import net.minecraft.item.equipment.EquipmentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.EquipmentSlot;
import com.github.crimsondawn45.fabricshieldlib.lib.object.FabricShieldItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ToolMaterial;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;



public class ModItems {

    public static final int BASE_DURABILITY = 15;
    // ── Coins ──────────────────────────────────────────────────────────────────
    // ── Coins ──────────────────────────────────────────────────────────────────
    public static final Item STONE_COIN = registerItem(
            "stone_coin",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "stone_coin")))
            )
    );
    public static final Item COPPER_COIN = registerItem(
            "copper_coin",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "copper_coin")))
            )
    );
    public static final Item IRON_COIN = registerItem(
            "iron_coin",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "iron_coin")))
            )
    );
    public static final Item GOLD_COIN = registerItem(
            "gold_coin",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "gold_coin")))
            )
    );
    public static final Item DIAMOND_COIN = registerItem(
            "diamond_coin",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "diamond_coin")))
            )
    );
    public static final Item NETHERITE_COIN = registerItem(
            "netherite_coin",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "netherite_coin")))
            )
    );

    // ── Default (dark) Chokha ──────────────────────────────────────────────────
    public static final RegistryKey<EquipmentAsset> GUIDITE_ARMOR_MATERIAL_KEY = RegistryKey.of(
            EquipmentAssetKeys.REGISTRY_KEY, Identifier.of(georgiancraft.MOD_ID, "guidite"));

    public static final TagKey<Item> REPAIRS_GUIDITE_ARMOR = TagKey.of(
            Registries.ITEM.getKey(), Identifier.of(georgiancraft.MOD_ID, "repairs_guidite_armor"));

    public static final ArmorMaterial INSTANCE = new ArmorMaterial(
            BASE_DURABILITY,
            Map.of(
                    EquipmentType.HELMET, 2,
                    EquipmentType.CHESTPLATE, 8,
                    EquipmentType.LEGGINGS, 6,
                    EquipmentType.BOOTS, 3
            ),
            30,
            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            0.0F, 0.0F,
            REPAIRS_GUIDITE_ARMOR,
            GUIDITE_ARMOR_MATERIAL_KEY
    );

    // ── Red Chokha ─────────────────────────────────────────────────────────────
    public static final RegistryKey<EquipmentAsset> CHOKHA_RED_KEY = RegistryKey.of(
            EquipmentAssetKeys.REGISTRY_KEY, Identifier.of(georgiancraft.MOD_ID, "chokha_red"));

    public static final ArmorMaterial INSTANCE_RED = new ArmorMaterial(
            BASE_DURABILITY,
            Map.of(
                    EquipmentType.HELMET, 2,
                    EquipmentType.CHESTPLATE, 8,
                    EquipmentType.LEGGINGS, 6,
                    EquipmentType.BOOTS, 3
            ),
            30,
            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            0.0F, 0.0F,
            REPAIRS_GUIDITE_ARMOR,
            CHOKHA_RED_KEY
    );

    // ── White Chokha ───────────────────────────────────────────────────────────
    public static final RegistryKey<EquipmentAsset> CHOKHA_WHITE_KEY = RegistryKey.of(
            EquipmentAssetKeys.REGISTRY_KEY, Identifier.of(georgiancraft.MOD_ID, "chokha_white"));

    public static final ArmorMaterial INSTANCE_WHITE = new ArmorMaterial(
            BASE_DURABILITY,
            Map.of(
                    EquipmentType.HELMET, 2,
                    EquipmentType.CHESTPLATE, 8,
                    EquipmentType.LEGGINGS, 6,
                    EquipmentType.BOOTS, 3
            ),
            30,
            SoundEvents.ITEM_ARMOR_EQUIP_IRON,
            0.0F, 0.0F,
            REPAIRS_GUIDITE_ARMOR,
            CHOKHA_WHITE_KEY
    );

    // ══════════════════════════════════════════════════════════════════════════
    // Items
    // ══════════════════════════════════════════════════════════════════════════

    // ── Default Chokha ─────────────────────────────────────────────────────────
    public static final Item SVANURI_QUDI = registerItem(
            "svanuri_qudi",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "svanuri_qudi")))
                    .armor(INSTANCE, EquipmentType.HELMET)
                    .maxDamage(EquipmentType.HELMET.getMaxDamage(BASE_DURABILITY))
            )
    );
    public static final Item SVANURI_CHESTPLATE = registerItem(
            "svanuri_chestplate",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "svanuri_chestplate")))
                    .armor(INSTANCE, EquipmentType.CHESTPLATE)
                    .maxDamage(EquipmentType.CHESTPLATE.getMaxDamage(BASE_DURABILITY))
            )
    );
    public static final Item SVANURI_LEGGINGS = registerItem(
            "svanuri_leggings",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "svanuri_leggings")))
                    .armor(INSTANCE, EquipmentType.LEGGINGS)
                    .maxDamage(EquipmentType.LEGGINGS.getMaxDamage(BASE_DURABILITY))
            )
    );
    public static final Item SVANURI_BOOTS = registerItem(
            "svanuri_boots",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "svanuri_boots")))
                    .armor(INSTANCE, EquipmentType.BOOTS)
                    .maxDamage(EquipmentType.BOOTS.getMaxDamage(BASE_DURABILITY))
            )
    );

    // ── Red Chokha (witeli choxa) ──────────────────────────────────────────────
    public static final Item SVANURI_CHESTPLATE_RED = registerItem(
            "svanuri_chestplate_red",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "svanuri_chestplate_red")))
                    .armor(INSTANCE_RED, EquipmentType.CHESTPLATE)
                    .maxDamage(EquipmentType.CHESTPLATE.getMaxDamage(BASE_DURABILITY))
            )
    );
    public static final Item SVANURI_LEGGINGS_RED = registerItem(
            "svanuri_leggings_red",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "svanuri_leggings_red")))
                    .armor(INSTANCE_RED, EquipmentType.LEGGINGS)
                    .maxDamage(EquipmentType.LEGGINGS.getMaxDamage(BASE_DURABILITY))
            )
    );
    public static final Item SVANURI_BOOTS_RED = registerItem(
            "svanuri_boots_red",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "svanuri_boots_red")))
                    .armor(INSTANCE_RED, EquipmentType.BOOTS)
                    .maxDamage(EquipmentType.BOOTS.getMaxDamage(BASE_DURABILITY))
            )
    );

    // ── White Chokha (tetri choxa) ─────────────────────────────────────────────
    public static final Item SVANURI_CHESTPLATE_WHITE = registerItem(
            "svanuri_chestplate_white",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "svanuri_chestplate_white")))
                    .armor(INSTANCE_WHITE, EquipmentType.CHESTPLATE)
                    .maxDamage(EquipmentType.CHESTPLATE.getMaxDamage(BASE_DURABILITY))
            )
    );
    public static final Item SVANURI_LEGGINGS_WHITE = registerItem(
            "svanuri_leggings_white",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "svanuri_leggings_white")))
                    .armor(INSTANCE_WHITE, EquipmentType.LEGGINGS)
                    .maxDamage(EquipmentType.LEGGINGS.getMaxDamage(BASE_DURABILITY))
            )
    );
    public static final Item SVANURI_BOOTS_WHITE = registerItem(
            "svanuri_boots_white",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "svanuri_boots_white")))
                    .armor(INSTANCE_WHITE, EquipmentType.BOOTS)
                    .maxDamage(EquipmentType.BOOTS.getMaxDamage(BASE_DURABILITY))
            )
    );

    // ── Other items ────────────────────────────────────────────────────────────
    public static final Item SVANURI_PHARI = registerItem(
            "svanuri_phari",
            new FabricShieldItem(
                    new Item.Settings()
                            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "svanuri_phari")))
                            .maxDamage(336)
                            .maxCount(1),
                    20,
                    9,
                    Items.GOLD_INGOT
            )
    );
    public static final Item CLUB = registerItem(
            "club",
            new ClubItem(
                    ToolMaterial.WOOD,
                    7.0f,
                    -3.2f,
                    new Item.Settings()
                            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "club")))
                            .maxDamage(ToolMaterial.WOOD.durability())
            )
    );
    public static final Item GORGASALI_SWORD = registerItem(
            "gorgasali_sword",
            new GorgasaliSwordItem(
                    ToolMaterial.NETHERITE,
                    5.0f,
                    -2.4f,
                    new Item.Settings()
                            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "gorgasali_sword")))
                            .maxDamage(ToolMaterial.NETHERITE.durability())
                            .enchantable(20)
            )
    );
    public static final Item IMERULI_SWORD = registerItem(
            "imeruli_sword",
            new ImeruliSwordItem(
                    ToolMaterial.IRON,
                    new Item.Settings()
                            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "imeruli_sword")))
                            .maxDamage(ToolMaterial.IRON.durability())
            )
    );
    public static final Item GORDA_SWORD = registerItem(
            "gorda_sword",
            new GordaSwordItem(
                    ToolMaterial.NETHERITE,
                    6.0f,
                    -2.4f,
                    new Item.Settings()
                            .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "gorda_sword")))
                            .maxDamage(ToolMaterial.NETHERITE.durability())
                            .enchantable(20)
            )
    );

    public static final Item YANWI = registerItem(
            "yanwi",
            new Item(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "yanwi")))
                    .maxCount(1)
            )
    );

    public static final Item YANWISAVSE = createFoodItem(
            "yanwi_savse", 2, 0.3f, true,
            List.of(
                    new StatusEffectInstance(StatusEffects.STRENGTH, 1200, 0),
                    new StatusEffectInstance(StatusEffects.REGENERATION, 600, 0),
                    new StatusEffectInstance(StatusEffects.SLOWNESS, 1200, 1),
                    new StatusEffectInstance(StatusEffects.NAUSEA, 1200, 1)
            ),
            true, YANWI, 1
    );

    public static final Item GRAPE = createFoodItem(
            "grape", 2, 0.3f, false,
            List.of(new StatusEffectInstance(StatusEffects.SPEED, 200, 0)),
            false, null, 64
    );

    public static final Item SULGUNI = createFoodItem(
            "sulguni", 3, 1.3f, false,
            List.of(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 0)),
            false, null, 64
    );

    public static final Item MCHADI = createFoodItem(
            "mchadi", 3, 1.8f, false,
            List.of(new StatusEffectInstance(StatusEffects.RESISTANCE, 200, 0)),
            false, null, 64
    );
    public static final Item LORI = createFoodItem(
            "lori", 6, 2.8f, false,
            List.of(new StatusEffectInstance(StatusEffects.RESISTANCE, 300, 0)),
            false, null, 64
    );

    public static final Item PKHALI = createFoodItem(
            "pkhali", 3, 1.4f, false,
            List.of(new StatusEffectInstance(StatusEffects.HASTE, 300, 0)),
            false, null, 64
    );

    public static final Item GEBJALIA = createFoodItem(
            "gebjalia", 6, 4.3f, false,
            List.of(new StatusEffectInstance(StatusEffects.REGENERATION, 120, 0)),
            false, null, 64
    );

    public static final Item CHURCHKHELA = createFoodItem(
            "churchkhela", 4, 2.4f, false,
            List.of(new StatusEffectInstance(StatusEffects.HASTE, 300, 0)),
            false, Items.STRING, 64
    );

    public static final Item KHINKALI = createFoodItem(
            "khinkali", 3, 10f, false,
            List.of(new StatusEffectInstance(StatusEffects.ABSORPTION, 200, 0)),
            false, null, 64
    );

    public static final Item GHOMI = createFoodItem(
            "ghomi", 2, 2f, false,
            List.of(new StatusEffectInstance(StatusEffects.FIRE_RESISTANCE, 200, 0)),
            false, null, 64
    );

    public static final Item ADJRAULI_KHACHAPURI = createFoodItem(
            "adjaruli_khachapuri", 2, 3f, false,
            List.of(new StatusEffectInstance(StatusEffects.SATURATION, 1, 0)),
            false, null, 64
    );

    public static final Item MWVADI = createFoodItem(
            "mwvadi", 2, 2.5f, false,
            List.of(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 0)),
            false, Items.STICK, 64
    );

    public static final Item SHKMERULI = createFoodItem(
            "shkmeruli", 4, 2.5f, false,
            List.of(new StatusEffectInstance(StatusEffects.STRENGTH, 200, 1)),
            false, null, 64
    );

    public static final Item AMIRAN_CHAIN = registerItem(
            "amiran_chain",
            new AmiranChainItem(new Item.Settings()
                    .maxCount(1)
                    .maxDamage(64)
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "amiran_chain")))
            )
    );

    public static final Item SALAMURI = registerItem(
            "salamuri",
            new SalamuriItem(new Item.Settings()
                    .registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, "salamuri")))
                    .maxCount(1)
            )
    );

    // ══════════════════════════════════════════════════════════════════════════
    // Helpers
    // ══════════════════════════════════════════════════════════════════════════

    private static Item createFoodItem(
            String name,
            int nutrition,
            float saturation,
            boolean alwaysEdible,
            List<StatusEffectInstance> effects,
            boolean useDrinkAnimation,
            Item returnItem,
            int maxstackcount
    ) {
        FoodComponent.Builder foodBuilder = new FoodComponent.Builder()
                .nutrition(nutrition)
                .saturationModifier(saturation);

        if (alwaysEdible) foodBuilder.alwaysEdible();

        FoodComponent food = foodBuilder.build();

        Item.Settings settings = new Item.Settings().maxCount(maxstackcount);

        ConsumableComponent.Builder consumableBuilder = useDrinkAnimation
                ? ConsumableComponents.drink()
                : ConsumableComponents.food();

        if (effects != null && !effects.isEmpty()) {
            for (StatusEffectInstance effect : effects) {
                consumableBuilder.consumeEffect(new ApplyEffectsConsumeEffect(effect, 1.0f));
            }
        }

        settings.food(food, consumableBuilder.build());
        settings.registryKey(RegistryKey.of(RegistryKeys.ITEM, Identifier.of(georgiancraft.MOD_ID, name)));

        return registerItem(name, new ReturnableItem(settings, returnItem));
    }

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, Identifier.of(georgiancraft.MOD_ID, name), item);
    }

    public static void registerModItems() {
        georgiancraft.LOGGER.info("Registering Mod Items for " + georgiancraft.MOD_ID);

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS).register(entries -> {
            entries.add(KHINKALI);
            entries.add(GRAPE);
            entries.add(CHURCHKHELA);
            entries.add(YANWI);
            entries.add(YANWISAVSE);
            // Default Chokha
            entries.add(SVANURI_QUDI);
            entries.add(SVANURI_PHARI);
            entries.add(SVANURI_CHESTPLATE);
            entries.add(SVANURI_LEGGINGS);
            entries.add(SVANURI_BOOTS);
            // Red Chokha
            entries.add(SVANURI_CHESTPLATE_RED);
            entries.add(SVANURI_LEGGINGS_RED);
            entries.add(SVANURI_BOOTS_RED);
            // White Chokha
            entries.add(SVANURI_CHESTPLATE_WHITE);
            entries.add(SVANURI_LEGGINGS_WHITE);
            entries.add(SVANURI_BOOTS_WHITE);
            // Food
            entries.add(SULGUNI);
            entries.add(GEBJALIA);
            entries.add(GHOMI);
            entries.add(SALAMURI);
            entries.add(MWVADI);
            entries.add(ADJRAULI_KHACHAPURI);
            entries.add(MCHADI);
            entries.add(PKHALI);
            entries.add(LORI);
            entries.add(SHKMERULI);
            //coins
            entries.add(STONE_COIN);
            entries.add(COPPER_COIN);
            entries.add(IRON_COIN);
            entries.add(GOLD_COIN);
            entries.add(DIAMOND_COIN);
            entries.add(NETHERITE_COIN);
        });

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(entries -> {
            entries.add(CLUB);
            entries.add(GORGASALI_SWORD);
            entries.add(AMIRAN_CHAIN);
            entries.add(IMERULI_SWORD);
            entries.add(GORDA_SWORD);
        });
    }

    // ══════════════════════════════════════════════════════════════════════════
    // Inner classes
    // ══════════════════════════════════════════════════════════════════════════

    private static class ReturnableItem extends Item {
        private final Item returnItem;

        public ReturnableItem(Settings settings, Item returnItem) {
            super(settings);
            this.returnItem = returnItem;
        }

        @Override
        public ItemStack finishUsing(ItemStack stack, World world, LivingEntity user) {
            ItemStack result = super.finishUsing(stack, world, user);
            if (!world.isClient && user instanceof PlayerEntity player && returnItem != null) {
                player.getInventory().offerOrDrop(new ItemStack(returnItem));
            }
            return result;
        }
    }
}