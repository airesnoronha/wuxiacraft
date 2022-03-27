package com.lazydragonstudios.wuxiacraft.formation;

import com.lazydragonstudios.wuxiacraft.blocks.StatRuneBlock;
import com.lazydragonstudios.wuxiacraft.blocks.entity.FormationCore;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.BlockPositionSource;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.gameevent.GameEventListener;
import net.minecraft.world.level.gameevent.PositionSource;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class FormationGameEventListener implements GameEventListener {

	private BlockPos position;
	private int radius;

	private PositionSource listenerSource;

	public FormationGameEventListener(BlockPos position, int radius) {
		this.position = position;
		this.radius = radius;
		this.listenerSource = new BlockPositionSource(position);
	}

	@Override
	public PositionSource getListenerSource() {
		return this.listenerSource;
	}

	@Override
	public int getListenerRadius() {
		return this.radius+1;
	}

	@Override
	public boolean handleGameEvent(Level level, GameEvent event, @Nullable Entity sourceEntity, BlockPos pos) {
		if(!event.equals(GameEvent.BLOCK_DESTROY)) return false;
		var blockState = level.getBlockState(pos);
		var block = blockState.getBlock();
		if(!(block instanceof StatRuneBlock)) return false;
		var blockEntity = level.getBlockEntity(this.position);
		if(!(blockEntity instanceof FormationCore formationCore)) return false;
		if(formationCore.containsRune(pos)) formationCore.deactivate();
		return true;
	}
}
