package com.lazydragonstudios.wuxiacraft.cultivation;

import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerElementalStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemElementalStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import com.lazydragonstudios.wuxiacraft.init.WuxiaRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.TechniqueContainer;

import java.math.BigDecimal;
import java.util.HashMap;

public class SystemContainer {

	/**
	 * The Cultivation system this data belongs to
	 */
	public final System system;

	/**
	 * The current stage id for this cultivation
	 */
	public ResourceLocation currentStage;

	/**
	 * Holds all specific stats of a system
	 */
	private final HashMap<PlayerSystemStat, BigDecimal> systemStats;

	/**
	 * Holds all specific stats of a system for each element
	 */
	private final HashMap<ResourceLocation, HashMap<PlayerSystemElementalStat, BigDecimal>> systemElementalStats;

	/**
	 * Holds all the technique data
	 */
	public final TechniqueContainer techniqueData;

	/**
	 * The constructor for this system cultivation stats
	 *
	 * @param system the system this belongs to
	 */
	public SystemContainer(System system) {
		this.system = system;
		this.currentStage = system.defaultStage;
		this.systemStats = new HashMap<>();
		this.systemElementalStats = new HashMap<>();
		techniqueData = new TechniqueContainer(this.system);
		for (var stat : PlayerSystemStat.values()) {
			this.systemStats.put(stat, stat.defaultValue);
			if (stat == PlayerSystemStat.ENERGY) {
				if (this.system == System.DIVINE) {
					this.systemStats.put(stat, new BigDecimal("10"));
				} else if (this.system == System.BODY) {
					this.systemStats.put(stat, new BigDecimal("7"));
				}
			}
		}
	}

	public BigDecimal getStat(PlayerSystemStat stat) {
		BigDecimal statValue = this.systemStats.getOrDefault(stat, BigDecimal.ZERO);
		for (var elementLocation : WuxiaRegistries.ELEMENTS.getKeys()) {
			if (!this.systemElementalStats.containsKey(elementLocation)) continue;
			if (!this.systemElementalStats.get(elementLocation).containsKey(PlayerSystemElementalStat.FOUNDATION)) continue;
			var element = WuxiaRegistries.ELEMENTS.getValue(elementLocation);
			if (element == null) continue;
			var foundation = this.systemElementalStats.get(elementLocation).get(PlayerSystemElementalStat.FOUNDATION);
			statValue = statValue.add(element.getFoundationStatValue(stat, foundation));
		}
		return statValue;
	}

	public BigDecimal getStat(ResourceLocation elementLocation, PlayerSystemElementalStat stat) {
		return this.systemElementalStats.getOrDefault(elementLocation, new HashMap<>()).getOrDefault(stat, BigDecimal.ZERO);
	}

	/**
	 * Gets the value of the player stats in this system by getting it from stage, foundation and technique modifier
	 * This is not saved because this should just be used for the cultivation class to store it's value
	 *
	 * @param stat the stat to be queried
	 * @return the stat value with all modifiers
	 */
	public BigDecimal getStat(PlayerStat stat) {
		BigDecimal statValue = this.getStage().getStat(stat);
		//this looks like statsValue = statsValue * (1 + techniqueModifier)
		statValue = statValue.multiply(BigDecimal.ONE.add(this.techniqueData.modifier.stats.getOrDefault(stat, BigDecimal.ZERO)));
		for (var elementLocation : WuxiaRegistries.ELEMENTS.getKeys()) {
			if (!this.systemElementalStats.containsKey(elementLocation)) continue;
			if (!this.systemElementalStats.get(elementLocation).containsKey(PlayerSystemElementalStat.FOUNDATION)) continue;
			var element = WuxiaRegistries.ELEMENTS.getValue(elementLocation);
			if (element == null) continue;
			var foundation = this.systemElementalStats.get(elementLocation).get(PlayerSystemElementalStat.FOUNDATION);
			statValue = statValue.add(element.getFoundationStatValue(stat, foundation));
		}
		return statValue;
	}

