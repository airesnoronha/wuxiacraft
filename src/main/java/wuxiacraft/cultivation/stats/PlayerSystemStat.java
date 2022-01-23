package wuxiacraft.cultivation.stats;

import java.math.BigDecimal;

public enum PlayerSystemStat {
	CULTIVATION_BASE(new BigDecimal("0"), false),
	FOUNDATION(new BigDecimal("0"), false),
	ENERGY(new BigDecimal("0"), true),
	ENERGY_REGEN(new BigDecimal("0"), false),
	MAX_ENERGY(new BigDecimal("10"), false);

	public final BigDecimal defaultValue;

	/**
	 * Whether it can modify the value of this stat outside SystemContainer class
	 * using @see {@link wuxiacraft.cultivation.SystemContainer#setStat}
	 */
	public final boolean isModifiable;

	PlayerSystemStat(BigDecimal defaultValue, boolean isModifiable) {
		this.defaultValue = defaultValue;
		this.isModifiable = isModifiable;
	}
}
