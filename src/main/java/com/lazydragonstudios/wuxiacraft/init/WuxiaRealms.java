package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.CultivationRealm;
import com.lazydragonstudios.wuxiacraft.cultivation.CultivationStage;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerElementalStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.math.BigDecimal;
import java.util.function.Consumer;

@SuppressWarnings("unused")
public class WuxiaRealms {

	public static DeferredRegister<CultivationRealm> REALM_REGISTER = DeferredRegister.create(CultivationRealm.class, WuxiaCraft.MOD_ID);
	public static DeferredRegister<CultivationStage> STAGE_REGISTER = DeferredRegister.create(CultivationStage.class, WuxiaCraft.MOD_ID);

	//************************************
	// body realms
	//************************************

	public static RegistryObject<CultivationRealm> BODY_MORTAL_REALM = REALM_REGISTER
			.register("body_mortal_realm",
					() -> new CultivationRealm("body_mortal_realm",
							System.BODY
					));

	//************************************
	// divine realms
	//************************************

	public static RegistryObject<CultivationRealm> DIVINE_MORTAL_REALM = REALM_REGISTER
			.register("divine_mortal_realm",
					() -> new CultivationRealm("divine_mortal_realm",
							System.DIVINE
					));

	//************************************
	// essence realms
	//************************************

	public static RegistryObject<CultivationRealm> ESSENCE_MORTAL_REALM = REALM_REGISTER
			.register("essence_mortal_realm",
					() -> new CultivationRealm("essence_mortal_realm",
							System.ESSENCE
					));

	public static RegistryObject<CultivationRealm> ESSENCE_GATHERING_REALM = REALM_REGISTER
			.register("essence_gathering_realm",
					() -> new CultivationRealm("essence_gathering_realm",
							System.ESSENCE
					));

	public static RegistryObject<CultivationRealm> FOUNDATION_ESTABLISHMENT_REALM = REALM_REGISTER
			.register("foundation_establishment",
					() -> new CultivationRealm("foundation_establishment_realm",
							System.ESSENCE
					));

	public static RegistryObject<CultivationRealm> ESSENCE_REVOLVING_CORE_REALM = REALM_REGISTER
			.register("essence_revolving_core_realm",
					() -> new CultivationRealm("essence_revolving_core_realm",
							System.ESSENCE
					));

	public static RegistryObject<CultivationRealm> ESSENCE_IMMORTAL_SEA_REALM = REALM_REGISTER
			.register("essence_immortal_sea_realm",
					() -> new CultivationRealm("essence_immortal_sea_realm",
							System.ESSENCE
					));

	public static RegistryObject<CultivationRealm> ESSENCE_IMMORTAL_TRANSFORMATION_REALM = REALM_REGISTER
			.register("essence_immortal_transformation_realm",
					() -> new CultivationRealm("essence_immortal_transformation_realm",
							System.ESSENCE
					));

	//*********************************
	// body stages
	//*********************************

	public static RegistryObject<CultivationStage> BODY_MORTAL_STAGE = STAGE_REGISTER
			.register("body_mortal_stage",
					() -> new CultivationStage(
							System.BODY,
							new ResourceLocation(WuxiaCraft.MOD_ID, "body_mortal_realm"),
							null, null
					)
							.addSystemStat(System.BODY, PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("100"))
			);

	//*********************************
	// divine stages
	//*********************************

	public static RegistryObject<CultivationStage> DIVINE_MORTAL_STAGE = STAGE_REGISTER
			.register("divine_mortal_stage",
					() -> new CultivationStage(
							System.DIVINE,
							new ResourceLocation(WuxiaCraft.MOD_ID, "divine_mortal_realm"),
							null, null
					)
							.addSystemStat(System.DIVINE, PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("100"))
			);

	//*********************************
	// essence stages
	//*********************************