	/**
	 * Gets the value of the player stats in this system by getting it from stage, foundation and technique modifier
	 * This is not saved because this should just be used for the cultivation class to store its value
	 *
	 * @param elementLocation the element of the stat
	 * @param stat            the stat to be queried
	 * @return the stat value with all modifiers
	 */
	public BigDecimal getStat(ResourceLocation elementLocation, PlayerElementalStat stat) {
		BigDecimal statValue = this.getStage().getStat(elementLocation, stat);
		//this looks like statsValue = statsValue * (1 + techniqueModifier)
		statValue = statValue.multiply(BigDecimal.ONE.add(this.techniqueData.modifier.getStat(elementLocation, stat)));
		if (!this.systemElementalStats.containsKey(elementLocation)) return statValue;
		if (!this.systemElementalStats.get(elementLocation).containsKey(PlayerSystemElementalStat.FOUNDATION))
			return statValue;
		var element = WuxiaRegistries.ELEMENTS.getValue(elementLocation);
		if (element == null) return statValue;
		var foundation = this.systemElementalStats.get(elementLocation).get(PlayerSystemElementalStat.FOUNDATION);
		statValue = statValue.add(element.getFoundationStatValue(stat, foundation));
		return statValue;
	}

	public void setStat(PlayerSystemStat stat, BigDecimal value) {
		if (stat.isModifiable) {
			this.systemStats.put(stat, value.max(BigDecimal.ZERO));
		}
	}

	/**
	 * @return the current cultivation realm this cultivation is at
	 */
	public CultivationRealm getRealm() {
		return WuxiaRegistries.CULTIVATION_REALMS.getValue(this.getStage().realm);
	}

	/**
	 * @return the current cultivation stage this cultivation is at
	 */
	public CultivationStage getStage() {
		return WuxiaRegistries.CULTIVATION_STAGES.getValue(this.currentStage);
	}

	public void setSystemElementalStat(ResourceLocation element, PlayerSystemElementalStat stat, BigDecimal value) {
		this.systemElementalStats.putIfAbsent(element, new HashMap<>());
		this.systemElementalStats.get(element).put(stat, value);
	}

	public boolean hasEnergy(BigDecimal amount) {
		return this.getStat(PlayerSystemStat.ENERGY).compareTo(amount) >= 0;
	}

	public boolean consumeEnergy(BigDecimal amount) {
		if (hasEnergy(amount)) {
			this.systemStats.put(PlayerSystemStat.ENERGY, this.getStat(PlayerSystemStat.ENERGY).subtract(amount));
			return true;
		}
		return false;
	}

	public void addEnergy(BigDecimal amount) {
		if (amount.compareTo(new BigDecimal("0")) > 0) {
			this.systemStats.put(PlayerSystemStat.ENERGY, this.getStat(PlayerSystemStat.ENERGY).add(amount));
		}
	}

