package com.lazydragonstudios.wuxiacraft.blocks;

import com.lazydragonstudios.wuxiacraft.blocks.entity.InscriberEntity;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TechniqueInscriber extends BaseEntityBlock {

	public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
	public static final VoxelShape SHAPE_BASE_LATITUDINAL = Shapes.or(Block.box(1d, 0d, 3d, 1d + 14d, 3d, 3d + 10d), Block.box(2d, 2d, 5d, 2d + 4d, 2d + 13d, 5d + 6d), Block.box(10d, 2d, 5d, 10d + 4d, 2d + 13d, 5d + 6d));
	public static final VoxelShape SHAPE_BASE_LONGITUDINAL = Shapes.or(Block.box(3d, 0d, 1d, 3d + 10d, 0d + 3d, 1d + 14d), Block.box(5d, 2d, 2d, 5d + 6d, 2d + 13d, 2d + 4d), Block.box(5d, 2d, 10d, 5d + 6d, 2d + 13d, 10d + 4d));
	public static final VoxelShape SHAPE_TABLE_EAST = Shapes.or(Block.box(1.0D, 10.0D, 0.0D, 5.333333D, 14.0D, 16.0D), Block.box(5.333333D, 12.0D, 0.0D, 9.666667D, 16.0D, 16.0D), Block.box(9.666667D, 14.0D, 0.0D, 15.0D, 19.0D, 16.0D), SHAPE_BASE_LONGITUDINAL);
	public static final VoxelShape SHAPE_TABLE_WEST = Shapes.or(Block.box(10.666667D, 10.0D, 0.0D, 15.0D, 14.0D, 16.0D), Block.box(6.333333D, 12.0D, 0.0D, 10.666667D, 16.0D, 16.0D), Block.box(1.0D, 14.0D, 0.0D, 7.333333D, 19.0D, 16.0D), SHAPE_BASE_LONGITUDINAL);
	public static final VoxelShape SHAPE_TABLE_SOUTH = Shapes.or(Block.box(0.0D, 10.0D, 1.0D, 16.0D, 14.0D, 5.333333D), Block.box(0.0D, 12.0D, 5.333333D, 16.0D, 16.0D, 9.666667D), Block.box(0.0D, 14.0D, 9.666667D, 16.0D, 19.0D, 15.0D), SHAPE_BASE_LATITUDINAL);
	public static final VoxelShape SHAPE_TABLE_NORTH = Shapes.or(Block.box(0.0D, 10.0D, 10.666667D, 16.0D, 14.0D, 15.0D), Block.box(0.0D, 12.0D, 6.333333D, 16.0D, 16.0D, 10.666667D), Block.box(0.0D, 14.0D, 1.0D, 16.0D, 19.0D, 7.333333D), SHAPE_BASE_LATITUDINAL);

	public TechniqueInscriber(Properties properties) {
		super(properties);
		this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
	}

	@Override
	public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		if (level.isClientSide) {
			return InteractionResult.SUCCESS;
		} else {
			this.openContainer(level, pos, player);
			return InteractionResult.CONSUME;
		}
	}

	protected void openContainer(Level level, BlockPos pos, Player player) {
		BlockEntity blockentity = level.getBlockEntity(pos);
		if (blockentity instanceof InscriberEntity) {
			player.openMenu((MenuProvider) blockentity);
		}
	}



	@Override
	public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext ctx) {
		return switch (state.getValue(FACING)) {
			case NORTH -> SHAPE_TABLE_NORTH;
			case SOUTH -> SHAPE_TABLE_SOUTH;
			case EAST -> SHAPE_TABLE_EAST;
			case WEST -> SHAPE_TABLE_WEST;
			default -> SHAPE_BASE_LONGITUDINAL;
		};
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext ctx) {
		return switch (state.getValue(FACING)) {
			case NORTH -> SHAPE_TABLE_NORTH;
			case SOUTH -> SHAPE_TABLE_SOUTH;
			case EAST -> SHAPE_TABLE_EAST;
			case WEST -> SHAPE_TABLE_WEST;
			default -> SHAPE_BASE_LONGITUDINAL;
		};
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rotation) {
		return state.setValue(FACING, rotation.rotate(state.getValue(FACING)));
	}

	@Override
	protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> state) {
		state.add(FACING);
	}

	@Nullable
	@Override
	public BlockState getStateForPlacement(BlockPlaceContext context) {
		return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
	}

	@Override
	public boolean isPathfindable(BlockState state, BlockGetter getter, BlockPos pos, PathComputationType type) {
		return false;
	}

	@Override
	public RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.MODEL;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new InscriberEntity(pos, state);
	}

}
