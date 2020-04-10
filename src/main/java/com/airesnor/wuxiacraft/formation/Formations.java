package com.airesnor.wuxiacraft.formation;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Formations {

	public static final List<Formation> FORMATIONS = new ArrayList<>();

	//passive generation
	public static final Formation WEAK_QI_GATHERING_ARRAY = new FormationQiGathering("weak_qi_gathering_array", 0.03f, 32f);
	public static final Formation FIVE_ELEMENTS_GATHERING_ARRAY = new FormationQiGathering("five_elements_gathering_array", 0.09f, 48f);
	public static final Formation YIN_YANG_GATHERING_ARRAY = new FormationQiGathering("yin_yang_gathering_array", 0.288f, 64f);
	public static final Formation NATURE_SPIRIT_GATHERING_ARRAY = new FormationQiGathering("nature_spirit_gathering_array", 0.9794f, 72f);

	//furnace powering
	public static final Formation FURNACE_POWERING_FORMATION = new FormationFurnacePower("furnace_powering_formation", 15f, 100f, 16f);

	//barriers
	public static final Formation SOUL_LAND_BARRIER = new FormationPlayerBarrier("soul_land_barrier", 20f, 1000f, 128f, 8.5f);
	public static final Formation FOREIGN_QI_BARRIER = new FormationPlayerBarrier("foreign_qi_barrier", 20f, 1000f, 156f, 28.75f);
	public static final Formation EARTHLY_BLOCKADE = new FormationPlayerBarrier("earthly_blockade", 20f, 1000f, 184f, 118.45f);
	public static final Formation FIRMAMENT_PROTECTION = new FormationPlayerBarrier("firmament_protection", 20f, 1000f, 202f, 425.89f);
	public static final Formation LAW_CONDENSING_BARRIER = new FormationPlayerBarrier("law_condensing_barrier", 20f, 1000f, 232f, 1894.7f);
	public static final Formation HEAVENLY_DOME_RECREATION = new FormationPlayerBarrier("heavenly_dome_recreation", 20f, 1000f, 256f, 8953.17f);


	//Cultivation formations
	public static final Formation SOUL_GATHERING_FORMATION = new FormationCultivationHelper("soul_gathering_formation", 6, 220f, 8f, 0.3f);
	public static final Formation QI_PATHS_OPENER_ARRAY = new FormationCultivationHelper("qi_paths_opener_array", 12f, 440f, 8f, 0.6f);
	public static final Formation DANTIAN_FORMING_ARRAY = new FormationCultivationHelper("dantian_forming_array", 21.34, 782.22, 8f, 1.067f);
	public static final Formation EARTH_QI_INTENSIFIER_FORMATION = new FormationCultivationHelper("earth_qi_intensifier_formation", 40, 1460.66, 8f, 2f);
	public static final Formation SKY_QI_INTENSIFIER_FORMATION = new FormationCultivationHelper("sky_qi_intensifier_formation", 96, 3544.44, 8f, 4.8f);
	public static final Formation LAW_PROJECTION_FORMATION = new FormationCultivationHelper("law_projection_formation", 306.6, 11244.44, 8f, 15.33f);
	public static final Formation HEAVENLY_WAR_FORMATION = new FormationCultivationHelper("heavenly_war_formation", 766.6, 28111.11, 8f, 38.33f);
	public static final Formation IMMORTAL_GRASP_FORMATION = new FormationCultivationHelper("immortal_grasp_formation", 2633.2, 96555.55, 8f, 131.66f);
	public static final Formation IMMORTAL_ASCENSION_FORMATION = new FormationCultivationHelper("immortal_ascension_formation", 7000, 256666.66, 8f, 350f);
	public static final Formation HEAVENLY_REACH_FORMATION = new FormationCultivationHelper("heavenly_reach_formation", 29666.6, 1087777.77, 8f, 1483.33);
	public static final Formation TIME_ESSENCE_FORMATION = new FormationCultivationHelper("time_essence_formation", 100000, 333333.33, 8f, 5000);
	public static final Formation IMMORTAL_DESTROYER_FORMATION = new FormationCultivationHelper("immortal_destroyer_formation", 400000, 14666666.66f, 8f, 20000f);
	public static final Formation DIVINITY_GRASP_FORMATION = new FormationCultivationHelper("divinity_grasp_formation", 193333.2, 70888888.88, 8f, 96666.66);
	public static final Formation CATACLYSM_FORMATION = new FormationCultivationHelper("cataclysm_formation", 8166666.6, 299444444.44, 8f, 408333.33);
	public static final Formation GODHOOD_KEEPER_FORMATION = new FormationCultivationHelper("godhood_keeper_formation", 38500000, 1411666666.66, 8f, 1925000);

}
