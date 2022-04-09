package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit;

import com.lazydragonstudios.wuxiacraft.combat.WuxiaDamageSource;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillStat;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.init.WuxiaElements;
import com.lazydragonstudios.wuxiacraft.init.WuxiaSkillAspects;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.phys.HitResult;

import java.math.BigDecimal;

public class SkillExplosionAspect extends SkillHitAspect {

	public SkillExplosionAspect() {
		super();
		this.activation = (player, skill, result) -> {
			if (result == null) return false;
			var pos = result.getLocation();
			var interaction = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(player.level, player) ? Explosion.BlockInteraction.BREAK : Explosion.BlockInteraction.NONE;
			boolean fire = false;
			var cultivation = Cultivation.get(player);
			var systemData = cultivation.getSystemData(System.ESSENCE);
			var systemStrength = systemData.getStat(PlayerStat.STRENGTH);
			var skillStrength = systemStrength.multiply(BigDecimal.valueOf(cultivation.getStrengthRegulator()));
			BigDecimal damage = skill.getStatValue(SkillStat.STRENGTH).multiply(skillStrength.multiply(new BigDecimal("3")));
			var damageSource = new WuxiaDamageSource("wuxiacraft.explosion_skill", WuxiaElements.PHYSICAL.get(), player, damage);
			player.level.explode(player, damageSource, null, pos.x, pos.y, pos.z, damage.floatValue() * 0.1f, fire, interaction);
			return false;
		};
	}

	@Override
	public SkillAspectType getType() {
		return WuxiaSkillAspects.EXPLOSION.get();
	}
}
