package com.lazydragonstudios.wuxiacraft.blocks;

import com.lazydragonstudios.wuxiacraft.blocks.entity.FormationCore;
import com.lazydragonstudios.wuxiacraft.formation.FormationGameEventListener;
import com.lazydragonstudios.wuxiacraft.formation.FormationTicker;
import com.lazydragonstudios.wuxiacraft.init.WuxiaBlocks;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FormationCoreBlock extends BaseEntityBlock {

	public static final VoxelShape VOXEL_SHAPE = Shapes.or(
			Block.box(0d, 0d, 0d, 4d, 4d, 4d),
			Block.box(12d, 0d, 0d, 16d, 4d, 4d),
			Block.box(0d, 0d, 12d, 4d, 4d, 16d),
			Block.box(12d, 0d, 12d, 16d, 4d, 16d),
			Block.box(4d, 1d, 1d, 12d, 3d, 3d),
			Block.box(4d, 1d, 13d, 12d, 3d, 15d),
			Block.box(1d, 1d, 4d, 3d, 3d, 12d),
			Block.box(13d, 1d, 4d, 15d, 3d, 12d),
			Block.box(5d, 5d, 5d, 11d, 11d, 11d)
	);

	private final int formationRadius;

	private final Block coreBlock;

	public FormationCoreBlock(Properties properties) {
		this(properties, 1, Blocks.OAK_LOG);
	}

	public FormationCoreBlock(Properties properties, Block coreBlock) {
		this(properties, 1, coreBlock);
	}

	public FormationCoreBlock(Properties properties, int formationRadius, Block coreBlock) {
		super(properties);
		this.formationRadius = formationRadius;
		this.coreBlock = coreBlock;
	}

	@Override
	public InteractionResult use(BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
		var blockEntity = level.getBlockEntity(pos);
		if (!(blockEntity instanceof FormationCore core)) return InteractionResult.PASS;
		if (level.isClientSide) return InteractionResult.SUCCESS;
		if (player.isCrouching()) {
			var entity = new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(BlockItem.byBlock(this.coreBlock), 1));
			core.deactivate();
			entity.setNoPickUpDelay();
			level.addFreshEntity(entity);
			level.setBlockAndUpdate(pos, WuxiaBlocks.FORMATION_CORE_BASE.get().defaultBlockState());
		} else {
			if (core.isActive()) core.deactivate();
			else core.activate(player.getUUID());
		}
		return InteractionResult.CONSUME;
	}

	@Nullable
	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new FormationCore(pos, state).setRuneRange(this.formationRadius);
	}

	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
		return VOXEL_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState p_60572_, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
		return VOXEL_SHAPE;
	}

	@Nullable
	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> entityType) {
		//noinspection unchecked
		return (BlockEntityTicker<T>) new FormationTicker();
	}

	@Override
	public RenderShape getRenderShape(BlockState p_49232_) {
		return RenderShape.MODEL;
	}


	@Nullable
	@Override
	public <T extends BlockEntity> GameEventListener getListener(Level level, T blockEntity) {
		return new FormationGameEventListener(blockEntity.getBlockPos(), this.formationRadius);
	}

	@Override
	public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos thisPos) {
		BlockPos below = thisPos.below();
		BlockState belowState = levelReader.getBlockState(below);
		return belowState.isFaceSturdy(levelReader, below, Direction.UP);
	}

	@Override
	public void appendHoverText(ItemStack itemStack, @Nullable BlockGetter blockGetter, List<Component> tooltipList, TooltipFlag tooltipFlag) {
		super.appendHoverText(itemStack, blockGetter, tooltipList, tooltipFlag);
		var comp = new TranslatableComponent("wuxiacraft.gui.formation.radius", this.formationRadius);
		tooltipList.add(comp);
	}
}
