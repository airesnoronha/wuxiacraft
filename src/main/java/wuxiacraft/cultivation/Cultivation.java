package wuxiacraft.cultivation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.init.WuxiaRegistries;

import java.util.HashMap;

public class Cultivation implements ICultivation {

	/**
	 * The cultivation information for each system
	 */
	public HashMap<System, SystemContainer> systemCultivation;

	/**
	 * the current health of this character
	 */
	public double health;

	public Cultivation() {
		this.systemCultivation = new HashMap<>();
		this.systemCultivation.put(System.BODY, new SystemContainer(System.BODY));
		this.systemCultivation.put(System.DIVINE, new SystemContainer(System.DIVINE));
		this.systemCultivation.put(System.ESSENCE, new SystemContainer(System.ESSENCE));
	}

	@Override
	public double getHealth() {
		return health;
	}

	@Override
	public void setHealth(double health) {
		this.health = health;
	}

	@Override
	public SystemContainer getSystemData(System system) {
		return systemCultivation.get(system);
	}

	public double getMaxHealth() {
		double maxHealth = 10;
		for(var system : System.values()) {
			maxHealth += getSystemData(system).getStage().maxHealth;
		}
		return maxHealth;
	}

	public double getStrength() {
		double strength = 1;
		for(var system : System.values()) {
			strength += getSystemData(system).getStage().strength;
		}
		return strength;
	}

	public double getAgility() {
		double agility = 1;
		for(var system : System.values()) {
			agility += getSystemData(system).getStage().agility;
		}
		return agility;
	}

	public class SystemContainer {

		/**
		 * The Cultivation system this data belongs to
		 */
		public final System system;

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
		 * The constructor for this system cultivation stats
		 * @param system the system this belongs to
		 */
		public SystemContainer(System system) {
			this.system = system;
			this.cultivationBase = 0;
			this.foundation = 0;
			this.energy = 0;
			this.currentRealm = system.defaultRealm;
			this.currentStage = system.defaultStage;
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
		 * Attempts a breakthrough at this system
		 */
		public void attemptBreakthrough() {
			//TODO define chances of failure
		}

		/**
		 * Checks whether this system has enough energy for perhaps a certain action
		 * @param amount the amount of energy expected to be found
		 * @return true if this has energy
		 */
		public boolean hasEnergy(double amount) {
			return amount >= this.energy;
		}

		/**
		 * Consumes energy if it has energy
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

		public CompoundTag serialize() {
			CompoundTag tag = new CompoundTag();
			tag.putString("current_realm", this.currentRealm.toString());
			tag.putString("current_stage", this.currentStage.toString());
			tag.putDouble("cultivation_base", this.cultivationBase);
			tag.putDouble("foundation", this.foundation);
			tag.putDouble("energy", this.energy);
			return tag;
		}

		public void deserialize(CompoundTag tag) {
			this.currentRealm = new ResourceLocation(tag.getString("current_realm"));
			this.currentStage = new ResourceLocation(tag.getString("current_stage"));
			this.cultivationBase = tag.getDouble("cultivation_base");
			this.foundation = tag.getDouble("foundation");
			this.energy = tag.getDouble("energy");
		}
	}

	public enum System {
		BODY(new ResourceLocation(WuxiaCraft.MOD_ID, "body_mortal_realm"), new ResourceLocation(WuxiaCraft.MOD_ID, "body_mortal_stage")),
		DIVINE(new ResourceLocation(WuxiaCraft.MOD_ID, "divine_mortal_realm"), new ResourceLocation(WuxiaCraft.MOD_ID, "divine_mortal_stage")),
		ESSENCE(new ResourceLocation(WuxiaCraft.MOD_ID, "essence_mortal_realm"), new ResourceLocation(WuxiaCraft.MOD_ID, "essence_mortal_stage"));

		public final ResourceLocation defaultRealm;
		public final ResourceLocation defaultStage;

		System(ResourceLocation defaultRealm, ResourceLocation defaultStage) {
			this.defaultRealm = defaultRealm;
			this.defaultStage = defaultStage;
		}
	}

}
