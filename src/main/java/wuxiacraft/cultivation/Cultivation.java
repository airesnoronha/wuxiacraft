package wuxiacraft.cultivation;

import wuxiacraft.cultivation.technique.Technique;
import wuxiacraft.util.MathUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Cultivation implements ICultivation {

	/**
	 * Or qi, is the energy used to make miracles, spells and anything supernatural i guess
	 */
	private double energy;

	/**
	 * The stats for the body cultivation system
	 */
	private final SystemStats bodyStats;

	/**
	 * The stats for the divine cultivation system
	 */
	private final SystemStats divineStats;

	/**
	 * The stats for the essence cultivation system
	 */
	private final SystemStats essenceStats;

	/**
	 * The current technique being trained for body
	 */
	@Nullable
	private KnownTechnique bodyTechnique;

	/**
	 * The current technique being trained for divinity
	 */
	@Nullable
	private KnownTechnique divineTechnique;

	/**
	 * The current technique being trained for essence
	 */
	@Nullable
	private KnownTechnique essenceTechnique;

	/**
	 * A Timer to sync stuff between client and server
	 */
	private int TickerTime = 0;
	/**
	 * This is the base for the cultivation, here will hold most of the information from cultivation
	 */
	public Cultivation() {
		this.bodyStats = new SystemStats(CultivationLevel.System.BODY);
		this.divineStats = new SystemStats(CultivationLevel.System.DIVINE);
		this.essenceStats = new SystemStats(CultivationLevel.System.ESSENCE);
		this.bodyTechnique = null;
		this.divineTechnique = null;
		this.essenceTechnique = null;
	}

	@Override
	public int getTickerTime() {
		return TickerTime;
	}

	@Override
	public void advanceTimer() {
		TickerTime++;
		if(TickerTime > 100) TickerTime = 0;
	}

	@Override
	public double getEnergy() {
		return energy;
	}

	@Override
	public void addEnergy(double amount) {
		this.setEnergy(this.energy + amount);
	}

	@Override
	public void setEnergy(double energy) {
		this.energy = MathUtils.clamp(energy, 0, this.getMaxEnergy());
	}

	@Override
	public double getMaxEnergy() {
		double techniquesMultipliers = 1;
		if (this.bodyTechnique != null) techniquesMultipliers += bodyTechnique.getModifiers().maxEnergy;
		if (this.divineTechnique != null) techniquesMultipliers += divineTechnique.getModifiers().maxEnergy;
		if (this.essenceTechnique != null) techniquesMultipliers += essenceTechnique.getModifiers().maxEnergy;
		return (18 * this.getEssenceModifier() + 8 * this.getBodyModifier() + 12 * this.getDivineModifier()) * techniquesMultipliers;
	}

	@Override
	public double getBodyModifier() {
		return this.getStatsBySystem(CultivationLevel.System.BODY).getModifier();
	}

	@Override
	public double getDivineModifier() {
		return this.getStatsBySystem(CultivationLevel.System.DIVINE).getModifier();
	}

	@Override
	public double getEssenceModifier() {
		return this.getStatsBySystem(CultivationLevel.System.ESSENCE).getModifier();
	}

	@Override
	public void advanceRank(CultivationLevel.System system) {
		SystemStats stats = getStatsBySystem(system);
		if (this.essenceStats.getLevel() == CultivationLevel.DEFAULT_ESSENCE_LEVEL) { //start cultivation
			double beforeFoundationOverBase = this.essenceStats.getFoundation() / this.essenceStats.getLevel().getProgressBySubLevel(this.essenceStats.getSubLevel());
			this.bodyStats.setLevel(this.bodyStats.getLevel().nextLevel(CultivationLevel.BODY_LEVELS));
			this.divineStats.setLevel(this.bodyStats.getLevel().nextLevel(CultivationLevel.DIVINE_LEVELS));
			this.essenceStats.setLevel(this.bodyStats.getLevel().nextLevel(CultivationLevel.ESSENCE_LEVELS));
			this.essenceStats.setFoundation(this.essenceStats.getLevel().getProgressBySubLevel(0) * beforeFoundationOverBase);
			this.bodyStats.setFoundation(this.essenceStats.getFoundation());
			this.divineStats.setFoundation(this.essenceStats.getFoundation());
		} else { //rise sub level
			double beforeModifier = stats.getModifier();
			stats.setBase(0);
			stats.setSubLevel(stats.getSubLevel() + 1);
			if (stats.getSubLevel() >= stats.getLevel().subLevels) {
				stats.setLevel(stats.getLevel().nextLevel(CultivationLevel.getListBySystem(system)));
				stats.setSubLevel(0);
				// probably the current modifier growth rate is 1.2, this way there is a little loss on foundation
				double modifierDifference = beforeModifier * 1.19 - stats.getModifier();
				if (modifierDifference > 0) { //then correct the foundation there is actually at leas 19% increase in strength i hope
					stats.setFoundation(stats.getFoundation() + stats.getLevel().getProgressBySubLevel(stats.getSubLevel()) * modifierDifference /
							(0.6 * stats.getLevel().getModifierBySubLevel(stats.getSubLevel())));
				}
			}
		}
	}

	@Override
	public void addBaseToSystem(double amount, CultivationLevel.System system) {
		this.getStatsBySystem(system).addBase(amount);
	}

	@Override
	@Nonnull
	public SystemStats getStatsBySystem(CultivationLevel.System system) {
		switch (system) {
			case BODY:
				return this.bodyStats;
			case DIVINE:
				return this.divineStats;
			case ESSENCE:
				return this.essenceStats;
		}
		return this.bodyStats;
	}

	@Override
	@Nullable
	public KnownTechnique getTechniqueBySystem(CultivationLevel.System system) {
		switch (system) {
			case BODY:
				return this.bodyTechnique;
			case DIVINE:
				return this.divineTechnique;
			case ESSENCE:
				return this.essenceTechnique;
		}
		return null;
	}

	@Override
	public void addTechnique(Technique technique) {
		switch (technique.getSystem()) {
			case BODY:
				this.bodyTechnique = new KnownTechnique(technique, 0);
			case DIVINE:
				this.divineTechnique = new KnownTechnique(technique, 0);
			case ESSENCE:
				this.essenceTechnique = new KnownTechnique(technique, 0);
		}
	}

	@Override
	public void setKnownTechnique(Technique technique, double proficiency) {
		switch (technique.getSystem()) {
			case BODY:
				this.bodyTechnique = new KnownTechnique(technique, proficiency);
			case DIVINE:
				this.divineTechnique = new KnownTechnique(technique, proficiency);
			case ESSENCE:
				this.essenceTechnique = new KnownTechnique(technique, proficiency);
		}
	}

	@Override
	public void copyFrom(ICultivation cultivation) {
		this.bodyStats.copyFrom(cultivation.getStatsBySystem(CultivationLevel.System.BODY));
		this.divineStats.copyFrom(cultivation.getStatsBySystem(CultivationLevel.System.DIVINE));
		this.essenceStats.copyFrom(cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE));
		this.bodyTechnique = cultivation.getTechniqueBySystem(CultivationLevel.System.BODY);
		this.divineTechnique = cultivation.getTechniqueBySystem(CultivationLevel.System.DIVINE);
		this.essenceTechnique = cultivation.getTechniqueBySystem(CultivationLevel.System.ESSENCE);
		this.energy = cultivation.getEnergy();
	}
}
