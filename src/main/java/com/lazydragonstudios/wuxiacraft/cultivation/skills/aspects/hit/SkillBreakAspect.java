package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit;

import com.lazydragonstudios.wuxiacraft.combat.WuxiaDamageSource;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillStat;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.init.WuxiaElements;
import com.lazydragonstudios.wuxiacraft.init.WuxiaSkillAspects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.math.BigDecimal;

public class SkillBreakAspect extends SkillHitAspect {

	private int toolLevel = 0;

	public SkillBreakAspect() {
		this.activation = (player, skill, result) -> {
			if (player.level.isClientSide) return false;
			if (result == null) return false;
			if (result instanceof BlockHitResult blockHitResult) {
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
			} else if (result instanceof EntityHitResult entityHitResult) {
				var target = entityHitResult.getEntity();
				var cultivation = Cultivation.get(player);
				var systemData = cultivation.getSystemData(System.ESSENCE);
				if (target instanceof LivingEntity livingEntity) {
					var damage = skill.getStatValue(SkillStat.STRENGTH).multiply(systemData.getStat(PlayerStat.STRENGTH).multiply(new BigDecimal("0.5")));
					var damageSource = new WuxiaDamageSource("wuxiacraft.skill.break", WuxiaElements.PHYSICAL.get(), livingEntity, damage);
					target.hurt(damageSource, damageSource.getDamage().floatValue());
				}
				return true;
			}
			return false;
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
