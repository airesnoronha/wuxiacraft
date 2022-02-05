package com.lazydragonstudios.wuxiacraft.cultivation;

import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.HashMap;

public class CultivationStage extends ForgeRegistryEntry<CultivationStage> {

	/**
	 * The name of this stage
	 */
	public final String name;

	/**
	 * The cultivation system this belongs to
	 */
	public final System system;

	private final HashMap<PlayerStat, BigDecimal> playerStats = new HashMap<>();

	private final HashMap<System, HashMap<PlayerSystemStat, BigDecimal>> systemStats;

	/**
	 * The next stage to this stage
	 * Null if last stage in realm
	 */
	@Nullable
	public final ResourceLocation nextStage;

	/**
	 * Constructor for this cultivation stage
	 *
	 * @param name      The stage name
	 * @param system    the Stage Cultivation System
	 * @param nextStage then next stage to this stage, null if last in realm
	 */
	public CultivationStage(String name, System system, @Nullable ResourceLocation nextStage) {
		this.name = name;
		this.system = system;
		this.nextStage = nextStage;
		systemStats = new HashMap<>();
		//TODO find a better name
		for (var s : System.values()) {
			systemStats.put(s, new HashMap<>());
		}
	}

	public CultivationStage addSystemStat(System system, PlayerSystemStat stat, BigDecimal value) {
		this.systemStats.get(system).put(stat, value);
		return this;
	}

	public CultivationStage addPlayerStat(PlayerStat stat, BigDecimal value) {
		this.playerStats.put(stat, value);
		return this;
	}

	public BigDecimal getSystemStat(System system, PlayerSystemStat stat) {
		return this.systemStats.get(system).getOrDefault(stat, BigDecimal.ZERO);
	}

	public BigDecimal getPlayerStat(PlayerStat stat) {
		return this.playerStats.getOrDefault(stat, BigDecimal.ZERO);
	}
}
