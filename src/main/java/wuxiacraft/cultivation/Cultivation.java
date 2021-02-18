package wuxiacraft.cultivation;

import javax.annotation.Nonnull;

public class Cultivation implements ICultivation {

	public static class SystemStats {

		private CultivationLevel level; // major realm
		private int subLevel; //rank
		private double base; //base -- xp
		private double foundation;

		public SystemStats () {
			this.level = CultivationLevel.DEFAULT_BODY_LEVEL;
			this.subLevel = 0;
			this.base = 0;
			this.foundation = 0;
		}

		public SystemStats (CultivationLevel.System system) {
			switch (system) {
				case BODY:
					this.level = CultivationLevel.DEFAULT_BODY_LEVEL;
					break;
				case DIVINE:
					this.level = CultivationLevel.DEFAULT_DIVINE_LEVEL;
					break;
				case ESSENCE:
					this.level = CultivationLevel.DEFAULT_ESSENCE_LEVEL;
					break;
			}
			this.level = CultivationLevel.DEFAULT_BODY_LEVEL;
			this.subLevel = 0;
			this.base = 0;
			this.foundation = 0;
		}

		public CultivationLevel getLevel() {
			return level;
		}

		public void setLevel(CultivationLevel level) {
			this.level = level;
		}

		public int getSubLevel() {
			return subLevel;
		}

		public void setSubLevel(int subLevel) {
			this.subLevel = subLevel;
		}

		public double getBase() {
			return base;
		}

		public void setBase(double base) {
			this.base = base;
		}

		public double getFoundation() {
			return foundation;
		}

		public void setFoundation(double foundation) {
			this.foundation = foundation;
		}

		public void addBase(double amount) {
			this.base = Math.max(0, amount + this.base);
		}

		public double getModifier() {
			return Math.max(0, this.getLevel().getModifierBySubLevel(this.getSubLevel()) *
			(0.4 + Math.min(21, (this.getFoundation() / this.getLevel().getProgressBySubLevel(this.getSubLevel()))) * 0.6));
		}

		public void copyFrom(SystemStats stats) {
			this.level = stats.level;
			this.foundation = stats.foundation;
			this.subLevel = stats.subLevel;
			this.base = stats.base;
		}
	}

	private double energy;

	private SystemStats bodyStats;
	private SystemStats divineStats;
	private SystemStats essenceStats;

	@Override
	public double getEnergy() {
		return energy;
	}

	@Override
	public void setEnergy(double energy) {
		this.energy = energy;
	}

	public double getMaxEnergy(double energy) {
		return 18 * this.getEssenceModifier() + 8 * this.getBodyModifier() + 12 * this.getDivineModifier();
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
		if(this.essenceStats.getLevel() == CultivationLevel.DEFAULT_ESSENCE_LEVEL) { //start cultivation
			double beforeFoundationOverBase = this.essenceStats.getFoundation() / this.essenceStats.getLevel().getProgressBySubLevel(this.essenceStats.getSubLevel());
			this.bodyStats.setLevel(this.bodyStats.level.nextLevel(CultivationLevel.BODY_LEVELS));
			this.divineStats.setLevel(this.bodyStats.level.nextLevel(CultivationLevel.DIVINE_LEVELS));
			this.essenceStats.setLevel(this.bodyStats.level.nextLevel(CultivationLevel.ESSENCE_LEVELS));
			this.essenceStats.setFoundation(this.essenceStats.getLevel().getProgressBySubLevel(0)*beforeFoundationOverBase);
			this.bodyStats.setFoundation(this.essenceStats.getFoundation());
			this.divineStats.setFoundation(this.essenceStats.getFoundation());
		}
		else { //rise sub level
			double beforeModifier = stats.getModifier();
			stats.setBase(0);
			stats.setSubLevel(stats.getSubLevel()+1);
			if(stats.getSubLevel() >= stats.getLevel().subLevels){
				stats.setLevel(stats.getLevel().nextLevel(CultivationLevel.getListBySystem(system)));
				stats.setSubLevel(0);
				// probably the current modifier growth rate is 1.2, this way there is a little loss on foundation
				double modifierDifference = beforeModifier * 1.19 - stats.getModifier();
				if(modifierDifference > 0) { //then correct the foundation there is actually at leas 19% increase in strength i hope
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
		switch(system) {
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
	public void copyFrom(ICultivation cultivation) {
		this.bodyStats.copyFrom(cultivation.getStatsBySystem(CultivationLevel.System.BODY));
		this.divineStats.copyFrom(cultivation.getStatsBySystem(CultivationLevel.System.DIVINE));
		this.essenceStats.copyFrom(cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE));
	}

	public Cultivation () {
		this.bodyStats = new SystemStats(CultivationLevel.System.BODY);
		this.divineStats = new SystemStats(CultivationLevel.System.DIVINE);
		this.essenceStats = new SystemStats(CultivationLevel.System.ESSENCE);
	}

}
