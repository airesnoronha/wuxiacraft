package com.lazydragonstudios.wuxiacraft.cultivation.stats;

public enum PlayerSystemElementalStat {
	FOUNDATION(true);

	public final boolean isModifiable;

	PlayerSystemElementalStat(boolean isModifiable) {
		this.isModifiable = isModifiable;
	}
}
