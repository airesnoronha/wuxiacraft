package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit;

import com.lazydragonstudios.wuxiacraft.combat.WuxiaDamageSource;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillStat;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.init.WuxiaElements;
import com.lazydragonstudios.wuxiacraft.init.WuxiaSkillAspects;
import com.lazydragonstudios.wuxiacraft.util.SkillUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.TickTask;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

import java.math.BigDecimal;
import java.util.HashSet;

public class SkillChopAspect extends SkillHitAspect {

	private int toolLevel = 0;

	public SkillChopAspect() {
		this.activation = (player, skill, result) -> {
			if (player.level.isClientSide) return false;
			var server = player.level.getServer();
			if (server == null) return false;
			if (result == null) return false;
			if (result instanceof BlockHitResult blockHitResult) {
				var blockPos = blockHitResult.getBlockPos();
				var treeBlocks = SkillUtil.getLogsToBreak(blockPos, player.level, new HashSet<>());
				for (var pos : treeBlocks) {
					server.doRunTask(new TickTask(1, () -> {
						if (!player.mayInteract(player.level, pos)) return;
						player.level.destroyBlock(pos, true, player);
					}));
				}
				return true;
			} else if (result instanceof EntityHitResult entityHitResult) {
				var target = entityHitResult.getEntity();
				var cultivation = Cultivation.get(player);
				var systemData = cultivation.getSystemData(System.ESSENCE);
				if (target instanceof LivingEntity livingEntity) {
					var damage = skill.getStatValue(SkillStat.STRENGTH).multiply(systemData.getStat(PlayerStat.STRENGTH).multiply(new BigDecimal("3")));
					var damageSource = new WuxiaDamageSource("wuxiacraft.skill.chop", WuxiaElements.PHYSICAL.get(), livingEntity, damage).setInstantDeath();
					target.hurt(damageSource, 1f);
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
		return WuxiaSkillAspects.CHOP.get();
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
