package com.georgiancraft.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import net.minecraft.world.tick.ScheduledTickView;

public class GeorgianFlagBlock extends HorizontalFacingBlock {
    public static final MapCodec<GeorgianFlagBlock> CODEC = createCodec(GeorgianFlagBlock::new);
    public static final EnumProperty<Part> PART = EnumProperty.of("part", Part.class);

    private static final VoxelShape NORTH_SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 7.5, 16.0, 16.0, 8.5);
    private static final VoxelShape EAST_WEST_SHAPE = Block.createCuboidShape(7.5, 0.0, 0.0, 8.5, 16.0, 16.0);

    public GeorgianFlagBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(PART, Part.LEFT));
    }

    @Override
    protected MapCodec<GeorgianFlagBlock> getCodec() {
        return CODEC;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING, PART);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, net.minecraft.world.BlockView world, BlockPos pos, ShapeContext context) {
        return state.get(FACING).getAxis() == Direction.Axis.Z ? NORTH_SOUTH_SHAPE : EAST_WEST_SHAPE;
    }

    @Override
    public VoxelShape getCollisionShape(BlockState state, net.minecraft.world.BlockView world, BlockPos pos, ShapeContext context) {
        return getOutlineShape(state, world, pos, context);
    }

    @Override
    public VoxelShape getSidesShape(BlockState state, BlockView world, BlockPos pos) {
        return VoxelShapes.fullCube();
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext ctx) {
        Direction facing = ctx.getHorizontalPlayerFacing().getOpposite();
        BlockPos pos = ctx.getBlockPos();
        Direction rightDirection = facing.rotateYClockwise();
        BlockPos rightPos = pos.offset(rightDirection);

        if (!ctx.getWorld().getBlockState(rightPos).canReplace(ctx)) {
            return null;
        }

        return this.getDefaultState().with(FACING, facing).with(PART, Part.LEFT);
    }

    @Override
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
        if (!world.isClient) {
            Direction rightDirection = state.get(FACING).rotateYClockwise();
            BlockPos rightPos = pos.offset(rightDirection);
            world.setBlockState(rightPos, state.with(PART, Part.RIGHT), Block.NOTIFY_ALL);
        }
    }

    @Override
    protected BlockState getStateForNeighborUpdate(
            BlockState state,
            WorldView world,
            ScheduledTickView tickView,
            BlockPos pos,
            Direction direction,
            BlockPos neighborPos,
            BlockState neighborState,
            Random random
    ) {
        Direction companionDirection = getCompanionDirection(state);
        if (direction == companionDirection) {
            if (!neighborState.isOf(this) || neighborState.get(PART) == state.get(PART) || neighborState.get(FACING) != state.get(FACING)) {
                return Blocks.AIR.getDefaultState();
            }
        }
        return super.getStateForNeighborUpdate(state, world, tickView, pos, direction, neighborPos, neighborState, random);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient) {
            BlockPos companionPos = pos.offset(getCompanionDirection(state));
            BlockState companionState = world.getBlockState(companionPos);
            if (companionState.isOf(this) && companionState.get(PART) != state.get(PART) && companionState.get(FACING) == state.get(FACING)) {
                world.setBlockState(companionPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
                world.syncWorldEvent(player, 2001, companionPos, Block.getRawIdFromState(companionState));
            }
        }
        return super.onBreak(world, pos, state, player);
    }

    private static Direction getCompanionDirection(BlockState state) {
        Direction rightDirection = state.get(FACING).rotateYClockwise();
        return state.get(PART) == Part.LEFT ? rightDirection : rightDirection.getOpposite();
    }

    public enum Part implements StringIdentifiable {
        LEFT,
        RIGHT;

        @Override
        public String asString() {
            return this == LEFT ? "left" : "right";
        }
    }
}