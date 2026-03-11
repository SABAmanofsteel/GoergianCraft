package com.georgiancraft;

import net.fabricmc.fabric.api.event.player.*;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.ActionResult;

public class georgiancraftProtection {

    public static void register() {

        // Prevent breaking blocks
        PlayerBlockBreakEvents.BEFORE.register((world, player, pos, state, blockEntity) -> {
            if (world.isClient()) return true;
            if (!(player instanceof ServerPlayerEntity)) return true;

            if (isProtected(player)) {
                player.sendMessage(
                        net.minecraft.text.Text.literal("§cYou cannot break blocks here."),
                        true
                );
                return false; // cancel
            }
            return true;
        });

        // Prevent placing blocks
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient()) return ActionResult.PASS;
            if (!(player instanceof ServerPlayerEntity)) return ActionResult.PASS;

            net.minecraft.item.ItemStack stack = player.getStackInHand(hand);
            if (stack.getItem() instanceof net.minecraft.item.BlockItem && isProtected(player)) {
                player.sendMessage(
                        net.minecraft.text.Text.literal("§cYou cannot place blocks here."),
                        true
                );
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });

        // Prevent attacking entities (PvP and mobs)
        AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
            if (world.isClient()) return ActionResult.PASS;
            if (!(player instanceof ServerPlayerEntity)) return ActionResult.PASS;

            if (entity instanceof ServerPlayerEntity && isProtected(player)) {
                player.sendMessage(
                        net.minecraft.text.Text.literal("§cYou cannot attack other players here."),
                        true
                );
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });

        // Prevent interacting with blocks (opening chests, doors, etc.)
        UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
            if (world.isClient()) return ActionResult.PASS;
            if (!(player instanceof ServerPlayerEntity)) return ActionResult.PASS;

            net.minecraft.block.BlockState state = world.getBlockState(hitResult.getBlockPos());
            boolean isInteractable = state.getBlock() instanceof net.minecraft.block.ChestBlock
                    || state.getBlock() instanceof net.minecraft.block.FurnaceBlock
                    || state.getBlock() instanceof net.minecraft.block.CraftingTableBlock;

            if (isInteractable && isProtected(player)) {
                player.sendMessage(
                        net.minecraft.text.Text.literal("§cYou cannot interact with blocks here."),
                        true
                );
                return ActionResult.FAIL;
            }
            return ActionResult.PASS;
        });
    }

    /**
     * Central protection check.
     * Right now this always returns false (no one is blocked).
     * Replace this logic with whatever condition you need —
     * e.g. check world dimension, a specific region, a player tag, a gamemode, etc.
     */
    private static boolean isProtected(net.minecraft.entity.player.PlayerEntity player) {
        // Must be in the Overworld
        if (player.getWorld().getRegistryKey() != net.minecraft.world.World.OVERWORLD) {
            return false;
        }

        // Must be within a 50-block cube centered on 0,0,0 (X and Z only, full vertical range)
        net.minecraft.util.math.BlockPos pos = player.getBlockPos();
        if (pos.getX() < -25 || pos.getX() > 25 || pos.getZ() < -25 || pos.getZ() > 25) {
            return false;
        }

        // Block players with permission level under 4 (i.e. not operators)
        if (((net.minecraft.server.network.ServerPlayerEntity) player).hasPermissionLevel(4)) {
            return false;
        }

        return true; // player is in the zone AND not an op → block them
    }
}