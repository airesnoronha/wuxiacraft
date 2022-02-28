package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import com.lazydragonstudios.wuxiacraft.WuxiaCraft;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class WuxiaTechniqueAspects {

	public static DeferredRegister<TechniqueAspect> ASPECTS = DeferredRegister.create(TechniqueAspect.class, WuxiaCraft.MOD_ID);

	public static RegistryObject<TechniqueAspect> START = ASPECTS.register("aspect_start", AspectStart::new);

	/**
	 * this is an empty aspect should, should not even be mentioned
	 * but is there to serve as default for when the grid is empty and avoid null pointers
	 * plus it says it won't connect to anyone
	 */
	public static RegistryObject<TechniqueAspect> EMPTY = ASPECTS.register("aspect_empty",
			() -> new TechniqueAspect() {
				@Override
				public boolean canConnect(TechniqueAspect aspect) {
					return false;
				}
			}
	);

	//////////////////////////////////////////
	//           Gathering Nodes            //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> BODY_GATHERING = ASPECTS.register("body_gathering",
			() -> new AspectSystemGather(System.BODY)
	);

	public static RegistryObject<TechniqueAspect> DIVINE_GATHERING = ASPECTS.register("divine_gathering",
			() -> new AspectSystemGather(System.DIVINE)
	);

	public static RegistryObject<TechniqueAspect> ESSENCE_GATHERING = ASPECTS.register("essence_gathering",
			() -> new AspectSystemGather(System.ESSENCE)
	);

	//////////////////////////////////////////
	//           Fire Generation ones       //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> EMBER_ASPECT = ASPECTS.register("ember",
			() -> new AspectElementalGenerator(1d, WuxiaElements.FIRE.getId())
					.addCheckpoint(new TechniqueAspect.Checkpoint("sparkle", new BigDecimal("5000")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("fire_intent", new BigDecimal("15000")))
					.addCheckpoint(new TechniqueAspect.Checkpoint("remaining_fire", new BigDecimal("35000")))
	);

	//////////////////////////////////////////
	//       Fire Transformation ones       //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> SCORCH = ASPECTS.register("scorch",
			() -> new AspectElementSystemConverter(3d, WuxiaElements.FIRE.getId(), System.BODY)
	);

	//////////////////////////////////////////
	//       Wooden Generation ones         //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> SEED = ASPECTS.register("seed",
			() -> new AspectElementalGenerator(1d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> MOSS = ASPECTS.register("moss",
			() -> new AspectElementalGenerator(3d, WuxiaElements.WOOD.getId())
	);

	//////////////////////////////////////////
	//       Wood Transformation ones       //
	//////////////////////////////////////////


	//////////////////////////////////////////
	//       Wooden Special ones            //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> LICHEN = ASPECTS.register("lichen",
			() -> new AspectElementElementConverter(3d, 1.5d, WuxiaElements.WOOD.getId(), WuxiaElements.WOOD.getId()));

	public static RegistryObject<TechniqueAspect> CHARCOAL = ASPECTS.register("charcoal",
			() -> new AspectElementElementConverter(3d, 0.7d, WuxiaElements.WOOD.getId(), WuxiaElements.FIRE.getId()));
}
