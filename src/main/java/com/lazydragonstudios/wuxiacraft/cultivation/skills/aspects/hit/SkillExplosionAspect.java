package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit;

import com.lazydragonstudios.wuxiacraft.combat.WuxiaDamageSource;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillStat;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspectType;
import com.lazydragonstudios.wuxiacraft.init.WuxiaElements;
import com.lazydragonstudios.wuxiacraft.init.WuxiaSkillAspects;
import net.minecraft.world.level.Explosion;

import java.math.BigDecimal;

public class SkillExplosionAspect extends SkillHitAspect {

	public SkillExplosionAspect() {
		super();
		this.skillStats.put(SkillStat.CAST_TIME, new BigDecimal("1.2"));
		this.skillStats.put(SkillStat.COOLDOWN, new BigDecimal("1.5"));
		this.skillStats.put(SkillStat.COST, new BigDecimal("2"));
		this.activation = (player, chain, result) -> {
			if (result == null) return false;
			var pos = result.getLocation();
			var interaction = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(player.level, player) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
			boolean fire = false;
			var damageSource = new WuxiaDamageSource("wuxiacraft.explosion_skill", WuxiaElements.PHYSICAL.get(), player, new BigDecimal("15"));
			player.level.explode(player, damageSource, null, pos.x, pos.y, pos.z, 5f, fire, interaction);
			return false;
		};
	}

	@Override
	public SkillAspectType getType() {
		return WuxiaSkillAspects.EXPLOSION.get();
	}
}
