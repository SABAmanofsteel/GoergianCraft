package com.georgiancraft.client.renderer;

import com.georgiancraft.entity.AmiranChainEntity;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import java.util.List;

public class AmiranChainRenderer {

    public static void register() {
        WorldRenderEvents.AFTER_ENTITIES.register(AmiranChainRenderer::render);
    }

    private static void render(WorldRenderContext ctx) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;

        float tickDelta = ctx.tickCounter().getTickProgress(true);
        Vec3d camera = ctx.camera().getPos();

        // Find all chain entities in the world
        List<AmiranChainEntity> chains = client.world.getEntitiesByClass(
                AmiranChainEntity.class,
                client.player.getBoundingBox().expand(32),
                e -> e.getOwner() instanceof PlayerEntity
        );

        if (chains.isEmpty()) return;

        MatrixStack matrices = ctx.matrixStack();
        VertexConsumer line = ctx.consumers().getBuffer(RenderLayer.getLines());

        for (AmiranChainEntity entity : chains) {
            if (!(entity.getOwner() instanceof PlayerEntity player)) continue;

            Vec3d entityPos = entity.getLerpedPos(tickDelta);
            Vec3d playerPos = player.getLerpedPos(tickDelta)
                    .add(0, player.getEyeHeight(player.getPose()) * 0.85, 0);

            // Offset relative to camera
            double ex = entityPos.x - camera.x;
            double ey = entityPos.y - camera.y;
            double ez = entityPos.z - camera.z;

            double px = playerPos.x - camera.x;
            double py = playerPos.y - camera.y;
            double pz = playerPos.z - camera.z;

            matrices.push();
            Matrix4f mat = matrices.peek().getPositionMatrix();

            int SEGMENTS = 16;
            for (int i = 0; i < SEGMENTS; i++) {
                float t = (float) i / SEGMENTS;
                float tNext = (float) (i + 1) / SEGMENTS;

                double droop      = Math.sin(t     * Math.PI) * 0.15;
                double droopNext  = Math.sin(tNext  * Math.PI) * 0.15;

                float lx  = (float) MathHelper.lerp(t,     px, ex);
                float ly  = (float)(MathHelper.lerp(t,     py, ey) - droop);
                float lz  = (float) MathHelper.lerp(t,     pz, ez);

                float lxN = (float) MathHelper.lerp(tNext, px, ex);
                float lyN = (float)(MathHelper.lerp(tNext, py, ey) - droopNext);
                float lzN = (float) MathHelper.lerp(tNext, pz, ez);

                float ndx = lxN - lx;
                float ndy = lyN - ly;
                float ndz = lzN - lz;
                float len = (float) Math.sqrt(ndx * ndx + ndy * ndy + ndz * ndz);
                if (len == 0) continue;
                ndx /= len; ndy /= len; ndz /= len;

                line.vertex(mat, lx,  ly,  lz ).color(180, 180, 180, 255).normal(ndx, ndy, ndz);
                line.vertex(mat, lxN, lyN, lzN).color(180, 180, 180, 255).normal(ndx, ndy, ndz);
            }

            matrices.pop();
        }
    }
}