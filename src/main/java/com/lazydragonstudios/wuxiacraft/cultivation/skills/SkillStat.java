package com.lazydragonstudios.wuxiacraft.cultivation.skills;

import java.math.BigDecimal;

public enum SkillStat {
	COST(BigDecimal.ONE, false),
	COOLDOWN(BigDecimal.ONE, false),
	CAST_TIME(BigDecimal.ONE, false),
	CURRENT_COOLDOWN(BigDecimal.ONE, true),
	CURRENT_CASTING(BigDecimal.ONE, true);

	public final BigDecimal defaultValue;

	public final boolean isModifiable;

	SkillStat(BigDecimal defaultValue, boolean isModifiable) {
		this.defaultValue = defaultValue;
		this.isModifiable = isModifiable;
	}
}
