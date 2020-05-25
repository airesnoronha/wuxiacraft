package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.blocks.WuxiaBlocks;
import com.airesnor.wuxiacraft.blocks.SpiritStoneStackBlock;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.CultivationLevel;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.IFoundation;
import com.airesnor.wuxiacraft.cultivation.ISealing;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skill;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.KnownTechnique;
import com.airesnor.wuxiacraft.dimensions.WuxiaDimensions;
import com.airesnor.wuxiacraft.entities.mobs.WanderingCultivator;
import com.airesnor.wuxiacraft.entities.tileentity.SpiritStoneStackTileEntity;
import com.airesnor.wuxiacraft.items.*;
import com.airesnor.wuxiacraft.networking.*;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import com.airesnor.wuxiacraft.utils.MathUtils;
import com.airesnor.wuxiacraft.utils.TeleportationUtil;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAINearestAttackableTarget;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.FoodStats;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.FOVUpdateEvent;
import net.minecraftforge.event.entity.living.*;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

@Mod.EventBusSubscriber
public class EventHandler {

	private static final String strength_mod_name = "wuxiacraft.attack_damage";
	private static final String speed_mod_name = "wuxiacraft.movement_speed";
	private static final String armor_mod_name = "wuxiacraft.armor";
	private static final String attack_speed_mod_name = "wuxiacraft.attack_speed";
	private static final String health_mod_name = "wuxiacraft.health";
	private static final String swim_mod_name = "wuxiacraft.swim";

	private static final Field foodStats = ReflectionHelper.findField(FoodStats.class, "foodSaturationLevel", "field_75125_b");

	static {
		foodStats.setAccessible(true);
	}

