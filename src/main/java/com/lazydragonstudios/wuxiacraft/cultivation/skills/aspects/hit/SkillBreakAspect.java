package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit;

import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillStat;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import com.lazydragonstudios.wuxiacraft.init.WuxiaSkillAspects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.phys.BlockHitResult;

import java.math.BigDecimal;

public class SkillBreakAspect extends SkillHitAspect {

	private int toolLevel = 0;

	public SkillBreakAspect() {
		this.skillStats.put(SkillStat.CAST_TIME, new BigDecimal("15"));
		this.skillStats.put(SkillStat.COOLDOWN, new BigDecimal("3"));
		this.skillStats.put(SkillStat.COST, new BigDecimal("0.1"));
		this.activation = (player, chain, result) -> {
			if (player.level.isClientSide) return false;
			if (result == null) return false;
			if (!(result instanceof BlockHitResult blockHitResult)) return false;
			var blockPos = blockHitResult.getBlockPos();
			if (!player.mayInteract(player.level, blockPos)) return false;
			var blockState = player.level.getBlockState(blockPos);
			var destroySpeed = blockState.getDestroySpeed(player.level, blockPos);
			if (destroySpeed < 0) return false;
			if (this.toolLevel < 3 && blockState.is(BlockTags.NEEDS_DIAMOND_TOOL)) {
				return false;
			} else if (this.toolLevel < 2 && blockState.is(BlockTags.NEEDS_IRON_TOOL)) {
				return false;
			} else if (this.toolLevel < 1 && blockState.is(BlockTags.NEEDS_STONE_TOOL)) {
				return false;
			}
			player.level.destroyBlock(blockPos, true, player);
			return true;
		};
	}

	public int getToolLevel() {
		return toolLevel;
	}

	public void setToolLevel(int toolLevel) {
		this.toolLevel = toolLevel;
	}

	@Override
	public SkillAspectType getType() {
		return WuxiaSkillAspects.BREAK.get();
	}

	@Override
	public CompoundTag serialize() {
		var tag = super.serialize();
		tag.putInt("tool-level", this.toolLevel);
		return tag;
	}

	@Override
	public void deserialize(CompoundTag tag) {
		super.deserialize(tag);
		this.toolLevel = tag.getInt("tool-level");
	}
}
