package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.cultivation.CultivationRealm;
import com.lazydragonstudios.wuxiacraft.cultivation.CultivationStage;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.math.BigDecimal;

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

	public static RegistryObject<CultivationRealm> ESSENCE_CONSOLIDATION_REALM = REALM_REGISTER
			.register("essence_consolidation_realm",
					() -> new CultivationRealm("essence_consolidation_realm",
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
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("1000")) //1k
			);

	public static RegistryObject<CultivationStage> ESSENCE_QI_GATHERING_STAGE = STAGE_REGISTER
			.register("essence_qi_gathering_stage",
					() -> new CultivationStage(
							System.ESSENCE,
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_gathering_realm"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_mortal_realm"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_pathways_stage")
					)
							.addPlayerStat(PlayerStat.MAX_HEALTH, new BigDecimal("3"))
							.addPlayerStat(PlayerStat.STRENGTH, new BigDecimal("1.5"))
							.addPlayerStat(PlayerStat.AGILITY, new BigDecimal("0.1"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("5000")) //5k
							.addSystemStat(System.ESSENCE, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.001"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.CULTIVATION_SPEED, new BigDecimal("0.01"))
							.addSystemStat(System.BODY, PlayerSystemStat.MAX_ENERGY, new BigDecimal("1"))
							.addSystemStat(System.DIVINE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("1"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("3"))

			);

	public static RegistryObject<CultivationStage> ESSENCE_QI_PATHWAYS_STAGE = STAGE_REGISTER
			.register("essence_qi_pathways_stage",
					() -> new CultivationStage(
							System.ESSENCE,
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_gathering_realm"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_gathering_stage"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_condensation_stage")
					)
							.addPlayerStat(PlayerStat.MAX_HEALTH, new BigDecimal("4"))
							.addPlayerStat(PlayerStat.STRENGTH, new BigDecimal("2"))
							.addPlayerStat(PlayerStat.AGILITY, new BigDecimal("0.1"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("15000")) //20k
							.addSystemStat(System.ESSENCE, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.001"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.CULTIVATION_SPEED, new BigDecimal("0.005"))
							.addSystemStat(System.BODY, PlayerSystemStat.MAX_ENERGY, new BigDecimal("1.5"))
							.addSystemStat(System.DIVINE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("1.5"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("4"))
			);

	public static RegistryObject<CultivationStage> ESSENCE_QI_CONDENSATION_STAGE = STAGE_REGISTER
			.register("essence_qi_condensation_stage",
					() -> new CultivationStage(
							System.ESSENCE,
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_gathering_realm"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_pathways_stage"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_phenomenon_stage")
					)
							.addPlayerStat(PlayerStat.MAX_HEALTH, new BigDecimal("5"))
							.addPlayerStat(PlayerStat.STRENGTH, new BigDecimal("2.5"))
							.addPlayerStat(PlayerStat.AGILITY, new BigDecimal("0.2"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("20000")) //45k
							.addSystemStat(System.ESSENCE, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.0015"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.CULTIVATION_SPEED, new BigDecimal("0.005"))
							.addSystemStat(System.BODY, PlayerSystemStat.MAX_ENERGY, new BigDecimal("2"))
							.addSystemStat(System.DIVINE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("2"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("5"))
			);

	public static RegistryObject<CultivationStage> ESSENCE_QI_PHENOMENON_STAGE = STAGE_REGISTER
			.register("essence_qi_phenomenon_stage",
					() -> new CultivationStage(
							System.ESSENCE,
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_gathering_realm"),
							new ResourceLocation(WuxiaCraft.MOD_ID, "essence_qi_condensation_stage"),
							null
					)
							.addPlayerStat(PlayerStat.MAX_HEALTH, new BigDecimal("7"))
							.addPlayerStat(PlayerStat.STRENGTH, new BigDecimal("4"))
							.addPlayerStat(PlayerStat.AGILITY, new BigDecimal("0.4"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_CULTIVATION_BASE, new BigDecimal("55000")) //100k
							.addSystemStat(System.ESSENCE, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.002"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.CULTIVATION_SPEED, new BigDecimal("0.015"))
							.addSystemStat(System.BODY, PlayerSystemStat.MAX_ENERGY, new BigDecimal("3"))
							.addSystemStat(System.BODY, PlayerSystemStat.ENERGY_REGEN, new BigDecimal("0.01"))
							.addSystemStat(System.DIVINE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("3"))
							.addSystemStat(System.ESSENCE, PlayerSystemStat.MAX_ENERGY, new BigDecimal("7"))
			);
}
