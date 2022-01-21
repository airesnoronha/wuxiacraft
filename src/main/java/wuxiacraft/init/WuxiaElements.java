package wuxiacraft.init;

import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.cultivation.Element;

public class WuxiaElements {

	public static DeferredRegister<Element> ELEMENTS = DeferredRegister.create(Element.class, WuxiaCraft.MOD_ID);

	public static RegistryObject<Element> PHYSICAL = ELEMENTS.register("physical", () -> new Element("physical"));

	public static RegistryObject<Element> FIRE = ELEMENTS.register("fire", () -> new Element("fire"));
	public static RegistryObject<Element> LIGHTNING = ELEMENTS.register("lightning", () -> new Element("lightning"));

}
