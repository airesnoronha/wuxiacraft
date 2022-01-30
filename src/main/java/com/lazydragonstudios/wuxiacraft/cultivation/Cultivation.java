package com.lazydragonstudios.wuxiacraft.cultivation;

import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.init.WuxiaTechniqueAspects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import com.lazydragonstudios.wuxiacraft.capabilities.CultivationProvider;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.AspectContainer;

import java.math.BigDecimal;
import java.util.HashMap;

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
	 * Known Aspects and proficiency
	 */
	public AspectContainer aspects;

	public Cultivation() {
		this.systemCultivation = new HashMap<>();
		this.playerStats = new HashMap<>();
		for (var stat : PlayerStat.values()) {
			this.playerStats.put(stat, stat.defaultValue);
		}
		this.systemCultivation.put(System.BODY, new SystemContainer(System.BODY));
		this.systemCultivation.put(System.DIVINE, new SystemContainer(System.DIVINE));
		this.systemCultivation.put(System.ESSENCE, new SystemContainer(System.ESSENCE));
		this.aspects = new AspectContainer();
		this.aspects.addAspectProficiency(this, WuxiaTechniqueAspects.START.getId(), BigDecimal.TEN);
	}

	public static ICultivation get(Player target) {
		var cultOpt =  target.getCapability(CultivationProvider.CULTIVATION_PROVIDER).resolve();
		return cultOpt.orElseGet(Cultivation::new);
	}

	@Override
	public BigDecimal getPlayerStat(PlayerStat stat) {
		return this.playerStats.getOrDefault(stat, stat.defaultValue);
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

	@Override
	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		for (var stat : this.playerStats.keySet()) {
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
			if (tag.contains("stat-" + stat.name().toLowerCase())) {
				this.playerStats.put(stat, new BigDecimal(tag.getString("stat-" + stat.name().toLowerCase())));
			} else {
				this.playerStats.put(stat, new BigDecimal("0"));
			}
		}
		getSystemData(System.BODY).deserialize(tag.getCompound("body-data"));
		getSystemData(System.DIVINE).deserialize(tag.getCompound("divine-data"));
		getSystemData(System.ESSENCE).deserialize(tag.getCompound("essence-data"));
		if (tag.contains("aspect-data")) {
			this.aspects.deserialize(tag.getCompound("aspect-data"));
		}
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
