package wuxiacraft.cultivation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import wuxiacraft.cultivation.stats.PlayerSystemStat;
import wuxiacraft.cultivation.technique.TechniqueContainer;
import wuxiacraft.init.WuxiaRegistries;

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
		return this.systemStats.getOrDefault(stat, new BigDecimal("0"));
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

	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		tag.putString("current_realm", this.currentRealm.toString());
		tag.putString("current_stage", this.currentStage.toString());
		for (var stat : PlayerSystemStat.values()) {
			tag.putString("stat-" + stat.name().toLowerCase(), this.getStat(stat).toPlainString());
		}
		tag.put("technique-data", this.techniqueData.serialize());
		return tag;
	}

	public void deserialize(CompoundTag tag) {
		this.currentRealm = new ResourceLocation(tag.getString("current_realm"));
		this.currentStage = new ResourceLocation(tag.getString("current_stage"));
		for (var stat : PlayerSystemStat.values()) {
			this.systemStats.put(stat, new BigDecimal(tag.getString("stat-" + stat.name().toLowerCase())));
		}
		CompoundTag techDataTag = (CompoundTag) tag.get("technique-data");
		if (techDataTag != null) {
			this.techniqueData.deserialize(techDataTag);
		}
	}

}
