package com.lazydragonstudios.wuxiacraft.blocks;

import com.lazydragonstudios.wuxiacraft.blocks.entity.RunemakingTable;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
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
public class RunemakingTableBlock extends BaseEntityBlock {

	public static final VoxelShape VOXEL_SHAPE = Shapes.or(
			Block.box(0, 0, 0, 3, 12, 3), //legs
			Block.box(13, 0, 0, 16, 12, 3),
			Block.box(13, 0, 13, 16, 12, 16),
			Block.box(0, 0, 13, 3, 12, 16),
			Block.box(0, 12, 0, 16, 15, 16), // tabletop
			Block.box(0, 15, 0, 2, 16, 2), //diamond chunks
			Block.box(14, 15, 0, 16, 16, 2),
			Block.box(14, 15, 14, 16, 16, 16),
			Block.box(0, 15, 14, 2, 16, 16),
			Block.box(2, 15, 0, 14, 16, 1), //golden borders
			Block.box(2, 15, 15, 14, 16, 16),
			Block.box(0, 15, 2, 1, 16, 14),
			Block.box(15, 15, 2, 16, 16, 14)
	);

	public RunemakingTableBlock(Properties properties) {
		super(properties);
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new RunemakingTable(pPos, pState);
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
		return super.getTicker(pLevel, pState, pBlockEntityType);
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
		if (blockentity instanceof RunemakingTable) {
			player.openMenu((MenuProvider) blockentity);
		}
	}

	@Override
	public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return VOXEL_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
		return VOXEL_SHAPE;
	}

	@Override
	public RenderShape getRenderShape(BlockState pState) {
		return RenderShape.MODEL;
	}

}
