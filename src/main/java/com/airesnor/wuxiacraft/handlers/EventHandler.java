package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.CultTechProvider;
import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.skills.SkillCap;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.items.ItemScroll;
import com.airesnor.wuxiacraft.items.Items;
import com.airesnor.wuxiacraft.networking.*;
import com.airesnor.wuxiacraft.proxy.ClientProxy;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

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
		if(cultivation != null  && cultTech != null && skillCap != null && !player.world.isRemote) {
			WuxiaCraft.logger.info("Restoring " + player.getDisplayNameString() + " cultivation.");
			NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), cultivation.getCurrentProgress(), cultivation.getEnergy(), cultivation.getPelletCooldown()), (EntityPlayerMP) player);
			NetworkWrapper.INSTANCE.sendTo(new SpeedHandicapMessage(cultivation.getSpeedHandicap()),(EntityPlayerMP)player);
			NetworkWrapper.INSTANCE.sendTo(new CultTechMessage(cultTech), (EntityPlayerMP)player);
			NetworkWrapper.INSTANCE.sendTo(new SkillCapMessage(skillCap), (EntityPlayerMP)player);
		}
	}

	@SubscribeEvent
	public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event) {
		if(event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.getEntity();
			ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
			ICultTech cultTech = player.getCapability(CultTechProvider.CULT_TECH_CAPABILITY, null);
			if(cultivation != null) {

				cultivation.advTimer();
				cultivation.lessenPelletCooldown();
				//each 100 ticks will sync the cultivation
				if(cultivation.getUpdateTimer() == 100) {
					if(!player.world.isRemote) {
						NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), cultivation.getCurrentProgress(), cultivation.getEnergy(), cultivation.getPelletCooldown()), (EntityPlayerMP) player);
						NetworkWrapper.INSTANCE.sendTo(new CultTechMessage(cultTech), (EntityPlayerMP) player);
					}
					cultivation.resetTimer();
				}
				//each 10 ticks to apply modifiers, i guess that every tick is too cpu consuming
				// let's say you have 100 players on a server
				if(cultivation.getUpdateTimer() % 10 == 0) {
					if(!player.world.isRemote) {
						applyModifiers(player, cultivation);
						cultTech.updateTechniques(player, cultivation);
					}
				}
				if(player.world.isRemote) {

					if (player.capabilities.isFlying && cultivation.getEnergy() <= 0) {
						player.capabilities.isFlying = false;
					}
					player.capabilities.allowFlying = player.isCreative() || cultivation.getCurrentLevel().canFly;
					player.capabilities.setFlySpeed((float)player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
					player.stepHeight = !player.isSneaking() ? Math.min(3.1f, 0.6f*(1+0.55f*cultivation.getCurrentLevel().getSpeedModifierBySubLevel(cultivation.getCurrentSubLevel()))): 0.6f;
					player.sendPlayerAbilities();
				}

				//playerAddProgress(player, cultivation, 0.1f);
				cultivation.addEnergy(cultivation.getCurrentLevel().getMaxEnergyByLevel(cultivation.getCurrentSubLevel()) * 0.0005F );
			}
		}
	}

	@SubscribeEvent
	public void onPlayerProcessSkills(LivingEvent.LivingUpdateEvent event) {
		if(event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.getEntity();
			ISkillCap skillCap = player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
			if(skillCap.getCooldown() >= 0) {
				skillCap.stepCooldown(-1f);
			}
			if(skillCap.getActiveSkill() != -1) {
				ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
				Skill skill = skillCap.getSelectedSkills().get(skillCap.getActiveSkill());
				if(skillCap.isCasting() && cultivation.hasEnergy(skill.getCost())) {
					skillCap.stepCastProgress(1f);
					skill.castingEffect(player);
				} else if (skillCap.isDoneCasting()) {
					skillCap.resetCastProgress();
					skillCap.setDoneCasting(false);
				}
				if(skillCap.isCasting() && skillCap.getCastProgress() >= skill.getCastTime() && skillCap.getCooldown() <= 0) {
					if(cultivation.hasEnergy(skill.getCost())) {
						if(skill.activate(player)) {
							if(!player.isCreative()) cultivation.remEnergy(skill.getCost());
							playerAddProgress(player, cultivation, skill.getProgress());
							skillCap.resetCastProgress();
							skillCap.stepCooldown(skill.getCooldown());
						}
					}
				}
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = event.player;
		if(!player.world.isRemote) return;
		ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);

		float distance = (float) Math.sqrt(Math.pow(player.lastTickPosX - player.posX, 2) + Math.pow(player.lastTickPosY - player.posY, 2) + Math.pow(player.lastTickPosZ - player.posZ, 2));

		if(cultivation != null) {
			if(player.capabilities.isFlying) {
				float totalRem = 0f;
				if (!cultivation.getCurrentLevel().freeFlight) {
					float fly_cost = 1f;
					totalRem+= fly_cost;
				}
				if (distance > 0) {
					float cost = 0.4f;
					totalRem+=distance*cost;
				}
				if(!player.isCreative()) {
					cultivation.remEnergy(totalRem);
					NetworkWrapper.INSTANCE.sendToServer(new EnergyMessage(1, totalRem));
				}
			}
			else { //flying is not an exercise
				playerAddProgress(player, cultivation, distance*0.1f);
				NetworkWrapper.INSTANCE.sendToServer(new ProgressMessage(0,distance*0.1f));
			}
		}
	}

	@SubscribeEvent
	public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
		if(event.getEntityLiving() instanceof  EntityPlayer) {
			ICultivation cultivation = event.getEntity().getCapability(CultivationProvider.CULTIVATION_CAP, null);
			event.getEntity().motionY += 0.19*cultivation.getCurrentLevel().getSpeedModifierBySubLevel(cultivation.getCurrentSubLevel())*((float)WuxiaCraftConfig.speedHandicap/100f);
		}
	}

	@SubscribeEvent
	public void onPlayerFall(LivingFallEvent event) {
		if(event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer)event.getEntity();
			ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
			if(player.world.isRemote || event.getDistance() < 3) return;
			if(cultivation.getCurrentLevel().canFly) {
				event.setDistance(0);
			} else {
				event.setDistance(event.getDistance()-1.45f*cultivation.getCurrentLevel().getStrengthModifierBySubLevel(cultivation.getCurrentSubLevel()));
			}
		}
	}

	@SubscribeEvent
	public void onPlayerHitEntity(LivingDamageEvent event) {
		if(event.getSource().getTrueSource() instanceof EntityPlayer  ) {
			EntityPlayer player = (EntityPlayer)event.getSource().getTrueSource();
			ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
			if(cultivation != null) {
				playerAddProgress(player, cultivation,0.5f* event.getAmount());
				NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), cultivation.getCurrentProgress(), cultivation.getEnergy(), cultivation.getPelletCooldown()), (EntityPlayerMP) player);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerDigBlock(BlockEvent.HarvestDropsEvent event) {
		EntityPlayer player = event.getHarvester();
		if(player!=null) {
			IBlockState block = event.getState();
			ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
			if(cultivation !=null) {
				playerAddProgress(player, cultivation, 0.1f*block.getBlockHardness(event.getWorld(), event.getPos()));
				NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), cultivation.getCurrentProgress(), cultivation.getEnergy(), cultivation.getPelletCooldown()), (EntityPlayerMP) player);
			}
		}
	}

	@SubscribeEvent
	public void onPlayerBreakSpeed(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {
		ICultivation cultivation = event.getEntityPlayer().getCapability(CultivationProvider.CULTIVATION_CAP,null);
		event.setNewSpeed(event.getNewSpeed()*(1+0.5f*cultivation.getCurrentLevel().getStrengthModifierBySubLevel(cultivation.getCurrentSubLevel())));
	}

	/**
	 * Qhen the player die, he gets punished and loses all sublevels, and energy
	 * Except true gods that loses 20 levels
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

		if(cultivation != null) {
			cultivation.setCurrentLevel(old_cultivation.getCurrentLevel());

			if(old_cultivation.getCurrentLevel() == CultivationLevel.TRUE_GOD) {
				cultivation.setCurrentSubLevel(Math.max(0, old_cultivation.getCurrentSubLevel()-20));
			} else {
				cultivation.setCurrentSubLevel(0);
			}
			WuxiaCraft.logger.info("Restoring " + player.getDisplayNameString() + " cultivation.");
		}
		if(cultTech != null) {
			cultTech.getKnownTechniques().clear();
			cultTech.getKnownTechniques().addAll(oldCultTech.getKnownTechniques());
		}
		if(skillCap != null) {
		    skillCap.stepCastProgress(oldSkillCap.getCastProgress());
		    skillCap.stepCooldown(oldSkillCap.getCooldown());
		    skillCap.getKnownSkills().clear();
		    skillCap.getKnownSkills().addAll(oldSkillCap.getKnownSkills());
        }
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		EntityPlayer player = event.player;
		if(player.world.isRemote) return;
		ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
		ICultTech cultTech = player.getCapability(CultTechProvider.CULT_TECH_CAPABILITY, null);
		ISkillCap skillCap = player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
		WuxiaCraft.logger.info("Applying " + player.getDisplayNameString() + " cultivation.");
		NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), cultivation.getCurrentProgress(), cultivation.getEnergy(), cultivation.getPelletCooldown()), (EntityPlayerMP) player);
		NetworkWrapper.INSTANCE.sendTo(new EnergyMessage(),(EntityPlayerMP)player);
		NetworkWrapper.INSTANCE.sendTo(new CultTechMessage(cultTech), (EntityPlayerMP)player);
		NetworkWrapper.INSTANCE.sendTo(new SkillCapMessage(skillCap), (EntityPlayerMP)player);
		applyModifiers(player, cultivation);
	}

	@SubscribeEvent
	public void onMobDrop(LivingDropsEvent event) {
		if(event.getEntity() instanceof EntityMob) {
			//scrolls
			List<Item> scrolls = new ArrayList<>();
			for(Item i : Items.ITEMS) {
				if(i instanceof ItemScroll) {
					scrolls.add(i);
				}
			}
			Random rnd = new Random();
			ItemStack drop = new ItemStack(scrolls.get(rnd.nextInt(scrolls.size())), 1);
			if(rnd.nextInt(50) == 1) {
				event.getDrops().add(new EntityItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, drop));
			}
			//pellets
			ItemStack energy_pellet = new ItemStack(Items.ENERGY_RECOVERY_PELLET);
			if(rnd.nextInt(10) == 1) {
				event.getDrops().add(new EntityItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, energy_pellet));
			}
			ItemStack progress = new ItemStack(Items.BODY_REFINEMENT_PELLET);
			if(rnd.nextInt(30) == 1) {
				event.getDrops().add(new EntityItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, progress));
			}
		}
	}

	@SubscribeEvent
	public void onBreakScheduledBlocks(TickEvent.PlayerTickEvent event) {
		ISkillCap skillCap = event.player.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
		if(skillCap != null ) {
			int i = 5;
			while(!skillCap.isScheduledEmpty() && i >  0) {
				i--;
				event.player.world.destroyBlock(skillCap.popScheduledBlockBreaks(), true);
			}
		}
	}

	public static void applyModifiers(EntityPlayer player, ICultivation cultivation) {

		//as most props are additive, so i'll remove the which is the supposed base
		float level_str_mod = cultivation.getCurrentLevel().getStrengthModifierBySubLevel(cultivation.getCurrentSubLevel()) - 1;
		float level_spd_mod = (cultivation.getCurrentLevel().getSpeedModifierBySubLevel(cultivation.getCurrentSubLevel())- 1)*(cultivation.getSpeedHandicap()/100f) ;

		AttributeModifier strength_mod = new AttributeModifier(strength_mod_name, level_str_mod, 0);
		AttributeModifier health_mod = new AttributeModifier(health_mod_name, 3*level_str_mod, 0);
		//since armor base is 0, it'll add 2*strength as armor
		//I'll use for now strength for increase every other stat, since it's almost the same after all
		AttributeModifier armor_mod = new AttributeModifier(armor_mod_name, level_str_mod*0.7f, 0);
		AttributeModifier speed_mod = new AttributeModifier(speed_mod_name, level_spd_mod*0.2, 0);
		AttributeModifier attack_speed_mod = new AttributeModifier(attack_speed_mod_name, level_spd_mod, 1);

		//remove any previous strength modifiers
		for(AttributeModifier mod : player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).getModifiers()) {
			if(mod.getName().equals(strength_mod_name)) {
				player.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).removeModifier(mod);
			}
		}

		//remove any previous speed modifiers
		for(AttributeModifier mod : player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getModifiers()) {
			if(mod.getName().equals(speed_mod_name)) {
				player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).removeModifier(mod);
			}
		}

		//remove any previous attack speed modifiers
		for(AttributeModifier mod : player.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).getModifiers()) {
			if(mod.getName().equals(attack_speed_mod_name)) {
				player.getEntityAttribute(SharedMonsterAttributes.ATTACK_SPEED).removeModifier(mod);
			}
		}

		//remove any previous armor modifiers
		for(AttributeModifier mod : player.getEntityAttribute(SharedMonsterAttributes.ARMOR).getModifiers()) {
			if(mod.getName().equals(armor_mod_name)) {
				player.getEntityAttribute(SharedMonsterAttributes.ARMOR).removeModifier(mod);
			}
		}

		//remove any previous max health modifiers
		for(AttributeModifier mod : player.getEntityAttribute(SharedMonsterAttributes.MAX_HEALTH).getModifiers()) {
			if(mod.getName().equals(health_mod_name)) {
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

	public static void playerAddProgress(EntityPlayer player, ICultivation cultivation, float amount) {
		ICultTech cultTech = player.getCapability(CultTechProvider.CULT_TECH_CAPABILITY, null);
		if(cultTech != null) {
			cultTech.progress(amount);
		}
		if(cultivation.addProgress(amount)) {
			if(!player.world.isRemote) {
				player.sendStatusMessage(new TextComponentString("Congratulations! You now are at " + cultivation.getCurrentLevel().getLevelName(cultivation.getCurrentSubLevel())),false);
				NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), (int) cultivation.getCurrentProgress(), (int) cultivation.getEnergy(), cultivation.getPelletCooldown()), (EntityPlayerMP) player);
			}
		}
	}
}
