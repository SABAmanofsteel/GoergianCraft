package com.georgiancraft.entity;

import com.georgiancraft.item.ModItems;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.thrown.ThrownItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class AmiranChainEntity extends ThrownItemEntity {

    private static final double DASH_SPEED = 1.5;
    private static final double STOP_DISTANCE = 1.2;

    private Vec3d dashTarget;
    private LivingEntity dashOwner;
    private boolean dashing;

    public AmiranChainEntity(EntityType<AmiranChainEntity> type, World world) {
        super(type, world);
    }

    public AmiranChainEntity(World world, LivingEntity owner) {
        super(ModEntities.AMIRAN_CHAIN, world);
        setOwner(owner);
        setPosition(owner.getX(), owner.getEyeY() - 0.1, owner.getZ());
    }

    @Override
    protected Item getDefaultItem() {
        return Items.CHAIN;
    }

    private void stopDash() {
        if (dashOwner != null) {
            dashOwner.setVelocity(0, 0, 0);
            dashOwner.velocityModified = true;
        }

        dashTarget = null;
        dashOwner = null;
        dashing = false;
        discard();
    }

    @Override
    public void tick() {
        super.tick();

        if (getWorld().isClient || !dashing || dashTarget == null || dashOwner == null) {
            return;
        }

        if (dashOwner instanceof PlayerEntity player) {
            boolean stillHoldingUse = player.isUsingItem() && player.getActiveItem().isOf(ModItems.AMIRAN_CHAIN);
            if (!stillHoldingUse) {
                stopDash();
                return;
            }
        }

        Vec3d adjustedTarget = dashTarget.add(0, 1.0, 0);
        Vec3d current = dashOwner.getPos();

        if (current.distanceTo(adjustedTarget) <= STOP_DISTANCE) {
            stopDash();
            return;
        }

        Vec3d direction = adjustedTarget.subtract(current).normalize();
        dashOwner.setVelocity(direction.x * DASH_SPEED, direction.y * DASH_SPEED, direction.z * DASH_SPEED);
        dashOwner.velocityModified = true;
    }

    @Override
    protected void onCollision(HitResult hitResult) {
        super.onCollision(hitResult);

        if (getWorld().isClient || dashing) {
            return;
        }

        if (!(getOwner() instanceof LivingEntity owner)) {
            discard();
            return;
        }

        Vec3d target = null;
        if (hitResult.getType() == HitResult.Type.ENTITY) {
            target = ((EntityHitResult) hitResult).getEntity().getPos();
        } else if (hitResult.getType() == HitResult.Type.BLOCK) {
            target = Vec3d.ofCenter(((BlockHitResult) hitResult).getBlockPos());
        }

        if (target == null || target.distanceTo(owner.getPos()) > 50.0) {
            discard();
            return;
        }

        if (getWorld() instanceof ServerWorld serverWorld) {
            Vec3d start = owner.getPos().add(0, 1.0, 0);
            Vec3d end = target.add(0, 1.0, 0);
            Vec3d line = end.subtract(start);
            double distance = line.length();
            Vec3d direction = distance > 0 ? line.normalize() : Vec3d.ZERO;
            int particleCount = Math.max(1, (int) (distance * 4));

            for (int i = 0; i <= particleCount; i++) {
                double t = (double) i / particleCount;
                Vec3d point = start.add(direction.multiply(distance * t));
                serverWorld.spawnParticles(
                        ParticleTypes.FLAME,
                        point.x, point.y, point.z,
                        2, 0.1, 0.1, 0.1, 0.02
                );
            }
        }

        dashing = true;
        dashOwner = owner;
        dashTarget = target;

        setVelocity(Vec3d.ZERO);
        setNoGravity(true);
    }
}