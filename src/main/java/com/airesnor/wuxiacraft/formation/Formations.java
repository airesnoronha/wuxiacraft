package com.airesnor.wuxiacraft.formation;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public class Formations {

	public static final List<Formation> FORMATIONS = new ArrayList<>();

	public static final Formation WEAK_QI_GATHERING_ARRAY = new FormationQiGathering("weak_qi_gathering_array", 0.03f, 32f);
	public static final Formation FURNACE_POWERING_FORMATION = new FormationFurnacePower("furnace_powering_formation", 15f, 500f, 16f);
	public static final Formation SOUL_GATHERING_FORMATION = new FormationCultivationHelper("soul_gathering_formation", 10f, 500f, 8f, 2.5f);
	public static final Formation SOUL_LAND_BARRIER = new FormationPlayerBarrier("soul_land_barrier", 20f, 500f, 128f, 5.5f);

}
