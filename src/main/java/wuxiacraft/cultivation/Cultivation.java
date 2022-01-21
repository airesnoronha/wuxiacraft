package wuxiacraft.cultivation;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
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
			maxHealth += getSystemData(system).getStage().maxHealth;
		}
		return maxHealth;
	}

	@Override
	public double getStrength() {
		double strength = 1;
		for(var system : System.values()) {
			strength += getSystemData(system).getStage().strength;
		}
		return strength;
	}

	@Override
	public double getAgility() {
		double agility = 1;
		for(var system : System.values()) {
			var systemData = getSystemData(system);
			agility += systemData.getStage().agility;
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
