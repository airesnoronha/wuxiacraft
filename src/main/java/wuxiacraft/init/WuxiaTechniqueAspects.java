package wuxiacraft.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.cultivation.System;
import wuxiacraft.cultivation.technique.aspects.*;

public class WuxiaTechniqueAspects {

	public static DeferredRegister<TechniqueAspect> ASPECTS = DeferredRegister.create(TechniqueAspect.class, WuxiaCraft.MOD_ID);

	public static RegistryObject<TechniqueAspect> START = ASPECTS.register("aspect_start", () -> new AspectStart("aspect_start", new ResourceLocation(WuxiaCraft.MOD_ID, "textures/aspects/start_aspect.png"))
	);
	public static RegistryObject<TechniqueAspect> EMPTY = ASPECTS.register("aspect_empty", () -> new TechniqueAspect("aspect_empty", new ResourceLocation(WuxiaCraft.MOD_ID, "textures/aspects/empty.png")) {
		@Override
		public boolean canConnect(TechniqueAspect aspect) {
			return false;
		}
	});

	public static RegistryObject<TechniqueAspect> FIRE_ASPECT_1 = ASPECTS.register("fire_aspect_1",
			() -> new AspectElementalGenerator("fire_aspect_1", new ResourceLocation(WuxiaCraft.MOD_ID, "textures/aspects/ember.png"), 1d, new ResourceLocation(WuxiaCraft.MOD_ID, "fire"))
	);

	public static RegistryObject<TechniqueAspect> FIRE_CONNECT_TO_BODY_1 = ASPECTS.register("fire_connect_to_body_1",
			() -> new AspectElementSystemConverter("fire_connect_to_body_1", new ResourceLocation(WuxiaCraft.MOD_ID, "textures/aspects/ember.png"), 3d, new ResourceLocation(WuxiaCraft.MOD_ID, "fire"), System.BODY)
	);

	/**
	 * Basically means Getting the energy and inserting that to the body
	 */
	public static RegistryObject<TechniqueAspect> BODY_GATHERING = ASPECTS.register("body_gathering",
			() -> new AspectSystemGather("body_gathering", new ResourceLocation(WuxiaCraft.MOD_ID, "textures/aspects/ember.png"), System.BODY)
	);

	/**
	 * Basically means Getting the energy and inserting that to the body
	 */
	public static RegistryObject<TechniqueAspect> DIVINE_GATHERING = ASPECTS.register("divine_gathering",
			() -> new AspectSystemGather("body_gathering", new ResourceLocation(WuxiaCraft.MOD_ID, "textures/aspects/ember.png"), System.BODY)
	);

	/**
	 * Basically means Getting the energy and inserting that to the body
	 */
	public static RegistryObject<TechniqueAspect> ESSENCE_GATHERING = ASPECTS.register("essence_gathering",
			() -> new AspectSystemGather("body_gathering", new ResourceLocation(WuxiaCraft.MOD_ID, "textures/aspects/ember.png"), System.BODY)
	);

	/**
	 * Basically means Getting a bit of the energy from the body to use elsewhere
	 */
	//TODO create a AspectSystemRelease class and instantiate that here
	public static RegistryObject<TechniqueAspect> BODY_RELEASING = ASPECTS.register("body_releasing",
			() -> new TechniqueAspect("body_releasing", new ResourceLocation(WuxiaCraft.MOD_ID, "textures/aspects/ember.png")) {
			});

}
