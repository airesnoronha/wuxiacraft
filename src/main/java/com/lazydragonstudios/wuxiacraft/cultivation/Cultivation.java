package com.lazydragonstudios.wuxiacraft.cultivation;

import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import com.lazydragonstudios.wuxiacraft.capabilities.CultivationProvider;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.AspectContainer;

import java.math.BigDecimal;
import java.util.HashMap;

//TODO add a lives counter
public class Cultivation implements ICultivation {

	/**
	 * The cultivation information for each system
	 */
	public HashMap<System, SystemContainer> systemCultivation;

	/**
	 * Player specific stats
	 */
	private final HashMap<PlayerStat, BigDecimal> playerStats;

	/**
	 * this is for sync with the client and probably vice versa
	 * a substitute for this could've been entity.ticksAlive
	 * but that is not among us anymore
	 */
	private int tickTimer;

	/**
	 * this is for the server and client to convert body energy into essence energy
	 */
	private boolean exercising;

	/**
	 * Known Aspects and proficiency
	 */
	public AspectContainer aspects;

	public Cultivation() {
		this.systemCultivation = new HashMap<>();
		this.playerStats = new HashMap<>();
		for (var stat : PlayerStat.values()) {
			this.playerStats.put(stat, stat.defaultValue);
		}
		for (var system : System.values()) {
			SystemContainer systemData = new SystemContainer(system);
			this.systemCultivation.put(system, systemData);
		}
		this.aspects = new AspectContainer();
		this.exercising = false;
	}

	public static ICultivation get(Player target) {
		var cultOpt = target.getCapability(CultivationProvider.CULTIVATION_PROVIDER).resolve();
		return cultOpt.orElseGet(Cultivation::new);
	}

	@Override
	public BigDecimal getPlayerStat(PlayerStat stat) {
		BigDecimal statValue = this.playerStats.getOrDefault(stat, stat.defaultValue);
		for (var system : System.values()) {
			var data = getSystemData(system);
			statValue = statValue.add(data.getPlayerStat(stat));
		}
		return statValue;
	}

	@Override
	public void setPlayerStat(PlayerStat stat, BigDecimal value) {
		if (stat.isModifiable) {
			this.playerStats.put(stat, value.max(BigDecimal.ZERO));
		}
	}

	@Override
	public SystemContainer getSystemData(System system) {
		return systemCultivation.get(system);
	}

	public void calculateStats() {
		for (var stat : PlayerStat.values()) {
			if (stat.isModifiable) continue;
			var statValue = stat.defaultValue;
			for (var system : System.values()) {
				statValue = statValue.add(this.getSystemData(system).getPlayerStat(stat));
			}
		}
	}

	@Override
	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		for (var stat : this.playerStats.keySet()) {
			if (!stat.isModifiable) continue;
			tag.putString("stat-" + stat.name().toLowerCase(), this.playerStats.get(stat).toPlainString());
		}
		tag.put("body-data", getSystemData(System.BODY).serialize());
		tag.put("divine-data", getSystemData(System.DIVINE).serialize());
		tag.put("essence-data", getSystemData(System.ESSENCE).serialize());
		tag.put("aspect-data", this.aspects.serialize());
		return tag;
	}

	@Override
	public void deserialize(CompoundTag tag) {
		for (var stat : this.playerStats.keySet()) {
			if (!stat.isModifiable) continue;
			if (tag.contains("stat-" + stat.name().toLowerCase())) {
				this.playerStats.put(stat, new BigDecimal(tag.getString("stat-" + stat.name().toLowerCase())));
			} else {
				this.playerStats.put(stat, new BigDecimal("0"));
			}
		}
		calculateStats();
		if (tag.contains("body-data")) {
			getSystemData(System.BODY).deserialize(tag.getCompound("body-data"), this);
		}
		if (tag.contains("divine-data")) {
			getSystemData(System.DIVINE).deserialize(tag.getCompound("divine-data"), this);
		}
		if (tag.contains("essence-data")) {
			getSystemData(System.ESSENCE).deserialize(tag.getCompound("essence-data"), this);
		}
		if (tag.contains("aspect-data")) {
			this.aspects.deserialize(tag.getCompound("aspect-data"));
		}
	}

	@Override
	public boolean isExercising() {
		return exercising;
	}

	@Override
	public void setExercising(boolean exercising) {
		this.exercising = exercising;
	}

	@Override
	public AspectContainer getAspects() {
		return aspects;
	}

	/**
	 * Utility to increment to the tick timer
	 */
	@Override
	public void advanceTimer() {
		this.tickTimer++;
	}

	/**
	 * Utility to reset timer.
	 * Should only be used when a sync message is sent
	 */
	@Override
	public void resetTimer() {
		this.tickTimer = 0;
	}

	/**
	 * @return the time ticker. It's just for not exposing the ticker.
	 */
	@Override
	public int getTimer() {
		return this.tickTimer;
	}

}
