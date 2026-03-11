package com.georgiancraft.item;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class GordaSwordItem extends Item {

    private static final float DAMAGE = 8.0f;
    private static final float ATTACK_SPEED = -2.4f;
    private static final int COOLDOWN_TICKS = 200;
    private static final int WOLF_LIFETIME_TICKS = 400; // 20 seconds
    public static final java.util.HashMap<java.util.UUID, Long> GORDA_WOLVES = new java.util.HashMap<>();

    public GordaSwordItem(ToolMaterial material, float attackDamage, float attackSpeed, Item.Settings settings) {
        super(settings.attributeModifiers(createAttributeModifiers(material, attackDamage, attackSpeed)));
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (!world.isClient && world instanceof ServerWorld serverWorld) {

            // Spawn a wolf at the player's location
            WolfEntity wolf = new WolfEntity(net.minecraft.entity.EntityType.WOLF, world);
            wolf.refreshPositionAndAngles(user.getX(), user.getY(), user.getZ(), user.getYaw(), 0);

            // Tame it to the player so it obeys
            wolf.setOwner(user);
            wolf.setTamed(true, false);
            wolf.getAttributeInstance(net.minecraft.entity.attribute.EntityAttributes.MAX_HEALTH).setBaseValue(40.0);
            wolf.setHealth(80.0f);
            wolf.getAttributeInstance(net.minecraft.entity.attribute.EntityAttributes.ATTACK_DAMAGE).setBaseValue(10.0);
            wolf.getAttributeInstance(net.minecraft.entity.attribute.EntityAttributes.MOVEMENT_SPEED).setBaseValue(0.5);

            // Make it invisible so it doesn't look like a normal wolf
            wolf.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.INVISIBILITY,
                    WOLF_LIFETIME_TICKS + 20, // slightly longer than lifetime
                    0,
                    false,
                    false // hide particles so it stays stealthy
            ));

            // Give it glowing so the player can still see it
            wolf.addStatusEffect(new StatusEffectInstance(
                    StatusEffects.GLOWING,
                    WOLF_LIFETIME_TICKS + 20,
                    0,
                    false,
                    false
            ));

            // Name it (shows up on death / tab)
            wolf.setCustomName(net.minecraft.text.Text.literal("Shadow"));
            wolf.setCustomNameVisible(false);

            // Make the wolf aggressive toward whatever the player last hit
            // We'll sync it via the player's attack target tracking
            wolf.setAttacking(true);
            GORDA_WOLVES.put(wolf.getUuid(), serverWorld.getTime() + WOLF_LIFETIME_TICKS);
            serverWorld.spawnEntity(wolf);


            // Sound effect
            world.playSound(null, user.getBlockPos(),
                    SoundEvents.ENTITY_ENDER_DRAGON_AMBIENT,
                    SoundCategory.PLAYERS, 1.0f, 0.7f);

            user.getItemCooldownManager().set(user.getStackInHand(hand), COOLDOWN_TICKS);
        }

        return ActionResult.SUCCESS;
    }

    private static AttributeModifiersComponent createAttributeModifiers(
            ToolMaterial material, float attackDamage, float attackSpeed) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(
                                Identifier.ofVanilla("base_attack_damage"),
                                attackDamage + material.attackDamageBonus(),
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(
                                Identifier.ofVanilla("base_attack_speed"),
                                attackSpeed,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .build();
    }
}