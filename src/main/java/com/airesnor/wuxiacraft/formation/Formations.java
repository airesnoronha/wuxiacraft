package com.airesnor.wuxiacraft.formation;

import java.util.ArrayList;
import java.util.List;

public class Formations {

	public static final List<Formation> FORMATIONS = new ArrayList<>();

	public static final Formation WEAK_QI_GATHERING_ARRAY = new FormationQiGathering("weak_qi_gathering_array", 1f, 32f);
	public static final Formation FURNACE_POWERING_FORMATION = new FormationFurnacePower("furnace_powering_formation", 600f, 500f, 16f);

}
