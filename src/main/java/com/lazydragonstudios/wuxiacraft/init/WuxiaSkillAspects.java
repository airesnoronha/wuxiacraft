package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillStat;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.*;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.activator.*;
import com.lazydragonstudios.wuxiacraft.cultivation.skills.aspects.hit.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class WuxiaSkillAspects {

	public static DeferredRegister<SkillAspectType> ASPECTS = DeferredRegister.create(new ResourceLocation(WuxiaCraft.MOD_ID, "skill_aspects"), WuxiaCraft.MOD_ID);

	/**
	 * Directly hit ahead the caster, where the caster is looking at
	 */
	public static RegistryObject<SkillAspectType> PUNCH = ASPECTS.register("punch", () -> SkillAspectType.build(
			() -> new SkillTouchAspect()
					.setSkillStat(SkillStat.COST, new BigDecimal("0.1"))
					.setSkillStat(SkillStat.STRENGTH, new BigDecimal("1"))
					.setSkillStat(SkillStat.CAST_TIME, new BigDecimal("6"))
					.setSkillStat(SkillStat.COOLDOWN, new BigDecimal("2"))
	));

	/**
	 * Throw in the direction caster is looking at
	 */
	public static RegistryObject<SkillAspectType> SHOOT = ASPECTS.register("shoot", () -> SkillAspectType.build(
			() -> new SkillShootAspect()
					.setSkillStat(SkillStat.COST, new BigDecimal("1.5"))
					.setSkillStat(SkillStat.STRENGTH, new BigDecimal("0.7"))
					.setSkillStat(SkillStat.CAST_TIME, new BigDecimal("3"))
					.setSkillStat(SkillStat.COOLDOWN, new BigDecimal("2"))
	));

	/**
	 * Hits the caster
	 */
	public static RegistryObject<SkillAspectType> SELF = ASPECTS.register("self", () -> SkillAspectType.build(
			() -> new SkillSelfAspect()
					.setSkillStat(SkillStat.COST, new BigDecimal("1"))
					.setSkillStat(SkillStat.STRENGTH, new BigDecimal("1"))
					.setSkillStat(SkillStat.CAST_TIME, new BigDecimal("1"))
					.setSkillStat(SkillStat.COOLDOWN, new BigDecimal("1"))
	));

	/**
	 * Sword flight, when hit something, activates hit
	 */
	public static RegistryObject<SkillAspectType> SWORD_FLIGHT = ASPECTS.register("sword_flight", () -> SkillAspectType.build(
			() -> new SkillSwordFlightActivator()
					.setSkillStat(SkillStat.COST, new BigDecimal("0.35"))
					.setSkillStat(SkillStat.STRENGTH, new BigDecimal("1"))
					.setSkillStat(SkillStat.CAST_TIME, new BigDecimal("0"))
					.setSkillStat(SkillStat.COOLDOWN, new BigDecimal("0"))
	));

	/**
	 * Cast a wave ahead of the caster, hitting everything ahead of it
	 */
	public static RegistryObject<SkillAspectType> WAVE = ASPECTS.register("wave", () -> SkillAspectType.build(SkillActivatorAspect::new));

	/**
	 * Hits in a spherical shape around the caster
	 */
	public static RegistryObject<SkillAspectType> BARRIER = ASPECTS.register("barrier", () -> SkillAspectType.build(SkillActivatorAspect::new));

	/**
	 * Similar to above, but only ahead of the caster
	 */
	public static RegistryObject<SkillAspectType> SHIELD = ASPECTS.register("shield", () -> SkillAspectType.build(SkillActivatorAspect::new));

	/**
	 * Hits everything around the caster
	 */
	public static RegistryObject<SkillAspectType> AREA = ASPECTS.register("area", () -> SkillAspectType.build(SkillActivatorAspect::new));


	//TODO key of kings'law activator from botania

	//TODO add professions based activator and modifiers

	/**
	 * Keeps releasing multiple of the hit aspect
	 */
	public static RegistryObject<SkillAspectType> CHANNELING = ASPECTS.register("channeling", () -> SkillAspectType.build(SkillActivationModifierAspect::new));

	/**
	 * Charges the skill before casting increasing values
	 */
	public static RegistryObject<SkillAspectType> CHARGE = ASPECTS.register("charge", () -> SkillAspectType.build(SkillActivationModifierAspect::new));

	/**
	 * Similar to above, but only ahead of the caster
	 */
	public static RegistryObject<SkillAspectType> GRAVITY_MODIFIER = ASPECTS.register("gravity_modifier", () -> SkillAspectType.build(SkillActivationModifierAspect::new));

	/**
	 * Increases the range of activation for certain aspects
	 */
	public static RegistryObject<SkillAspectType> RANGE_MODIFIER = ASPECTS.register("range_modifier", () -> SkillAspectType.build(SkillActivationModifierAspect::new));

	/**
	 * Area of effect modifier (Activates around)
	 */
	public static RegistryObject<SkillAspectType> AREA_MODIFIER = ASPECTS.register("area_modifier", () -> SkillAspectType.build(SkillActivationModifierAspect::new));

	/**
	 * Similar to above, but only ahead of the caster
	 */
	public static RegistryObject<SkillAspectType> RADIUS_MODIFIER = ASPECTS.register("radius_modifier", () -> SkillAspectType.build(SkillActivationModifierAspect::new));

	// *********************************************
	//  On hit things, this is where the fun begins
	// ********************************************

	/**
	 * Creates an explosion on hit
	 */
	public static RegistryObject<SkillAspectType> EXPLOSION = ASPECTS.register("explosion", () -> SkillAspectType.build(
			() -> new SkillExplosionAspect()
					.setSkillStat(SkillStat.COST, new BigDecimal("4"))
					.setSkillStat(SkillStat.STRENGTH, new BigDecimal("1"))
					.setSkillStat(SkillStat.CAST_TIME, new BigDecimal("150"))
					.setSkillStat(SkillStat.COOLDOWN, new BigDecimal("100"))
	));

	/**
	 * Directly dealing damage
	 */
	public static RegistryObject<SkillAspectType> ATTACK = ASPECTS.register("attack", () -> SkillAspectType.build(
			() -> new SkillAttackAspect()
					.setSkillStat(SkillStat.COST, new BigDecimal("1.5"))
					.setSkillStat(SkillStat.STRENGTH, new BigDecimal("1"))
					.setSkillStat(SkillStat.CAST_TIME, new BigDecimal("20"))
					.setSkillStat(SkillStat.COOLDOWN, new BigDecimal("50"))
	));

	/**
	 * Fertilize plants
	 */
	public static RegistryObject<SkillAspectType> FERTILIZE = ASPECTS.register("fertilize", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Breaks blocks on hit
	 */
	public static RegistryObject<SkillAspectType> BREAK = ASPECTS.register("break", () -> SkillAspectType.build(
			() -> new SkillBreakAspect()
					.setSkillStat(SkillStat.COST, new BigDecimal("1"))
					.setSkillStat(SkillStat.STRENGTH, new BigDecimal("1"))
					.setSkillStat(SkillStat.CAST_TIME, new BigDecimal("60"))
					.setSkillStat(SkillStat.COOLDOWN, new BigDecimal("80"))
	));

	/**
	 * Chops a tree instantly
	 */
	public static RegistryObject<SkillAspectType> CHOP = ASPECTS.register("chop", () -> SkillAspectType.build(
			() -> new SkillChopAspect()
					.setSkillStat(SkillStat.COST, new BigDecimal("6"))
					.setSkillStat(SkillStat.STRENGTH, new BigDecimal("1"))
					.setSkillStat(SkillStat.CAST_TIME, new BigDecimal("120"))
					.setSkillStat(SkillStat.COOLDOWN, new BigDecimal("150"))
	));

	/**
	 * Opens a portal on hit, continuing on the direction of the hit
	 */
	public static RegistryObject<SkillAspectType> SPACE_TEAR = ASPECTS.register("space_tear", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Generates a coffin of defined block modifier around hit place
	 */
	public static RegistryObject<SkillAspectType> COFFIN = ASPECTS.register("coffin", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Ore mine blocks on hit
	 */
	public static RegistryObject<SkillAspectType> ORE_MINE = ASPECTS.register("ore_mine", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Heals the target on hit
	 */
	public static RegistryObject<SkillAspectType> HEAL = ASPECTS.register("heal", () -> SkillAspectType.build(
			() -> new SkillHealAspect()
					.setSkillStat(SkillStat.COST, new BigDecimal("4"))
					.setSkillStat(SkillStat.STRENGTH, new BigDecimal("1"))
					.setSkillStat(SkillStat.CAST_TIME, new BigDecimal("90"))
					.setSkillStat(SkillStat.COOLDOWN, new BigDecimal("60"))
	));

	/**
	 * Drains life from target on hit
	 */
	public static RegistryObject<SkillAspectType> LIFE_STEAL = ASPECTS.register("life_steal", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Summons minions
	 */
	public static RegistryObject<SkillAspectType> SUMMON_MINIONS = ASPECTS.register("summon_minions", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Applies potion effects
	 */
	public static RegistryObject<SkillAspectType> POTION_EFFECT = ASPECTS.register("potion_effect", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Summons lightnings on the activation position
	 */
	public static RegistryObject<SkillAspectType> SUMMON_LIGHTNING = ASPECTS.register("summon_lightning", () -> SkillAspectType.build(SkillHitAspect::new));

	/**
	 * Shares activation from the caster to the target
	 */
	public static RegistryObject<SkillAspectType> SHARE_CULTIVATION = ASPECTS.register("share_cultivation", () -> SkillAspectType.build(SkillHitAspect::new));
}
