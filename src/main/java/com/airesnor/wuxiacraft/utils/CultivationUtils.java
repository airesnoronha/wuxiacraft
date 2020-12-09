package com.airesnor.wuxiacraft.utils;

import com.airesnor.wuxiacraft.capabilities.*;
import com.airesnor.wuxiacraft.cultivation.*;
import com.airesnor.wuxiacraft.aura.AuraCap;
import com.airesnor.wuxiacraft.aura.IAuraCap;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.SkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.cultivation.techniques.CultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.entities.mobs.EntityCultivator;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.UnifiedCapabilitySyncMessage;
import com.airesnor.wuxiacraft.world.data.WorldVariables;
import com.airesnor.wuxiacraft.world.dimensions.WuxiaDimensions;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.DimensionType;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class CultivationUtils {

	private static final int CONGRATS_MESSAGE_COUNT = 5;

	@Nonnull
	public static ICultivation getCultivationFromEntity(EntityLivingBase entityIn) {
		ICultivation cultivation = null;
		if (entityIn instanceof EntityPlayer) {
			//noinspection ConstantConditions
			cultivation = entityIn.getCapability(CultivationProvider.CULTIVATION_CAP, null);
		} else if (entityIn instanceof EntityCultivator) {
			cultivation = ((EntityCultivator) entityIn).getCultivation();
		}
		if (cultivation == null) {
			cultivation = new Cultivation();
		}
		return cultivation;
	}

	@Nonnull
	public static ICultTech getCultTechFromEntity(EntityLivingBase entityIn) {
		ICultTech cultTech = null;
		if (entityIn instanceof EntityPlayer) {
			//noinspection ConstantConditions
			cultTech = entityIn.getCapability(CultTechProvider.CULT_TECH_CAPABILITY, null);
		}
		if (cultTech == null) {
			cultTech = new CultTech();
		}
		return cultTech;
	}

	@Nonnull
	public static ISkillCap getSkillCapFromEntity(EntityLivingBase entityIn) {
		ISkillCap skillCap = null;
		if (entityIn instanceof EntityPlayer) {
			//noinspection ConstantConditions
			skillCap = entityIn.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
		} else if (entityIn instanceof EntityCultivator) {
			skillCap = ((EntityCultivator) entityIn).getSkillCap();
		}
		if (skillCap == null) {
			skillCap = new SkillCap();
		}
		return skillCap;
	}

	/*@Nonnull
	public static IFoundation getFoundationFromEntity(EntityLivingBase entityIn) {
		IFoundation foundation = null;
		if (entityIn instanceof EntityPlayer)
			//noinspection ConstantConditions
			foundation = entityIn.getCapability(FoundationProvider.FOUNDATION_CAPABILITY, null);
		if (foundation == null) foundation = new Foundation();
		return foundation;
	}*/

	@Nonnull
	public static ISealing getSealingFromEntity(EntityLivingBase entityIn) {
		ISealing sealing = null;
		if (entityIn instanceof EntityPlayer) {
			//noinspection ConstantConditions
			sealing = entityIn.getCapability(SealingProvider.SEALING_CAPABILITY, null);
		}
		if (sealing == null) {
			sealing = new Sealing();
		}
		return sealing;
	}

	@Nonnull
	public static IBarrier getBarrierFromEntity(EntityLivingBase entityIn) {
		IBarrier barrier = null;
		if (entityIn instanceof EntityPlayer) {
			//noinspection ConstantConditions
			barrier = entityIn.getCapability(BarrierProvider.BARRIER_CAPABILITY, null);
		}
		if (barrier == null) {
			barrier = new Barrier();
		}
		return barrier;
	}

	@Nonnull
	public static IAuraCap getAuraFromEntity(EntityLivingBase entityIn) {
		IAuraCap auraCap = null;
		if (entityIn instanceof EntityPlayer) {
			//noinspection ConstantConditions
			auraCap = entityIn.getCapability(AuraCapProvider.AURA_CAPABILITY, null);
		}
		if (auraCap == null) {
			auraCap = new AuraCap();
		}
		return auraCap;
	}

	public static double getMaxEnergy(EntityLivingBase entityIn) {
		double energy = getCultivationFromEntity(entityIn).getMaxEnergy();
		energy *= (1 + getCultTechFromEntity(entityIn).getOverallModifiers().maxEnergy);
		return energy;
	}

	public static double getConstitutionFromEntity(EntityLivingBase entityIn) {
		double maxHealth = getCultivationFromEntity(entityIn).getConstitutionModifier();
		maxHealth *= 1 + getCultTechFromEntity(entityIn).getOverallModifiers().maxHealth;
		return maxHealth;
	}

	public static double getStrengthFromEntity(EntityLivingBase entityIn) {
		ICultivation cultivation = getCultivationFromEntity(entityIn);
		double strength = cultivation.getStrengthModifier();
		strength *= 1 + getCultTechFromEntity(entityIn).getOverallModifiers().strength;
		strength *= cultivation.getHandicap()/100.0;
		return strength;
	}

	public static double getDexterityFromEntity(EntityLivingBase entityIn) {
		ICultivation cultivation = getCultivationFromEntity(entityIn);
		double attackSpeed = cultivation.getDexterityModifier();
		attackSpeed *= 1 + getCultTechFromEntity(entityIn).getOverallModifiers().attackSpeed;
		attackSpeed *= cultivation.getHandicap()/100.0;
		return attackSpeed;
	}

	public static double getResistanceFromEntity(EntityLivingBase entityIn) {
		ICultivation cultivation = getCultivationFromEntity(entityIn);
		double armor = cultivation.getResistanceModifier();
		armor *= 1 + getCultTechFromEntity(entityIn).getOverallModifiers().armor;
		armor *= cultivation.getHandicap()/100.0;
		return armor;
	}

	public static double getAgilityFromEntity(EntityLivingBase entityIn) {
		ICultivation cultivation = getCultivationFromEntity(entityIn);
		double movementSpeed = cultivation.getAgilityModifier();
		movementSpeed *= 1 + getCultTechFromEntity(entityIn).getOverallModifiers().movementSpeed;

		if (cultivation.getMaxSpeed() >= 0) {
			double max_speed = cultivation.getMaxSpeed();
			movementSpeed = Math.min(max_speed, movementSpeed);
		}
		movementSpeed *= cultivation.getHandicap()/100.0;
		return movementSpeed;
	}

	public static void cultivatorAddProgress(EntityLivingBase player, Cultivation.System system, double amount, boolean techniques) {
		ICultivation cultivation = getCultivationFromEntity(player);
		ICultTech cultTech = getCultTechFromEntity(player);
		double enlightenment = 1;
		PotionEffect effect = player.getActivePotionEffect(Skills.ENLIGHTENMENT);
		if (effect != null) {
			enlightenment += 9 * effect.getAmplifier();
		}
		amount *= enlightenment;
		if (techniques) {
			cultTech.progress(amount, system);
		}
		//get world extra modifier
		List<Pair<Element, DimensionType>> elementsDim = new ArrayList<>();
		elementsDim.add(Pair.of(Element.FIRE, WuxiaDimensions.FIRE));
		elementsDim.add(Pair.of(Element.EARTH, WuxiaDimensions.EARTH));
		elementsDim.add(Pair.of(Element.METAL, WuxiaDimensions.METAL));
		elementsDim.add(Pair.of(Element.WATER, WuxiaDimensions.WATER));
		elementsDim.add(Pair.of(Element.WOOD, WuxiaDimensions.WOOD));
		for (Pair<Element, DimensionType> pair : elementsDim) {
			if (player.world.provider.getDimension() == pair.getValue().getId()) {
				if (cultTech.hasElement(pair.getKey())) {
					amount *= 1.75;
				}
				for (Element element : pair.getKey().getCounters()) {
					if (cultTech.hasElement(element)) amount *= 0.75;
				}
			}
		}
		if (system == Cultivation.System.ESSENCE) {
			amount *= MathUtils.clamp(cultivation.getDivineModifier() / Math.max(cultivation.getEssenceLevel().getModifierBySubLevel(cultivation.getEssenceSubLevel()), 1), 1, 20);
		}
		if (cultivation.getEssenceLevel() == BaseSystemLevel.DEFAULT_ESSENCE_LEVEL) {
			cultivation.addSystemFoundation(amount, Cultivation.System.ESSENCE);
		}
		if(!cultivation.getSuppress())
			cultivation.addSystemProgress(amount, system); //if reached max automatically will increase the foundation
		cultivation.addSystemFoundation(amount * 0.05, system); //5% extra to foundations
	}

	//This will call one lightning bolt at a time, or else all bolts would be called at the same time
	private static class BoltsScheduler extends Thread {

		private final EntityLivingBase player;
		private final double tribulationStrength;
		private BaseSystemLevel targetLevel;
		private int targetSubLevel;
		private Cultivation.System system;
		private final boolean customTribulation;

		public BoltsScheduler(EntityLivingBase player, double tribulationStrength, Cultivation.System system, BaseSystemLevel targetLevel, int targetSubLevel) {
			this.player = player;
			this.tribulationStrength = tribulationStrength;
			this.system = system;
			this.targetLevel = targetLevel;
			this.targetSubLevel = targetSubLevel;
			this.customTribulation = false;
		}

		public BoltsScheduler(EntityLivingBase player, double tribulationStrength, boolean customTribulation) {
			this.player = player;
			this.tribulationStrength = tribulationStrength;
			this.customTribulation = customTribulation;
		}

		@Override
		public void run() {
			if (player.world instanceof WorldServer) {
				WorldServer world = (WorldServer) player.world;
				ICultivation cultivation = getCultivationFromEntity(player);
				World w = player.getEntityWorld();
				double resistance = 1;
				if (this.system != null) {
					resistance = cultivation.getSystemModifier(this.system) + cultivation.getEssenceModifier() * 0.05 + cultivation.getBodyModifier() * 0.05 + cultivation.getDivineModifier() * 0.05; //new foundation system is already a way to resist tribulation
				}
				double multiplier = world.getGameRules().hasRule("tribulationMultiplier") ? world.getGameRules().getInt("tribulationMultiplier") : 1;
				double worldMultiplier = WorldVariables.get(w).getTribulationMultiplier();
				double strength = this.tribulationStrength * multiplier * worldMultiplier;
				final int bolts = MathUtils.clamp(1 + (int) (Math.round(resistance * 3 / strength)), 1, 12);
				float damage = (float) Math.max(2, strength - resistance);
				boolean isRaining = world.isRaining();
				world.addScheduledTask(() -> world.getWorldInfo().setRaining(true));
				for (int i = 0; i < bolts; i++) {
					boolean survived = player.isEntityAlive();
					if (!survived) return;
					world.addScheduledTask(() -> {
						EntityLightningBolt lightningBolt = new EntityLightningBolt(world, player.posX, player.posY + 1.0, player.posZ, true); // effect only won't cause damage
						world.addWeatherEffect(lightningBolt);
						player.attackEntityFrom(DamageSource.LIGHTNING_BOLT.setDamageIsAbsolute().setDamageBypassesArmor(), (float) Math.max(damage, strength * 0.1));
					});
					try {
						sleep(750);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				world.addScheduledTask(() -> world.getWorldInfo().setRaining(isRaining));
				int msgN = player.world.rand.nextInt(CONGRATS_MESSAGE_COUNT);
				world.addScheduledTask(() -> {
					boolean survived = player.isEntityAlive();
					if (survived && !this.customTribulation) {
						cultivation.riseSubLevel(system);
						((EntityPlayer) player).sendStatusMessage(new TextComponentString(TranslateUtils.translateKey("wuxiacraft.level_message.congrats_" + msgN) + " " + targetLevel.getLevelName(targetSubLevel)), false);
						NetworkWrapper.INSTANCE.sendTo(new UnifiedCapabilitySyncMessage(cultivation, getCultTechFromEntity(player), getSkillCapFromEntity(player), getAuraFromEntity(player), false), (EntityPlayerMP) player);
					}
				});
			}
		}
	}

	public static void callTribulation(@Nonnull EntityLivingBase player, double tribulationStrength, Cultivation.System system, BaseSystemLevel level, int subLevel) {
		if (!player.world.isRemote) {
			BoltsScheduler boltsScheduler = new BoltsScheduler(player, tribulationStrength, system, level, subLevel);
			boltsScheduler.start();
		}
	}

	public static void callCustomTribulation(@Nonnull EntityLivingBase player, double tribulationStrength, boolean customTribulation) {
		if (!player.world.isRemote) {
			BoltsScheduler boltsScheduler = new BoltsScheduler(player, tribulationStrength, customTribulation);
			boltsScheduler.start();
		}
	}

	private static class CustomThunder extends Thread {

		private final EntityLivingBase player;
		private final double strength;
		private final double reward;

		public CustomThunder(EntityLivingBase player, double strength, double reward) {
			this.player = player;
			this.strength = strength;
			this.reward = reward;
		}

		@Override
		public void run() {
			if (player.world instanceof WorldServer) {
				WorldServer world = (WorldServer) player.world;
				world.addScheduledTask(() -> {
					EntityLightningBolt lightningBolt = new EntityLightningBolt(world, player.posX, player.posY + 1.0, player.posZ, true); // effect only won't cause damage
					world.addWeatherEffect(lightningBolt);
					player.attackEntityFrom(DamageSource.LIGHTNING_BOLT.setDamageIsAbsolute().setDamageBypassesArmor(), (float) this.strength);
				});
				try {
					sleep(750);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				world.addScheduledTask(() -> {
					if (player.isEntityAlive()) {
						ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
						cultivation.addSystemProgress(this.reward, Cultivation.System.BODY);
					}
				});
			}
		}
	}

	public static void callCustomThunder(EntityLivingBase player, double strength, double reward) {
		if (!player.world.isRemote) {
			new CustomThunder(player, strength, reward).start();
		}
	}

}
