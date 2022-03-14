package com.lazydragonstudios.wuxiacraft.blocks.entity;

import com.lazydragonstudios.wuxiacraft.init.WuxiaBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class FormationCore extends BlockEntity {


	public FormationCore(BlockPos pos, BlockState blockState) {
		super(WuxiaBlockEntities.FORMATION_CORE.get(), pos, blockState);
	}


}
