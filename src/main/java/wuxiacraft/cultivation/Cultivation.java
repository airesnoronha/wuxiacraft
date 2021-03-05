package wuxiacraft.cultivation;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.LivingEntity;
import wuxiacraft.capabilities.CultivationProvider;
import wuxiacraft.cultivation.skill.Skill;
import wuxiacraft.cultivation.technique.Technique;
import wuxiacraft.cultivation.technique.TechniqueModifiers;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.LinkedList;
import java.util.List;

public class Cultivation implements ICultivation {

	/**
	 * Gets the cultivation capability from an entity
	 *
	 * @param entity the target entity
	 * @return the entity's cultivation
	 */
	@Nonnull
	public static ICultivation get(LivingEntity entity) {
		return entity.getCapability(CultivationProvider.CULTIVATION_PROVIDER, null).orElse(new Cultivation());
	}

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
	 * A List of the skills that are known
	 */
	private final List<Skill> knownSkills;

	/**
	 * This has to be int because it'll also select from techniques
	 */
	private final List<Integer> selectedSkills;

	/**
	 * the current skill cooldown
	 */
	private double skillCooldown;

	//Helpers rather than stats

	/**
	 * A Timer to sync stuff between client and server
	 */
	private int TickerTime = 0;

	/**
	 * This will record all the modifiers from a player throughout a tick, so that every tick it's only calculated once
	 */
	private TechniqueModifiers finalModifiers = new TechniqueModifiers(0, 20, 0, 0);

	/**
	 * max energy for each system, calculated every tick
	 */
	private double maxBodyEnergy;
	private double maxDivineEnergy;
	private double maxEssenceEnergy;

	private double bodyEnergyRegen;
	private double divineEnergyRegen;
	private double essenceEnergyRegen;

