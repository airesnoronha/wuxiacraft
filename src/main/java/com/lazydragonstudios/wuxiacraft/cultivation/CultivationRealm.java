package com.lazydragonstudios.wuxiacraft.cultivation;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.util.HashSet;

public class CultivationRealm extends ForgeRegistryEntry<CultivationRealm> {

	/**
	 * the Name of this realm
	 */
	public final String name;

	/**
	 * The cultivation system this realm belongs to
	 */
	public final System system;

	public CultivationRealm(String name, System system) {
		this.name = name;
		this.system = system;
	}
}
