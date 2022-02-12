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

	/**
	 * aspectEntry
	 * image="textures/aspects/start.png"
	 */
	public static RegistryObject<TechniqueAspect> START = ASPECTS.register("aspect_start",
			() -> new AspectStart("aspect_start", new ResourceLocation(WuxiaCraft.MOD_ID, "textures/aspects/start_aspect.png"))
					.setDescription("The starting aspect of every cultivation technique.\nIs there to serve as a reference for to where to start the implementation.")
	);

	/**
	 * this is an empty aspect should, should not even be mentioned
	 * but is there to serve as default for when the grid is empty and avoid null pointers
	 * plus it says it won't connect to anyone
	 */
	public static RegistryObject<TechniqueAspect> EMPTY = ASPECTS.register("aspect_empty", () -> new TechniqueAspect("aspect_empty", new ResourceLocation(WuxiaCraft.MOD_ID, "textures/aspects/empty.png")) {
		@Override
		public boolean canConnect(TechniqueAspect aspect) {
			return false;
		}
	});

	/**
	 * aspectEntry
	 */
	public static RegistryObject<TechniqueAspect> EMBER_ASPECT = ASPECTS.register("ember",
			() -> new AspectElementalGenerator("ember", new ResourceLocation(WuxiaCraft.MOD_ID, "/textures/aspects/ember.png"), 1d, new ResourceLocation(WuxiaCraft.MOD_ID, "fire"))
					.addCheckpoint(new TechniqueAspect.Checkpoint("sparkle", new BigDecimal("5000"), c -> {
					}))
					.addCheckpoint(new TechniqueAspect.Checkpoint("fire_intent", new BigDecimal("15000"), c -> {
					}))
					.addCheckpoint(new TechniqueAspect.Checkpoint("remaining_fire", new BigDecimal("35000"), c -> {
					}))
	);

	/**
	 * aspectEntry
	 */
	public static RegistryObject<TechniqueAspect> SCORCH = ASPECTS.register("scorch",
			() -> new AspectElementSystemConverter("scorch", new ResourceLocation(WuxiaCraft.MOD_ID, "/textures/aspects/scorch.png"), 3d, new ResourceLocation(WuxiaCraft.MOD_ID, "fire"), System.BODY)
	);

	/**
	 * Basically means Getting the energy and inserting that to the body
	 */
	public static RegistryObject<TechniqueAspect> BODY_GATHERING = ASPECTS.register("body_gathering",
			() -> new AspectSystemGather("body_gathering", new ResourceLocation(WuxiaCraft.MOD_ID, "/textures/aspects/body_gathering.png"), System.BODY)
	);

	/**
	 * Basically means Getting the energy and inserting that to the body
	 */
	public static RegistryObject<TechniqueAspect> DIVINE_GATHERING = ASPECTS.register("divine_gathering",
			() -> new AspectSystemGather("body_gathering", new ResourceLocation(WuxiaCraft.MOD_ID, "/textures/aspects/divine_gathering.png"), System.BODY)
	);

	/**
	 * Basically means Getting the energy and inserting that to the body
	 */
	public static RegistryObject<TechniqueAspect> ESSENCE_GATHERING = ASPECTS.register("essence_gathering",
			() -> new AspectSystemGather("body_gathering", new ResourceLocation(WuxiaCraft.MOD_ID, "/textures/aspects/essence_gathering.png"), System.BODY)
	);

	/**
	 * Basically means Getting a bit of the energy from the body to use elsewhere
	 */
	//TODO create a AspectSystemRelease class and instantiate that here
	public static RegistryObject<TechniqueAspect> BODY_RELEASING = ASPECTS.register("body_releasing",
			() -> new TechniqueAspect("body_releasing", new ResourceLocation(WuxiaCraft.MOD_ID, "/textures/aspects/ember.png")) {
			});
}
