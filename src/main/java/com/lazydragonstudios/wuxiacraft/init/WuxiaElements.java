package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerElementalStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.cultivation.Element;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class WuxiaElements {

	public static DeferredRegister<Element> ELEMENTS = DeferredRegister.create(Element.class, WuxiaCraft.MOD_ID);

	public static RegistryObject<Element> PHYSICAL = ELEMENTS.register("physical", Element::new
	);

	public static RegistryObject<Element> FIRE = ELEMENTS.register("fire", () -> new Element()
			.begets(new ResourceLocation(WuxiaCraft.MOD_ID, "earth"))
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "metal"))
			.statModifier(PlayerStat.STRENGTH, new BigDecimal("0.04"))
			.statModifier(PlayerElementalStat.PIERCE, new BigDecimal("0.06"))
			.statModifier(PlayerElementalStat.RESISTANCE, new BigDecimal("0.06"))
			.statModifier(PlayerSystemStat.MAX_ENERGY, new BigDecimal("-0.0005"))
			.addSkill(WuxiaSkillAspects.EXPLOSION.getId(), new BigDecimal("5000"))
	);

	public static RegistryObject<Element> EARTH = ELEMENTS.register("earth", () -> new Element()
			.begets(new ResourceLocation(WuxiaCraft.MOD_ID, "metal"))
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "water"))
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "wind"))
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "lightning"))
			.statModifier(PlayerStat.MAX_HEALTH, new BigDecimal("0.04"))
			.statModifier(PlayerSystemStat.MAX_ENERGY, new BigDecimal("0.04"))
			.statModifier(PlayerElementalStat.RESISTANCE, new BigDecimal("0.08"))
			.statModifier(PlayerStat.AGILITY, new BigDecimal("-0.0005"))
	);

	public static RegistryObject<Element> METAL = ELEMENTS.register("metal", () -> new Element()
			.begets(new ResourceLocation(WuxiaCraft.MOD_ID, "water"))
			.begets(new ResourceLocation(WuxiaCraft.MOD_ID, "lightning"))
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "wood"))
			.statModifier(PlayerStat.STRENGTH, new BigDecimal("0.02"))
			.statModifier(PlayerElementalStat.PIERCE, new BigDecimal("0.06"))
			.statModifier(PlayerElementalStat.RESISTANCE, new BigDecimal("0.02"))
			.statModifier(PlayerStat.HEALTH_REGEN_COST, new BigDecimal("0.001"))
	);

	public static RegistryObject<Element> WATER = ELEMENTS.register("water", () -> new Element()
			.begets(new ResourceLocation(WuxiaCraft.MOD_ID, "wood"))
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "fire"))
			.statModifier(PlayerStat.MAX_BARRIER, new BigDecimal("0.04"))
			.statModifier(PlayerSystemStat.MAX_ENERGY, new BigDecimal("0.08"))
			.statModifier(PlayerElementalStat.RESISTANCE, new BigDecimal("0.03"))
			.statModifier(PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("0.04"))
	);

	public static RegistryObject<Element> WOOD = ELEMENTS.register("wood", () -> new Element()
			.begets(new ResourceLocation(WuxiaCraft.MOD_ID, "fire"))
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "earth"))
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "poison"))
			.statModifier(PlayerStat.HEALTH_REGEN, new BigDecimal("0.00006"))
			.statModifier(PlayerStat.MAX_HEALTH, new BigDecimal("0.03"))
			.statModifier(PlayerElementalStat.RESISTANCE, new BigDecimal("0.04"))
			.statModifier(PlayerStat.STRENGTH, new BigDecimal("-0.0001"))
			.addSkill(WuxiaSkillAspects.HEAL.getId(), new BigDecimal("5000"))
	);

	public static RegistryObject<Element> LIGHTNING = ELEMENTS.register("lightning", () -> new Element()
			.begets(new ResourceLocation(WuxiaCraft.MOD_ID, "fire"))
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "water"))
			.statModifier(PlayerStat.STRENGTH, new BigDecimal("0.06"))
			.statModifier(PlayerStat.AGILITY, new BigDecimal("0.04"))
			.statModifier(PlayerElementalStat.RESISTANCE, new BigDecimal("0.01"))
			.statModifier(PlayerSystemStat.MAX_ENERGY, new BigDecimal("-0.0003"))
	);

	public static RegistryObject<Element> WIND = ELEMENTS.register("wind", () -> new Element()
			.begets(new ResourceLocation(WuxiaCraft.MOD_ID, "fire"))
			.begets(new ResourceLocation(WuxiaCraft.MOD_ID, "lightning"))
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "wood"))
			.statModifier(PlayerStat.AGILITY, new BigDecimal("0.6"))
			.statModifier(PlayerStat.DETECTION_RANGE, new BigDecimal("0.6"))
			.statModifier(PlayerElementalStat.RESISTANCE, new BigDecimal("0.1"))
			.statModifier(PlayerStat.STRENGTH, new BigDecimal("-0.0003"))
	);

	public static RegistryObject<Element> POISON = ELEMENTS.register("poison", () -> new Element()
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "water"))
			.statModifier(PlayerStat.AGILITY, new BigDecimal("0.06"))
			.statModifier(PlayerStat.DETECTION_RANGE, new BigDecimal("0.06"))
			.statModifier(PlayerElementalStat.RESISTANCE, new BigDecimal("0.01"))
			.statModifier(PlayerStat.STRENGTH, new BigDecimal("-0.003"))
	);

	public static RegistryObject<Element> LIGHT = ELEMENTS.register("light", () -> new Element()
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "darkness"))
			.statModifier(PlayerStat.AGILITY, new BigDecimal("0.06"))
			.statModifier(PlayerStat.DETECTION_STRENGTH, new BigDecimal("0.06"))
			.statModifier(PlayerStat.HEALTH_REGEN, new BigDecimal("0.00001"))
			.statModifier(PlayerElementalStat.RESISTANCE, new BigDecimal("-0.003"))
	);

	public static RegistryObject<Element> DARKNESS = ELEMENTS.register("darkness", () -> new Element()
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "light"))
			.statModifier(PlayerStat.STRENGTH, new BigDecimal("0.06"))
			.statModifier(PlayerStat.DETECTION_RESISTANCE, new BigDecimal("0.06"))
			.statModifier(PlayerElementalStat.RESISTANCE, new BigDecimal("0.01"))
			.statModifier(PlayerStat.HEALTH_REGEN, new BigDecimal("-0.0000001"))
	);

	public static RegistryObject<Element> SPACE = ELEMENTS.register("space", () -> new Element()
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "time"))
	);

	public static RegistryObject<Element> TIME = ELEMENTS.register("time", () -> new Element()
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "space"))
	);

	public static RegistryObject<Element> BUTT = ELEMENTS.register("butt", () -> new Element()
			.suppresses(new ResourceLocation(WuxiaCraft.MOD_ID, "wind"))
	);


}
