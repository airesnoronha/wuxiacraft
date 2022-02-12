package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillActivationModifierAspect;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillActivatorAspect;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillAspect;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.SkillHitAspect;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.HashMap;

@SuppressWarnings("unused")
public class WuxiaSkillAspects {

	public static DeferredRegister<SkillAspect> ASPECTS = DeferredRegister.create(SkillAspect.class, WuxiaCraft.MOD_ID);

	/**
	 * Directly hit ahead the caster, where the caster is looking at
	 */
	public static RegistryObject<SkillAspect> HIT_ASPECT = ASPECTS.register("hit_aspect", () -> new SkillActivatorAspect("hit_aspect"));

	/**
	 * Throw in the direction caster is looking at
	 */
	public static RegistryObject<SkillAspect> THROW_ASPECT = ASPECTS.register("throw_aspect", () -> new SkillActivatorAspect("throw_aspect"));

	/**
	 * Hits the caster
	 */
	public static RegistryObject<SkillAspect> SELF_ASPECT = ASPECTS.register("self_aspect", () -> new SkillActivatorAspect("self_aspect"));

	/**
	 * Sword flight, when hit something, activates hit
	 */
	public static RegistryObject<SkillAspect> SWORD_FLIGHT_ASPECT = ASPECTS.register("sword_flight_aspect", () -> new SkillActivatorAspect("sword_flight_aspect"));

	/**
	 * Keeps releasing multiple throws
	 */
	public static RegistryObject<SkillAspect> CHANNELING_ASPECT = ASPECTS.register("sword_flight_aspect", () -> new SkillActivatorAspect("sword_flight_aspect"));

	/**
	 * Cast a wave ahead of the caster, hitting everything ahead of it
	 */
	public static RegistryObject<SkillAspect> WAVE_ASPECT = ASPECTS.register("sword_flight_aspect", () -> new SkillActivatorAspect("sword_flight_aspect"));

	/**
	 * Hits in a spherical shape around the caster
	 */
	public static RegistryObject<SkillAspect> BARRIER_ASPECT = ASPECTS.register("barrier_flight_aspect", () -> new SkillActivatorAspect("sword_flight_aspect"));

	/**
	 * Similar to above, but only ahead of the caster
	 */
	public static RegistryObject<SkillAspect> SHIELD_ASPECT = ASPECTS.register("barrier_flight_aspect", () -> new SkillActivatorAspect("sword_flight_aspect"));

	/**
	 * Hits everything around the caster
	 */
	public static RegistryObject<SkillAspect> AREA_ASPECT = ASPECTS.register("barrier_flight_aspect", () -> new SkillActivatorAspect("sword_flight_aspect"));

	/**
	 * Hits everything around the caster
	 */
	public static RegistryObject<SkillAspect> AREA_CHANNELING_ASPECT = ASPECTS.register("barrier_flight_aspect", () -> new SkillActivatorAspect("sword_flight_aspect"));

	/**
	 * Hits the caster
	 */
	public static RegistryObject<SkillAspect> SELF_CHANNEL_ASPECT = ASPECTS.register("self_aspect", () -> new SkillActivatorAspect("self_aspect"));

	/**
	 * Charges the skill before casting
	 */
	public static RegistryObject<SkillAspect> CHARGE_MODIFIER_ASPECT = ASPECTS.register("self_aspect", () -> new SkillActivationModifierAspect("charge_modifier_aspect"));

	//TODO key of kings'law activator from botania

	//TODO add professions based activator and modifiers

	/**
	 * Similar to above, but only ahead of the caster
	 */
	public static RegistryObject<SkillAspect> GRAVITY_MODIFIER_ASPECT = ASPECTS.register("gravity_modifier_aspect", () -> new SkillActivationModifierAspect("gravity_modifier_aspect"));

	/**
	 * Similar to above, but only ahead of the caster
	 */
	public static RegistryObject<SkillAspect> RANGE_MODIFIER_ASPECT = ASPECTS.register("range_aspect", () -> new SkillActivationModifierAspect("range_aspect"));

	/**
	 * Area of effect modifier (Activates around)
	 */
	public static RegistryObject<SkillAspect> AREA_MODIFIER_ASPECT = ASPECTS.register("range_aspect", () -> new SkillActivationModifierAspect("range_aspect"));

