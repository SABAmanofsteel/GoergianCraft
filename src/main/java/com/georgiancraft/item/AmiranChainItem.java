package com.georgiancraft.item;

import com.georgiancraft.entity.AmiranChainEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class AmiranChainItem extends Item {

    private static final int COOLDOWN = 25;
    private static final float BASE_SPEED = 10.0f;

    public AmiranChainItem(Settings settings) {
        super(settings);
    }

    @Override
    public ActionResult use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);

        if (!world.isClient) {
            AmiranChainEntity chain = new AmiranChainEntity(world, user);

            float pitch = user.getPitch();
            float speedMultiplier = (pitch < 0) ? 1.0f + (-pitch / 90f) * 0.8f : 1.0f;
            float speed = BASE_SPEED * speedMultiplier;

            chain.setVelocity(user, pitch, user.getYaw(), 0f, speed, 0.5f);
            world.spawnEntity(chain);

            world.playSound(null, user.getBlockPos(),
                    SoundEvents.BLOCK_CHAIN_BREAK,
                    SoundCategory.NEUTRAL, 0.7f, 0.8f);
        }

        return ActionResult.CONSUME;
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (!world.isClient && user instanceof PlayerEntity player) {
            player.getItemCooldownManager().set(stack, COOLDOWN);
        }
        return super.onStoppedUsing(stack, world, user, remainingUseTicks);
    }
}