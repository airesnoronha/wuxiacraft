package com.lazydragonstudios.wuxiacraft.item;

import com.lazydragonstudios.wuxiacraft.cultivation.System;
import net.minecraft.world.item.Item;

public class TechniqueManual extends Item {

	private final System system;

	public TechniqueManual(Properties properties, System system) {
		super(properties);
		this.system = system;
	}

}
