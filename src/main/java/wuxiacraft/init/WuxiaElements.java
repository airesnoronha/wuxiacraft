package wuxiacraft.init;

import wuxiacraft.combat.ElementalDamageSource;
import wuxiacraft.cultivation.Element;

import java.util.LinkedList;
import java.util.List;

public class WuxiaElements {

	public static final LinkedList<Element> ELEMENTS = new LinkedList<>();
	public static final LinkedList<ElementalDamageSource> ELEMENTAL_DAMAGE_SOURCES = new LinkedList<>();

	public static ElementalDamageSource getSourceByElement(Element element) {
		for(ElementalDamageSource source : ELEMENTAL_DAMAGE_SOURCES) {
			if(source.getElement() == element)
				return source;
		}
		return ELEMENTAL_DAMAGE_SOURCES.getLast(); //probably is gonna be physical
	}

	//Five elements from book of changes
	public static Element WATER;
	public static Element WOOD;
	public static Element FIRE;
	public static Element EARTH;
	public static Element METAL;

	//extra elements from common sense at least
	public static Element AIR; // also wind
	public static Element LIGHTNING;
	public static Element ICE;

	public static Element LIGHT;
	public static Element DARK;

	//neutral element -- can evolve to spatial
	public static Element PHYSICAL;

	public static void initalize() {
		WATER = new Element("water").begets(WOOD).begets(ICE).overcomes(FIRE);
		WOOD = new Element("wood").begets(FIRE).overcomes(EARTH);
		FIRE = new Element("fire").begets(EARTH).overcomes(METAL).overcomes(ICE);
		EARTH = new Element("earth").begets(METAL).overcomes(WATER);
		METAL = new Element("metal").begets(WATER).overcomes(WOOD);

		AIR = new Element("air").begets(FIRE).overcomes(WOOD);
		LIGHTNING = new Element("lightning").begets(FIRE).overcomes(METAL).overcomes(WATER);
		ICE = new Element("ice").begets(EARTH).overcomes(FIRE);

		LIGHT = new Element("light").overcomes(DARK);
		DARK = new Element("dark").overcomes(LIGHT);

		PHYSICAL = new Element("physical");
	}

}
