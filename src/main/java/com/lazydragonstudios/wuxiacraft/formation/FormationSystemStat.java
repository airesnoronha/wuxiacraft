package com.lazydragonstudios.wuxiacraft.formation;

import java.math.BigDecimal;

public enum FormationSystemStat {
	CULTIVATION_SPEED(false),
	ENERGY_REGEN_RANGE(false, StatJoinOperation.maxOp),
	ENERGY_REGEN(false),
	ENERGY_REGEN_RUNE_COUNT(false);

	public final boolean isModifiable;

	private final StatJoinOperation op;

	FormationSystemStat(boolean isModifiable) {
		this.isModifiable = isModifiable;
		this.op = StatJoinOperation.addOp;
	}

	FormationSystemStat(boolean isModifiable, StatJoinOperation op) {
		this.isModifiable = isModifiable;
		this.op = op;
	}

	public BigDecimal join(BigDecimal initialValue, BigDecimal modifier) {
		return this.op.join(initialValue, modifier);
	}
}
