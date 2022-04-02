package com.lazydragonstudios.wuxiacraft.cultivation.skills;

import java.math.BigDecimal;

public enum SkillStat {
	STRENGTH(BigDecimal.ONE, false),
	COST(BigDecimal.ONE, false),
	COOLDOWN(BigDecimal.ONE, false),
	CAST_TIME(BigDecimal.ONE, false),
	CURRENT_COOLDOWN(BigDecimal.ZERO, true),
	CURRENT_CASTING(BigDecimal.ZERO, true);

	public final BigDecimal defaultValue;

	public final boolean isModifiable;

	SkillStat(BigDecimal defaultValue, boolean isModifiable) {
		this.defaultValue = defaultValue;
		this.isModifiable = isModifiable;
	}
}
