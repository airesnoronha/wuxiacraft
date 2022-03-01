package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import net.minecraft.resources.ResourceLocation;

import java.math.BigDecimal;
import java.util.HashMap;

public class ElementToStatConsumer extends ElementalConsumer {

	public final PlayerStat stat;

	public final BigDecimal amount;

	public ElementToStatConsumer(ResourceLocation element, double cost, PlayerStat stat, BigDecimal amount) {
		super(element, cost);
		this.stat = stat;
		this.amount = amount;
	}

	@Override
	public void consumed(HashMap<String, Object> metaData) {
		metaData.put("stat-" + this.stat.name().toLowerCase(), amount);
	}

	@Override
	public void notConsumed(HashMap<String, Object> metaData) {
	}

}
