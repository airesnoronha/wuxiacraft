package com.lazydragonstudios.wuxiacraft.formation;

import java.math.BigDecimal;

public enum FormationSystemStat {
	CULTIVATION_SPEED(false),
	CULTIVATION_RUNE_COUNT(false, false),
	ENERGY_REGEN_RANGE(false, StatJoinOperation.maxOp),
	ENERGY_REGEN(false),
	ENERGY_REGEN_RUNE_COUNT(false, false);

	public final boolean isModifiable;

	private final StatJoinOperation op;

	public final boolean displayTooltip;

	FormationSystemStat(boolean isModifiable) {
		this(isModifiable, StatJoinOperation.addOp, true);
	}

	FormationSystemStat(boolean isModifiable, boolean displayTooltip) {
		this(isModifiable, StatJoinOperation.addOp, displayTooltip);
	}

	FormationSystemStat(boolean isModifiable, StatJoinOperation op) {
		this(isModifiable, op, true);
	}

	FormationSystemStat(boolean isModifiable, StatJoinOperation op, boolean displayTooltip) {
		this.isModifiable = isModifiable;
		this.op = op;
		this.displayTooltip = displayTooltip;
	}

	public BigDecimal join(BigDecimal initialValue, BigDecimal modifier) {
		return this.op.join(initialValue, modifier);
	}
}
