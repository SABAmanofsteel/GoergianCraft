package com.georgiancraft.item;

import com.georgiancraft.sound.ModSounds;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class SalamuriItem extends Item {

    public SalamuriItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        return ActionResult.CONSUME;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.TOOT_HORN;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        int maxUse = getMaxUseTime(stack, user);

        // On first tick: play sound and set cooldown
        if (remainingUseTicks == maxUse - 1) {
            if (!world.isClient) {
                world.playSound(null, user.getBlockPos(),
                        ModSounds.SALAMURI_PLAY,
                        SoundCategory.PLAYERS, 1.0f, 1.0f);
            }
            if (user instanceof PlayerEntity player) {
                player.getItemCooldownManager().set(stack, 100);
            }
        }

        if (!world.isClient && remainingUseTicks % 4 == 0) {
            if (world instanceof ServerWorld serverWorld) {
                Random random = world.getRandom();
                double x = user.getX() + (random.nextDouble() - 0.5) * 1.5;
                double y = user.getEyeY() + 0.5 + random.nextDouble() * 0.5;
                double z = user.getZ() + (random.nextDouble() - 0.5) * 1.5;
                serverWorld.spawnParticles(ParticleTypes.NOTE, x, y, z, 1, 0, 0, 0, random.nextDouble());
            }
        }
    }
}