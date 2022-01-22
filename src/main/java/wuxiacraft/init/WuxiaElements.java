package wuxiacraft.init;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.cultivation.Element;

public class WuxiaElements {

	public static DeferredRegister<Element> ELEMENTS = DeferredRegister.create(Element.class, WuxiaCraft.MOD_ID);

	public static RegistryObject<Element> PHYSICAL = ELEMENTS.register("physical", () -> new Element("physical"));

	public static RegistryObject<Element> FIRE = ELEMENTS.register("fire", () -> new Element("fire"));
	public static RegistryObject<Element> EARTH = ELEMENTS.register("earth", () -> new Element("earth"));
	public static RegistryObject<Element> METAL = ELEMENTS.register("metal", () -> new Element("metal"));
	public static RegistryObject<Element> WATER = ELEMENTS.register("water", () -> new Element("water"));
	public static RegistryObject<Element> WOOD = ELEMENTS.register("wood", () -> new Element("wood"));

	public static RegistryObject<Element> LIGHTNING = ELEMENTS.register("lightning", () -> new Element("lightning"));
	public static RegistryObject<Element> WIND = ELEMENTS.register("wind", () -> new Element("wind"));

	public static RegistryObject<Element> LIGHT = ELEMENTS.register("light", () -> new Element("light"));
	public static RegistryObject<Element> DARKNESS = ELEMENTS.register("darkness", () -> new Element("darkness"));

	public static RegistryObject<Element> SPACE = ELEMENTS.register("space", () -> new Element("space"));
	public static RegistryObject<Element> TIME = ELEMENTS.register("time", () -> new Element("time"));

	/**
	 * Conquer the world with fart
	 */
	public static RegistryObject<Element> BUTT = ELEMENTS.register("butt", () -> new Element("butt"));







}
