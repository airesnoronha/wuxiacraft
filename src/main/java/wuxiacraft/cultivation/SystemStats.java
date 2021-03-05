package wuxiacraft.cultivation;

import net.minecraft.nbt.CompoundNBT;

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
	 * The energy to cast spells and stuff
	 */
	private double energy;

	/**
	 * This class will hold basic information for each system, since it's repeated through system
	 */
	public SystemStats(CultivationLevel.System system) {
		switch (system) {
			case BODY:
				this.level = CultivationLevel.DEFAULT_BODY_LEVEL;
				this.energy = 5;
				break;
			case DIVINE:
				this.level = CultivationLevel.DEFAULT_DIVINE_LEVEL;
				this.energy = 10;
				break;
			case ESSENCE:
				this.level = CultivationLevel.DEFAULT_ESSENCE_LEVEL;
				this.energy = 0;
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

	public double getEnergy() {
		return energy;
	}

	public void setEnergy(double energy) {
		this.energy = energy;
	}

	/**
	 * This adds cultivation base to this system
	 *
	 * @param amount The amount to be added
	 */
	public void addBase(double amount) {
		this.base = Math.max(0, amount + this.base);
	}

	public void addEnergy(double amount) {
		this.energy = Math.max(0, this.energy + amount);
	}

	/**
	 * @return How much the cultivation in this system will add power to the character
	 */
	public double getModifier() {
		// Since foundation will have some part in cultivation base as well
		double foundationAmount = this.getFoundation() + this.getBase();
		return Math.max(0, this.getLevel().getModifierBySubLevel(this.getSubLevel()) *
				(0.4 + Math.min(21, (foundationAmount / this.getLevel().getBaseBySubLevel(this.getSubLevel()))) * 0.6));
	}

	/**
	 * This will copy stats from another stats instance
	 *
	 * @param stats the instance to be copied from
	 */
	public void copyFrom(SystemStats stats) {
		this.level = stats.level;
		this.foundation = stats.foundation;
		this.subLevel = stats.subLevel;
		this.base = stats.base;
		this.energy = stats.energy;
	}

	public CompoundNBT writeToNBT() {
		CompoundNBT tag = new CompoundNBT();
		tag.putString("level", this.getLevel().levelName);
		tag.putInt("subLevel", this.getSubLevel());
		tag.putDouble("cultBase", this.getBase());
		tag.putDouble("foundation", this.getFoundation());
		tag.putDouble("energy", this.getEnergy());
		return tag;
	}

	public void readFromNBT(CompoundNBT tag, CultivationLevel.System system) {
		this.setLevel(CultivationLevel.getLevelBySystem(system, tag.getString("level")));
		this.setSubLevel(tag.getInt("subLevel"));
		this.setBase(tag.getDouble("cultBase"));
		this.setFoundation(tag.getDouble("foundation"));
		this.setEnergy(tag.getDouble("energy"));
	}
}