	/**
	 * It gives the client side information about cultivation which i stored server side
	 *
	 * @param event Description of whats happening
	 */
	@SubscribeEvent
	public void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
		if (!player.world.isRemote) {
			WuxiaCraft.logger.info("Setting player " + player.getDisplayNameString() + " cultivation levels.");
			NetworkWrapper.INSTANCE.sendTo(new CultivationLevelsMessage(), (EntityPlayerMP) player);
		}
		TextComponentString text = new TextComponentString("For a quick tutorial on the mod. \nPlease use the /culthelp command");
		text.getStyle().setColor(TextFormatting.GOLD);
		player.sendMessage(text);
	}

	/**
	 * The big great logic behind cultivation happens here
	 * This is the DanTian dark matter
	 *
	 * @param event Description of whats happening
	 */
	@SubscribeEvent
	public void onPlayerUpdateEvent(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
			ICultTech cultTech = CultivationUtils.getCultTechFromEntity(player);
			ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
			IFoundation foundation = CultivationUtils.getFoundationFromEntity(player);

			cultivation.advTimer();
			cultivation.lessenPillCooldown();
			//each 100 ticks will sync the cultivation
			if (cultivation.getUpdateTimer() == 100) {
				if (!player.world.isRemote) {
					NetworkWrapper.INSTANCE.sendTo(new UnifiedCapabilitySyncMessage(cultivation, cultTech, skillCap, foundation, false), (EntityPlayerMP) player);
				}
				cultivation.resetTimer();
			}
			//each 10 ticks to apply modifiers, i guess that every tick is too cpu consuming
			// let's say you have 100 players on a server
			if (cultivation.getUpdateTimer() % 20 == 0) {
				if (!player.world.isRemote) {
					applyModifiers(player);
				}
			}
			if (player.world.isRemote) {

				if (cultivation.getCurrentLevel().canFly && player.capabilities.isFlying && cultivation.getEnergy() <= 0) {
					player.capabilities.isFlying = false;
				}
				if (cultivation.getCurrentLevel().canFly) {
					player.capabilities.allowFlying = player.isCreative() || player.isSpectator() || cultivation.getCurrentLevel().canFly;
					player.capabilities.setFlySpeed((float) player.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue());
				}
				player.stepHeight = 0.6f;
				if (!player.isSneaking() && WuxiaCraftConfig.disableStepAssist) {
					float agilityModifier = (float) foundation.getAgilityModifier() * 0.4f;
					float dexterityModifier = (float) foundation.getDexterityModifier() * 0.4f;
					float strengthModifier = (float) foundation.getStrengthModifier() * 0.2f;
					player.stepHeight = Math.min(3.1f, 0.6f * (1 + 0.15f * (agilityModifier + dexterityModifier + strengthModifier)));
				}
				player.sendPlayerAbilities();
			}

			//playerAddProgress(player, cultivation, 0.1f);
			cultivation.addEnergy(cultivation.getMaxEnergy(foundation) * 0.00025F);
			if (cultivation.getEnergy() > cultivation.getMaxEnergy(foundation)) {
				cultivation.setEnergy(cultivation.getMaxEnergy(foundation));
			}
		}
	}

	/**
	 * A toggle for not spamming messages
	 */
	private boolean toggleSneaking = false;

	/**
	 * Handles whenever a player sneak it will get nearby players cultivation
	 *
	 * @param event Description of whats happening
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerRequestLevels(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			if (event.player.isSneaking()) {
				if (!toggleSneaking) {
					toggleSneaking = true;
					EntityPlayer player = event.player;
					ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
					NetworkWrapper.INSTANCE.sendToServer(new AskCultivationLevelMessage(cultivation.getCurrentLevel(), cultivation.getCurrentSubLevel(), player.getUniqueID()));
				}
			} else {
				toggleSneaking = false;
			}
		}
	}


	//So that formations doesn't overwork too
	private static long LastPlayerTickTime = 0;

	/**
	 * Handles the skills logic, cooldown and casting
	 * New casting logic: client handles cooldown and cast progress, client/server activates skills
	 *
	 * @param event Description of whats happening
	 */
	@SubscribeEvent
	public void onPlayerProcessSkills(LivingEvent.LivingUpdateEvent event) {
		if (event.getEntity() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getEntity();
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
			ICultTech cultTech = CultivationUtils.getCultTechFromEntity(player);
			ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
			IFoundation foundation = CultivationUtils.getFoundationFromEntity(player);
			if (player.world.isRemote) {
				long timeDiff = System.currentTimeMillis() - LastPlayerTickTime;
				if (timeDiff >= 50) { //20 per seconds
					skillCap.setFormationActivated(false); //allow next tick formations to do something here
					LastPlayerTickTime = System.currentTimeMillis();
				}
			} else {
				skillCap.setFormationActivated(false); //server won't have speed hack i hope
			}
			if (skillCap.getCooldown() > 0) {
				skillCap.stepCooldown(-1 - ((float) foundation.getDexterityModifier()) * 0.3f);
			} else {
				if (player.world.isRemote) {
					if (skillCap.getActiveSkill() >= 0 && !skillCap.getSelectedSkills().isEmpty()) {
						Skill selectedSkill = skillCap.getSelectedSkill(cultTech);
						if (selectedSkill != null) {
							if (skillCap.isCasting()) {
								if (cultivation.hasEnergy(selectedSkill.getCost())) {
									if (skillCap.getCastProgress() < selectedSkill.getCastTime() && selectedSkill.castingEffect(player)) {
										if (selectedSkill.castNotSpeedable) skillCap.stepCastProgress(1);
										else
											skillCap.stepCastProgress((float) foundation.getDexterityModifier() * 0.4f);
									}
									if (skillCap.getCastProgress() >= selectedSkill.getCastTime()) {
										if (selectedSkill.activate(player)) {
											skillCap.resetCastProgress();
											if (!player.isCreative())
												cultivation.remEnergy(selectedSkill.getCost());
											NetworkWrapper.INSTANCE.sendToServer(new ActivateSkillMessage(skillCap.getActiveSkill(), player.getUniqueID()));
											CultivationUtils.cultivatorAddProgress(player, selectedSkill.getProgress(), true, false, false);
										}
									}
								}
							}
							if (skillCap.isDoneCasting()) {
								skillCap.setDoneCasting(false);
								skillCap.resetCastProgress();
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Desperate tentative of not increasing the FOV, reduced a lot
	 *
	 * @param e Description of whats happening
	 */
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

	/**
	 * A toggle for not spamming messages
	 */
	private boolean toggleHasInGameFocus = false;

	/**
	 * Accumulates the fly cost to send to the server later
	 * This is an attempt to reduce the energy messages when someone's flying
	 */
	private float accumulatedFlyCost = 0f;

	/**
	 * Client side logic on every tick update
	 * It seems flight is only calculated client side
	 * Lame
	 *
	 * @param event Description of whats happening
	 */
	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerTick(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END) {
			EntityPlayer player = event.player;
			if (!player.world.isRemote) return;
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);

			if (!Minecraft.getMinecraft().inGameHasFocus) {
				if (!toggleHasInGameFocus) {
					toggleHasInGameFocus = true;
					ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(Minecraft.getMinecraft().player);
					skillCap.setCasting(false);
					skillCap.setDoneCasting(true);
				}
			} else if (toggleHasInGameFocus) {
				toggleHasInGameFocus = false;
			}

			float distance = (float) Math.sqrt(Math.pow(player.lastTickPosX - player.posX, 2) + Math.pow(player.lastTickPosY - player.posY, 2) + Math.pow(player.lastTickPosZ - player.posZ, 2));

			if (player.capabilities.isFlying) {
				float totalRem = 0f;
				float fly_cost = 2500f;
				float dist_cost = 1320f;
				if (!cultivation.getCurrentLevel().freeFlight) {
					totalRem += fly_cost;
				}
				if (distance > 0) {
					totalRem += distance * dist_cost;
				}
				if (!player.isCreative()) {
					cultivation.remEnergy(totalRem);
					accumulatedFlyCost += totalRem;
					if (cultivation.getUpdateTimer() % 10 == 0) {
						NetworkWrapper.INSTANCE.sendToServer(new EnergyMessage(1, accumulatedFlyCost, player.getUniqueID()));
						accumulatedFlyCost = 0;
					}
				}
			} /*else { //flying is not an exercise
			//CultivationUtils.cultivatorAddProgress(player, cultivation, distance * 0.1f);
			//NetworkWrapper.INSTANCE.sendToServer(new ProgressMessage(0, distance * 0.1f));
		}*/
		}
	}

	/**
	 * Whe a player jump, the get jump bonus base on its cultivation speed bonus
	 *
	 * @param event Description of whats happening
	 */
	@SubscribeEvent
	public void onPlayerJump(LivingEvent.LivingJumpEvent event) {
		if (event.getEntityLiving() instanceof EntityPlayer) {
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(event.getEntityLiving());
			IFoundation foundation = CultivationUtils.getFoundationFromEntity(event.getEntityLiving());
			double baseJumpSpeed = event.getEntity().motionY;
			double agilityModifier = (foundation.getAgilityModifier()) * 0.3;
			double strengthModifier = (foundation.getStrengthModifier()) * 0.7;
			double jumpSpeed = 0.05f * (agilityModifier + strengthModifier) * baseJumpSpeed;
			if (cultivation.getJumpLimit() >= 0) {
				jumpSpeed = Math.min(jumpSpeed, cultivation.getJumpLimit() * baseJumpSpeed);
			}
			event.getEntity().motionY += jumpSpeed;
		}
	}

	/**
	 * When a cultivator fall they'll have some fall height removed before damage is calculated
	 *
	 * @param event Description of whats happening
	 */
	@SubscribeEvent
	public void onCultivatorFall(LivingFallEvent event) {
		ICultivation cultivation = CultivationUtils.getCultivationFromEntity(event.getEntityLiving());
		if (event.getEntityLiving().world.isRemote || event.getDistance() < 3) return;
		if (cultivation.getCurrentLevel().canFly) {
			event.setDistance(0);
		} else {
			if (event.getEntityLiving() instanceof EntityPlayer) {
				IFoundation foundation = CultivationUtils.getFoundationFromEntity(event.getEntityLiving());
				float agilityModifier = (float) foundation.getAgilityModifier() * 0.2f;
				float strengthModifier = (float) foundation.getStrengthModifier() * 0.4f;
				float constitutionModifier = (float) foundation.getConstitution() * 0.4f;
				event.setDistance(event.getDistance() - 1.85f * (agilityModifier + strengthModifier + constitutionModifier));
			} else {
				event.setDistance(event.getDistance() - 1.85f * ((float) cultivation.getStrengthIncrease() - 1));
			}
		}
	}

	/**
	 * When the player hits an entity, gain a little progress, but i'm regretting
	 * Also applies 1 damage to entity if wearing dagger
	 *
	 * @param event Description of whats happening
	 */
	@SubscribeEvent
	public void onPlayerHitEntity(LivingDamageEvent event) {
		if (event.getSource().getTrueSource() instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) event.getSource().getTrueSource();
			if (!player.world.isRemote) {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
				CultivationUtils.cultivatorAddProgress(player, 0.25f, false, false, true);
				NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), (EntityPlayerMP) player);
			}
			ItemStack stack = player.getHeldItem(EnumHand.MAIN_HAND);
			if (stack.getItem() instanceof ItemDagger) {
				event.setAmount(1);
			}
		}
	}

	/**
	 * When they break some block, they gain a little progress based on the blocks hardness
	 *
	 * @param event Description of whats happening
	 */
	@SubscribeEvent
	public void onPlayerDigBlock(BlockEvent.HarvestDropsEvent event) {
		EntityPlayer player = event.getHarvester();
		if (player != null) {
			IBlockState block = event.getState();
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
			CultivationUtils.cultivatorAddProgress(player, 0.1f * block.getBlockHardness(event.getWorld(), event.getPos()), false, false, false);
			NetworkWrapper.INSTANCE.sendTo(new CultivationMessage(cultivation), (EntityPlayerMP) player);
		}
	}

	/**
	 * Increases a little the players break speed based on its strength
	 *
	 * @param event Description of whats happening
	 */
	@SubscribeEvent
	public void onPlayerBreakSpeed(net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed event) {
		ICultivation cultivation = CultivationUtils.getCultivationFromEntity(event.getEntityPlayer());
		IFoundation foundation = CultivationUtils.getFoundationFromEntity(event.getEntityPlayer());
		float baseSpeed = event.getOriginalSpeed();
		float dexterityModifier = (float) foundation.getDexterityModifier() * 0.5f;
		float strengthModifier = (float) foundation.getStrengthModifier() * 0.5f;
		float hasteModifier = 0.1f * baseSpeed * (dexterityModifier + strengthModifier);
		if (cultivation.getHasteLimit() >= 0) {
			hasteModifier = Math.min(hasteModifier, cultivation.getHasteLimit() * baseSpeed);
		}
		event.setNewSpeed(event.getOriginalSpeed() + hasteModifier);
	}

	/**
	 * When the player die, he gets punished and loses all sub levels, and energy
	 * Except true gods that loses 20 levels
	 * Loose 10% points of every foundation and their progress
	 *
	 * @param event Description of what happened, and what will come to be
	 */
	@SubscribeEvent
	public void onPlayerClone(net.minecraftforge.event.entity.player.PlayerEvent.Clone event) {
		EntityPlayer player = event.getEntityPlayer();
		EntityPlayer original = event.getOriginal();
		ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
		ICultivation oldCultivation = CultivationUtils.getCultivationFromEntity(original);
		ICultTech cultTech = CultivationUtils.getCultTechFromEntity(player);
		ICultTech oldCultTech = CultivationUtils.getCultTechFromEntity(original);
		ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
		ISkillCap oldSkillCap = CultivationUtils.getSkillCapFromEntity(original);
		IFoundation foundation = CultivationUtils.getFoundationFromEntity(player);
		IFoundation oldFoundation = CultivationUtils.getFoundationFromEntity(original);
		cultivation.setCurrentLevel(oldCultivation.getCurrentLevel());
		ISealing sealing = CultivationUtils.getSealingFromEntity(player);
		ISealing oldSealing = CultivationUtils.getSealingFromEntity(original);
		if (event.isWasDeath()) {

			if (oldCultivation.getCurrentLevel().levelName.equals(oldCultivation.getCurrentLevel().nextLevelName)) { //if last level, i hope
				cultivation.setCurrentSubLevel(Math.max(0, oldCultivation.getCurrentSubLevel() - 20));
			} else {
				cultivation.setCurrentSubLevel(0);
			}
			foundation.copyFrom(oldFoundation);
			foundation.applyDeathPunishment(oldCultivation);
			sealing.copyFrom(oldSealing);
		} else {
			cultivation.copyFrom(oldCultivation);
			foundation.copyFrom(oldFoundation);
			sealing.copyFrom(oldSealing);
		}

		WuxiaCraft.logger.info("Restoring " + player.getDisplayNameString() + " cultivation.");
		cultTech.copyFrom(oldCultTech);
		skillCap.copyFrom(oldSkillCap, true);
	}

	/**
	 * Restores the players cultivation when they respawn
	 *
	 * @param event Description of whats happening
	 */
	@SubscribeEvent
	public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
		if (event.player.world.isRemote) return;
		EntityPlayer player = event.player;
		ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
		ICultTech cultTech = CultivationUtils.getCultTechFromEntity(player);
		ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(player);
		IFoundation foundation = CultivationUtils.getFoundationFromEntity(player);
		WuxiaCraft.logger.info("Applying " + player.getDisplayNameString() + " cultivation.");
		NetworkWrapper.INSTANCE.sendTo(new UnifiedCapabilitySyncMessage(cultivation, cultTech, skillCap, foundation, true), (EntityPlayerMP) player);
		applyModifiers(player);
	}

	/**
	 * When a mob dies and we get it's loot table and add another loot
	 *
	 * @param event Description of whats happening
	 */
	@SubscribeEvent
	public void onMobDrop(LivingDropsEvent event) {
		if (event.getEntity() instanceof WanderingCultivator) {
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(event.getEntityLiving());
			//scrolls
			List<Item> scrolls = new ArrayList<>();
			for (Item i : WuxiaItems.ITEMS) {
				if (i instanceof ItemScroll) {
					scrolls.add(i);
				}
			}
			Random rnd = event.getEntity().world.rand;
			ItemStack drop = new ItemStack(scrolls.get(rnd.nextInt(scrolls.size())), 1);
			int bound = 40;
			CultivationLevel aux = CultivationLevel.BASE_LEVEL.getNextLevel();
			if (cultivation.getCurrentLevel() == aux) bound = 30;
			aux = aux.getNextLevel();
			if (cultivation.getCurrentLevel() == aux) bound = 15;
			aux = aux.getNextLevel();
			if (cultivation.getCurrentLevel() == aux) bound = 5;
			if (rnd.nextInt(bound) == 1) {
				event.getDrops().add(new EntityItem(event.getEntity().world, event.getEntity().posX, event.getEntity().posY, event.getEntity().posZ, drop));
			}
			for (EntityItem item : event.getDrops()) {
				ItemStack stack = item.getItem();
				if (stack.getItem() == WuxiaItems.RECIPE_SCROLL) {
					ItemRecipe.setRecipeAtRandom(stack);
				}
				item.setItem(stack);
			}
		}
	}

	/**
	 * Makes players that fly too high find their respective dimension, or random
	 * And come back to over world again
	 *
	 * @param event A description of what is happening
	 */
	@SubscribeEvent
	public void onPlayerFlyToAnotherDimension(TickEvent.PlayerTickEvent event) {
		if (event.side == Side.SERVER) {
			if (event.phase == TickEvent.Phase.END) {
				if (event.player.posY >= 2048) {
					if (event.player.world.provider.getDimension() == 0) {
						ICultivation cultivation = CultivationUtils.getCultivationFromEntity(event.player);
						CultivationLevel aux = CultivationLevel.BASE_LEVEL;
						for (int i = 0; i < 3; i++)
							aux = aux.getNextLevel(); //this way goes to dantian earth law lightning quick
						if (cultivation.getCurrentLevel().isGreaterThan(aux)) { //if player cultivation is greater than dantian equivalent
							double playerPosX = event.player.posX;
							double playerPosZ = event.player.posZ;
							ICultTech cultTech = CultivationUtils.getCultTechFromEntity(event.player);
							boolean found = false;
							int targetDimension = WuxiaDimensions.FIRE.getId();
							for (KnownTechnique kt : cultTech.getKnownTechniques()) {
								for (Element element : kt.getTechnique().getElements()) {
									if (Element.EARTH.equals(element) || Element.METAL.equals(element) || Element.FIRE.equals(element) || Element.WATER.equals(element) || Element.WOOD.equals(element)) {
										found = true;
										if (Element.EARTH.equals(element)) {
											targetDimension = WuxiaDimensions.EARTH.getId();
										}
										if (Element.METAL.equals(element)) {
											targetDimension = WuxiaDimensions.METAL.getId();
										}
										if (Element.WATER.equals(element)) {
											targetDimension = WuxiaDimensions.WATER.getId();
										}
										if (Element.WOOD.equals(element)) {
											targetDimension = WuxiaDimensions.WOOD.getId();
										}
									}
									break;
								}
								if (found) break;
							}
							if (!found) { //couldn't find one element
								int target = event.player.getRNG().nextInt(5); //0 to 4
								switch (target) {
									case 0:
										targetDimension = WuxiaDimensions.FIRE.getId();
										break;
									case 1:
										targetDimension = WuxiaDimensions.EARTH.getId();
										break;
									case 2:
										targetDimension = WuxiaDimensions.METAL.getId();
										break;
									case 3:
										targetDimension = WuxiaDimensions.WATER.getId();
										break;
									case 4:
										targetDimension = WuxiaDimensions.WOOD.getId();
										break;
								}
							}
							if (!MathUtils.inGroup(targetDimension, -1, 0, 1, WuxiaDimensions.MINING.getId())) {
								final int worldBorderSize = 2000000;
								if (playerPosX >= worldBorderSize) {
									playerPosX = worldBorderSize - 10;
								}
								if (playerPosZ >= worldBorderSize) {
									playerPosZ = worldBorderSize - 10;
								}
								if (playerPosX <= -worldBorderSize) {
									playerPosX = -worldBorderSize + 10;
								}
								if (playerPosZ <= -worldBorderSize) {
									playerPosZ = -worldBorderSize + 10;
								}
								TeleportationUtil.teleportPlayerToDimension((EntityPlayerMP) event.player, targetDimension, playerPosX + 0.5, 1512, playerPosZ + 0.5, event.player.rotationYaw, event.player.rotationPitch);
							} else if (targetDimension == WuxiaDimensions.MINING.getId()) {
								final int worldBorderSize = 3000000;
								if (playerPosX >= worldBorderSize) {
									playerPosX = worldBorderSize - 10;
								}
								if (playerPosZ >= worldBorderSize) {
									playerPosZ = worldBorderSize - 10;
								}
								if (playerPosX <= -worldBorderSize) {
									playerPosX = -worldBorderSize + 10;
								}
								if (playerPosZ <= -worldBorderSize) {
									playerPosZ = -worldBorderSize + 10;
								}
								TeleportationUtil.teleportPlayerToDimension((EntityPlayerMP) event.player, targetDimension, playerPosX + 0.5, 1512, playerPosZ + 0.5, event.player.rotationYaw, event.player.rotationPitch);
							} else {
								TeleportationUtil.teleportPlayerToDimension((EntityPlayerMP) event.player, targetDimension, playerPosX, 1512, playerPosZ, event.player.rotationYaw, event.player.rotationPitch);
							}
							IFoundation foundation = CultivationUtils.getFoundationFromEntity(event.player);
							double resistance = foundation.getAgilityModifier() + foundation.getConstitutionModifier() + foundation.getStrengthModifier(); //just these three
							event.player.attackEntityFrom(DamageSource.OUT_OF_WORLD, (float) Math.max(1, cultivation.getSpeedIncrease() * 9.0 - resistance));
						}
					} else if (MathUtils.inGroup(event.player.world.provider.getDimension(),
							WuxiaDimensions.EARTH.getId(),
							WuxiaDimensions.WOOD.getId(),
							WuxiaDimensions.METAL.getId(),
							WuxiaDimensions.WATER.getId(),
							WuxiaDimensions.FIRE.getId(),
							WuxiaDimensions.MINING.getId())) {
						double playerPosX = event.player.posX;
						double playerPosZ = event.player.posZ;
						//back to over world
						TeleportationUtil.teleportPlayerToDimension((EntityPlayerMP) event.player, 0, playerPosX + 0.5, 1512, playerPosZ + 0.5, event.player.rotationYaw, event.player.rotationPitch);
					}
				}
			}
		}
	}

	/**
	 * Destroys scheduled blocks from players skills, only 5 blocks per tick
	 *
	 * @param event Description of whats happening
	 */
	@SubscribeEvent
	public void onBreakScheduledBlocks(TickEvent.PlayerTickEvent event) {
		ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(event.player);
		int i = 5;
		while (!skillCap.isScheduledEmpty() && i > 0) {
			i--;
			event.player.world.destroyBlock(skillCap.popScheduledBlockBreaks(), true);
		}
	}

	/**
	 * When a mob spawn, it also searches for wandering cultivators
	 *
	 * @param event Description of whats happening
	 */
	@SubscribeEvent
	public void onSpawnLiving(LivingSpawnEvent event) {
		if (event.getEntityLiving() instanceof EntityMob && !(event.getEntityLiving() instanceof EntityCreeper)) {
			((EntityMob) event.getEntityLiving()).targetTasks.addTask(2, new EntityAINearestAttackableTarget<>((EntityCreature) event.getEntityLiving(), WanderingCultivator.class, true));
		}
	}

	/**
	 * This will get EntityItems that are from spirit stones and turn them into a stock pile;
	 *
	 * @param event Description of what is happening
	 */
	@SubscribeEvent
	public void onSpiritStoneStackFloats(TickEvent.WorldTickEvent event) {
		if (event.side == Side.SERVER && event.phase == TickEvent.Phase.END) {
			List<EntityItem> items = event.world.getEntities(EntityItem.class, input -> {
				boolean isSpiritStone = false;
				if (input != null) {
					if (input.getItem().getItem() instanceof ItemSpiritStone)
						isSpiritStone = true;
				}
				return isSpiritStone && input.ticksExisted >= 40 && input.getItem().getItemDamage() == 0;
			}); // 2 seconds existing right
			for (EntityItem item : items) {
				ItemStack stack = item.getItem();
				BlockPos pos = item.getPosition();
				Block spiritStones = WuxiaBlocks.SPIRIT_STONE_STACK_BLOCK;
				if (event.world.mayPlace(spiritStones, pos, true, EnumFacing.NORTH, item)) {
					event.world.setBlockState(pos, spiritStones.getDefaultState());
					SpiritStoneStackTileEntity te = (SpiritStoneStackTileEntity) event.world.getTileEntity(pos);
					if (te != null) {
						te.stack = stack;
					}
					item.setDead();
				} else if (event.world.getBlockState(pos).getBlock() == WuxiaBlocks.SPIRIT_STONE_STACK_BLOCK) {
					SpiritStoneStackTileEntity te = (SpiritStoneStackTileEntity) event.world.getTileEntity(pos);
					if (te != null) {
						if (te.stack.getItem() == stack.getItem()) { //meaning they're the same
							int remaining = te.stack.getMaxStackSize() - te.stack.getCount();
							int having = stack.getCount();
							int applying = Math.min(remaining, having);
							int left = having - applying;
							int right = te.stack.getCount() + applying;
							te.stack.setCount(right);
							stack.setCount(left);
							if (stack.isEmpty()) item.setDead();
							event.world.markAndNotifyBlock(pos, null, event.world.getBlockState(pos), event.world.getBlockState(pos), 3);
						}
					}
				}
			}
		}
	}

	/**
	 * Block#onBlockActivated wouldn't be called when sneaking, so i had to hack
	 *
	 * @param event A description of whats happening
	 */
	@SubscribeEvent
	public void onPlayerInteractSneaking(PlayerInteractEvent.RightClickBlock event) {
		if (event.getEntityPlayer().isSneaking() && event.getHand() == EnumHand.MAIN_HAND) {
			IBlockState blockState = event.getWorld().getBlockState(event.getPos());
			if (blockState.getBlock() instanceof SpiritStoneStackBlock) {
				Vec3d hit = event.getHitVec();
				if (blockState.getBlock().onBlockActivated(event.getWorld(), event.getPos(), blockState, event.getEntityPlayer(), event.getHand(), Objects.requireNonNull(event.getFace()), (float) hit.x, (float) hit.y, (float) hit.z)) {
					event.setUseBlock(Event.Result.ALLOW);
					event.setCanceled(true);
				}
			}
		}
	}

	/**
	 * When player has a cultivation that allows
	 *
	 * @param event A description of whats happening
	 */
	@SubscribeEvent
	public void onPlayerHunger(TickEvent.PlayerTickEvent event) {
		EntityPlayer player = event.player;
		ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
		double cost = (CultivationLevel.DEFAULTS.get(5).getMaxEnergyByLevel(1) * (5f / 18f));

		if (player.ticksExisted % 100 == 0) {
			if (player.getFoodStats().getFoodLevel() < 20 && cultivation.getCurrentLevel().energyAsFood) {
				if (cultivation.getCurrentLevel().needNoFood) {
					player.getFoodStats().setFoodLevel(20);
					try {
						foodStats.setFloat(player.getFoodStats(), 50f);
					} catch (Exception e) {
						WuxiaCraft.logger.error("Couldn't help with food, sorry!");
						e.printStackTrace();
					}
				} else if (cultivation.hasEnergy(cost)) {
					player.getFoodStats().setFoodLevel(20);
					try {
						foodStats.setFloat(player.getFoodStats(), 50f);
						cultivation.remEnergy(cost);
					} catch (Exception e) {
						WuxiaCraft.logger.error("Couldn't help with food, sorry!");
						e.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Applies the modifiers to the corresponding player based on its cultivation
	 *
	 * @param player Player to be applied
	 */
	public static void applyModifiers(EntityPlayer player) {

		ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
		IFoundation foundation = CultivationUtils.getFoundationFromEntity(player);
		ICultTech cultTech = CultivationUtils.getCultTechFromEntity(player);

		//Adds the potions effects from cult tech
		for (KnownTechnique kt : cultTech.getKnownTechniques()) {
			for (PotionEffect effect : kt.getTechniqueEffects()) {
				player.addPotionEffect(new PotionEffect(effect.getPotion(), effect.getDuration() + 19, effect.getAmplifier(), effect.getIsAmbient(), effect.doesShowParticles()));
			}
		}

		double level_spd_mod = foundation.getAgilityModifier() * SharedMonsterAttributes.MOVEMENT_SPEED.getDefaultValue() * 0.005 * 0.2;
		if (cultivation.getMaxSpeed() >= 0) {
			double max_speed = cultivation.getMaxSpeed() * SharedMonsterAttributes.MOVEMENT_SPEED.getDefaultValue() * 0.2;
			level_spd_mod = Math.min(max_speed, level_spd_mod);
		}
		level_spd_mod *= (cultivation.getSpeedHandicap() / 100f);

		AttributeModifier strength_mod = new AttributeModifier(strength_mod_name, foundation.getStrengthModifier() * 0.200f, 0);
		AttributeModifier health_mod = new AttributeModifier(health_mod_name, foundation.getConstitutionModifier() * 0.4f, 0);
		//since armor base is 0, it'll add 2*strength as armor
		//I'll use for now strength for increase every other stat, since it's almost the same after all
		AttributeModifier armor_mod = new AttributeModifier(armor_mod_name, foundation.getResistanceModifier() * 0.2f, 0);
		AttributeModifier speed_mod = new AttributeModifier(speed_mod_name, level_spd_mod, 0);
		AttributeModifier attack_speed_mod = new AttributeModifier(attack_speed_mod_name, foundation.getDexterityModifier() / 24f, 0);

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

		if (cultTech.hasElement(Element.WATER)) {
			AttributeModifier swim_speed_mod = new AttributeModifier(swim_mod_name, level_spd_mod, 0);

			//remove any previous swim speed modifiers
			for (AttributeModifier mod : player.getEntityAttribute(EntityPlayer.SWIM_SPEED).getModifiers()) {
				if (mod.getName().equals(swim_mod_name)) {
					player.getEntityAttribute(EntityPlayer.SWIM_SPEED).removeModifier(mod);
				}
			}
			player.getEntityAttribute(EntityPlayer.SWIM_SPEED).applyModifier(swim_speed_mod);
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
