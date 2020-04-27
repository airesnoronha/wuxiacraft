package com.airesnor.wuxiacraft.utils;

import com.airesnor.wuxiacraft.capabilities.CultTechProvider;
import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.capabilities.FoundationProvider;
import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.cultivation.*;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.SkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.cultivation.techniques.CultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.entities.mobs.EntityCultivator;
import com.airesnor.wuxiacraft.networking.CultivationMessage;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.UnifiedCapabilitySyncMessage;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import javax.annotation.Nonnull;

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

	@Nonnull
	public static IFoundation getFoundationFromEntity(EntityLivingBase entityIn) {
		IFoundation foundation = null;
		if (entityIn instanceof EntityPlayer)
			//noinspection ConstantConditions
			foundation = entityIn.getCapability(FoundationProvider.FOUNDATION_CAPABILITY, null);
		if (foundation == null) foundation = new Foundation();
		return foundation;
	}

	public static void cultivatorAddProgress(EntityLivingBase player, double amount, boolean techniques, boolean allowBreakThrough, boolean ignoreBottleneck) {
		ICultivation cultivation = getCultivationFromEntity(player);
		ICultTech cultTech = getCultTechFromEntity(player);
		ISkillCap skillCap = getSkillCapFromEntity(player);
		IFoundation foundation = getFoundationFromEntity(player);
		amount *= cultTech.getOverallCultivationSpeed();
		double enlightenment = 1;
		PotionEffect effect = player.getActivePotionEffect(Skills.ENLIGHTENMENT);
		if (effect != null) {
			enlightenment += 9 * effect.getAmplifier();
		}
		amount *= enlightenment;
		if (techniques) {
			cultTech.progress(amount);
		}
		if (!cultivation.getSuppress()) {
			double progressRel = cultivation.getCurrentProgress() / cultivation.getCurrentLevel().getProgressBySubLevel(cultivation.getCurrentSubLevel());
			double bottleneckAmount = ignoreBottleneck ? amount : amount * MathUtils.clamp(1.2f - progressRel, 0.2f, 1f);
			if (foundation.getSelectedAttribute() == -1) {
				CultivationLevel beforeTribulation = cultivation.getCurrentLevel();
				int beforeSubLevel = cultivation.getCurrentSubLevel();
				if (cultivation.addProgress(bottleneckAmount, allowBreakThrough)) {
					if (beforeSubLevel == cultivation.getCurrentSubLevel() && beforeTribulation == cultivation.getCurrentLevel()) {
						callTribulation(player);
					} else {
						if (player instanceof EntityPlayerMP) { //check if server side and if it's a player
							long bound = (long) Math.max(2, cultivation.getStrengthIncrease() / 10);
							if (bound > 0) {
								long rand = (long) Math.floor(Math.sqrt(player.getRNG().nextLong() % (bound * bound)));
								foundation.setAgility(foundation.getAgility() + bound - rand);
								rand = bound- (long) Math.floor(Math.sqrt(player.getRNG().nextLong() % (bound * bound)));
								foundation.setConstitution(foundation.getConstitution() + rand);
								rand = bound - (long) Math.floor(Math.sqrt(player.getRNG().nextLong() % (bound * bound)));
								foundation.setDexterity(foundation.getDexterity() + rand);
								rand = bound - (long) Math.floor(Math.sqrt(player.getRNG().nextLong() % (bound * bound)));
								foundation.setResistance(foundation.getResistance() + rand);
								rand = bound - (long) Math.floor(Math.sqrt(player.getRNG().nextLong() % (bound * bound)));
								foundation.setSpirit(foundation.getSpirit() + rand);
								rand = bound - (long) Math.floor(Math.sqrt(player.getRNG().nextLong() % (bound * bound)));
								foundation.setStrength(foundation.getStrength() + rand);
							}
							int msgN = player.world.rand.nextInt(CONGRATS_MESSAGE_COUNT);
							((EntityPlayer) player).sendStatusMessage(new TextComponentString(TranslateUtils.translateKey("wuxiacraft.level_message.congrats_" + msgN) + " " + cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel())), false);
							NetworkWrapper.INSTANCE.sendTo(new UnifiedCapabilitySyncMessage(cultivation, cultTech, skillCap, foundation, false), (EntityPlayerMP) player);
						}
					}
				}
			}
			switch (foundation.getSelectedAttribute()) {
				case 0:
					foundation.addAgilityProgress(bottleneckAmount);
					break;
				case 1:
					foundation.addConstitutionProgress(bottleneckAmount);
					break;
				case 2:
					foundation.addDexterityProgress(bottleneckAmount);
					break;
				case 3:
					foundation.addResistanceProgress(bottleneckAmount);
					break;
				case 4:
					foundation.addSpiritProgress(bottleneckAmount);
					break;
				case 5:
					foundation.addStrengthProgress(bottleneckAmount);
					break;
			}
		}
	}

	//This will call one lightning bot at a time, or else all bolts would be called at the same time
	private static class BoltsScheduler extends Thread {

		private final EntityLivingBase player;

		public BoltsScheduler(EntityLivingBase player) {
			this.player = player;
		}

		@Override
		public void run() {
			if (player.world instanceof WorldServer) {
				WorldServer world = (WorldServer) player.world;
				ICultivation cultivation = getCultivationFromEntity(player);
				IFoundation foundation = getFoundationFromEntity(player);
				double resistance = foundation.getAgilityModifier() + foundation.getConstitutionModifier() +
						foundation.getDexterityModifier() + foundation.getResistanceModifier() +
						foundation.getSpiritModifier() + foundation.getStrengthModifier();
				double strength = cultivation.getStrengthIncrease() * 12;
				final int bolts = MathUtils.clamp(1 + (int) (Math.round(resistance / (cultivation.getStrengthIncrease()*4))), 1, 12);
				float damage = (float) Math.max(1, strength - resistance);
				for (int i = 0; i < bolts; i++) {
					boolean survived = player.isEntityAlive();
					if(!survived) return;
					world.addScheduledTask(() -> {
						EntityLightningBolt lightningBolt = new EntityLightningBolt(world, player.posX, player.posY + 1.0, player.posZ, true); // effect only won't cause damage
						world.addWeatherEffect(lightningBolt);
						player.attackEntityFrom(DamageSource.LIGHTNING_BOLT.setDamageIsAbsolute().setDamageBypassesArmor(), damage / bolts);
					});
					try {
						sleep(750);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				world.addScheduledTask(() -> {
					boolean survived = player.isEntityAlive();
					if (survived) {
						cultivation.setCurrentSubLevel(cultivation.getCurrentSubLevel() + 1);
						if (cultivation.getCurrentSubLevel() >= cultivation.getCurrentLevel().subLevels) {
							cultivation.setCurrentSubLevel(0);
							cultivation.setCurrentLevel(cultivation.getCurrentLevel().getNextLevel());
						}
						long bound = (long) (cultivation.getStrengthIncrease() / 5);
						if (bound > 0) {
							long rand = bound - 1 - (long) Math.floor(Math.sqrt(player.getRNG().nextLong() % (bound * bound)));
							foundation.setAgility(foundation.getAgility() + rand);
							rand = bound - 1 - (long) Math.floor(Math.sqrt(player.getRNG().nextLong() % (bound * bound)));
							foundation.setConstitution(foundation.getConstitution() + rand);
							rand = bound - 1 - (long) Math.floor(Math.sqrt(player.getRNG().nextLong() % (bound * bound)));
							foundation.setDexterity(foundation.getDexterity() + rand);
							rand = bound - 1 - (long) Math.floor(Math.sqrt(player.getRNG().nextLong() % (bound * bound)));
							foundation.setResistance(foundation.getResistance() + rand);
							rand = bound - 1 - (long) Math.floor(Math.sqrt(player.getRNG().nextLong() % (bound * bound)));
							foundation.setSpirit(foundation.getSpirit() + rand);
							rand = bound - 1 - (long) Math.floor(Math.sqrt(player.getRNG().nextLong() % (bound * bound)));
							foundation.setStrength(foundation.getStrength() + rand);
						}
						int msgN = player.world.rand.nextInt(CONGRATS_MESSAGE_COUNT);
						((EntityPlayer) player).sendStatusMessage(new TextComponentString(TranslateUtils.translateKey("wuxiacraft.level_message.congrats_" + msgN) + " " + cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel())), false);
						NetworkWrapper.INSTANCE.sendTo(new UnifiedCapabilitySyncMessage(cultivation, getCultTechFromEntity(player), getSkillCapFromEntity(player), foundation, false), (EntityPlayerMP) player);
					}
				});
			}
		}
	}

	public static void callTribulation(@Nonnull EntityLivingBase player) {
		if (!player.world.isRemote) {
			BoltsScheduler boltsScheduler = new BoltsScheduler(player);
			boltsScheduler.start();
		}
	}

}