	private double healingAmount;
	private double healingCost;

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
		this.knownSkills = new LinkedList<>();
		this.selectedSkills = new LinkedList<>();
		this.skillCooldown = 0;
		this.HP = 20;
	}

	@Override
	public int getTickerTime() {
		return TickerTime;
	}

	@Override
	public void resetTickerTimer() {
		this.TickerTime = 0;
	}

	@Override
	public void advanceTimer() {
		TickerTime++;
		if (TickerTime > 100) TickerTime = 0;
	}

	@Override
	public double getBodyModifier() {
		return this.getStatsBySystem(CultivationLevel.System.BODY).getModifier() + this.getStatsBySystem(CultivationLevel.System.ESSENCE).getModifier() * 0.05;
	}

	@Override
	public double getDivineModifier() {
		return this.getStatsBySystem(CultivationLevel.System.DIVINE).getModifier() + this.getStatsBySystem(CultivationLevel.System.BODY).getModifier() * 0.05;
	}

	@Override
	public double getEssenceModifier() {
		return this.getStatsBySystem(CultivationLevel.System.ESSENCE).getModifier() + this.getStatsBySystem(CultivationLevel.System.DIVINE).getModifier() * 0.05;
	}

	@Override
	public void advanceRank(CultivationLevel.System system) {
		SystemStats stats = getStatsBySystem(system);
		if (this.essenceStats.getLevel() == CultivationLevel.DEFAULT_ESSENCE_LEVEL) { //start cultivation
			double beforeFoundationOverBase = this.essenceStats.getFoundation() / this.essenceStats.getLevel().getBaseBySubLevel(this.essenceStats.getSubLevel());
			this.bodyStats.setLevel(this.bodyStats.getLevel().nextLevel(CultivationLevel.BODY_LEVELS));
			this.divineStats.setLevel(this.bodyStats.getLevel().nextLevel(CultivationLevel.DIVINE_LEVELS));
			this.essenceStats.setLevel(this.bodyStats.getLevel().nextLevel(CultivationLevel.ESSENCE_LEVELS));
			this.essenceStats.setFoundation(this.essenceStats.getLevel().getBaseBySubLevel(0) * beforeFoundationOverBase);
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
				if (modifierDifference < 0) { //then correct the foundation there is actually at leas 19% increase in strength i hope
					stats.setFoundation(stats.getFoundation() + stats.getLevel().getBaseBySubLevel(stats.getSubLevel()) * modifierDifference /
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
				break;
			case DIVINE:
				this.divineTechnique = new KnownTechnique(technique, proficiency);
				break;
			case ESSENCE:
				this.essenceTechnique = new KnownTechnique(technique, proficiency);
				break;
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
	public List<Skill> getKnownSkills() {
		return ImmutableList.copyOf(knownSkills);
	}

	@Override
	public void addKnownSkill(Skill skill) {
		this.knownSkills.add(skill);
	}

	@Override
	public void resetKnownSkills() {
		this.knownSkills.clear();
	}

	public boolean removeKnownSkill(Skill skill) {
		return this.knownSkills.remove(skill);
	}

	@Override
	public List<Integer> getSelectedSkills() {
		return selectedSkills;
	}

	@Override
	public double getSkillCooldown() {
		return skillCooldown;
	}

	@Override
	public void setSkillCooldown(double skillCooldown) {
		this.skillCooldown = skillCooldown;
	}

	@Override
	public List<Skill> getAllKnownSkills() {
		List<Skill> techniqueSkills = new LinkedList<>();
		if (this.bodyTechnique != null)
			techniqueSkills.addAll(this.bodyTechnique.getKnownSkills());
		if (this.divineTechnique != null)
			techniqueSkills.addAll(this.divineTechnique.getKnownSkills());
		if (this.essenceTechnique != null)
			techniqueSkills.addAll(this.essenceTechnique.getKnownSkills());
		techniqueSkills.addAll(this.knownSkills);
		return ImmutableList.copyOf(techniqueSkills);
	}

	@Override
	public Skill getActiveSkill(int id) {
		List<Skill> knownSkills = this.getAllKnownSkills();
		if (id < this.selectedSkills.size()) {
			if (this.selectedSkills.get(id) < knownSkills.size()) {
				return knownSkills.get(this.selectedSkills.get(id));
			}
		}
		return null;
	}

	@Override
	public double getCastSpeed() {
		return 1;
	}

	@Override
	public void lowerCoolDown() {
		this.skillCooldown = Math.max(0, this.skillCooldown - 1);
	}

	@Override
	public void calculateFinalModifiers() {
		this.finalModifiers = new TechniqueModifiers(
				this.getBodyModifier() * 0.4 + this.getDivineModifier() * 0.1 + this.getEssenceModifier() * 0.8,
				20 + this.getBodyModifier() + this.getDivineModifier() * 0.4 + this.getEssenceModifier() * 0.1,
				this.getBodyModifier() * 0.2 + this.getDivineModifier() * 0.1 + this.getEssenceModifier() * 0.4,
				this.getBodyModifier() * 0.8 + this.getDivineModifier() * 0.6 + this.getEssenceModifier() * 0.14);
		this.maxBodyEnergy = 10 + this.getBodyModifier() * 10;
		this.maxDivineEnergy = 10 + this.getDivineModifier() * 10;
		this.maxEssenceEnergy = 10 + this.getEssenceModifier() * 10;
		this.bodyEnergyRegen = 0.00035; //after all it uses food
		this.divineEnergyRegen = 0.00007;
		this.essenceEnergyRegen = 0.000005; //this will gather when doing breathing exercises aka cultivating
		this.healingAmount = 0.0075;
		this.healingCost = 1.8;
		if (this.bodyTechnique != null) {
			this.finalModifiers = this.finalModifiers.add(this.bodyTechnique.getModifiers());
			this.bodyEnergyRegen *= (1 + this.bodyTechnique.getEnergyRegen());
			this.healingAmount *= (1 + this.bodyTechnique.getHealingAmountModifier());
			this.healingCost *= (1 + this.bodyTechnique.getHealingCostModifier());
		}
		if (this.divineTechnique != null) {
			this.finalModifiers = this.finalModifiers.add(this.divineTechnique.getModifiers());
			this.divineEnergyRegen *= (1 + this.divineTechnique.getEnergyRegen());
			this.healingAmount *= (1 + this.divineTechnique.getHealingAmountModifier());
			this.healingCost *= (1 + this.divineTechnique.getHealingCostModifier());
		}
		if (this.essenceTechnique != null) {
			this.finalModifiers = this.finalModifiers.add(this.essenceTechnique.getModifiers());
			this.essenceEnergyRegen *= (1 + this.essenceTechnique.getEnergyRegen());
			this.healingAmount *= (1 + this.essenceTechnique.getHealingAmountModifier());
			this.healingCost *= (1 + this.essenceTechnique.getHealingCostModifier());
		}
	}

	@Override
	public TechniqueModifiers getFinalModifiers() {
		return this.finalModifiers;
	}

	@Override
	public double getMaxBodyEnergy() {
		return maxBodyEnergy;
	}

	@Override
	public double getMaxDivineEnergy() {
		return maxDivineEnergy;
	}

	@Override
	public double getMaxEssenceEnergy() {
		return maxEssenceEnergy;
	}

	@Override
	public double getBodyEnergyRegen() {
		return bodyEnergyRegen;
	}

	@Override
	public double getDivineEnergyRegen() {
		return divineEnergyRegen;
	}

	@Override
	public double getEssenceEnergyRegen() {
		return essenceEnergyRegen;
	}

	@Override
	public double getHealingAmount() {
		return healingAmount;
	}

	@Override
	public double getHealingCost() {
		return healingCost;
	}

	@Override
	public void copyFrom(ICultivation cultivation) {
		this.bodyStats.copyFrom(cultivation.getStatsBySystem(CultivationLevel.System.BODY));
		this.divineStats.copyFrom(cultivation.getStatsBySystem(CultivationLevel.System.DIVINE));
		this.essenceStats.copyFrom(cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE));
		this.bodyTechnique = cultivation.getTechniqueBySystem(CultivationLevel.System.BODY);
		this.divineTechnique = cultivation.getTechniqueBySystem(CultivationLevel.System.DIVINE);
		this.essenceTechnique = cultivation.getTechniqueBySystem(CultivationLevel.System.ESSENCE);
		this.knownSkills.clear();
		this.knownSkills.addAll(cultivation.getAllKnownSkills());
		this.selectedSkills.clear();
		this.selectedSkills.addAll(cultivation.getSelectedSkills());
		this.skillCooldown = cultivation.getSkillCooldown();
		this.HP = cultivation.getHP();
	}
}
