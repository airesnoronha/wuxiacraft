package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.TechniqueGrid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.awt.*;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Set;
import java.util.function.Supplier;

public class WuxiaDefaultTechniqueManuals {

	private static HashMap<ResourceLocation, Supplier<ItemStack>> DEFAULT_MANUALS = new HashMap<>();

	public static void init() {
		TechniqueGrid fire_manipulation = new TechniqueGrid();
		fire_manipulation.addGridNode(new Point(0, 0), WuxiaTechniqueAspects.START.getId(), BigDecimal.TEN);
		fire_manipulation.addGridNode(new Point(-1, 0), WuxiaTechniqueAspects.CINDER.getId(), BigDecimal.TEN);
		fire_manipulation.addGridNode(new Point(1, 0), WuxiaTechniqueAspects.CINDER.getId(), BigDecimal.TEN);
		fire_manipulation.addGridNode(new Point(1, -1), WuxiaTechniqueAspects.MAGIC_BURNING.getId(), BigDecimal.TEN);
		fire_manipulation.addGridNode(new Point(-1, 1), WuxiaTechniqueAspects.MAGIC_BURNING.getId(), BigDecimal.TEN);
		fire_manipulation.addGridNode(new Point(0, -1), WuxiaTechniqueAspects.ESSENCE_GATHERING.getId(), BigDecimal.TEN);
		fire_manipulation.addGridNode(new Point(0, 1), WuxiaTechniqueAspects.ESSENCE_GATHERING.getId(), BigDecimal.TEN);
		registerNewManual(new ResourceLocation(WuxiaCraft.MOD_ID, "fire_manipulation"), 1, fire_manipulation);

		TechniqueGrid legends_of_water = new TechniqueGrid();
		legends_of_water.addGridNode(new Point(0, -1), WuxiaTechniqueAspects.START.getId(), BigDecimal.TEN);
		legends_of_water.addGridNode(new Point(1, -1), WuxiaTechniqueAspects.DROP.getId(), BigDecimal.TEN);
		legends_of_water.addGridNode(new Point(1, 0), WuxiaTechniqueAspects.DROP.getId(), BigDecimal.TEN);
		legends_of_water.addGridNode(new Point(0, 1), WuxiaTechniqueAspects.STREAM.getId(), BigDecimal.TEN);
		legends_of_water.addGridNode(new Point(-1, 1), WuxiaTechniqueAspects.ESSENCE_GATHERING.getId(), BigDecimal.TEN);
		registerNewManual(new ResourceLocation(WuxiaCraft.MOD_ID, "legends_of_water"), 1, legends_of_water);

		TechniqueGrid nature_observation = new TechniqueGrid();
		nature_observation.addGridNode(new Point(-1, 0), WuxiaTechniqueAspects.START.getId(), BigDecimal.TEN);
		nature_observation.addGridNode(new Point(-1, 1), WuxiaTechniqueAspects.SEED.getId(), BigDecimal.TEN);
		nature_observation.addGridNode(new Point(0, 1), WuxiaTechniqueAspects.SEED.getId(), BigDecimal.TEN);
		nature_observation.addGridNode(new Point(1, 0), WuxiaTechniqueAspects.BRANCHING.getId(), BigDecimal.TEN);
		nature_observation.addGridNode(new Point(1, -1), WuxiaTechniqueAspects.ESSENCE_GATHERING.getId(), BigDecimal.TEN);
		registerNewManual(new ResourceLocation(WuxiaCraft.MOD_ID, "nature_observation"), 1, nature_observation);

		TechniqueGrid lightning_replication = new TechniqueGrid();
		lightning_replication.addGridNode(new Point(1, -1), WuxiaTechniqueAspects.START.getId(), BigDecimal.TEN);
		lightning_replication.addGridNode(new Point(1, 0), WuxiaTechniqueAspects.SPARK.getId(), BigDecimal.TEN);
		lightning_replication.addGridNode(new Point(0, 0), WuxiaTechniqueAspects.ARC.getId(), BigDecimal.TEN);
		lightning_replication.addGridNode(new Point(-1, 1), WuxiaTechniqueAspects.ESSENCE_GATHERING.getId(), BigDecimal.TEN);
		registerNewManual(new ResourceLocation(WuxiaCraft.MOD_ID, "lightning_replication"), 1, lightning_replication);

		TechniqueGrid metallic_reinforcement = new TechniqueGrid();
		metallic_reinforcement.addGridNode(new Point(0, -1), WuxiaTechniqueAspects.START.getId(), BigDecimal.TEN);
		metallic_reinforcement.addGridNode(new Point(1, 0), WuxiaTechniqueAspects.ORE.getId(), BigDecimal.TEN);
		metallic_reinforcement.addGridNode(new Point(1, 0), WuxiaTechniqueAspects.ORE.getId(), BigDecimal.TEN);
		metallic_reinforcement.addGridNode(new Point(-1, 1), WuxiaTechniqueAspects.SHARPNESS.getId(), BigDecimal.TEN);
		metallic_reinforcement.addGridNode(new Point(-1, 1), WuxiaTechniqueAspects.SHARPNESS.getId(), BigDecimal.TEN);
		metallic_reinforcement.addGridNode(new Point(0, 1), WuxiaTechniqueAspects.ESSENCE_GATHERING.getId(), BigDecimal.TEN);
		registerNewManual(new ResourceLocation(WuxiaCraft.MOD_ID, "metallic_reinforcement"), 1, metallic_reinforcement);
	}

	public static void registerNewManual(ResourceLocation resourceLocation, Supplier<ItemStack> stackSupplier) {
		DEFAULT_MANUALS.put(resourceLocation, stackSupplier);
	}

	public static void registerNewManual(ResourceLocation resourceLocation, int radius, TechniqueGrid grid) {
		DEFAULT_MANUALS.put(resourceLocation, () -> {
			var itemStack = new ItemStack(WuxiaItems.ESSENCE_MANUAL.get());
			var itemTag = itemStack.getTag();
			if (itemTag == null) {
				itemTag = new CompoundTag();
				itemStack.setTag(itemTag);
			}
			itemTag.putString("name", resourceLocation.getNamespace() + ".technique." + resourceLocation.getPath());
			itemTag.putInt("radius", radius);
			itemTag.put("technique-grid", grid.serialize());
			return itemStack;
		});
	}

	@Nullable
	public static Supplier<ItemStack> getDefaultManual(ResourceLocation resourceLocation) {
		return DEFAULT_MANUALS.get(resourceLocation);
	}

	public static Set<ResourceLocation> getAllKeys() {
		return DEFAULT_MANUALS.keySet();
	}

}
