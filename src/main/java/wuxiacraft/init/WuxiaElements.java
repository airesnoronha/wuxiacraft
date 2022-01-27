package wuxiacraft.init;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.cultivation.Element;

public class WuxiaElements {

	public static DeferredRegister<Element> ELEMENTS = DeferredRegister.create(Element.class, WuxiaCraft.MOD_ID);

	/**
	 * elementEntry=elements.fire
	 * desc="physical manipulation"
	 */
	public static RegistryObject<Element> PHYSICAL = ELEMENTS.register("physical", () -> new Element("physical"));

	/**
	 * elementEntry=elements.fire
	 * desc="fire manipulation"
	 */
	public static RegistryObject<Element> FIRE = ELEMENTS.register("fire", () -> new Element("fire")
			.begets(new ResourceLocation(WuxiaCraft.MOD_ID, "earth"))
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "metal"))
	);

	/**
	 * elementEntry=elements.fire
	 * desc="earth manipulation"
	 */
	public static RegistryObject<Element> EARTH = ELEMENTS.register("earth", () -> new Element("earth")
			.begets(new ResourceLocation(WuxiaCraft.MOD_ID, "water"))
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "metal"))
	);

	/**
	 * elementEntry=elements.fire
	 * desc="metal manipulation"
	 */
	public static RegistryObject<Element> METAL = ELEMENTS.register("metal", () -> new Element("metal")
			.begets(new ResourceLocation(WuxiaCraft.MOD_ID, "water"))
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "wood"))
	);

	/**
	 * elementEntry=elements.fire
	 * desc="water manipulation"
	 */
	public static RegistryObject<Element> WATER = ELEMENTS.register("water", () -> new Element("water")
			.begets(new ResourceLocation(WuxiaCraft.MOD_ID, "wood"))
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "fire"))
	);

	/**
	 * elementEntry=elements.fire
	 * desc="wood manipulation"
	 */
	public static RegistryObject<Element> WOOD = ELEMENTS.register("wood", () -> new Element("wood")
			.begets(new ResourceLocation(WuxiaCraft.MOD_ID, "fire"))
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "earth"))
	);

	/**
	 * elementEntry=elements.fire
	 * desc="lightning manipulation"
	 */
	public static RegistryObject<Element> LIGHTNING = ELEMENTS.register("lightning", () -> new Element("lightning")
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "water"))
	);

	/**
	 * elementEntry=elements.fire
	 * desc="air/wind manipulation"
	 */
	public static RegistryObject<Element> WIND = ELEMENTS.register("wind", () -> new Element("wind")
			.begets(new ResourceLocation(WuxiaCraft.MOD_ID, "fire"))
	);

	/**
	 * elementEntry=elements.fire
	 * desc="light manipulation"
	 */
	public static RegistryObject<Element> LIGHT = ELEMENTS.register("light", () -> new Element("light")
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "darkness"))
	);

	/**
	 * elementEntry=elements.fire
	 * desc="darkness manipulation"
	 */
	public static RegistryObject<Element> DARKNESS = ELEMENTS.register("darkness", () -> new Element("darkness")
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "light"))
	);

	/**
	 * elementEntry=elements.fire
	 * desc="space manipulation"
	 */
	public static RegistryObject<Element> SPACE = ELEMENTS.register("space", () -> new Element("space")
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "time"))
	);

	/**
	 * elementEntry=elements.fire
	 * desc="time manipulation"
	 */
	public static RegistryObject<Element> TIME = ELEMENTS.register("time", () -> new Element("time")
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "space"))
	);

	/**
	 * elementEntry=elements.butt
	 * desc="Conquer the world with fart"
	 */
	public static RegistryObject<Element> BUTT = ELEMENTS.register("butt", () -> new Element("butt")
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "wind"))
	);


}
