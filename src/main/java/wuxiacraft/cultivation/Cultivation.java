package wuxiacraft.cultivation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.capabilities.cultivation.CultivationProvider;

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

	/**
	 * this is for sync with the client and probably vice versa
	 * a substitute for this could've been entity.ticksAlive
	 * but that is not among us anymore
	 */
	private int tickTimer;

	public Cultivation() {
		this.systemCultivation = new HashMap<>();
		this.systemCultivation.put(System.BODY, new SystemContainer(System.BODY));
		this.systemCultivation.put(System.DIVINE, new SystemContainer(System.DIVINE));
		this.systemCultivation.put(System.ESSENCE, new SystemContainer(System.ESSENCE));
	}

	public static ICultivation get(Player target) {
		return target.getCapability(CultivationProvider.CULTIVATION_PROVIDER).orElse(new Cultivation());
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

	@Override
	public double getMaxHealth() {
		double maxHealth = 10;
		for(var system : System.values()) {
			maxHealth += getSystemData(system).getMaxHealth();
		}
		return maxHealth;
	}

	@Override
	public double getStrength() {
		double strength = 0; //punches already have 1 we only add to it
		for(var system : System.values()) {
			strength += getSystemData(system).getStrength();
		}
		return strength;
	}

	@Override
	public double getAgility() {
		double agility = 1;
		for(var system : System.values()) {
			var systemData = getSystemData(system);
			agility += systemData.getAgility();
		}
		return agility;
	}

	@Override
	public CompoundTag serialize() {
		CompoundTag tag = new CompoundTag();
		tag.putDouble("health", this.health);
		tag.put("body-data", getSystemData(System.BODY).serialize());;
		tag.put("divine-data", getSystemData(System.DIVINE).serialize());
		tag.put("essence-data", getSystemData(System.ESSENCE).serialize());;
		return tag;
	}

	@Override
	public void deserialize(CompoundTag tag) {
		this.health = tag.getDouble("health");
		getSystemData(System.BODY).deserialize(tag.getCompound("body-data"));
		getSystemData(System.DIVINE).deserialize(tag.getCompound("divine-data"));
		getSystemData(System.ESSENCE).deserialize(tag.getCompound("essence-data"));
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
