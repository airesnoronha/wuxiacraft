package wuxiacraft.cultivation.stats;

import java.math.BigDecimal;

public enum PlayerStat {
	STRENGTH(new BigDecimal("0.0"), false),
	AGILITY(new BigDecimal("0.0"), false),
	HEALTH(new BigDecimal("20.0"), true),
	MAX_HEALTH(new BigDecimal("20.0"), false),
	HEALTH_REGEN(new BigDecimal("1.0"), false),
	HEALTH_REGEN_COST(new BigDecimal("1.0"), false);

	public final BigDecimal defaultValue;

	/**
	 * this means that the value of this stat can be modified from outside Cultivation class
	 * using {@link wuxiacraft.cultivation.Cultivation#setPlayerStat}
	 */
	public final boolean isModifiable;

	PlayerStat(BigDecimal defaultValue, boolean isModifiable) {
		this.defaultValue = defaultValue;
		this.isModifiable = isModifiable;
	}
}
