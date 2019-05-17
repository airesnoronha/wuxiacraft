package com.airesnor.wuxiacraft.cultivation;

import net.minecraft.client.resources.I18n;

public enum CultivationCategory {
	ENERGY_PERCEPTION("wuxiacraft.category.energy_perception"),
	MARTIAL_LAW("wuxiacraft.category.martial_law"),
	IMMORTAL_FOUNDATION("wuxiacraft.category.immortal_foundation"),
	DIVINE_LAW("wuxiacraft.category.divine_law");

	/**
	 * Name for displaying
	 */
	private final String name;

	/**
	 * Creates a new Level Category
	 * @param name the name for displaying
	 */
	CultivationCategory(String name) {
		this.name = name;
	}

	/**
	 * Getter for the category name
	 * @return the name of this category
	 */
	public String getName() {
		return I18n.format(this.name);
	}
}
