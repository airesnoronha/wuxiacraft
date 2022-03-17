package com.lazydragonstudios.wuxiacraft.formation;

public enum FormationStat {
	BARRIER_AMOUNT(true),
	BARRIER_MAX_AMOUNT(false),
	BARRIER_STRENGTH(false),
	BARRIER_RANGE(false);

	public final boolean isModifiable;

	FormationStat(boolean isModifiable) {
		this.isModifiable = isModifiable;
	}
}
