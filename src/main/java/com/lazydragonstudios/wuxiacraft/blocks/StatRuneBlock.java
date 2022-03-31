package com.lazydragonstudios.wuxiacraft.blocks;

import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.formation.FormationStat;
import com.lazydragonstudios.wuxiacraft.formation.FormationSystemStat;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

@SuppressWarnings("deprecation")
@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class StatRuneBlock extends Block {

	public final HashMap<FormationStat, BigDecimal> formationStats;
	public final HashMap<System, HashMap<FormationSystemStat, BigDecimal>> formationSystemStats;

	public final VoxelShape VOXEL_SHAPE = Shapes.or(Block.box(1, 0, 1, 15, 2, 15));

	public StatRuneBlock(Properties properties) {
		super(properties);
		formationStats = new HashMap<>();
		formationSystemStats = new HashMap<>();
	}

	public StatRuneBlock addStat(FormationStat stat, BigDecimal value) {
		this.formationStats.put(stat, value);
		return this;
	}

	public StatRuneBlock addStat(System system, FormationSystemStat stat, BigDecimal value) {
		this.formationSystemStats.putIfAbsent(system, new HashMap<>());
		this.formationSystemStats.get(system).put(stat, value);
		return this;
	}

	@Override
	public VoxelShape getShape(BlockState p_60555_, BlockGetter p_60556_, BlockPos p_60557_, CollisionContext p_60558_) {
		return VOXEL_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState p_60572_, BlockGetter p_60573_, BlockPos p_60574_, CollisionContext p_60575_) {
		return VOXEL_SHAPE;
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
		for (var stat : this.formationStats.keySet()) {
			var comp = new TranslatableComponent("wuxiacraft.gui.formation." + stat.name().toLowerCase(),
					this.formationStats.get(stat).toPlainString()
			);
			tooltipList.add(comp);
		}
		for (var system : this.formationSystemStats.keySet()) {
			for (var stat : this.formationSystemStats.get(system).keySet()) {
				if (!stat.displayTooltip) continue;
				var comp = new TranslatableComponent("wuxiacraft.gui.formation." + stat.name().toLowerCase(),
						new TranslatableComponent("wuxiacraft.system." + system.name().toLowerCase()),
						this.formationSystemStats.get(system).get(stat).toPlainString()
				);
				tooltipList.add(comp);
			}
		}
	}
}
