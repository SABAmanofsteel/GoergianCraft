package com.georgiancraft.item;

import net.minecraft.component.type.AttributeModifierSlot;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolMaterial;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ImeruliSwordItem extends Item {

    private static final float ATTACK_DAMAGE = 7.0f;
    private static final float ATTACK_SPEED = -2.4f;
    private static final int TAG_DURATION_TICKS = 100; // 5 seconds
    private static final int COOLDOWN_TICKS = 60;      // 3 second cooldown after teleport

    // Maps player UUID -> [target UUID, expiry time]
    private static final Map<UUID, UUID> taggedTarget = new HashMap<>();
    private static final Map<UUID, Long> tagExpiry = new HashMap<>();

    public ImeruliSwordItem(ToolMaterial material, Item.Settings settings) {
        super(settings.attributeModifiers(createAttributeModifiers(material)));
    }

    @Override
    public void postHit(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        stack.damage(1, attacker, EquipmentSlot.MAINHAND);

        if (attacker instanceof PlayerEntity player) {
            taggedTarget.put(player.getUuid(), target.getUuid());
            tagExpiry.put(player.getUuid(), target.getWorld().getTime() + TAG_DURATION_TICKS);

            player.sendMessage(Text.literal("§6[Imeruli Sword] §eTarget tagged for 5 seconds!"), true);

            if (!target.getWorld().isClient && target.getWorld() instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.WITCH,
                        target.getX(), target.getY() + 1, target.getZ(),
                        10, 0.3, 0.5, 0.3, 0.05);
            }
        }
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        if (world.isClient) return ActionResult.SUCCESS;

        UUID playerUUID = user.getUuid();

        if (!taggedTarget.containsKey(playerUUID)) {
            user.sendMessage(Text.literal("§c[Imeruli Sword] §7No target tagged!"), true);
            return ActionResult.FAIL;
        }

        long expiry = tagExpiry.getOrDefault(playerUUID, 0L);
        if (world.getTime() > expiry) {
            taggedTarget.remove(playerUUID);
            tagExpiry.remove(playerUUID);
            user.sendMessage(Text.literal("§c[Imeruli Sword] §7Tag has expired!"), true);
            return ActionResult.FAIL;
        }

        UUID targetUUID = taggedTarget.get(playerUUID);

        // Find the entity in the world
        Entity target = null;
        if (world instanceof ServerWorld serverWorld) {
            for (Entity e : serverWorld.iterateEntities()) {
                if (e.getUuid().equals(targetUUID)) {
                    target = e;
                    break;
                }
            }
        }

        if (target == null) {
            user.sendMessage(Text.literal("§c[Imeruli Sword] §7Target not found!"), true);
            return ActionResult.FAIL;
        }

        // Teleport
        ServerWorld serverWorld = (ServerWorld) world;
        user.requestTeleport(target.getX(), target.getY(), target.getZ());

        // Effects
        serverWorld.spawnParticles(ParticleTypes.PORTAL,
                target.getX(), target.getY() + 1, target.getZ(),
                30, 0.5, 1, 0.5, 0.1);
        world.playSound(null, user.getBlockPos(), SoundEvents.ENTITY_ENDERMAN_TELEPORT,
                SoundCategory.PLAYERS, 1.0f, 1.0f);

        user.sendMessage(Text.literal("§6[Imeruli Sword] §aTeleported to target!"), true);

        // Clear tag and apply cooldown
        taggedTarget.remove(playerUUID);
        tagExpiry.remove(playerUUID);
        user.getItemCooldownManager().set(user.getStackInHand(hand), COOLDOWN_TICKS);

        return ActionResult.SUCCESS;
    }

    private static AttributeModifiersComponent createAttributeModifiers(ToolMaterial material) {
        return AttributeModifiersComponent.builder()
                .add(EntityAttributes.ATTACK_DAMAGE,
                        new EntityAttributeModifier(
                                Identifier.ofVanilla("base_attack_damage"),
                                ATTACK_DAMAGE + material.attackDamageBonus(),
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .add(EntityAttributes.ATTACK_SPEED,
                        new EntityAttributeModifier(
                                Identifier.ofVanilla("base_attack_speed"),
                                ATTACK_SPEED,
                                EntityAttributeModifier.Operation.ADD_VALUE),
                        AttributeModifierSlot.MAINHAND)
                .build();
    }
}