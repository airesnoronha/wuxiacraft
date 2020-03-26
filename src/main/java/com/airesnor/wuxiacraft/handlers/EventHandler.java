package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.CultTechProvider;
import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.techniques.CultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.entities.mobs.WanderingCultivator;
import com.airesnor.wuxiacraft.items.ItemRecipe;
import com.airesnor.wuxiacraft.items.ItemScroll;
import com.airesnor.wuxiacraft.items.Items;
import com.airesnor.wuxiacraft.networking.*;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Mod.EventBusSubscriber
public class EventHandler {

	private static final String strength_mod_name = "wuxiacraft.attack_damage";
	private static final String speed_mod_name = "wuxiacraft.movement_speed";
	private static final String armor_mod_name = "wuxiacraft.armor";
	private static final String attack_speed_mod_name = "wuxiacraft.attack_speed";
	private static final String health_mod_name = "wuxiacraft.health";

	@SubscribeEvent
	public void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
		ICultTech cultTech = player.getCapability(CultTechProvider.CULT_TECH_CAPABILITY, null);
		ISkillCap skillCap = player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
		if (cultivation != null && cultTech != null && skillCap != null && !player.world.isRemote) {
			WuxiaCraft.logger.info("Restoring " + player.getDisplayNameString() + " cultivation.");
			NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), cultivation.getCurrentProgress(), cultivation.getEnergy(), cultivation.getPillCooldown(), cultivation.getSuppress()), (EntityPlayerMP) player);
			NetworkWrapper.INSTANCE.sendTo(new SpeedHandicapMessage(cultivation.getSpeedHandicap(), cultivation.getMaxSpeed(), cultivation.getHasteLimit(), cultivation.getJumpLimit()), (EntityPlayerMP) player);
			NetworkWrapper.INSTANCE.sendTo(new CultTechMessage(cultTech), (EntityPlayerMP) player);
			NetworkWrapper.INSTANCE.sendTo(new SkillCapMessage(skillCap), (EntityPlayerMP) player);
		}
	}

	@SubscribeEvent
	public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
			ICultTech cultTech = CultivationUtils.getCultTechFromEntity(player);
			ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
			if (cultivation != null) {

				cultivation.advTimer();
				cultivation.lessenPillCooldown();
				//each 100 ticks will sync the cultivation
				if (cultivation.getUpdateTimer() == 100) {
					if (!player.world.isRemote) {
						NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), cultivation.getCurrentProgress(), cultivation.getEnergy(), cultivation.getPillCooldown(), cultivation.getSuppress()), (EntityPlayerMP) player);
						NetworkWrapper.INSTANCE.sendTo(new CultTechMessage(cultTech), (EntityPlayerMP) player);
						NetworkWrapper.INSTANCE.sendTo(new SkillCapMessage(skillCap), (EntityPlayerMP) player);
					}
					cultivation.resetTimer();
				}
				//each 10 ticks to apply modifiers, i guess that every tick is too cpu consuming
				// let's say you have 100 players on a server
				if (cultivation.getUpdateTimer() % 10 == 0) {
					if (!player.world.isRemote) {
						applyModifiers(player, cultivation);
						cultTech.updateTechniques(player, cultivation);
					}
				}
				if (player.world.isRemote) {

					if (player.capabilities.isFlying && cultivation.getEnergy() <= 0 && !cultivation.getCurrentLevel().freeFlight) {
						player.capabilities.isFlying = false;
					}
					player.capabilities.allowFlying = player.isCreative() || cultivation.getCurrentLevel().canFly;
					player.capabilities.setFlySpeed((float) player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
					player.stepHeight = 0.6f;
					if (!player.isSneaking() && WuxiaCraftConfig.disableStepAssist) {
						player.stepHeight = Math.min(3.1f, 0.6f * (1 + 0.55f * cultivation.getCurrentLevel().getSpeedModifierBySubLevel(cultivation.getCurrentSubLevel())));
					}
					player.sendPlayerAbilities();
				}

				//playerAddProgress(player, cultivation, 0.1f);
				cultivation.addEnergy(cultivation.getMaxEnergy() * 0.0005F);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerRequestLevels(TickEvent.PlayerTickEvent event) {
		if (event.player.isSneaking()) {
			EntityPlayer player = event.player;
			ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
			NetworkWrapper.INSTANCE.sendToServer(new AskCultivationLevelMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), player.getName()));
		}
	}

	@SubscribeEvent
	public void onPlayerProcessSkills(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
			ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
			if (skillCap.getCooldown() > 0) {
				skillCap.stepCooldown(-1f - cultivation.getSpeedIncrease()*0.002f);
			}
			else if (skillCap.getActiveSkill() != -1) {
				Skill skill = skillCap.getSelectedSkills().get(skillCap.getActiveSkill());
				if (skillCap.isCasting() && cultivation.hasEnergy(skill.getCost())) {
					if (skillCap.getCastProgress() < skill.getCastTime())
						skillCap.stepCastProgress(cultivation.getSpeedIncrease());
					skill.castingEffect(player);
				} else if (skillCap.isDoneCasting()) {
					skillCap.resetCastProgress();
					skillCap.setDoneCasting(false);
				}
				if (skillCap.isCasting() && skillCap.getCastProgress() >= skill.getCastTime() && skillCap.getCooldown() <= 0) {
					if (cultivation.hasEnergy(skill.getCost())) {
						if (skill.activate(player)) {
							if (!player.isCreative()) cultivation.remEnergy(skill.getCost());
							CultivationUtils.cultivatorAddProgress(player, cultivation, skill.getProgress());
							skillCap.stepCooldown(skill.getCooldown());
							skillCap.resetCastProgress();
						}
					}
				}
			}
			if(skillCap.getCooldown() < 0) skillCap.resetCooldown();
		}
	}

	@SubscribeEvent
	public void fovChange(FOVUpdateEvent e) {
		float f = 1.0F;
		EntityPlayer entity = e.getEntity();
		if (entity.capabilities.isFlying) {
			f *= 1.1F;
		}

		IAttributeInstance iattributeinstance = entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED);

		double cultivation_speed = 0f;
		for (AttributeModifier mod : iattributeinstance.getModifiers()) {
			if (mod.getName().equals(speed_mod_name)) {
				cultivation_speed += mod.getAmount();
			} else if (mod.getName().equals(CultTech.SPEED__MOD)) {
				cultivation_speed += mod.getAmount();
			}
		}

		f = (float) ((double) f * (((iattributeinstance.getAttributeValue() - cultivation_speed) / (double) entity.capabilities.getWalkSpeed() + 1.0D) / 2.0D));


		if (entity.capabilities.getWalkSpeed() == 0.0F || Float.isNaN(f) || Float.isInfinite(f)) {
			f = 1.0F;
		}

		if (entity.isHandActive() && entity.getActiveItemStack().getItem() == net.minecraft.init.Items.BOW) {
			int i = entity.getItemInUseMaxCount();
			float f1 = (float) i / 20.0F;

			if (f1 > 1.0F) {
				f1 = 1.0F;
			} else {
				f1 = f1 * f1;
			}

			f *= 1.0F - f1 * 0.15F;
		}
		e.setNewfov(f);
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {

		EntityPlayer player = event.player;
		if (!player.world.isRemote) return;
		ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);

		float distance = (float) Math.sqrt(Math.pow(player.lastTickPosX - player.posX, 2) + Math.pow(player.lastTickPosY - player.posY, 2) + Math.pow(player.lastTickPosZ - player.posZ, 2));

		if (cultivation != null) {
			if (player.capabilities.isFlying) {
				float totalRem = 0f;
				float fly_cost = 1000f;
				float dist_cost = 660f;
				if (!cultivation.getCurrentLevel().freeFlight) {
					totalRem += fly_cost;
				}
				if (distance > 0) {
					totalRem += distance * dist_cost;
				}
				if (!player.isCreative()) {
					cultivation.remEnergy(totalRem);
					NetworkWrapper.INSTANCE.sendToServer(new EnergyMessage(1, totalRem));
				}
			} else { //flying is not an exercise
				CultivationUtils.cultivatorAddProgress(player, cultivation, distance * 0.1f);
				NetworkWrapper.INSTANCE.sendToServer(new ProgressMessage(0, distance * 0.1f));
			}
		}
	}

	@SubscribeEvent
	public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			ICultivation cultivation = event.getEntity().getCapability(CultivationProvider.CULTIVATION_CAP, null);
			float baseJumpSpeed = (float) event.getEntity().motionY;
			float jumpSpeed = 0.19f * cultivation.getSpeedIncrease();
			if (cultivation.getJumpLimit() >= 0) {
				jumpSpeed = Math.min(jumpSpeed, cultivation.getJumpLimit() * baseJumpSpeed);
			}
			event.getEntity().motionY += jumpSpeed;
		}
	}

	@SubscribeEvent
	public void onPlayerFall(LivingFallEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
			if (player.world.isRemote || event.getDistance() < 3) return;
			if (cultivation.getCurrentLevel().canFly) {
				event.setDistance(0);
			} else {
				event.setDistance(event.getDistance() - 1.45f * cultivation.getStrengthIncrease());
			}
		}
	}

	@SubscribeEvent
	public void onPlayerHitEntity(LivingDamageEvent event) {
		if (event.getSource().getTrueSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
			if (cultivation != null) {
				CultivationUtils.cultivatorAddProgress(player, cultivation, 0.5f * event.getAmount());
				NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), cultivation.getCurrentProgress(), cultivation.getEnergy(), cultivation.getPillCooldown(), cultivation.getSuppress()), (EntityPlayerMP) player);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerDigBlock(BlockEvent.HarvestDropsEvent event) {
		EntityPlayer player = event.getHarvester();
		if (player != null) {
			IBlockState block = event.getState();
			ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
			if (cultivation != null) {
				CultivationUtils.cultivatorAddProgress(player, cultivation, 0.1f * block.getBlockHardness(event.getWorld(), event.getPos()));
				NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), cultivation.getCurrentProgress(), cultivation.getEnergy(), cultivation.getPillCooldown(),cultivation.getSuppress()), (EntityPlayerMP) player);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerBreakSpeed(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {
		ICultivation cultivation = event.getEntityPlayer().getCapability(CultivationProvider.CULTIVATION_CAP, null);
		float hasteModifier = 0.1f * (cultivation.getStrengthIncrease() - 1);
		if (cultivation.getHasteLimit() >= 0) {
			hasteModifier = Math.min(hasteModifier, cultivation.getHasteLimit() * event.getOriginalSpeed());
		}
		event.setNewSpeed(event.getOriginalSpeed() + hasteModifier);
	}

	/**
	 * When the player die, he gets punished and loses all sublevels, and energy
	 * Except true gods that loses 20 levels
	 *
	 * @param event
	 */
	@SubscribeEvent
	public void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
		EntityPlayer player = event.getEntityPlayer();
		ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
		ICultivation old_cultivation = event.getOriginal().getCapability(CultivationProvider.CULTIVATION_CAP, null);
		ICultTech cultTech = player.getCapability(CultTechProvider.CULT_TECH_CAPABILITY, null);
		ICultTech oldCultTech = event.getOriginal().getCapability(CultTechProvider.CULT_TECH_CAPABILITY, null);
		ISkillCap skillCap = player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
		ISkillCap oldSkillCap = event.getOriginal().getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);

		if (cultivation != null) {
			cultivation.setCurrentLevel(old_cultivation.getCurrentLevel());

			if (old_cultivation.getCurrentLevel() == CultivationLevel.TRUE_GOD) {
				cultivation.setCurrentSubLevel(Math.max(0, old_cultivation.getCurrentSubLevel() - 20));
			} else {
				cultivation.setCurrentSubLevel(0);
			}
			WuxiaCraft.logger.info("Restoring " + player.getDisplayNameString() + " cultivation.");
		}
		if (cultTech != null) {
			cultTech.getKnownTechniques().clear();
			cultTech.getKnownTechniques().addAll(oldCultTech.getKnownTechniques());
		}
		if (skillCap != null) {
			skillCap.stepCastProgress(oldSkillCap.getCastProgress());
			skillCap.stepCooldown(oldSkillCap.getCooldown());
			skillCap.getKnownSkills().clear();
			skillCap.getKnownSkills().addAll(oldSkillCap.getKnownSkills());
			skillCap.getSelectedSkills().clear();
			skillCap.getSelectedSkills().addAll(oldSkillCap.getSelectedSkills());
			skillCap.setActiveSkill(oldSkillCap.getActiveSkill());
		}
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (event.player.world.isRemote) return;
		EntityPlayer player = event.player;
		ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
		ICultTech cultTech = CultivationUtils.getCultTechFromEntity(player);
		ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
		WuxiaCraft.logger.info("Applying " + player.getDisplayNameString() + " cultivation.");
		NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), cultivation.getCurrentProgress(), cultivation.getEnergy(), cultivation.getPillCooldown(), cultivation.getSuppress()), (EntityPlayerMP) player);
		NetworkWrapper.INSTANCE.sendTo(new EnergyMessage(), (EntityPlayerMP) player);
		NetworkWrapper.INSTANCE.sendTo(new CultTechMessage(cultTech), (EntityPlayerMP) player);
		NetworkWrapper.INSTANCE.sendTo(new SkillCapMessage(skillCap), (EntityPlayerMP) player);
		applyModifiers(player, cultivation);
	}

	@SubscribeEvent
	public void onMobDrop(LivingDropsEvent event) {
		if (event.getEntity() instanceof WanderingCultivator) {
			//scrolls
			List<Item> scrolls = new ArrayList<>();
			for (Item i : Items.ITEMS) {
				if (i instanceof ItemScroll) {
					scrolls.add(i);
				}
			}
			Random rnd = event.getEntity().world.rand;
			ItemStack drop = new ItemStack(scrolls.get(rnd.nextInt(scrolls.size())), 1);
			if (rnd.nextInt(50) == 1) {
				event.getDrops().add(new EntityItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, drop));
			}
			for(EntityItem item : event.getDrops()) {
				ItemStack stack = item.getItem();
				if(stack.getItem() == Items.RECIPE_SCROLL) {
					ItemRecipe.setRecipeAtRandom(stack);
				}
				item.setItem(stack);
			}
		}
	}

	@SubscribeEvent
	public void onBreakScheduledBlocks(TickEvent.PlayerTickEvent event) {
		ISkillCap skillCap = event.player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
		if (skillCap != null) {
			int i = 5;
			while (!skillCap.isScheduledEmpty() && i > 0) {
				i--;
				event.player.world.destroyBlock(skillCap.popScheduledBlockBreaks(), true);
			}
		}
	}

	@SubscribeEvent
	public void onSpawnLiving(LivingSpawnEvent event) {
		if(event.getEntityLiving() instanceof EntityMob) {
			((EntityMob) event.getEntityLiving()).targetTasks.addTask(2, new EntityAINearestAttackableTarget((EntityCreature) event.getEntityLiving(), WanderingCultivator.class, true));
		}
	}

	public static void applyModifiers(EntityPlayer player, ICultivation cultivation) {

		//as most props are additive, so i'll remove the which is the supposed base
		float level_str_mod = cultivation.getStrengthIncrease() - 1;
		float level_spd_mod = (cultivation.getSpeedIncrease() - 1);
		if (cultivation.getMaxSpeed() >= 0) {
			float max_speed = cultivation.getMaxSpeed() * (float) SharedMonsterAttributes.MOVEMENT_SPEED.getDefaultValue();
			level_spd_mod = Math.min(max_speed, level_spd_mod);
		}
		level_spd_mod *= (cultivation.getSpeedHandicap() / 100f);

		AttributeModifier strength_mod = new AttributeModifier(strength_mod_name, level_str_mod, 0);
		AttributeModifier health_mod = new AttributeModifier(health_mod_name, 3 * level_str_mod, 0);
		//since armor base is 0, it'll add 2*strength as armor
		//I'll use for now strength for increase every other stat, since it's almost the same after all
		AttributeModifier armor_mod = new AttributeModifier(armor_mod_name, level_str_mod * 0.7f, 0);
		AttributeModifier speed_mod = new AttributeModifier(speed_mod_name, level_spd_mod * 0.2, 0);
		AttributeModifier attack_speed_mod = new AttributeModifier(attack_speed_mod_name, level_spd_mod, 1);

		//remove any previous strength modifiers
		for (AttributeModifier mod : player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getModifiers()) {
			if (mod.getName().equals(strength_mod_name)) {
				player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).removeModifier(mod);
			}
		}

		//remove any previous speed modifiers
		for (AttributeModifier mod : player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifiers()) {
			if (mod.getName().equals(speed_mod_name)) {
				player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(mod);
			}
		}

		//remove any previous attack speed modifiers
		for (AttributeModifier mod : player.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getModifiers()) {
			if (mod.getName().equals(attack_speed_mod_name)) {
				player.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).removeModifier(mod);
			}
		}

		//remove any previous armor modifiers
		for (AttributeModifier mod : player.getEntityAttribute(SharedMonsterAttributes.ARMOR).getModifiers()) {
			if (mod.getName().equals(armor_mod_name)) {
				player.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(mod);
			}
		}

		//remove any previous max health modifiers
		for (AttributeModifier mod : player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getModifiers()) {
			if (mod.getName().equals(health_mod_name)) {
				player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).removeModifier(mod);
			}
		}

		//apply current modifiers
		player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).applyModifier(strength_mod);
		player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).applyModifier(speed_mod);
		player.getEntityAttribute(SharedMonsterAttributes.ARMOR).applyModifier(armor_mod);
		player.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).applyModifier(attack_speed_mod);
		player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).applyModifier(health_mod);

		//WuxiaCraft.logger.info(String.format("Applying %s modifiers from %s.", player.getDisplayNameString(), cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel())));
	}

}
