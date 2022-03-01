package com.lazydragonstudios.wuxiacraft.cultivation.stats;

public enum PlayerElementalStat {
	COMPREHENSION(true),
	RESISTANCE(false),
	PIERCE(false);

	public final boolean isModifiable;

	PlayerElementalStat(boolean isModifiable) {
		this.isModifiable = isModifiable;
	}
}