	public static RegistryObject<CultivationStage> ESSENCE_MORTAL_STAGE = STAGE_REGISTER
			.register("essence_mortal_stage",
					() -> new CultivationStage(
							System.ESSENCE,
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_mortal_realm"),
							null,
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_gathering_stage")
					)
							.setOnCultivate(cultivateFlatAmounts(BigDecimal.ONE, BigDecimal.ONE))
							.setOnCultivationFailure(cultivateFailureEnergy(BigDecimal.ONE))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("1000")) //1k
			);

	public static RegistryObject<CultivationStage> ESSENCE_QI_GATHERING_STAGE = STAGE_REGISTER
			.register("essence_qi_gathering_stage",
					() -> new CultivationStage(
							System.ESSENCE,
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_gathering_realm"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_mortal_stage"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_pathways_stage")
					)
							.setOnCultivate(cultivateFlatAmounts(new BigDecimal("2.5"), BigDecimal.ONE))
							.setOnCultivationFailure(cultivateFailureEnergy(new BigDecimal("2.5")))
							.addPlayerStat(PlayerStat.MAX_HEALTH, new BigDecimal("4"))
							.addPlayerStat(PlayerStat.STRENGTH, new BigDecimal("2"))
							.addPlayerStat(PlayerStat.AGILITY, new BigDecimal("0.01"))
							.addPlayerStat(PlayerStat.DETECTION_RANGE, new BigDecimal("1"))
							.addPlayerStat(PlayerStat.DETECTION_STRENGTH, new BigDecimal("0.5"))
							.addPlayerStat(PlayerStat.DETECTION_RESISTANCE, new BigDecimal("0.1"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN, new BigDecimal("0.001"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN_COST, new BigDecimal("0.001"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("4000"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.CULTIVATION_SPEED, new BigDecimal("0.01"))
							.addSystemStat(System.BODY, PlayerSystemStat.MAX_ENERGY, new BigDecimal("1"))
							.addSystemStat(System.DIVINE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("1"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("3"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.RESISTANCE, new BigDecimal("0"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.PIERCE, new BigDecimal("3"))
							.addSkill(WuxiaSkillAspects.PUNCH.getId())
							.addSkill(WuxiaSkillAspects.SELF.getId())
							.addSkill(WuxiaSkillAspects.ATTACK.getId())

			);

	public static RegistryObject<CultivationStage> ESSENCE_QI_PATHWAYS_STAGE = STAGE_REGISTER
			.register("essence_qi_pathways_stage",
					() -> new CultivationStage(
							System.ESSENCE,
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_gathering_realm"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_gathering_stage"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_condensation_stage")
					)
							.setOnCultivate(cultivateFlatAmounts(new BigDecimal("4"), BigDecimal.ONE))
							.setOnCultivationFailure(cultivateFailureEnergy(new BigDecimal("4")))
							.addPlayerStat(PlayerStat.MAX_HEALTH, new BigDecimal("1"))
							.addPlayerStat(PlayerStat.STRENGTH, new BigDecimal("0"))
							.addPlayerStat(PlayerStat.AGILITY, new BigDecimal("0.01"))
							.addPlayerStat(PlayerStat.DETECTION_RANGE, new BigDecimal("2"))
							.addPlayerStat(PlayerStat.DETECTION_STRENGTH, new BigDecimal("0.6"))
							.addPlayerStat(PlayerStat.DETECTION_RESISTANCE, new BigDecimal("0.2"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN, new BigDecimal("0.001"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN_COST, new BigDecimal("0.002"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("6000")) //20k
							.addSystemStat(System.ESSENCE, PlayerSystemStat.CULTIVATION_SPEED, new BigDecimal("0.005"))
							.addSystemStat(System.BODY, PlayerSystemStat.MAX_ENERGY, new BigDecimal("1.5"))
							.addSystemStat(System.DIVINE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("1.5"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("4"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.RESISTANCE, new BigDecimal("1"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.PIERCE, new BigDecimal("1"))
							.addSkill(WuxiaSkillAspects.BREAK.getId())
			);

	public static RegistryObject<CultivationStage> ESSENCE_QI_CONDENSATION_STAGE = STAGE_REGISTER
			.register("essence_qi_condensation_stage",
					() -> new CultivationStage(
							System.ESSENCE,
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_gathering_realm"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_pathways_stage"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_phenomenon_stage")
					)
							.setOnCultivate(cultivateFlatAmounts(new BigDecimal("4"), new BigDecimal("2.5")))
							.setOnCultivationFailure(cultivateFailureEnergy(new BigDecimal("4")))
							.addPlayerStat(PlayerStat.MAX_HEALTH, new BigDecimal("2"))
							.addPlayerStat(PlayerStat.STRENGTH, new BigDecimal("1"))
							.addPlayerStat(PlayerStat.AGILITY, new BigDecimal("0.01"))
							.addPlayerStat(PlayerStat.DETECTION_RANGE, new BigDecimal("3"))
							.addPlayerStat(PlayerStat.DETECTION_STRENGTH, new BigDecimal("0.7"))
							.addPlayerStat(PlayerStat.DETECTION_RESISTANCE, new BigDecimal("0.3"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN, new BigDecimal("0.001"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN_COST, new BigDecimal("0.003"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("9000"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.CULTIVATION_SPEED, new BigDecimal("0.001"))
							.addSystemStat(System.BODY, PlayerSystemStat.MAX_ENERGY, new BigDecimal("2"))
							.addSystemStat(System.DIVINE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("2"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("5"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.01"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.RESISTANCE, new BigDecimal("1"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.PIERCE, new BigDecimal("1"))
							.addSkill(WuxiaSkillAspects.CHOP.getId())
			);

	public static RegistryObject<CultivationStage> ESSENCE_QI_PHENOMENON_STAGE = STAGE_REGISTER
			.register("essence_qi_phenomenon_stage",
					() -> new CultivationStage(
							System.ESSENCE,
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_gathering_realm"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_condensation_stage"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_shaping_stage")
					)
							.addPlayerStat(PlayerStat.MAX_HEALTH, new BigDecimal("3"))
							.addPlayerStat(PlayerStat.STRENGTH, new BigDecimal("2"))
							.addPlayerStat(PlayerStat.AGILITY, new BigDecimal("0.02"))
							.addPlayerStat(PlayerStat.DETECTION_RANGE, new BigDecimal("5"))
							.addPlayerStat(PlayerStat.DETECTION_STRENGTH, new BigDecimal("0.8"))
							.addPlayerStat(PlayerStat.DETECTION_RESISTANCE, new BigDecimal("0.4"))
							.addPlayerStat(PlayerStat.MAX_BARRIER, new BigDecimal("5"))
							.addPlayerStat(PlayerStat.BARRIER_REGEN, new BigDecimal("0.01"))
							.addPlayerStat(PlayerStat.BARRIER_REGEN_COST, new BigDecimal("0.03"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN, new BigDecimal("0.002"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN_COST, new BigDecimal("0.004"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("15000"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.CULTIVATION_SPEED, new BigDecimal("0.015"))
							.addSystemStat(System.BODY, PlayerSystemStat.MAX_ENERGY, new BigDecimal("2.5"))
							.addSystemStat(System.BODY, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.001"))
							.addSystemStat(System.DIVINE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("2.5"))
							.addSystemStat(System.DIVINE, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.001"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("8"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.002"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.ADDITIONAL_GRID_RADIUS, new BigDecimal("1"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.RESISTANCE, new BigDecimal("1"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.PIERCE, new BigDecimal("1"))
							.addSkill(WuxiaSkillAspects.SHOOT.getId())
			);

	//Change realms -> big leap in stats
	public static RegistryObject<CultivationStage> ESSENCE_QI_SHAPING_STAGE = STAGE_REGISTER
			.register("essence_qi_shaping_stage",
					() -> new CultivationStage(
							System.ESSENCE,
							new ResourceLocation(WuxiaCraft.MOD_ID, "foundation_establishment_realm"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_phenomenon_stage"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_solidification_stage")
					)
							.addPlayerStat(PlayerStat.MAX_HEALTH, new BigDecimal("15"))
							.addPlayerStat(PlayerStat.STRENGTH, new BigDecimal("4"))
							.addPlayerStat(PlayerStat.AGILITY, new BigDecimal("0.08"))
							.addPlayerStat(PlayerStat.DETECTION_STRENGTH, new BigDecimal("1"))
							.addPlayerStat(PlayerStat.DETECTION_RANGE, new BigDecimal("5"))
							.addPlayerStat(PlayerStat.MAX_BARRIER, new BigDecimal("6"))
							.addPlayerStat(PlayerStat.BARRIER_REGEN, new BigDecimal("0.03"))
							.addPlayerStat(PlayerStat.BARRIER_REGEN_COST, new BigDecimal("0.07"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN, new BigDecimal("0.008"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN_COST, new BigDecimal("0.015"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("25000"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.CULTIVATION_SPEED, new BigDecimal("0.2"))
							.addSystemStat(System.BODY, PlayerSystemStat.MAX_ENERGY, new BigDecimal("6"))
							.addSystemStat(System.BODY, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.005"))
							.addSystemStat(System.DIVINE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("6"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("15"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.005"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.RESISTANCE, new BigDecimal("3"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.PIERCE, new BigDecimal("4"))
			);

	public static RegistryObject<CultivationStage> ESSENCE_QI_MOLDING_STAGE = STAGE_REGISTER
			.register("essence_qi_molding_stage",
					() -> new CultivationStage(
							System.ESSENCE,
							new ResourceLocation(WuxiaCraft.MOD_ID, "foundation_establishment_realm"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_shaping_stage"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_solidification_stage")
					)
							.addPlayerStat(PlayerStat.MAX_HEALTH, new BigDecimal("7"))
							.addPlayerStat(PlayerStat.STRENGTH, new BigDecimal("1"))
							.addPlayerStat(PlayerStat.AGILITY, new BigDecimal("0.03"))
							.addPlayerStat(PlayerStat.MAX_BARRIER, new BigDecimal("8"))
							.addPlayerStat(PlayerStat.BARRIER_REGEN, new BigDecimal("0.04"))
							.addPlayerStat(PlayerStat.BARRIER_REGEN_COST, new BigDecimal("0.09"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN, new BigDecimal("0.003"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN_COST, new BigDecimal("0.007"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("100000"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.CULTIVATION_SPEED, new BigDecimal("0.2"))
							.addSystemStat(System.BODY, PlayerSystemStat.MAX_ENERGY, new BigDecimal("6"))
							.addSystemStat(System.BODY, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.02"))
							.addSystemStat(System.DIVINE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("6"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("15"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.007"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.RESISTANCE, new BigDecimal("1"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.PIERCE, new BigDecimal("1"))
			);

	public static RegistryObject<CultivationStage> ESSENCE_QI_SOLIDIFICATION_STAGE = STAGE_REGISTER
			.register("essence_qi_solidification_stage",
					() -> new CultivationStage(
							System.ESSENCE,
							new ResourceLocation(WuxiaCraft.MOD_ID, "foundation_establishment_realm"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_molding_stage"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_core_shaping_stage")
					)
							.addPlayerStat(PlayerStat.MAX_HEALTH, new BigDecimal("7"))
							.addPlayerStat(PlayerStat.STRENGTH, new BigDecimal("2"))
							.addPlayerStat(PlayerStat.AGILITY, new BigDecimal("0.04"))
							.addPlayerStat(PlayerStat.MAX_BARRIER, new BigDecimal("11"))
							.addPlayerStat(PlayerStat.BARRIER_REGEN, new BigDecimal("0.05"))
							.addPlayerStat(PlayerStat.BARRIER_REGEN_COST, new BigDecimal("0.11"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN, new BigDecimal("0.004"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN_COST, new BigDecimal("0.009"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("100000"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.CULTIVATION_SPEED, new BigDecimal("0.4"))
							.addSystemStat(System.BODY, PlayerSystemStat.MAX_ENERGY, new BigDecimal("10"))
							.addSystemStat(System.BODY, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.03"))
							.addSystemStat(System.DIVINE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("10"))
							.addSystemStat(System.DIVINE, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.03"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("25"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.012"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.RESISTANCE, new BigDecimal("1"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.PIERCE, new BigDecimal("2"))
			);

	public static RegistryObject<CultivationStage> ESSENCE_CORE_SHAPING_STAGE = STAGE_REGISTER
			.register("essence_core_shaping_stage",
					() -> new CultivationStage(
							System.ESSENCE,
							new ResourceLocation(WuxiaCraft.MOD_ID, "foundation_establishment_realm"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_solidification_stage"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_golden_core_stage")
					)
							.addPlayerStat(PlayerStat.MAX_HEALTH, new BigDecimal("8"))
							.addPlayerStat(PlayerStat.STRENGTH, new BigDecimal("2"))
							.addPlayerStat(PlayerStat.AGILITY, new BigDecimal("0.04"))
							.addPlayerStat(PlayerStat.MAX_BARRIER, new BigDecimal("15"))
							.addPlayerStat(PlayerStat.BARRIER_REGEN, new BigDecimal("0.07"))
							.addPlayerStat(PlayerStat.BARRIER_REGEN_COST, new BigDecimal("0.18"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN, new BigDecimal("0.004"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN_COST, new BigDecimal("0.009"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("100000"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.CULTIVATION_SPEED, new BigDecimal("0.8"))
							.addSystemStat(System.BODY, PlayerSystemStat.MAX_ENERGY, new BigDecimal("15"))
							.addSystemStat(System.BODY, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.05"))
							.addSystemStat(System.DIVINE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("15"))
							.addSystemStat(System.DIVINE, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.05"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("42"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.023"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.ADDITIONAL_GRID_RADIUS, new BigDecimal("1"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.RESISTANCE, new BigDecimal("1"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.PIERCE, new BigDecimal("4"))
			);
	//TODO finish golden core stage
	public static RegistryObject<CultivationStage> ESSENCE_GOLDEN_CORE_STAGE = STAGE_REGISTER
			.register("essence_golden_core_stage",
					() -> new CultivationStage(
							System.ESSENCE,
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_revolving_core_realm"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_core_shaping_stage"),
							null
					)
							.addPlayerStat(PlayerStat.MAX_HEALTH, new BigDecimal("33"))
							.addPlayerStat(PlayerStat.STRENGTH, new BigDecimal("6"))
							.addPlayerStat(PlayerStat.AGILITY, new BigDecimal("0.04"))
							.addPlayerStat(PlayerStat.MAX_BARRIER, new BigDecimal("15"))
							.addPlayerStat(PlayerStat.BARRIER_REGEN, new BigDecimal("0.07"))
							.addPlayerStat(PlayerStat.BARRIER_REGEN_COST, new BigDecimal("0.18"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN, new BigDecimal("0.004"))
							.addPlayerStat(PlayerStat.HEALTH_REGEN_COST, new BigDecimal("0.009"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("100000"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.CULTIVATION_SPEED, new BigDecimal("0.8"))
							.addSystemStat(System.BODY, PlayerSystemStat.MAX_ENERGY, new BigDecimal("15"))
							.addSystemStat(System.BODY, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.05"))
							.addSystemStat(System.DIVINE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("15"))
							.addSystemStat(System.DIVINE, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.05"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("42"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.023"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.RESISTANCE, new BigDecimal("6"))
							.addElementalStat(WuxiaElements.PHYSICAL.getId(), PlayerElementalStat.PIERCE, new BigDecimal("10"))
			);

	public static Consumer<Player> cultivateFlatAmounts(BigDecimal energy, BigDecimal cultivationBase) {
		return player -> {
			var cultivation = Cultivation.get(player);
			var essenceData = cultivation.getSystemData(System.ESSENCE);
			if (essenceData.consumeEnergy(energy)) {
				cultivation.addCultivationBase(player, System.ESSENCE, cultivationBase);
			}
		};
	}

	public static Consumer<Player> cultivateFailureEnergy(BigDecimal energy) {
		return player -> {
			var cultivation = Cultivation.get(player);
			var essenceData = cultivation.getSystemData(System.ESSENCE);
			essenceData.consumeEnergy(energy);
		};
	}

}
