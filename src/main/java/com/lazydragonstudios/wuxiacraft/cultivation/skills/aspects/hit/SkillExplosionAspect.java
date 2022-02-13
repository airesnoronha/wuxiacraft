package com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit;

import net.minecraft.world.level.Explosion;

import java.math.BigDecimal;

public class SkillExplosionAspect extends SkillHitAspect {

	public SkillExplosionAspect() {
		super();
		this.activation = (player, chain, result) -> {
			if (result == null) return false;
			var pos = result.getLocation();
			var interaction = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(player.level, player) ? Explosion.BlockInteraction.DESTROY : Explosion.BlockInteraction.NONE;
			boolean fire = false;
			player.level.explode(player, pos.x, pos.y, pos.z, 15f, fire, interaction);
			return false;
		};
		this.castModifier = new BigDecimal("1.2");
		this.cooldownModifier = new BigDecimal("1.5");
		this.costModifier = new BigDecimal("2");
	}
}
