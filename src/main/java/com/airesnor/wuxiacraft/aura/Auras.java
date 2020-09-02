package com.airesnor.wuxiacraft.aura;

import net.minecraft.util.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class Auras {

	public static final Map<ResourceLocation, Aura> AURAS = new HashMap<>();

	public static final Aura BASE_AURA_BLACK = new BaseAura("base_aura_black", 0.05f, 0.05f, 0.05f);
	public static final Aura BASE_AURA_DARK_BLUE = new BaseAura("base_aura_dark_blue", 0.05f, 0.05f, 0.66f);
	public static final Aura BASE_AURA_DARK_GREEN = new BaseAura("base_aura_dark_green", 0.05f, 0.66f, 0.05f);
	public static final Aura BASE_AURA_DARK_AQUA = new BaseAura("base_aura_dark_aqua", 0.05f, 0.66f, 0.66f);
	public static final Aura BASE_AURA_DARK_RED = new BaseAura("base_aura_dark_red", 0.66f, 0.05f, 0.05f);
	public static final Aura BASE_AURA_PURPLE = new BaseAura("base_aura_purple", 0.66f, 0.05f, 0.66f);
	public static final Aura BASE_AURA_GOLD = new BaseAura("base_aura_gold", 1.0f, 0.66f, 0.05f);
	public static final Aura BASE_AURA_GRAY = new BaseAura("base_aura_gray", 0.66f, 0.66f, 0.66f);
	public static final Aura BASE_AURA_DARK_GRAY = new BaseAura("base_aura_dark_gray", 0.33f, 0.33f, 0.33f);
	public static final Aura BASE_AURA_BLUE = new BaseAura("base_aura_blue", 0.33f, 0.33f, 1.0f);
	public static final Aura BASE_AURA_GREEN = new BaseAura("base_aura_green", 0.33f, 1.0f, 0.33f);
	public static final Aura BASE_AURA_AQUA = new BaseAura("base_aura_aqua", 0.33f, 1.0f, 1.0f);
	public static final Aura BASE_AURA_RED = new BaseAura("base_aura_red", 1.0f, 0.33f, 0.33f);
	public static final Aura BASE_AURA_LIGHT_PURPLE = new BaseAura("base_aura_light_purple", 1.0f, 0.33f, 1.0f);
	public static final Aura BASE_AURA_YELLOW = new BaseAura("base_aura_yellow", 1.0f, 1.0f, 0.33f);
	public static final Aura BASE_AURA_WHITE = new BaseAura("base_aura_white", 1.0f, 1.0f, 1.0f);

	public static final Aura ELECTRIC_AURA_BLACK = new ElectricAura("electric_aura_black", 0.05f, 0.05f, 0.05f);
	public static final Aura ELECTRIC_AURA_DARK_BLUE = new ElectricAura("electric_aura_dark_blue", 0.05f, 0.05f, 0.76f);
	public static final Aura ELECTRIC_AURA_DARK_GREEN = new ElectricAura("electric_aura_dark_green", 0.05f, 0.76f, 0.05f);
	public static final Aura ELECTRIC_AURA_DARK_AQUA = new ElectricAura("electric_aura_dark_aqua", 0.05f, 0.76f, 0.76f);
	public static final Aura ELECTRIC_AURA_DARK_RED = new ElectricAura("electric_aura_dark_red", 0.76f, 0.05f, 0.05f);
	public static final Aura ELECTRIC_AURA_PURPLE = new ElectricAura("electric_aura_purple", 0.76f, 0.05f, 0.76f);
	public static final Aura ELECTRIC_AURA_GOLD = new ElectricAura("electric_aura_gold", 1.0f, 0.76f, 0.05f);
	public static final Aura ELECTRIC_AURA_GRAY = new ElectricAura("electric_aura_gray", 0.66f, 0.66f, 0.66f);
	public static final Aura ELECTRIC_AURA_DARK_GRAY = new ElectricAura("electric_aura_dark_gray", 0.33f, 0.33f, 0.33f);
	public static final Aura ELECTRIC_AURA_BLUE = new ElectricAura("electric_aura_blue", 0.40f, 0.40f, 1.00f);
	public static final Aura ELECTRIC_AURA_GREEN = new ElectricAura("electric_aura_green", 0.40f, 1.00f, 0.40f);
	public static final Aura ELECTRIC_AURA_AQUA = new ElectricAura("electric_aura_aqua", 0.40f, 1.00f, 1.00f);
	public static final Aura ELECTRIC_AURA_RED = new ElectricAura("electric_aura_red", 1.0f, 0.4f, 0.4f);
	public static final Aura ELECTRIC_AURA_LIGHT_PURPLE = new ElectricAura("electric_aura_light_purple", 1.0f, 0.4f, 1.0f);
	public static final Aura ELECTRIC_AURA_YELLOW = new ElectricAura("electric_aura_yellow", 1.0f, 1.0f, 0.4f);
	public static final Aura ELECTRIC_AURA_WHITE = new ElectricAura("electric_aura_white", 1f, 1f, 1f);
}
