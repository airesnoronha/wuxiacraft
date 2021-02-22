package wuxiacraft.cultivation;

import net.minecraft.entity.LivingEntity;
import wuxiacraft.capabilities.CultivationProvider;
import wuxiacraft.cultivation.technique.Technique;
import wuxiacraft.cultivation.technique.TechniqueModifiers;
import wuxiacraft.util.MathUtils;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class Cultivation implements ICultivation {

	/**
	 * Gets the cultivation capability from an enetity
	 *
	 * @param entity the target entity
	 * @return the entity's cultivaiton
	 */
	@Nonnull
	public static ICultivation get(LivingEntity entity) {
		return entity.getCapability(CultivationProvider.CULTIVATION_PROVIDER, null).orElse(new Cultivation());
	}

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
	 * Character current HP
	 */
	private double HP;

	/**
	 * A Timer to sync stuff between client and server
	 */
	private int TickerTime = 0;

	/**
	 * This will record all the modifiers from a player throughout a tick, so that every tick it's only calculated once
	 */
	private TechniqueModifiers finalModifiers = new TechniqueModifiers(0,0,20, 0, 0, 0);

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
		this.HP = 20;
	}

	@Override
	public int getTickerTime() {
		return TickerTime;
	}

	@Override
	public void advanceTimer() {
		TickerTime++;
		if (TickerTime > 100) TickerTime = 0;
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
	public double getHP() {
		return HP;
	}

	@Override
	public void setHP(double HP) {
		this.HP = HP;
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
	public double getResistanceToElement(Element element) {
		double resistance = 0;
		if (this.bodyTechnique != null) {
			for (Element resistor : this.bodyTechnique.getTechnique().getElements()) {
				if (resistor.getOvercomesList().contains(element)) {
					resistance += this.bodyTechnique.getReleaseFactor() * this.getBodyModifier();
				} else if (resistor == element) {
					resistance += this.bodyTechnique.getReleaseFactor() * 0.5 * this.getBodyModifier();
				}
				//this can wrong in so many ways, probably noobs will die if they meet their counter in one hit
				else if (resistor.getBegetsList().contains(element)) {
					resistance -= this.getBodyModifier() * (1.2 - this.bodyTechnique.getReleaseFactor());
				}
			}
		}
		return resistance;
	}

	@Override
	public void calculateFinalModifiers() {
		this.finalModifiers = new TechniqueModifiers(
				this.getBodyModifier() * 0.7 + this.getDivineModifier() * 0.1 + this.getEssenceModifier() * 0.7,
				this.getBodyModifier() * 0.4 + this.getDivineModifier() * 0.1 + this.getEssenceModifier() * 0.8,
				20 + this.getBodyModifier() + this.getDivineModifier() * 0.4 + this.getEssenceModifier() * 0.1,
				this.getBodyModifier() * 0.2 + this.getDivineModifier() * 0.1 + this.getEssenceModifier() * 0.4,
				this.getBodyModifier() * 0.8 + this.getDivineModifier() * 0.6 + this.getEssenceModifier() * 0.14,
				this.getBodyModifier() * 8 + this.getDivineModifier() * 12 + this.getEssenceModifier() * 18);
		if (this.bodyTechnique != null)
			this.finalModifiers = this.finalModifiers.add(this.bodyTechnique.getModifiers());
		if (this.divineTechnique != null)
			this.finalModifiers = this.finalModifiers.add(this.divineTechnique.getModifiers());
		if (this.essenceTechnique != null)
			this.finalModifiers = this.finalModifiers.add(this.essenceTechnique.getModifiers());
	}

	@Override
	public TechniqueModifiers getFinalModifiers() {
		return this.finalModifiers;
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
		this.HP = cultivation.getHP();
	}
}
