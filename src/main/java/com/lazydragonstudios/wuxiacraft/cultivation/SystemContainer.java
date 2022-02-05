package com.lazydragonstudios.wuxiacraft.cultivation;

import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
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
	 * The current realm id for this cultivation
	 */
	public ResourceLocation currentRealm;

	/**
	 * The current stage id for this cultivation
	 */
	public ResourceLocation currentStage;

	/**
	 * Holds all specific stats of a system
	 */
	private final HashMap<PlayerSystemStat, BigDecimal> systemStats;

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
		this.currentRealm = system.defaultRealm;
		this.currentStage = system.defaultStage;
		this.systemStats = new HashMap<>();
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
		return this.systemStats.getOrDefault(stat, BigDecimal.ZERO);
	}

	public BigDecimal getPlayerStat(PlayerStat stat) {
		BigDecimal statValue = this.getStage().getPlayerStat(stat);
		//this looks like statsValue = statsValue * (1 + techniqueModifier)
		statValue = statValue.multiply(BigDecimal.ONE.add(this.techniqueData.modifier.stats.getOrDefault(stat, BigDecimal.ZERO)));
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
		return WuxiaRegistries.CULTIVATION_REALMS.getValue(this.currentRealm);
	}

	/**
	 * @return the current cultivation stage this cultivation is at
	 */
	public CultivationStage getStage() {
		return WuxiaRegistries.CULTIVATION_STAGES.getValue(this.currentStage);
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
				stageValue = stageValue.add(systemData.getStage().getSystemStat(this.system, stat));
			}
			var techniqueModifier = this.techniqueData.modifier.systemStats.get(this.system).getOrDefault(stat, BigDecimal.ZERO);
			//value = value + stageValue * (1 + techModifier)
			value = value.add(stageValue).multiply(BigDecimal.ONE.add(techniqueModifier));
			this.systemStats.put(stat, value);
		}
	}

	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		tag.putString("current_realm", this.currentRealm.toString());
		tag.putString("current_stage", this.currentStage.toString());
		for (var stat : PlayerSystemStat.values()) {
			if (!stat.isModifiable) continue;
			tag.putString("stat-" + stat.name().toLowerCase(), this.getStat(stat).toPlainString());
		}
		tag.put("technique-data", this.techniqueData.serialize());
		return tag;
	}

	public void deserialize(CompoundTag tag, ICultivation cultivation) {
		if (tag.contains("current_realm")) {
			this.currentRealm = new ResourceLocation(tag.getString("current_realm"));
		}
		if (tag.contains("current_realm")) {
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
		CompoundTag techDataTag = (CompoundTag) tag.get("technique-data");
		if (techDataTag != null) {
			this.techniqueData.deserialize(techDataTag);
		}
		calculateStats(cultivation);
	}

}
