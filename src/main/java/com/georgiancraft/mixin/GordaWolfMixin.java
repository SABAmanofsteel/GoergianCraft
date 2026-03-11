package com.georgiancraft.mixin;

import com.georgiancraft.item.GordaSwordItem;
import net.minecraft.entity.passive.WolfEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WolfEntity.class)
public class GordaWolfMixin {

    @Inject(method = "tick", at = @At("TAIL"))
    private void onTick(CallbackInfo ci) {
        WolfEntity wolf = (WolfEntity)(Object)this;

        if (wolf.getWorld().isClient) return;
        if (!GordaSwordItem.GORDA_WOLVES.containsKey(wolf.getUuid())) return;

        ServerWorld serverWorld = (ServerWorld) wolf.getWorld();
        long expiry = GordaSwordItem.GORDA_WOLVES.get(wolf.getUuid());

        if (serverWorld.getTime() >= expiry) {
            serverWorld.spawnParticles(ParticleTypes.POOF,
                    wolf.getX(), wolf.getY() + 0.5, wolf.getZ(),
                    10, 0.3, 0.3, 0.3, 0.05);
            GordaSwordItem.GORDA_WOLVES.remove(wolf.getUuid());
            wolf.discard();
        }
    }
}