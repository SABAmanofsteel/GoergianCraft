package com.georgiancraft.block;

import com.georgiancraft.item.ModItems;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.block.BlockState;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;

public class VaziBlock extends Block implements net.minecraft.block.Fertilizable {

    public static final int MAX_AGE = 3;
    public static final IntProperty AGE = IntProperty.of("age", 0, MAX_AGE);

    public enum Half implements StringIdentifiable {
        UPPER, LOWER;
        public String asString() { return this == UPPER ? "upper" : "lower"; }
    }

    public static final EnumProperty<Half> HALF = EnumProperty.of("half", Half.class);

    public VaziBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState()
                .with(HALF, Half.LOWER)
                .with(AGE, 0));
    }
    @Override
    public boolean isFertilizable(WorldView world, BlockPos pos, BlockState state) {
        return state.get(HALF) == Half.LOWER && state.get(AGE) < MAX_AGE;
    }

    @Override
    public boolean canGrow(World world, Random random, BlockPos pos, BlockState state) {
        return state.get(HALF) == Half.LOWER;
    }

    @Override
    public void grow(ServerWorld world, Random random, BlockPos pos, BlockState state) {
        if (state.get(HALF) == Half.LOWER) {
            int age = state.get(AGE);
            if (age < MAX_AGE) {
                syncAge(world, pos, state, age + 1);
            }
        }
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(HALF, AGE);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        BlockPos pos = ctx.getBlockPos();
        World world = ctx.getWorld();
        if (pos.getY() < world.getTopYInclusive() && world.getBlockState(pos.up()).canReplace(ctx)) {
            return this.getDefaultState().with(HALF, Half.LOWER).with(AGE, 0);
        }
        return null;
    }


    // Keep top and bottom in sync for age
    private void syncAge(World world, BlockPos pos, BlockState state, int newAge) {
        world.setBlockState(pos, state.with(AGE, newAge), 3);
        if (state.get(HALF) == Half.LOWER) {
            BlockState above = world.getBlockState(pos.up());
            if (above.isOf(this)) world.setBlockState(pos.up(), above.with(AGE, newAge), 3);
        } else {
            BlockState below = world.getBlockState(pos.down());
            if (below.isOf(this)) world.setBlockState(pos.down(), below.with(AGE, newAge), 3);
        }
    }

    @Override
    public boolean hasRandomTicks(BlockState state) {
        // Only tick on the lower half to avoid double-growing
        return state.get(HALF) == Half.LOWER && state.get(AGE) < MAX_AGE;
    }

    @Override
    public void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        int age = state.get(AGE);
        if (age < MAX_AGE) {
            syncAge(world, pos, state, age + 1);
        }
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos,
                                 PlayerEntity player, BlockHitResult hit) {
        if (world.isClient) return ActionResult.SUCCESS;

        // If clicking bottom, redirect check to top
        BlockPos checkPos = state.get(HALF) == Half.LOWER ? pos.up() : pos;
        BlockState checkState = state.get(HALF) == Half.LOWER ? world.getBlockState(pos.up()) : state;

        if (checkState.isOf(this) && checkState.get(AGE) >= MAX_AGE) {
            dropStack(world, checkPos, new ItemStack(ModItems.GRAPE));
            // Reset from bottom pos always
            BlockPos bottomPos = state.get(HALF) == Half.LOWER ? pos : pos.down();
            BlockState bottomState = world.getBlockState(bottomPos);
            syncAge(world, bottomPos, bottomState, 0);
            return ActionResult.SUCCESS;
        }
        return ActionResult.PASS;
    }

    // Break both halves when one is broken
    @Override
    protected void onBlockAdded(BlockState state, World world, BlockPos pos,
                                BlockState oldState, boolean notify) {
        if (!world.isClient && state.get(HALF) == Half.LOWER) {
            world.setBlockState(pos.up(),
                    this.getDefaultState().with(HALF, Half.UPPER).with(AGE, 0), 3);
        }
        super.onBlockAdded(state, world, pos, oldState, notify);
    }

    @Override
    protected BlockState getStateForNeighborUpdate(BlockState state, WorldView world, ScheduledTickView tickView,
                                                   BlockPos pos, Direction direction, BlockPos neighborPos,
                                                   BlockState neighborState, Random random) {
        Half half = state.get(HALF);
        if (half == Half.LOWER && direction == Direction.UP) {
            if (!neighborState.isOf(this)) return Blocks.AIR.getDefaultState();
        } else if (half == Half.UPPER && direction == Direction.DOWN) {
            if (!neighborState.isOf(this)) return Blocks.AIR.getDefaultState();
        }
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public boolean canPlaceAt(BlockState state, net.minecraft.world.WorldView world, BlockPos pos) {
        if (state.get(HALF) == Half.UPPER) return true; // top is placed by code, trust it
        return world.getBlockState(pos.down()).isSolid();
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
        return VoxelShapes.fullCube();
    }
}