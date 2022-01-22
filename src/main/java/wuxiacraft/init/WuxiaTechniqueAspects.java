package wuxiacraft.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.cultivation.technique.TechniqueAspect;

public class WuxiaTechniqueAspects {

	public static DeferredRegister<TechniqueAspect> ASPECTS = DeferredRegister.create(TechniqueAspect.class, WuxiaCraft.MOD_ID);

	public static RegistryObject<TechniqueAspect> START = ASPECTS.register("aspect_start", () -> new TechniqueAspect("aspect_start")
			.addExpected(new ResourceLocation(WuxiaCraft.MOD_ID, "fire_aspect_1"))
	);
	public static RegistryObject<TechniqueAspect> EMPTY = ASPECTS.register("aspect_empty", () -> new TechniqueAspect("aspect_empty"));

	public static RegistryObject<TechniqueAspect> FIRE_ASPECT_1 = ASPECTS.register("fire_aspect_1", () -> new TechniqueAspect("fire_aspect_1")
			.addExpected(new ResourceLocation(WuxiaCraft.MOD_ID, "fire_connect_to_body_1"))
	);

	public static RegistryObject<TechniqueAspect> FIRE_CONNECT_TO_BODY_1 = ASPECTS.register("fire_connect_to_body_1", () -> new TechniqueAspect("fire_connect_to_body_1")
			.addExpected(new ResourceLocation(WuxiaCraft.MOD_ID, "body_gathering"))
	);

	public static RegistryObject<TechniqueAspect> BODY_GATHERING = ASPECTS.register("body_gathering", () -> new TechniqueAspect("body_gathering"));

}
