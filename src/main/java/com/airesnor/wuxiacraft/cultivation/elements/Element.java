package com.airesnor.wuxiacraft.cultivation.elements;

import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.text.TextFormatting;

import java.util.*;

public class Element {
    private String name;
    private TextFormatting color;
    private List<Element> counters;
    private EnumParticleTypes particle;

    public static final List<Element> ELEMENTS = new ArrayList<>();

    public Element (String name, TextFormatting tf, EnumParticleTypes particle) {
        this.name = name;
        this.color = tf;
        this.counters = new ArrayList<>();
        this.particle = particle;
        ELEMENTS.add(this);
    }

    public void AddCounter(Element counter) {
        this.counters.add(counter);
    }

    public boolean isCounter(Element counter) {
        return this.counters.contains(counter);
    }

    public String getName() {
        return I18n.format("wuxiacraft.elements."+this.name);
    }

    public String getUnlocalizedName() {
        return "wuxiacraft.elements." + this.name;
    }

    public String getColor() {
        return this.color.toString();
    }

    public EnumParticleTypes getParticle() {
        return particle;
    }

    public static final Element FIRE = new Element("fire", TextFormatting.RED, EnumParticleTypes.FLAME);
    public static final Element EARTH = new Element("earth", TextFormatting.YELLOW, EnumParticleTypes.CRIT);
    public static final Element METAL = new Element("metal", TextFormatting.GRAY, EnumParticleTypes.SMOKE_NORMAL);
    public static final Element WATER = new Element("water", TextFormatting.AQUA, EnumParticleTypes.WATER_SPLASH);
    public static final Element WOOD = new Element("wood", TextFormatting.DARK_GREEN, EnumParticleTypes.VILLAGER_HAPPY);
    public static final Element LIGHT = new Element("light", TextFormatting.WHITE, EnumParticleTypes.CLOUD);
    public static final Element DARK = new Element("dark", TextFormatting.DARK_GRAY, EnumParticleTypes.DRAGON_BREATH);

    public static final Element WIND = new Element("wind", TextFormatting.GOLD, EnumParticleTypes.SPELL_MOB_AMBIENT);
    public static final Element LIGHTNING = new Element("lightning", TextFormatting.LIGHT_PURPLE, EnumParticleTypes.CRIT_MAGIC);
    public static final Element ICE = new Element("ice", TextFormatting.DARK_AQUA, EnumParticleTypes.DRIP_WATER);

    public static void init() {

        FIRE.AddCounter(WATER);
        FIRE.AddCounter(EARTH);

        EARTH.AddCounter(METAL);
        EARTH.AddCounter(WOOD);
        EARTH.AddCounter(ICE);

        METAL.AddCounter(FIRE);
        METAL.AddCounter(WATER);
        METAL.AddCounter(LIGHTNING);

        WATER.AddCounter(WOOD);
        WATER.AddCounter(EARTH);
        WATER.AddCounter(ICE);

        WOOD.AddCounter(FIRE);
        WOOD.AddCounter(METAL);
        WOOD.AddCounter(WIND);

        LIGHT.AddCounter(DARK);
        DARK.AddCounter(LIGHT);

        WIND.AddCounter(FIRE);
        WIND.AddCounter(EARTH);
        WIND.AddCounter(METAL);

        LIGHTNING.AddCounter(EARTH);
        LIGHTNING.AddCounter(WOOD);

        ICE.AddCounter(FIRE);
        ICE.AddCounter(METAL);
    }
}