	/**
	 * Similar to above, but only ahead of the caster
	 */
	public static RegistryObject<SkillAspect> RADIUS_MODIFIERS = ASPECTS.register("gravity_modifier_aspect", () -> new SkillActivationModifierAspect("gravity_modifier_aspect"));

	/**
	 * Creates an explosion on hit
	 */
	public static RegistryObject<SkillAspect> EXPLOSION_ASPECT = ASPECTS.register("explosion_aspect", () -> new SkillHitAspect("explosion_aspect"));

	/**
	 * Directly dealing damage
	 */
	public static RegistryObject<SkillAspect> ATTACK_ASPECT = ASPECTS.register("explosion_aspect", () -> new SkillHitAspect("explosion_aspect"));

	/**
	 * Fertilize plants
	 */
	public static RegistryObject<SkillAspect> FERTILIZE_ASPECT = ASPECTS.register("explosion_aspect", () -> new SkillHitAspect("explosion_aspect"));

	/**
	 * Breaks blocks on hit
	 */
	public static RegistryObject<SkillAspect> BREAK_ASPECT = ASPECTS.register("explosion_aspect", () -> new SkillHitAspect("explosion_aspect"));

	/**
	 * Opens a portal on hit, continuing on the direction of the hit
	 */
	public static RegistryObject<SkillAspect> SPACE_TEAR_ASPECT = ASPECTS.register("space_tear_aspect", () -> new SkillHitAspect("space_tear_aspect"));

	/**
	 * Generates a coffin of defined block modifier around hit place
	 */
	public static RegistryObject<SkillAspect> COFFIN_ASPECT = ASPECTS.register("space_tear_aspect", () -> new SkillHitAspect("space_tear_aspect"));

	/**
	 * Ore mine blocks on hit
	 */
	public static RegistryObject<SkillAspect> ORE_MINE_ASPECT = ASPECTS.register("space_tear_aspect", () -> new SkillHitAspect("space_tear_aspect"));

	/**
	 * Heals the target on hit
	 */
	public static RegistryObject<SkillAspect> HEAL_ASPECT = ASPECTS.register("space_tear_aspect", () -> new SkillHitAspect("space_tear_aspect"));

	/**
	 * Drains life from target on hit
	 */
	public static RegistryObject<SkillAspect> LIFE_STEAL_ASPECT = ASPECTS.register("space_tear_aspect", () -> new SkillHitAspect("space_tear_aspect"));

	/**
	 * Summons minions
	 */
	public static RegistryObject<SkillAspect> SUMMON_MINIONS_ASPECT = ASPECTS.register("summons_minions_aspect", () -> new SkillHitAspect("summon_minions_aspect"));

	/**
	 * Applies potion effects
	 */
	public static RegistryObject<SkillAspect> POTION_EFFECT_ASPECT = ASPECTS.register("potion_effect_aspect", () -> new SkillHitAspect("potion_effect_aspect"));

	/**
	 * Summons lightnings on the activation position
	 */
	public static RegistryObject<SkillAspect> SUMMON_LIGHTNING_ASPECT = ASPECTS.register("summon_lightning_aspect", () -> new SkillHitAspect("summon_lightning_aspect"));

	/**
	 * Shares activation from the caster to the target
	 */
	public static RegistryObject<SkillAspect> SHARE_CULTIVATION_ASPECT = ASPECTS.register("share_cultivation_aspect", () -> new SkillHitAspect("share_cultivation_aspect"));

	/**
	 * Elemental modifiers
	 */
	public static HashMap<ResourceLocation, RegistryObject<SkillAspect>> ELEMENTAL_MODIFIERS = new HashMap<>();

	/**
	 * Potion Effects to be Applied to PPL
	 */
	public static HashMap<ResourceLocation, RegistryObject<SkillAspect>> MOB_EFFECT_MODIFIERS = new HashMap<>();

	/**
	 * Blocks modifiers, I guess you can create blocks with it
	 * Should be used with care
	 */
	public static HashMap<ResourceLocation, RegistryObject<SkillAspect>> BLOCKS_MODIFIERS = new HashMap<>();
}
