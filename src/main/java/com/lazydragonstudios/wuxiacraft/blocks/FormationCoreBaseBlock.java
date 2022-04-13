package com.lazydragonstudios.wuxiacraft.blocks;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.init.WuxiaBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.ParametersAreNonnullByDefault;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FormationCoreBaseBlock extends Block {

	public static TagKey<Item> FORMATION_CORE_BLOCKS = ItemTags.create(new ResourceLocation(WuxiaCraft.MOD_ID, "formation_core_blocks"));

	public static final VoxelShape VOXEL_SHAPE = Shapes.or(
			Block.box(0d, 0d, 0d, 4d, 4d, 4d),
			Block.box(12d, 0d, 0d, 16d, 4d, 4d),
			Block.box(0d, 0d, 12d, 4d, 4d, 16d),
			Block.box(12d, 0d, 12d, 16d, 4d, 16d),
			Block.box(4d, 1d, 1d, 12d, 3d, 3d),
			Block.box(4d, 1d, 13d, 12d, 3d, 15d),
			Block.box(1d, 1d, 4d, 3d, 3d, 12d),
			Block.box(13d, 1d, 4d, 15d, 3d, 12d)
	);

	public FormationCoreBaseBlock(Properties properties) {
		super(properties);
	}

	@Override
	public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult hitResult) {
		var itemStack = player.getItemInHand(interactionHand);
		var item = itemStack.getItem();
		if (!(item instanceof BlockItem blockItem)) return InteractionResult.PASS;
		var block = blockItem.getBlock();
		Block formationCoreResult = null;
		if (block.equals(Blocks.OAK_LOG)) {
			formationCoreResult = WuxiaBlocks.OAK_FORMATION_CORE.get();
		} else if (block.equals(Blocks.BIRCH_LOG)) {
			formationCoreResult = WuxiaBlocks.BIRCH_FORMATION_CORE.get();
		} else if (block.equals(Blocks.SPRUCE_LOG)) {
			formationCoreResult = WuxiaBlocks.SPRUCE_FORMATION_CORE.get();
		} else if (block.equals(Blocks.JUNGLE_LOG)) {
			formationCoreResult = WuxiaBlocks.JUNGLE_FORMATION_CORE.get();
		} else if (block.equals(Blocks.ACACIA_LOG)) {
			formationCoreResult = WuxiaBlocks.ACACIA_FORMATION_CORE.get();
		} else if (block.equals(Blocks.DARK_OAK_LOG)) {
			formationCoreResult = WuxiaBlocks.DARK_OAK_FORMATION_CORE.get();
		} else if (block.equals(Blocks.STONE)) {
			formationCoreResult = WuxiaBlocks.STONE_FORMATION_CORE.get();
		} else if (block.equals(Blocks.COPPER_BLOCK)) {
			formationCoreResult = WuxiaBlocks.COPPER_FORMATION_CORE.get();
		} else if (block.equals(Blocks.IRON_BLOCK)) {
			formationCoreResult = WuxiaBlocks.IRON_FORMATION_CORE.get();
		} else if (block.equals(Blocks.GOLD_BLOCK)) {
			formationCoreResult = WuxiaBlocks.GOLD_FORMATION_CORE.get();
		} else if (block.equals(Blocks.LAPIS_BLOCK)) {
			formationCoreResult = WuxiaBlocks.LAPIS_FORMATION_CORE.get();
		} else if (block.equals(Blocks.DIAMOND_BLOCK)) {
			formationCoreResult = WuxiaBlocks.DIAMOND_FORMATION_CORE.get();
		} else if (block.equals(Blocks.EMERALD_BLOCK)) {
			formationCoreResult = WuxiaBlocks.EMERALD_FORMATION_CORE.get();
		}
		if (formationCoreResult == null) return InteractionResult.FAIL;
		itemStack.shrink(1);
		level.setBlockAndUpdate(pos, formationCoreResult.defaultBlockState());
		return InteractionResult.SUCCESS;
	}

	@Override
	public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return VOXEL_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState blockState, BlockGetter blockGetter, BlockPos pos, CollisionContext context) {
		return VOXEL_SHAPE;
	}

	@Override
	public boolean isPathfindable(BlockState p_60475_, BlockGetter p_60476_, BlockPos p_60477_, PathComputationType p_60478_) {
		return false;
	}

	@Override
	public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos thisPos) {
		BlockPos below = thisPos.below();
		BlockState belowState = levelReader.getBlockState(below);
		return belowState.isFaceSturdy(levelReader, below, Direction.UP);
	}
}