	public void calculateStats(ICultivation cultivation) {
		for (var stat : PlayerSystemStat.values()) {
			if (stat.isModifiable) continue;
			var value = stat.defaultValue;
			if (this.system == System.ESSENCE && stat == PlayerSystemStat.ENERGY_REGEN) {
				value = BigDecimal.ZERO;
			}
			var stageValue = BigDecimal.ZERO;
			for (var system : System.values()) {
				var systemData = cultivation.getSystemData(system);
				stageValue = stageValue.add(systemData.getStage().getStat(this.system, stat));
			}
			var foundationValue = BigDecimal.ZERO;
			for (var elementLocation : WuxiaRegistries.ELEMENTS.getKeys()) {
				var foundation = this.systemElementalStats.getOrDefault(elementLocation, new HashMap<>())
						.getOrDefault(PlayerSystemElementalStat.FOUNDATION, BigDecimal.ZERO);
				var element = WuxiaRegistries.ELEMENTS.getValue(elementLocation);
				if (element == null) continue;
				if (foundation.compareTo(BigDecimal.ZERO) <= 0) continue;
				foundationValue = foundationValue.add(element.getFoundationStatValue(stat, foundation));
			}
			var techniqueModifier = this.techniqueData.modifier.systemStats.get(this.system).getOrDefault(stat, BigDecimal.ZERO);
			//value= value + stageValue + foundationValue
			value = value.add(stageValue).add(foundationValue);
			if (stat == PlayerSystemStat.CULTIVATION_SPEED) {
				//value = value + techModifier
				value = value.add(techniqueModifier);
			} else {
				//value = value * (1 + techModifier)
				value = value.multiply(BigDecimal.ONE.add(techniqueModifier));
			}
			this.systemStats.put(stat, value);
		}
		for (var elementLocation : WuxiaRegistries.ELEMENTS.getKeys()) {
			for (var stat : PlayerSystemElementalStat.values()) {
				if (stat.isModifiable) continue;
				var value = BigDecimal.ZERO;
				var stageValue = this.getStage().getStat(this.system, elementLocation, stat);
				var techniqueModifier = this.techniqueData.modifier.getStat(this.system, elementLocation, stat);
				value = value.add(stageValue).multiply(BigDecimal.ONE.multiply(techniqueModifier));
				this.systemElementalStats.putIfAbsent(elementLocation, new HashMap<>());
				this.systemElementalStats.get(elementLocation).put(stat, value);
			}
		}
	}

	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		tag.putString("current_stage", this.currentStage.toString());
		for (var stat : PlayerSystemStat.values()) {
			if (!stat.isModifiable) continue;
			tag.putString("stat-" + stat.name().toLowerCase(), this.getStat(stat).toPlainString());
		}
		var elementStatsTag = new CompoundTag();
		for (var element : this.systemElementalStats.keySet()) {
			var currentElementStatsTag = new CompoundTag();
			for (var stat : PlayerSystemElementalStat.values()) {
				if (!stat.isModifiable) continue;
				currentElementStatsTag.putString("stat-" + stat.name().toLowerCase(),
						this.systemElementalStats.get(element).getOrDefault(stat, BigDecimal.ZERO).toPlainString());
			}
			elementStatsTag.put("element-stats-" + element, elementStatsTag);
		}
		tag.put("technique-data", this.techniqueData.serialize());
		tag.put("elemental-stats", elementStatsTag);
		return tag;
	}

	public void deserialize(CompoundTag tag, ICultivation cultivation) {
		if (tag.contains("current_stage")) {
			this.currentStage = new ResourceLocation(tag.getString("current_stage"));
		}
		for (var stat : PlayerSystemStat.values()) {
			if (!stat.isModifiable) continue;
			String statName = "stat-" + stat.name().toLowerCase();
			if (tag.contains(statName)) {
				this.systemStats.put(stat, new BigDecimal(tag.getString(statName)));
			} else {
				this.systemStats.put(stat, stat.defaultValue);
			}
		}
		if (tag.contains("elemental-stats")) {
			var rawElementalStatsTag = tag.get("elemental-stats");
			if (rawElementalStatsTag instanceof CompoundTag elementalStatsTag) {
				for (var elementLocation : WuxiaRegistries.ELEMENTS.getKeys()) {
					if (!elementalStatsTag.contains("element-stats-" + elementLocation)) continue;
					var rawCurrentElementalStatsTag = elementalStatsTag.get("element-stats-" + elementLocation);
					if (!(rawCurrentElementalStatsTag instanceof CompoundTag currentElementStatsTag)) continue;
					for (var stat : PlayerSystemElementalStat.values()) {
						if (!stat.isModifiable) continue;
						if (!(currentElementStatsTag.contains("stat-" + stat.name().toLowerCase()))) continue;
						var value = currentElementStatsTag.getString("stat-" + stat.name().toLowerCase());
						this.systemElementalStats.putIfAbsent(elementLocation, new HashMap<>());
						this.systemElementalStats.get(elementLocation).put(stat, new BigDecimal(value));
					}
				}
			}
		}
		CompoundTag techDataTag = (CompoundTag) tag.get("technique-data");
		if (techDataTag != null) {
			this.techniqueData.deserialize(techDataTag);
		}
		calculateStats(cultivation);
	}

}
