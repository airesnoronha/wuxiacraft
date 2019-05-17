package com.airesnor.wuxiacraft.cultivation.techniques;

import net.minecraft.client.resources.I18n;

public enum TechniqueTier {
	MORTAL("wuxiacraft.tier.mortal", 500f, 1500f, 4000f),
	MARTIAL("wuxiacraft.tier.martial", 1500f, 5000f, 20000f),
	IMMORTAL("wuxiacraft.tier.immortal", 3000f, 10000f, 50000f),
	DIVINE("wuxiacraft.tier.divine", 5000f, 15000f, 100000f);

	private final String name;
	public final float smallProgress;
	public final float greatProgress;
	public final float perfectionProgress;

	TechniqueTier(String name, float smallProgress, float greatProgress, float perfectionProgress) {
		this.name = name;
		this.smallProgress = smallProgress;
		this.greatProgress = greatProgress;
		this.perfectionProgress = perfectionProgress;
	}

	public String getName() {
		return I18n.format(this.name);
	}
}
