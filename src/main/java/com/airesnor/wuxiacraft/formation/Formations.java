package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.world.dimensions.WuxiaDimensions;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Formations {

	public static final List<Formation> FORMATIONS = new ArrayList<>();

	//passive generation
	public static final Formation WEAK_QI_GATHERING_ARRAY = new FormationQiGathering("weak_qi_gathering_array", 0.12f, 32f);
	public static final Formation FIVE_ELEMENTS_GATHERING_ARRAY = new FormationQiGathering("five_elements_gathering_array", 0.46f, 48f);
	public static final Formation YIN_YANG_GATHERING_ARRAY = new FormationQiGathering("yin_yang_gathering_array", 1.2f, 64f);
	public static final Formation NATURE_SPIRIT_GATHERING_ARRAY = new FormationQiGathering("nature_spirit_gathering_array", 4.5, 72f);
	public static final Formation GOLDEN_YANG_QI_ARRAY = new FormationQiGathering("golden_yang_qi_array", 12.2, 72f);
	public static final Formation HEAVENLY_QI_BORROW_ARRAY = new FormationQiGathering("heavenly_qi_borrow_array", 36.6, 72f);

	//furnace powering
	public static final Formation FURNACE_POWERING_FORMATION = new FormationFurnacePower("furnace_powering_formation", 15f, 100f, 16f);

	//barriers
	public static final Formation SOUL_LAND_BARRIER = new FormationPlayerBarrier("soul_land_barrier", 7f, 1000f, 128f, 12.75f);
	public static final Formation FOREIGN_QI_BARRIER = new FormationPlayerBarrier("foreign_qi_barrier", 24.2f, 1000f, 156f, 50.3125f);
	public static final Formation EARTHLY_BLOCKADE = new FormationPlayerBarrier("earthly_blockade", 103.52f, 1000f, 184f, 236.9);
	public static final Formation FIRMAMENT_PROTECTION = new FormationPlayerBarrier("firmament_protection", 389.25f, 1000f, 202f, 958.2525);
	public static final Formation LAW_CONDENSING_BARRIER = new FormationPlayerBarrier("law_condensing_barrier", 1654.8f, 1000f, 232f, 4263.075); //don't have diagram
	public static final Formation HEAVENLY_DOME_RECREATION = new FormationPlayerBarrier("heavenly_dome_recreation", 8167.53f, 1000f, 256f, 22382.925); //don't have diagram

	//mob suppression
	public static final Formation WEAK_MOB_SUPPRESSION_ARRAY = new FormationMobSuppression("weak_mob_suppression_array", 3f, 650f, 80, 1f);
	public static final Formation MOB_IMMUNITY_ARRAY = new FormationMobSuppression("mob_immunity_array", 7f, 850f, 96, 3f);
	public static final Formation EVIL_REPELLING_FORMATION = new FormationMobSuppression("evil_repelling_formation", 12f, 1250f, 128, 15f);

	//Cultivation formations
	public static final Formation SOUL_GATHERING_FORMATION = new FormationCultivationHelper("soul_gathering_formation", 6, 220f, 8f, 0.15f);
	public static final Formation QI_PATHS_OPENER_ARRAY = new FormationCultivationHelper("qi_paths_opener_array", 12, 440f, 8f, 0.3f);
	public static final Formation DANTIAN_FORMING_ARRAY = new FormationCultivationHelper("dantian_forming_array", 21.2, 782.22, 8f, 0.503f);
	public static final Formation EARTH_QI_INTENSIFIER_FORMATION = new FormationCultivationHelper("earth_qi_intensifier_formation", 40, 1460.66, 8f, 1);
	public static final Formation SKY_QI_INTENSIFIER_FORMATION = new FormationCultivationHelper("sky_qi_intensifier_formation", 48, 3544.44, 8f, 4.8f); //don't have diagram
	public static final Formation LAW_PROJECTION_FORMATION = new FormationCultivationHelper("law_projection_formation", 153.3, 11244.44, 8f, 15.33f); //don't have diagram
	public static final Formation HEAVENLY_WAR_FORMATION = new FormationCultivationHelper("heavenly_war_formation", 383.33, 28111.11, 8f, 38.33f); //don't have diagram
	public static final Formation IMMORTAL_GRASP_FORMATION = new FormationCultivationHelper("immortal_grasp_formation", 1316.6, 96555.55, 8f, 131.66f); //don't have diagram
	public static final Formation IMMORTAL_ASCENSION_FORMATION = new FormationCultivationHelper("immortal_ascension_formation", 3500, 256666.66, 8f, 350f); //don't have diagram
	public static final Formation HEAVENLY_REACH_FORMATION = new FormationCultivationHelper("heavenly_reach_formation", 14833.33, 1087777.77, 8f, 1483.33); //don't have diagram
	public static final Formation TIME_ESSENCE_FORMATION = new FormationCultivationHelper("time_essence_formation", 50000, 333333.33, 8f, 5000); //don't have diagram
	public static final Formation IMMORTAL_DESTROYER_FORMATION = new FormationCultivationHelper("immortal_destroyer_formation", 200000, 14666666.66f, 8f, 20000f); //don't have diagram
	public static final Formation DIVINITY_GRASP_FORMATION = new FormationCultivationHelper("divinity_grasp_formation", 966666.66, 70888888.88, 8f, 96666.66); //don't have diagram
	public static final Formation CATACLYSM_FORMATION = new FormationCultivationHelper("cataclysm_formation", 4083333.33, 299444444.44, 8f, 408333.33); //don't have diagram
	public static final Formation GODHOOD_KEEPER_FORMATION = new FormationCultivationHelper("godhood_keeper_formation", 19250000, 1411666666.66, 8f, 1925000); //don't have diagram

	//Teleportation
	public static final Formation TELEPOSITION_ARRAY = new FormationTeleportation("teleposition_array", 7.5, 3800, 5000);

	//Dimension
	public static final Formation MINING_DIMENSION = new FormationDimensionChanger("mining_dimension_array", 0.85, 1300, WuxiaDimensions.MINING.getId());
	public static final Formation FIRE_DIMENSION = new FormationDimensionChanger("fire_dimension_array", 2.03, 2100,  WuxiaDimensions.FIRE.getId());
	public static final Formation EARTH_DIMENSION = new FormationDimensionChanger("earth_dimension_array", 2.03, 2100,  WuxiaDimensions.EARTH.getId());
	public static final Formation METAL_DIMENSION = new FormationDimensionChanger("metal_dimension_array", 2.03, 2100,  WuxiaDimensions.METAL.getId());
	public static final Formation WATER_DIMENSION = new FormationDimensionChanger("water_dimension_array", 2.03, 2100,  WuxiaDimensions.WATER.getId());
	public static final Formation WOOD_DIMENSION = new FormationDimensionChanger("wood_dimension_array", 2.03, 2100,  WuxiaDimensions.WOOD.getId());

	//Slaughter
	public static final Formation WEAK_SLAUGHTER_FORMATION = new FormationSlaughter("weak_slaughter_formation", 7, 650, 24, 3f);
}
