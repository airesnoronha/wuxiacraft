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

	/**
	 * Add stats to the player in this stage
	 */
	private final HashMap<PlayerStat, BigDecimal> playerStats = new HashMap<>();

	/**
	 * Add stats to the player to specific system
	 */
	private final HashMap<System, HashMap<PlayerSystemStat, BigDecimal>> systemStats;

	/**
	 * This is the realm this stage belongs to
	 */
	public final ResourceLocation realm;

	/**
	 * The next stage to this stage
	 * Null if not next stage
	 */
	@Nullable
	public final ResourceLocation nextStage;

	/**
	 * The previous stage to this stage
	 * This is used to when dropping back a stage when cultivation base is not stabilized
	 * Null if not previous stage
	 */
	@Nullable
	public final ResourceLocation previousStage;

	/**
	 * Constructor for this cultivation stage
	 * @param name      The stage name
	 * @param system    the Stage Cultivation System
	 * @param realm
	 * @param nextStage then next stage to this stage, null if last in realm
	 * @param previousStage
	 */
	public CultivationStage(String name, System system, ResourceLocation realm, @Nullable ResourceLocation nextStage, @Nullable ResourceLocation previousStage) {
		this.name = name;
		this.system = system;
		this.realm = realm;
		this.nextStage = nextStage;
		this.previousStage = previousStage;
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
