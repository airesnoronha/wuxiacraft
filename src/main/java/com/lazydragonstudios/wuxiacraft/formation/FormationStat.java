package com.lazydragonstudios.wuxiacraft.formation;

import java.math.BigDecimal;

public enum FormationStat {
	ENERGY_COST(false),
	ENERGY_GENERATION(false),
	BARRIER_AMOUNT(true),
	BARRIER_MAX_AMOUNT(false),
	BARRIER_STRENGTH(false),
	BARRIER_REGEN(false),
	BARRIER_RANGE(false, StatJoinOperation.maxOp),
	BARRIER_COOLDOWN(true);

	public final boolean isModifiable;

	private final StatJoinOperation op;

	FormationStat(boolean isModifiable) {
		this.isModifiable = isModifiable;
		this.op = StatJoinOperation.addOp;
	}

	FormationStat(boolean isModifiable, StatJoinOperation op) {
		this.isModifiable = isModifiable;
		this.op = op;
	}

	public BigDecimal join(BigDecimal initialValue, BigDecimal modifier) {
		return this.op.join(initialValue, modifier);
	}

}
