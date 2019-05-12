package com.airesnor.wuxiacraft.cultivation;

public enum CultivationCategory {
	ENERGY_PERCEPTION("Energy Perception", CultivationLevel.BODY_REFINEMENT),
	MARTIAL_LAW("Martial Law", CultivationLevel.EARTH_LAW),
	IMMORTAL_FOUNDATION("Immortal Foundation", CultivationLevel.IMMORTALITY_LAW),
	DIVINE_LAW("Divine Law", CultivationLevel.MARTIAL_IMMORTAL);

	/**
	 * Name for displaying
	 */
	private String name;

	/**
	 * A level to return to when dying.
	 * Which means that upon death players have penalities.
	 * Only Exception is true god level, after all they're gods
	 * Which will make then return some levels
	 */
	public final CultivationLevel firstLevel;

	/**
	 * Creates a new Level Category
	 * @param name the name for displaying
	 */
	CultivationCategory(String name, CultivationLevel level) {
		this.name = name;
		this.firstLevel = level;
	}

	/**
	 * Getter for the category name
	 * @return the name of this category
	 */
	public String getName() {
		return name;
	}
}
