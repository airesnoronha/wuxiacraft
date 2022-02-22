package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.*;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.activator.SkillActivatorAspect;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.activator.SkillTouchAspect;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit.SkillAttackAspect;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit.SkillExplosionAspect;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit.SkillHitAspect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class WuxiaSkillAspects {

	public static DeferredRegister<SkillAspectType> ASPECTS = DeferredRegister.create(SkillAspectType.class, WuxiaCraft.MOD_ID);

	/**
	 * Directly hit ahead the caster, where the caster is looking at
	 */
	public static RegistryObject<SkillAspectType> HIT_ASPECT = ASPECTS.register("hit_aspect", () -> SkillAspectType.build(SkillTouchAspect::new));

	/**
	 * Throw in the direction caster is looking at
	 */
	public static RegistryObject<SkillAspectType> THROW_ASPECT = ASPECTS.register("throw_aspect", () -> SkillAspectType.build(SkillActivatorAspect::new));

	/**
	 * Hits the caster
	 */
	public static RegistryObject<SkillAspectType> SELF_ASPECT = ASPECTS.register("self_aspect", () -> SkillAspectType.build(SkillActivatorAspect::new));

	/**
	 * Sword flight, when hit something, activates hit
	 */
	public static RegistryObject<SkillAspectType> SWORD_FLIGHT_ASPECT = ASPECTS.register("sword_flight_aspect", () -> SkillAspectType.build(SkillActivatorAspect::new));

	/**
	 * Cast a wave ahead of the caster, hitting everything ahead of it
	 */
	public static RegistryObject<SkillAspectType> WAVE_ASPECT = ASPECTS.register("wave_aspect", () -> SkillAspectType.build(SkillActivatorAspect::new));

	/**
	 * Hits in a spherical shape around the caster
	 */
	public static RegistryObject<SkillAspectType> BARRIER_ASPECT = ASPECTS.register("barrier_aspect", () -> SkillAspectType.build(SkillActivatorAspect::new));

	/**
	 * Similar to above, but only ahead of the caster
	 */
	public static RegistryObject<SkillAspectType> SHIELD_ASPECT = ASPECTS.register("shield_aspect", () -> SkillAspectType.build(SkillActivatorAspect::new));

	/**
	 * Hits everything around the caster
	 */
	public static RegistryObject<SkillAspectType> AREA_ASPECT = ASPECTS.register("area_aspect", () -> SkillAspectType.build(SkillActivatorAspect::new));

	/**
	 * Keeps releasing multiple throws
	 */
	public static RegistryObject<SkillAspectType> CHANNELING_ASPECT = ASPECTS.register("channeling_aspect", () -> SkillAspectType.build(SkillActivatorAspect::new));

	/**
	 * Hits everything around the caster
	 */
	public static RegistryObject<SkillAspectType> AREA_CHANNELING_ASPECT = ASPECTS.register("area_channeling_aspect", () -> SkillAspectType.build(SkillActivatorAspect::new));

	/**
	 * Hits the caster constantly
	 */
	public static RegistryObject<SkillAspectType> SELF_CHANNEL_ASPECT = ASPECTS.register("self_channel_aspect", () -> SkillAspectType.build(SkillActivatorAspect::new));

	/**
	 * hits an barrier around the caster constantly
	 */
	public static RegistryObject<SkillAspectType> BARRIER_CHANNELING_ASPECT = ASPECTS.register("barrier_channeling_aspect", () -> SkillAspectType.build(SkillActivatorAspect::new));

	//TODO key of kings'law activator from botania

	//TODO add professions based activator and modifiers

	/**
	 * Charges the skill before casting increasing values
	 */
	public static RegistryObject<SkillAspectType> CHARGE_MODIFIER_ASPECT = ASPECTS.register("charge_modifier_aspect", () -> SkillAspectType.build(SkillActivationModifierAspect::new));

	/**
	 * Similar to above, but only ahead of the caster
	 */
	public static RegistryObject<SkillAspectType> GRAVITY_MODIFIER_ASPECT = ASPECTS.register("gravity_modifier_aspect", () -> SkillAspectType.build(SkillActivationModifierAspect::new));

	/**
	 * Increases the range of activation for certain aspects
	 */
	public static RegistryObject<SkillAspectType> RANGE_MODIFIER_ASPECT = ASPECTS.register("range_modifier_aspect", () -> SkillAspectType.build(SkillActivationModifierAspect::new));

	/**
	 * Area of effect modifier (Activates around)
	 */
	public static RegistryObject<SkillAspectType> AREA_MODIFIER_ASPECT = ASPECTS.register("area_modifier_aspect", () -> SkillAspectType.build(SkillActivationModifierAspect::new));

	/**
	 * Similar to above, but only ahead of the caster
	 */
	public static RegistryObject<SkillAspectType> RADIUS_MODIFIERS = ASPECTS.register("radius_modifiers", () -> SkillAspectType.build(SkillActivationModifierAspect::new));

	// *********************************************
	//  On hit things, this is where the fun begins
	// ********************************************

	/**
	 * Creates an explosion on hit
	 */
	public static RegistryObject<SkillAspectType> EXPLOSION_ASPECT = ASPECTS.register("explosion_aspect", () -> SkillAspectType.build(SkillExplosionAspect::new));

	/**
	 * Directly dealing damage
	 */
	public static RegistryObject<SkillAspectType> ATTACK_ASPECT = ASPECTS.register("attack_aspect", () -> SkillAspectType.build(SkillAttackAspect::new));

	/**
	 * Fertilize plants
	 */
	public static RegistryObject<SkillAspectType> FERTILIZE_ASPECT = ASPECTS.register("fertilize_aspect", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Breaks blocks on hit
	 */
	public static RegistryObject<SkillAspectType> BREAK_ASPECT = ASPECTS.register("break_aspect", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Opens a portal on hit, continuing on the direction of the hit
	 */
	public static RegistryObject<SkillAspectType> SPACE_TEAR_ASPECT = ASPECTS.register("space_tear_aspect", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Generates a coffin of defined block modifier around hit place
	 */
	public static RegistryObject<SkillAspectType> COFFIN_ASPECT = ASPECTS.register("coffin_aspect", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Ore mine blocks on hit
	 */
	public static RegistryObject<SkillAspectType> ORE_MINE_ASPECT = ASPECTS.register("ore_mine_aspect", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Heals the target on hit
	 */
	public static RegistryObject<SkillAspectType> HEAL_ASPECT = ASPECTS.register("heal_aspect", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Drains life from target on hit
	 */
	public static RegistryObject<SkillAspectType> LIFE_STEAL_ASPECT = ASPECTS.register("life_steal_aspect", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Summons minions
	 */
	public static RegistryObject<SkillAspectType> SUMMON_MINIONS_ASPECT = ASPECTS.register("summon_minions_aspect", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Applies potion effects
	 */
	public static RegistryObject<SkillAspectType> POTION_EFFECT_ASPECT = ASPECTS.register("potion_effect_aspect", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Summons lightnings on the activation position
	 */
	public static RegistryObject<SkillAspectType> SUMMON_LIGHTNING_ASPECT = ASPECTS.register("summon_lightning_aspect", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Shares activation from the caster to the target
	 */
	public static RegistryObject<SkillAspectType> SHARE_CULTIVATION_ASPECT = ASPECTS.register("share_cultivation_aspect", () -> SkillAspectType.build(SkillHitAspect::new));
}
