package com.lazydragonstudios.wuxiacraft.cultivation;

import com.lazydragonstudios.wuxiacraft.cultivation.skills.SkillContainer;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerElementalStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemElementalStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import com.lazydragonstudios.wuxiacraft.event.CultivatingEvent;
import com.lazydragonstudios.wuxiacraft.init.WuxiaConfigs;
import com.lazydragonstudios.wuxiacraft.init.WuxiaMobEffects;
import com.lazydragonstudios.wuxiacraft.init.WuxiaRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import com.lazydragonstudios.wuxiacraft.capabilities.CultivationProvider;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.AspectContainer;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import java.math.BigDecimal;
import java.math.RoundingMode;
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

	private final HashMap<ResourceLocation, HashMap<PlayerElementalStat, BigDecimal>> playerElementalStats;

	/**
	 * this is for sync with the client and probably vice versa
	 * a substitute for this could've been entity.ticksAlive
	 * but that is not among us anymore
	 */
	private int tickTimer;

	private boolean semiDead;

	private int semiDeadTime;

	/**
	 * this is for the server and client to convert body energy into essence energy
	 */
	private boolean exercising;

	/**
	 * Known Aspects and proficiency
	 */
	public AspectContainer aspects;

	/**
	 * whether the player is in combat mode
	 */
	public boolean combat;

	/**
	 * the skill data for this character
	 */
	public SkillContainer skills;

	/**
	 * this formation core block position
	 */
	@Nullable
	private BlockPos formationCore;

	/**
	 * is divine sense on
	 */
	private boolean isDivineSense;

	private double agilityRegulator;

	private double strengthRegulator;

	public Cultivation() {
		this.systemCultivation = new HashMap<>();
		this.playerStats = new HashMap<>();
		this.playerElementalStats = new HashMap<>();
		for (var stat : PlayerStat.values()) {
			this.playerStats.put(stat, stat.defaultValue);
		}
		for (var system : System.values()) {
			SystemContainer systemData = new SystemContainer(system);
			this.systemCultivation.put(system, systemData);
		}
		this.aspects = new AspectContainer();
		this.skills = new SkillContainer();
		this.exercising = false;
		this.combat = false;
		this.semiDead = false;
		this.semiDeadTime = 0;
		this.isDivineSense = false;
		this.formationCore = null;
	}

	public static ICultivation get(Player target) {
		var cultOpt = target.getCapability(CultivationProvider.CULTIVATION_PROVIDER).resolve();
		return cultOpt.orElseGet(Cultivation::new);
	}

	@Override
	public void setFormation(@Nullable BlockPos blockPos) {
		this.formationCore = blockPos;
	}

	@Override
	@Nullable
	public BlockPos getFormation() {
		return this.formationCore;
	}

	@Override
	public BigDecimal getStat(PlayerStat stat) {
		return this.getStat(stat, false);
	}

	@Override
	public BigDecimal getStat(PlayerStat stat, boolean fullValue) {
		if (!fullValue) {
			var statValue = this.playerStats.getOrDefault(stat, stat.defaultValue);
			if (stat == PlayerStat.STRENGTH) {
				return statValue.multiply(BigDecimal.valueOf(this.strengthRegulator));
			} else if (stat == PlayerStat.AGILITY) {
				return statValue.multiply(BigDecimal.valueOf(this.agilityRegulator));
			}
		}
		return this.playerStats.getOrDefault(stat, stat.defaultValue);
	}

	@Override
	public BigDecimal getStat(ResourceLocation elementLocation, PlayerElementalStat stat) {
		return this.playerElementalStats.getOrDefault(elementLocation, new HashMap<>()).getOrDefault(stat, BigDecimal.ZERO);
	}

	@Override
	public void setStat(PlayerStat stat, BigDecimal value) {
		if (!stat.isModifiable) return;
		this.playerStats.put(stat, value.max(BigDecimal.ZERO));
	}

	@Override
	public void setStat(ResourceLocation element, PlayerElementalStat stat, BigDecimal value) {
		if (!stat.isModifiable) return;
		this.playerElementalStats.putIfAbsent(element, new HashMap<>());
		this.playerElementalStats.get(element).put(stat, value.max(BigDecimal.ZERO));
	}

	@Override
	public void addStat(PlayerStat stat, BigDecimal value) {
		this.setStat(stat, this.getStat(stat).add(value));
	}

	@Override
	public void addStat(ResourceLocation element, PlayerElementalStat stat, BigDecimal value) {
		this.setStat(element, stat, this.getStat(element, stat).add(value));
	}

	@Override
	public SystemContainer getSystemData(System system) {
		return systemCultivation.get(system);
	}

	@Override
	public void addCultivationBase(Player player, System system, BigDecimal amount) {
		CultivatingEvent event = new CultivatingEvent(player, system, amount);
		if (MinecraftForge.EVENT_BUS.post(event)) return;
		var systemData = this.getSystemData(system);
		this.getSystemData(System.DIVINE).consumeEnergy(amount);
		amount = event.getAmount();
		var grid = systemData.techniqueData.grid.getGrid();
		for (var aspectLocation : grid.values()) {
			this.aspects.addAspectProficiency(aspectLocation, amount, this);
		}
		systemData.techniqueData.grid.fixProficiencies(this.aspects);
		if (player.hasEffect(WuxiaMobEffects.PILL_RESONANCE.get())) {
			var instance = player.getEffect(WuxiaMobEffects.PILL_RESONANCE.get());
			var amplifier = instance.getAmplifier();
			//amount = amount * (1 + (6 * amplifier))
			amount = amount.multiply(BigDecimal.ONE.add(new BigDecimal("6").multiply(BigDecimal.valueOf(amplifier))));
		}
		if (player.hasEffect(WuxiaMobEffects.SPIRITUAL_RESONANCE.get())) {
			var instance = player.getEffect(WuxiaMobEffects.SPIRITUAL_RESONANCE.get());
			var amplifier = instance.getAmplifier();
			//amount = amount * (1 + (8 * amplifier))
			amount = amount.multiply(BigDecimal.ONE.add(new BigDecimal("8").multiply(BigDecimal.valueOf(amplifier))));
		}
		if (player.hasEffect(WuxiaMobEffects.ENLIGHTENMENT.get())) {
			var instance = player.getEffect(WuxiaMobEffects.SPIRITUAL_RESONANCE.get());
			var amplifier = instance.getAmplifier();
			//amount = amount * (1 + (3 * amplifier))
			amount = amount.multiply(BigDecimal.ONE.add(new BigDecimal("3").multiply(BigDecimal.valueOf(amplifier))));
		}
		var elements = systemData.techniqueData.modifier.elements;
		for (var elementLocation : elements.keySet()) {
			systemData.addStat(elementLocation, PlayerSystemElementalStat.FOUNDATION, BigDecimal.valueOf(elements.get(elementLocation) * 0.1));
			this.addStat(elementLocation, PlayerElementalStat.COMPREHENSION, BigDecimal.valueOf(elements.get(elementLocation)));
		}
		var cultSpeed = systemData.getStat(PlayerSystemStat.CULTIVATION_SPEED);
		amount = amount.multiply(BigDecimal.ONE.add(cultSpeed).multiply(BigDecimal.valueOf(WuxiaConfigs.CULTIVATION_SPEED_MULTIPLIER.get())));
		systemData.addStat(PlayerSystemStat.CULTIVATION_BASE, amount);
	}

	@Override
	public boolean attemptBreakthrough(System system) {
		var systemData = this.getSystemData(system);
		var initialStage = systemData.currentStage;
		var cultBase = systemData.getStat(PlayerSystemStat.CULTIVATION_BASE);
		var maxCultBase = systemData.getStat(PlayerSystemStat.MAX_CULTIVATION_BASE);
		if (cultBase.compareTo(maxCultBase) < 0) return false;
		//TODO logic for chanced breakthrough
		var stage = systemData.getStage();
		if (stage.nextStage == null) return false;
		systemData.currentStage = stage.nextStage;
		systemData.setStat(PlayerSystemStat.CULTIVATION_BASE, BigDecimal.ZERO);
		this.setStat(PlayerStat.LIVES, this.getStat(PlayerStat.LIVES).add(BigDecimal.ONE).min(BigDecimal.valueOf(WuxiaConfigs.MAX_LIVES.get())));
		return !systemData.currentStage.equals(initialStage);
	}

	@Override
	public void calculateStats() {
		for (var stat : PlayerStat.values()) {
			if (stat.isModifiable) continue;
			var statValue = stat.defaultValue;
			for (var system : System.values()) {
				statValue = statValue.add(this.getSystemData(system).getStat(stat));
			}
			statValue = statValue.max(BigDecimal.ZERO);
			this.playerStats.put(stat, statValue);
		}
		for (var elementLocation : WuxiaRegistries.ELEMENTS.getKeys()) {
			for (var stat : PlayerElementalStat.values()) {
				//small cleaning, if the value is zero on the stat value then remove from the memory
				if (this.playerElementalStats.containsKey(elementLocation)) {
					if (this.playerElementalStats.get(elementLocation).getOrDefault(stat, BigDecimal.ZERO).compareTo(BigDecimal.ZERO) == 0) {
						this.playerElementalStats.get(elementLocation).remove(stat);
					}
				}
				if (stat.isModifiable) continue;
				var statValue = BigDecimal.ZERO;
				for (var system : System.values()) {
					var systemData = this.getSystemData(system);
					statValue = statValue.add(systemData.getStat(elementLocation, stat));
				}
				if (statValue.compareTo(BigDecimal.ZERO) > 0) {
					this.playerElementalStats.putIfAbsent(elementLocation, new HashMap<>());
					this.playerElementalStats.get(elementLocation).put(stat, statValue);
				}
			}
		}
		this.skills.knownSkills.clear();
		for (var elementLocation : this.playerElementalStats.keySet()) {
			var element = WuxiaRegistries.ELEMENTS.getValue(elementLocation);
			if (element == null) continue;
			if (this.playerElementalStats.get(elementLocation).containsKey(PlayerElementalStat.COMPREHENSION)) {
				var comprehension = this.playerElementalStats.get(elementLocation).get(PlayerElementalStat.COMPREHENSION);
				for (var skillAspect : element.skillAspects.keySet()) {
					var comprehensionRequired = element.skillAspects.get(skillAspect);
					if (comprehension.compareTo(comprehensionRequired) < 0) continue;
					this.skills.knownSkills.add(skillAspect);
				}
			}
		}
		for (var system : System.values()) {
			var systemData = this.getSystemData(system);
			this.skills.knownSkills.addAll(systemData.techniqueData.modifier.skills);
			this.skills.knownSkills.addAll(systemData.getStage().getSkillsAspects());
			systemData.calculateStats(this);
		}
	}

	@Override
	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		for (var stat : this.playerStats.keySet()) {
			if (!stat.isModifiable) continue;
			BigDecimal statValue = this.playerStats.get(stat);
			int scale = statValue.scale();
			statValue = statValue.setScale(Math.min(10, scale), RoundingMode.DOWN);
			tag.putString("stat-" + stat.name().toLowerCase(), statValue.toPlainString());
			this.playerStats.put(stat, statValue);
		}
		var elementStatsTag = new CompoundTag();
		for (var element : this.playerElementalStats.keySet()) {
			var currentElementStatsTag = new CompoundTag();
			for (var stat : PlayerElementalStat.values()) {
				if (!stat.isModifiable) continue;
				BigDecimal statValue = this.playerElementalStats.get(element).getOrDefault(stat, BigDecimal.ZERO);
				int scale = statValue.scale();
				statValue = statValue.setScale(Math.min(10, scale), RoundingMode.DOWN);
				currentElementStatsTag.putString("stat-" + stat.name().toLowerCase(),
						statValue.toPlainString());
				this.playerElementalStats.get(element).put(stat, statValue);
			}
			elementStatsTag.put("element-stats-" + element, currentElementStatsTag);
		}
		tag.put("elemental-stats", elementStatsTag);
		tag.put("body-data", getSystemData(System.BODY).serialize());
		tag.put("divine-data", getSystemData(System.DIVINE).serialize());
		tag.put("essence-data", getSystemData(System.ESSENCE).serialize());
		tag.put("aspect-data", this.aspects.serialize());
		tag.put("skills-data", this.skills.serialize());
		tag.putBoolean("semi-dead", this.semiDead);
		tag.putInt("semi-dead-time", this.semiDeadTime);
		if (this.formationCore != null) {
			var formationTag = new CompoundTag();
			formationTag.putInt("x", this.formationCore.getX());
			formationTag.putInt("y", this.formationCore.getY());
			formationTag.putInt("z", this.formationCore.getZ());
			tag.put("formation", formationTag);
		}
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
		this.playerElementalStats.clear();
		if (tag.contains("elemental-stats")) {
			var rawElementalStatsTag = tag.get("elemental-stats");
			if (rawElementalStatsTag instanceof CompoundTag elementalStatsTag) {
				for (var element : WuxiaRegistries.ELEMENTS.getKeys()) {
					if (elementalStatsTag.contains("element-stats-" + element)) {
						for (var stat : PlayerElementalStat.values()) {
							if (!stat.isModifiable) continue;
							CompoundTag elementTag = elementalStatsTag.getCompound("element-stats-" + element);
							if (elementTag.contains("stat-" + stat.name().toLowerCase())) {
								var value = elementTag.getString("stat-" + stat.name().toLowerCase());
								this.playerElementalStats.putIfAbsent(element, new HashMap<>());
								this.playerElementalStats.get(element).put(stat, new BigDecimal(value));
							}
						}
					}
				}
			}
		}
		if (tag.contains("body-data")) {
			getSystemData(System.BODY).deserialize(tag.getCompound("body-data"));
		}
		if (tag.contains("divine-data")) {
			getSystemData(System.DIVINE).deserialize(tag.getCompound("divine-data"));
		}
		if (tag.contains("essence-data")) {
			getSystemData(System.ESSENCE).deserialize(tag.getCompound("essence-data"));
		}
		if (tag.contains("aspect-data")) {
			this.aspects.deserialize(tag.getCompound("aspect-data"), this);
		}
		if (tag.contains("skills-data")) {
			this.skills.deserialize(tag.getCompound("skills-data"));
		}
		if (tag.contains("semi-dead")) {
			this.semiDead = tag.getBoolean("semi-dead");
		}
		if (tag.contains("semi-dead-time")) {
			this.semiDeadTime = tag.getInt("semi-dead-time");
		}
		if (tag.contains("formation")) {
			var formationTag = tag.getCompound("formation");
			int x = formationTag.getInt("x");
			int y = formationTag.getInt("y");
			int z = formationTag.getInt("z");
			this.formationCore = new BlockPos(x, y, z);
		} else {
			this.formationCore = null;
		}
		if (tag.contains("regulators")) {
			var regulatorsTag = tag.getCompound("regulators");
			if (regulatorsTag.contains("strength")) {
				this.strengthRegulator = regulatorsTag.getDouble("strength");
			}
			if (regulatorsTag.contains("agility")) {
				this.agilityRegulator = regulatorsTag.getDouble("agility");
			}
		}
		calculateStats();
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

	@Override
	public SkillContainer getSkills() {
		return skills;
	}

	@Override
	public boolean isCombat() {
		return combat;
	}

	@Override
	public void setCombat(boolean combat) {
		this.combat = combat;
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

	@Override
	public void setSemiDeadState(boolean state) {
		this.semiDead = state;
		this.semiDeadTime = 0;
	}

	@Override
	public void advanceSemiDead(int cooldown) {
		this.semiDeadTime++;
		if (this.semiDeadTime >= cooldown) {
			this.setSemiDeadState(false);
		}
	}

	@Override
	public boolean isSemiDead() {
		return this.semiDead;
	}

	@Override
	public int getSemiDeadTimer() {
		return this.semiDeadTime;
	}

	@Override
	public boolean isDivineSense() {
		return isDivineSense;
	}

	@Override
	public void setDivineSense(boolean divineSense) {
		isDivineSense = divineSense;
	}

	@Override
	public double getAgilityRegulator() {
		return agilityRegulator;
	}

	@Override
	public void setAgilityRegulator(double agilityRegulator) {
		this.agilityRegulator = agilityRegulator;
	}

	@Override
	public double getStrengthRegulator() {
		return strengthRegulator;
	}

	@Override
	public void setStrengthRegulator(double strengthRegulator) {
		this.strengthRegulator = strengthRegulator;
	}
}
