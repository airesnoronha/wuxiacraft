package wuxiacraft.cultivation;

public class SystemStats {

	/**
	 * The cultivation level in this system
	 */
	private CultivationLevel level; // major realm

	/**
	 * The sub level, or rank, in this system, starting from 0
	 */
	private int subLevel; //rank

	/**
	 * The cultivation base, aka experience
	 */
	private double base; //cultivation base -- xp

	/**
	 * The foundation, that will make the character stronger
	 */
	private double foundation;

	/**
	 * This class will hold basic information for each system, since it's repeated through system
	 */
	public SystemStats() {
		this.level = CultivationLevel.DEFAULT_BODY_LEVEL;
		this.subLevel = 0;
		this.base = 0;
		this.foundation = 0;
	}

	public SystemStats(CultivationLevel.System system) {
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

	/**
	 * This adds cultivation base to this system
	 * @param amount The amount to be added
	 */
	public void addBase(double amount) {
		this.base = Math.max(0, amount + this.base);
	}

	/**
	 * @return How much the cultivation in this system will add power to the character
	 */
	public double getModifier() {
		return Math.max(0, this.getLevel().getModifierBySubLevel(this.getSubLevel()) *
				(0.4 + Math.min(21, (this.getFoundation() / this.getLevel().getProgressBySubLevel(this.getSubLevel()))) * 0.6));
	}

	/**
	 * This will copy stats from another stats instance
	 * @param stats the instance to be copied from
	 */
	public void copyFrom(SystemStats stats) {
		this.level = stats.level;
		this.foundation = stats.foundation;
		this.subLevel = stats.subLevel;
		this.base = stats.base;
	}
}
