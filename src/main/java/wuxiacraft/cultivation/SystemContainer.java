package wuxiacraft.cultivation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import wuxiacraft.init.WuxiaRegistries;

import javax.annotation.Nullable;

public class SystemContainer {

	/**
	 * The Cultivation system this data belongs to
	 */
	public final Cultivation.System system;

	/**
	 * The amount of qi this player has accumulated
	 */

	public double cultivationBase;

	/**
	 * The foundation built by this character
	 */
	public double foundation;

	/**
	 * The energy available to make magical stuff
	 */
	public double energy;

	/**
	 * The current realm id for this cultivation
	 */
	public ResourceLocation currentRealm;

	/**
	 * The current stage id for this cultivation
	 */
	public ResourceLocation currentStage;

	/**
	 * Proficiency in the technique
	 */
	@Nullable
	public ResourceLocation technique;

	/**
	 * Proficiency in the technique
	 * private because each time we change it we will calculate the proficiency modifier
	 */
	private double proficiency;

	/**
	 * this is also private, this is here for fast access basically,
	 * not just querying it everytime and calculate it again
	 */
	private double techniqueModifier;

	/**
	 * The constructor for this system cultivation stats
	 *
	 * @param system the system this belongs to
	 */
	public SystemContainer(Cultivation.System system) {
		this.system = system;
		this.cultivationBase = 0;
		this.foundation = 0;
		this.energy = 0;
		this.currentRealm = system.defaultRealm;
		this.currentStage = system.defaultStage;
		this.technique = null;
		this.proficiency = 0;
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

	/**
	 * @return the current cultivated technique for this system. Null if none
	 */
	@Nullable
	public Technique getTechnique() {
		if (this.technique == null) return null;
		return WuxiaRegistries.TECHNIQUES.getValue(this.technique);
	}

	/**
	 * adds cultivation base to a character
	 *
	 * @param amount the amount to be added
	 */
	public void addCultivationBase(double amount) {
		if (amount > 0) {
			this.cultivationBase += amount;
			this.foundation += amount * 0.001;
		}
	}

	/**
	 * @return the current proficiency at this technique
	 */
	public double getProficiency() {
		return proficiency;
	}

	/**
	 * Set's the current technique proficiency
	 * Also updates synchronously the technique modifier as well, so we don't keep interpolating on each query of the modifier
	 * @param proficiency the new proficiency to be set
	 */
	public void setProficiency(double proficiency) {
		this.proficiency = Math.max(0, proficiency);
		var technique = this.getTechnique();
		if(technique != null) {
			var checkpoint = technique.getCheckpoint(this.proficiency);
			if(checkpoint.nextCheckpoint() != null) {
				var nextCheckpoint = technique.checkpoints.get(checkpoint.nextCheckpoint());
				var factor = (proficiency - checkpoint.proficiency()) / (nextCheckpoint.proficiency() - checkpoint.proficiency());
				this.techniqueModifier = checkpoint.modifier() + (nextCheckpoint.modifier() - checkpoint.modifier()) * factor;
			}
			else {
				this.techniqueModifier = checkpoint.modifier();
			}
		}
	}

	public double getTechniqueModifier() {
		return techniqueModifier;
	}

	/**
	 * Attempts a breakthrough at this system
	 */
	public void attemptBreakthrough() {
		//TODO define chances of failure
	}

	/**
	 * Checks whether this system has enough energy for perhaps a certain action
	 *
	 * @param amount the amount of energy expected to be found
	 * @return true if this has energy
	 */
	public boolean hasEnergy(double amount) {
		return amount >= this.energy;
	}

	/**
	 * Consumes energy if it has energy
	 *
	 * @param amount the amount of energy it must consume
	 * @return true if the energy has been consumed
	 */
	public boolean consumeEnergy(double amount) {
		if (hasEnergy(amount)) {
			this.energy -= amount;
			return true;
		}
		return false;
	}

	/**
	 * @return the maximum energy this system has. Depends on technique.
	 */
	public double getMaxEnergy() {
		return this.getStage().maxEnergy;
	}


	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		tag.putString("current_realm", this.currentRealm.toString());
		tag.putString("current_stage", this.currentStage.toString());
		tag.putDouble("cultivation_base", this.cultivationBase);
		tag.putDouble("foundation", this.foundation);
		tag.putDouble("energy", this.energy);
		tag.putString("technique", this.technique != null ? this.technique.toString() : "null");
		tag.putDouble("proficiency", this.proficiency);
		return tag;
	}

	public void deserialize(CompoundTag tag) {
		this.currentRealm = new ResourceLocation(tag.getString("current_realm"));
		this.currentStage = new ResourceLocation(tag.getString("current_stage"));
		this.cultivationBase = tag.getDouble("cultivation_base");
		this.foundation = tag.getDouble("foundation");
		this.energy = tag.getDouble("energy");
		String tagTechnique = tag.getString("technique");
		if (!tagTechnique.equals("null")) {
			this.technique = new ResourceLocation(tagTechnique);
		}
		//mainly because of calculating the technique modifier
		this.setProficiency(tag.getDouble("proficiency"));
	}

}
