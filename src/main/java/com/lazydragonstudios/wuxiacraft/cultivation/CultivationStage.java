package com.lazydragonstudios.wuxiacraft.cultivation;

import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerElementalStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemElementalStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import com.lazydragonstudios.wuxiacraft.init.WuxiaRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.registries.ForgeRegistryEntry;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;
import java.util.function.Consumer;

public class CultivationStage extends ForgeRegistryEntry<CultivationStage> {

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
	 * Add elemental stats to this stage
	 */
	private final HashMap<ResourceLocation, HashMap<PlayerElementalStat, BigDecimal>> elementalStats;

	/**
	 * Adds system elemental stats to this stage
	 */
	private final HashMap<System, HashMap<ResourceLocation, HashMap<PlayerSystemElementalStat, BigDecimal>>> systemElementalStats;

	/**
	 * The skill aspects this stage is going to unlock
	 */
	private final HashSet<ResourceLocation> skillsAspects;

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

	private Consumer<Player> onCultivate;

	private Consumer<Player> onCultivationFailure;

	/**
	 * Constructor for this cultivation stage
	 *
	 * @param system        the Stage Cultivation System
	 * @param realm         the realm this stage is in
	 * @param previousStage a reference to the previous stage, null if this is the first
	 * @param nextStage     a reference to the next stage, null if this is the last
	 */
	public CultivationStage(System system, ResourceLocation realm, @Nullable ResourceLocation previousStage, @Nullable ResourceLocation nextStage) {
		this.system = system;
		this.realm = realm;
		this.nextStage = nextStage;
		this.previousStage = previousStage;
		this.systemStats = new HashMap<>();
		this.elementalStats = new HashMap<>();
		this.systemElementalStats = new HashMap<>();
		this.skillsAspects = new HashSet<>();
		for (var s : System.values()) {
			systemStats.put(s, new HashMap<>());
		}
		this.onCultivate = p -> {
		};
		this.onCultivationFailure = p -> {
		};
	}

	public CultivationStage addSystemStat(System system, PlayerSystemStat stat, BigDecimal value) {
		if (stat.isModifiable) return this;
		this.systemStats.get(system).put(stat, value);
		return this;
	}

	public CultivationStage addPlayerStat(PlayerStat stat, BigDecimal value) {
		if (stat.isModifiable) return this;
		this.playerStats.put(stat, value);
		return this;
	}

	public CultivationStage addElementalStat(ResourceLocation element, PlayerElementalStat stat, BigDecimal value) {
		if (stat.isModifiable) return this;
		this.elementalStats.putIfAbsent(element, new HashMap<>());
		this.elementalStats.get(element).put(stat, value);
		return this;
	}

	public CultivationStage addSystemElementalStat(System system, ResourceLocation element, PlayerSystemElementalStat stat, BigDecimal value) {
		if (stat.isModifiable) return this;
		this.systemElementalStats.putIfAbsent(system, new HashMap<>());
		this.systemElementalStats.get(system).putIfAbsent(element, new HashMap<>());
		this.systemElementalStats.get(system).get(element).put(stat, value);
		return this;
	}

	public CultivationStage addSkill(ResourceLocation aspectLocation) {
		this.skillsAspects.add(aspectLocation);
		return this;
	}

	public CultivationStage setOnCultivate(Consumer<Player> onCultivate) {
		this.onCultivate = onCultivate;
		return this;
	}

	public CultivationStage setOnCultivationFailure(Consumer<Player> onCultivationFailure) {
		this.onCultivationFailure = onCultivationFailure;
		return this;
	}

	/**
	 * Recursively adds stats from previous stages
	 *
	 * @param stat The stat to be queried
	 * @return the stat value or zero if not found
	 */
	public BigDecimal getStat(PlayerStat stat) {
		BigDecimal stageValue = this.playerStats.getOrDefault(stat, BigDecimal.ZERO);
		if (this.previousStage == null) return stageValue;
		var aux = WuxiaRegistries.CULTIVATION_STAGES.getValue(this.previousStage);
		if (aux == null) return stageValue;
		stageValue = stageValue.add(aux.getStat(stat));
		return stageValue;
	}

	/**
	 * Recursively adds stats from previous stages
	 *
	 * @param system the system to query the stat
	 * @param stat   the stat to be queried
	 * @return the stat value or zero if not found
	 */
	public BigDecimal getStat(System system, PlayerSystemStat stat) {
		var stageValue = this.systemStats.get(system).getOrDefault(stat, BigDecimal.ZERO);
		if (this.previousStage == null) return stageValue;
		var aux = WuxiaRegistries.CULTIVATION_STAGES.getValue(this.previousStage);
		if (aux == null) return stageValue;
		stageValue = stageValue.add(aux.getStat(system, stat));
		return stageValue;
	}

	/**
	 * Recursively adds stats from previous stages
	 *
	 * @param element the element of the stat
	 * @param stat    the stat to be queried
	 * @return the stat value or zero if not found
	 */
	public BigDecimal getStat(ResourceLocation element, PlayerElementalStat stat) {
		var stageValue = this.elementalStats
				.getOrDefault(element, new HashMap<>()).getOrDefault(stat, BigDecimal.ZERO);
		if (this.previousStage == null) return stageValue;
		var aux = WuxiaRegistries.CULTIVATION_STAGES.getValue(this.previousStage);
		if (aux == null) return stageValue;
		stageValue = stageValue.add(aux.getStat(element, stat));
		return stageValue;
	}


	/**
	 * Recursively adds stats from previous stages
	 *
	 * @param system  the system of the stat
	 * @param element the element of the stat
	 * @param stat    the stat to be queried
	 * @return the stat value or zero if not found
	 */
	public BigDecimal getStat(System system, ResourceLocation element, PlayerSystemElementalStat stat) {
		var stageValue = this.systemElementalStats
				.getOrDefault(system, new HashMap<>()).getOrDefault(element, new HashMap<>()).getOrDefault(stat, BigDecimal.ZERO);
		if (this.previousStage == null) return stageValue;
		var aux = WuxiaRegistries.CULTIVATION_STAGES.getValue(this.previousStage);
		if (aux == null) return stageValue;
		stageValue = stageValue.add(aux.getStat(system, element, stat));
		return stageValue;
	}

	public HashSet<ResourceLocation> getSkillsAspects() {
		HashSet<ResourceLocation> skills = new HashSet<>(this.skillsAspects);
		if (this.previousStage == null) return skills;
		var aux = WuxiaRegistries.CULTIVATION_STAGES.getValue(this.previousStage);
		if (aux == null) return skills;
		skills.addAll(aux.getSkillsAspects());
		return skills;
	}

	public void cultivate(Player player) {
		this.onCultivate.accept(player);
	}

	public void cultivationFailure(Player player) {
		this.onCultivationFailure.accept(player);
	}
}
