package com.lazydragonstudios.wuxiacraft.cultivation.technique;

import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerElementalStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemElementalStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import net.minecraft.resources.ResourceLocation;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

public class TechniqueModifier {

	public final HashMap<PlayerStat, BigDecimal> stats = new HashMap<>();

	public final HashMap<System, HashMap<PlayerSystemStat, BigDecimal>> systemStats = new HashMap<>();

	public final HashMap<ResourceLocation, HashMap<PlayerElementalStat, BigDecimal>> elementalStats = new HashMap<>();

	public final HashMap<System, HashMap<ResourceLocation, HashMap<PlayerSystemElementalStat, BigDecimal>>> systemElementalStats = new HashMap<>();

	public final HashMap<ResourceLocation, Double> elements = new HashMap<>();

	public final HashSet<ResourceLocation> skills = new HashSet<>();

	public boolean validTechnique;

	public TechniqueModifier() {
		for (var system : System.values()) {
			systemStats.put(system, new HashMap<>());
		}
		validTechnique = false;
	}

	public void add(TechniqueModifier tMod) {
		for (var stat : tMod.stats.keySet()) {
			this.stats.put(stat, this.stats.getOrDefault(stat, BigDecimal.ZERO).add(tMod.stats.get(stat)));
		}
		for (var system : System.values()) {
			var systemStats = this.systemStats.get(system);
			if (systemStats == null) continue;
			for (var stat : tMod.systemStats.get(system).keySet()) {
				systemStats.put(stat, systemStats.getOrDefault(stat, BigDecimal.ZERO).add(tMod.systemStats.get(system).get(stat)));
			}
		}
		for (var key : tMod.elements.keySet()) {
			this.elements.put(key, this.elements.getOrDefault(key, 0d) + tMod.elements.get(key));
		}
	}

	public void subtract(TechniqueModifier tMod) {
		for (var stat : tMod.stats.keySet()) {
			this.stats.put(stat, this.stats.getOrDefault(stat, BigDecimal.ZERO).subtract(tMod.stats.get(stat)));
		}
		for (var system : System.values()) {
			var systemStats = this.systemStats.get(system);
			if (systemStats == null) continue;
			for (var stat : tMod.systemStats.get(system).keySet()) {
				systemStats.put(stat, systemStats.getOrDefault(stat, BigDecimal.ZERO).subtract(tMod.systemStats.get(system).get(stat)));
			}
		}
		for (var key : tMod.elements.keySet()) {
			this.elements.put(key, this.elements.getOrDefault(key, 0d) - tMod.elements.get(key));
		}
	}

	public BigDecimal getStat(ResourceLocation element, PlayerElementalStat stat) {
		return this.elementalStats.getOrDefault(element, new HashMap<>()).getOrDefault(stat, BigDecimal.ZERO);
	}

	public BigDecimal getStat(System system, ResourceLocation element, PlayerSystemElementalStat stat) {
		return this.systemElementalStats.getOrDefault(system, new HashMap<>()).getOrDefault(element, new HashMap<>()).getOrDefault(stat, BigDecimal.ZERO);
	}

	public BigDecimal getStat(PlayerStat stat) {
		return this.stats.getOrDefault(stat, BigDecimal.ZERO);
	}

	public BigDecimal getStat(System system, PlayerSystemStat stat) {
		return this.systemStats.getOrDefault(system, new HashMap<>()).getOrDefault(stat, BigDecimal.ZERO);
	}


	public void setStat(ResourceLocation element, PlayerElementalStat stat, BigDecimal value) {
		this.elementalStats.putIfAbsent(element, new HashMap<>());
		this.elementalStats.get(element).put(stat, value);
	}

	public void setStat(System system, ResourceLocation element, PlayerSystemElementalStat stat, BigDecimal value) {
		this.systemElementalStats.putIfAbsent(system, new HashMap<>());
		this.systemElementalStats.get(system).putIfAbsent(element, new HashMap<>());
		this.systemElementalStats.get(system).get(element).put(stat, value);
	}

	/*
		public CompoundTag serialize() {
			var tag = new CompoundTag();
			var playerStatsTag = new ListTag();
			var playerSystemStatsTag = new CompoundTag();
			var elementsTag = new ListTag();
			for (var stat : this.stats.keySet()) {
				var statTag = new CompoundTag();
				statTag.putString("stat-name", stat.name());
				statTag.putString("stat-value", this.stats.get(stat).toPlainString());
				playerStatsTag.add(statTag);
			}
			for (var system : System.values()) {
				var systemStatsTag = new ListTag();
				for (var stat : this.systemStats.get(system).keySet()) {
					var statTag = new CompoundTag();
					statTag.putString("stat-name", stat.name());
					statTag.putString("stat-value", this.systemStats.get(system).get(stat).toPlainString());
					systemStatsTag.add(statTag);
				}
				playerSystemStatsTag.put(system.name().toLowerCase() + "-stat-list", systemStatsTag);
			}
			for (var element : this.elements.keySet()) {
				var elementTag = new CompoundTag();
				elementTag.putString("element-name", element.toString());
				elementTag.putDouble("element-value", this.elements.get(element));
				elementsTag.add(elementTag);
			}
			tag.put("stats-list", playerStatsTag);
			tag.put("elements-list", elementsTag);
			return tag;
		}

		public void deserialize(CompoundTag tag) {
			ListTag playerStatsTag = (ListTag) tag.get("stats-list");
			CompoundTag playerSystemStatsTag = (CompoundTag) tag.get("system-stats-list");
			ListTag elementsTag = (ListTag) tag.get("elements-list");
			if (playerStatsTag != null) {
				for (var rawStatTag : playerStatsTag) {
					if (rawStatTag instanceof CompoundTag statTag) {
						PlayerStat stat = PlayerStat.valueOf(statTag.getString("stat-name"));
						BigDecimal value = new BigDecimal(statTag.getString("stat-value"));
						this.stats.put(stat, value);
					}
				}
			}
			if (playerSystemStatsTag != null) {
				for (var system : System.values()) {
					var systemStatsList = (ListTag) playerSystemStatsTag.get(system.name().toLowerCase() + "-stat-list");
					if (systemStatsList == null) continue;
					for (var rawStatTag : systemStatsList) {
						if (rawStatTag instanceof CompoundTag statTag) {
							PlayerSystemStat stat = PlayerSystemStat.valueOf(statTag.getString("stat-name"));
							BigDecimal value = new BigDecimal(statTag.getString("stat-value"));
							this.systemStats.get(system).put(stat, value);
						}
					}
				}
			}
			if (elementsTag != null) {
				for (var rawElementTag : elementsTag) {
					if (rawElementTag instanceof CompoundTag elementTag) {
						ResourceLocation element = new ResourceLocation(elementTag.getString("element-name"));
						double value = elementTag.getDouble("element-value");
						this.elements.put(element, value);
					}
				}
			}
		}
	*/
	public boolean isValidTechnique() {
		return validTechnique;
	}

	public void setValidTechnique(boolean validTechnique) {
		this.validTechnique = validTechnique;
	}
}
