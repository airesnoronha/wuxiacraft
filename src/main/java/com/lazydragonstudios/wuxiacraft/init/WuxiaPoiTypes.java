package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;

import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class WuxiaPoiTypes {

	public static final DeferredRegister<PoiType> POI_TYPES = DeferredRegister.create(ForgeRegistries.POI_TYPES, WuxiaCraft.MOD_ID);

	public static RegistryObject<PoiType> TECHNIQUE_INSCRIBER = POI_TYPES
		.register("cultivator",
			() -> new PoiType("cultivator",
				PoiType.getBlockStates(WuxiaBlocks.TECHNIQUE_INSCRIBER.get()),
				1,
				1
			)
		);
}