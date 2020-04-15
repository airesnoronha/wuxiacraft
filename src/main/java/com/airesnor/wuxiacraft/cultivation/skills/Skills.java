package com.airesnor.wuxiacraft.cultivation.skills;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.KnownTechnique;
import com.airesnor.wuxiacraft.entities.skills.FireThrowable;
import com.airesnor.wuxiacraft.entities.skills.SwordBeamThrowable;
import com.airesnor.wuxiacraft.entities.skills.WaterBladeThrowable;
import com.airesnor.wuxiacraft.entities.skills.WaterNeedleThrowable;
import com.airesnor.wuxiacraft.handlers.RendererHandler;
import com.airesnor.wuxiacraft.networking.*;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import com.airesnor.wuxiacraft.utils.OreUtils;
import com.airesnor.wuxiacraft.utils.SkillUtils;
import com.airesnor.wuxiacraft.utils.TreeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Enchantments;
import net.minecraft.init.MobEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

public class Skills {

	public static final List<Skill> SKILLS = new ArrayList<>();

	public static void init() {
		SKILLS.add(CULTIVATE);
		SKILLS.add(GATHER_WOOD);
		SKILLS.add(ACCELERATE_GROWTH);
		SKILLS.add(FLAMES);
		SKILLS.add(FIRE_BAll);
		SKILLS.add(METAL_DETECTION);
		SKILLS.add(ORE_SUCTION);
		SKILLS.add(EARTH_SUCTION);
		SKILLS.add(EARTHLY_WALL);
		SKILLS.add(WATER_NEEDLE);
		SKILLS.add(WATER_BLADE);
		SKILLS.add(SELF_HEALING);
		SKILLS.add(HEALING_HANDS);
		SKILLS.add(WALL_CROSSING);
		SKILLS.add(WEAK_SWORD_BEAM);
		SKILLS.add(SWORD_BEAM_BARRAGE);
		SKILLS.add(WEAK_SWORD_FLIGHT);
		SKILLS.add(LIGHT_FEET_LEAP);
		SKILLS.add(LIGHT_FEET_SKILL);
		SKILLS.add(MINOR_POWER_PUNCH);
		SKILLS.add(MINOR_BODY_REINFORCEMENT);
		SKILLS.add(ADEPT_SWORD_FLIGHT);
	}

	public static final Potion ENLIGHTENMENT = new EnlightenmentPotion("enlightenment");

	public static final ISkillAction APPLY_SLOWNESS = actor -> {
		PotionEffect effect1 = new PotionEffect(MobEffects.SLOWNESS, 35, 3, false, false);
		PotionEffect effect2 = new PotionEffect(MobEffects.MINING_FATIGUE, 35, 2, false, false);
		actor.addPotionEffect(effect1);
		actor.addPotionEffect(effect2);
		return true;
	};

	public static final Skill CULTIVATE = new Skill("cultivate", false, 1f, 10f, 300f, 0f)
			.setAction(actor -> {
				if (!actor.world.isRemote) {
					int bound = 100;
					int amplifier = 0;
					PotionEffect effect = actor.getActivePotionEffect(ENLIGHTENMENT);
					if (effect != null) {
						bound = 300;
						amplifier = 1;
						if (effect.getAmplifier() == 1) {
							bound = 800;
							amplifier = 2;
						}
					}
					if (actor.getRNG().nextInt(bound) == 0) {
						effect = new PotionEffect(ENLIGHTENMENT, 20 * 60 * (3 - amplifier), amplifier, true, true);
						actor.addPotionEffect(effect);
					}
				}
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
				CultivationUtils.cultivatorAddProgress(actor, cultivation, 0.001f, true, true);
				return true;
			})
			.setWhenCasting(actor -> {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
				ICultTech cultTech = CultivationUtils.getCultTechFromEntity(actor);
				ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(actor);
				double amount = cultTech.getOverallCultivationSpeed() * 0.25 * 10;
				float energy = cultTech.getOverallCultivationSpeed() * 1.25f * 10;
				skillCap.stepCastProgress(-cultivation.getSpeedIncrease() + 1);
				if ((int) skillCap.getCastProgress() % 10 == 9) {
					NetworkWrapper.INSTANCE.sendToServer(new ActivatePartialSkillMessage("applySlowness", cultivation.hasEnergy(energy) ? energy : 0));
				}
				if (cultivation.hasEnergy(energy)) {
					if ((int) skillCap.getCastProgress() % 5 == 0) {
						for (KnownTechnique kt : cultTech.getKnownTechniques()) {
							int particles = 6;
							for (Element e : kt.getTechnique().getElements()) {
								for (int i = 0; i < particles; i++) {
									float randX = 2 * actor.world.rand.nextFloat() - 1;
									float randY = 2 * actor.world.rand.nextFloat() - 1;
									float randZ = 2 * actor.world.rand.nextFloat() - 1;
									float dist = (float) Math.sqrt(randX * randX + randY * randY + randZ * randZ) * 30f;
									SpawnParticleMessage spm = new SpawnParticleMessage(e.getParticle(), false, actor.posX + randX, actor.posY + 0.9f + randY, actor.posZ + randZ, -randX / dist, -randY / dist, -randZ / dist, 0);
									NetworkWrapper.INSTANCE.sendToServer(spm);
								}
							}
						}
					}
					if (actor instanceof EntityPlayer) {
						if ((int) skillCap.getCastProgress() % 10 == 9) {
							CultivationUtils.cultivatorAddProgress(actor, cultivation, amount, false, false);
							cultivation.remEnergy(energy);
							NetworkWrapper.INSTANCE.sendToServer(new ProgressMessage(0, amount, false, false));
							NetworkWrapper.INSTANCE.sendToServer(new ActivatePartialSkillMessage("applySlowness", cultivation.hasEnergy(energy) ? energy : 0));
						}
					}
				}
				return cultivation.hasEnergy(energy);
			});

	public static final Skill GATHER_WOOD = new Skill("gather_wood", false, 80f, 3f, 20f, 0f)
			.setAction(actor -> {
				World worldIn = actor.world;
				boolean activated = false;
				BlockPos pos = SkillUtils.rayTraceBlock(actor, 5, 1f);
				if (pos != null) {
					IBlockState blockState = worldIn.getBlockState(pos);
					List<Block> logs = new ArrayList<>();
					logs.add(Blocks.LOG);
					logs.add(Blocks.LOG2);
					if (logs.contains(blockState.getBlock())) {
						Stack<BlockPos> tree = TreeUtils.findTree(worldIn, pos);
						if (!tree.isEmpty()) {
							//WuxiaCraft.logger.info("Added block to break");
							BlockPos pos2 = tree.pop();
							if (!actor.world.isRemote) {
								actor.world.destroyBlock(pos2, true);
								for (int i = 0; i < 10; i++) {
									double x = pos2.getX() + actor.getRNG().nextFloat();
									double y = pos2.getY() + actor.getRNG().nextFloat();
									double z = pos2.getZ() + actor.getRNG().nextFloat();
									double my = 0.05 + actor.getRNG().nextFloat() * 0.08;
									SpawnParticleMessage spm = new SpawnParticleMessage(EnumParticleTypes.VILLAGER_HAPPY, false, x, y, z, 0, my, 0, 0);
									SkillUtils.sendMessageWithinRange((WorldServer) actor.world, pos2, 64, spm);
								}
							}
							activated = true;
						}
					}
				}
				return activated;
			});

	public static final Skill ACCELERATE_GROWTH = new Skill("accelerate_growth", false, 120f, 1f, 60f, 0f)
			.setAction(actor -> {
				World worldIn = actor.world;
				BlockPos pos = SkillUtils.rayTraceBlock(actor, 6, 1f);
				boolean activated = false;
				if (pos != null) {
					List<BlockPos> neighbors = new ArrayList<>();
					neighbors.add(pos);
					neighbors.add(pos.north());
					neighbors.add(pos.east());
					neighbors.add(pos.west());
					neighbors.add(pos.south());
					neighbors.add(pos.north().east());
					neighbors.add(pos.north().west());
					neighbors.add(pos.south().east());
					neighbors.add(pos.south().west());
					for (BlockPos p : neighbors) {
						if (worldIn.getBlockState(p).getBlock() instanceof IGrowable) {
							IGrowable iGrowable = (IGrowable) worldIn.getBlockState(p).getBlock();
							if (iGrowable.canGrow(worldIn, p, worldIn.getBlockState(p), worldIn.isRemote)) {
								iGrowable.grow(worldIn, worldIn.rand, p, worldIn.getBlockState(p));
								if (!actor.world.isRemote) {
									for (int i = 0; i < 20; i++) {
										double x = pos.getX() + actor.getRNG().nextFloat();
										double y = pos.getY() + actor.getRNG().nextFloat();
										double z = pos.getZ() + actor.getRNG().nextFloat();
										double my = 0.05 + actor.getRNG().nextFloat() * 0.08;
										SpawnParticleMessage spm = new SpawnParticleMessage(EnumParticleTypes.VILLAGER_HAPPY, false, x, y, z, 0, my, 0, 0);
										NetworkWrapper.INSTANCE.sendToAll(spm);
									}
								}
								activated = true;
							}
						}
					}
				}
				return activated;
			});

	public static final Skill FLAMES = new Skill("flames", true, 80f, 1.5f, 20f, 0f)
			.setAction(actor -> {
				float shootPitch = actor.rotationPitch;
				float shootYaw = actor.rotationYawHead;
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
				for (int i = 0; i < 3; i++) {
					FireThrowable ft = new FireThrowable(actor.world, actor, Math.min(10f, cultivation.getStrengthIncrease() / 3f));
					ft.shoot(actor, shootPitch, shootYaw, 0.3f, 1.2f, 0.4f);
					actor.world.spawnEntity(ft);
				}
				return true;
			});

	public static final Skill FIRE_BAll = new Skill("fire_ball", true, 480f, 3f, 120f, 0f)
			.setAction(actor -> {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
				for (int i = 0; i < 5; i++) {
					FireThrowable ft = new FireThrowable(actor.world, actor, Math.min(4 + cultivation.getStrengthIncrease() * 1.2f, 40f), 600, 60, 1.2f);
					ft.shoot(actor, actor.rotationPitch, actor.rotationYawHead, 0.3f, Math.min(0.8f + cultivation.getSpeedIncrease() * 0.4f, 1.8f), 0.4f);
					actor.world.spawnEntity(ft);
				}
				return true;
			});

	public static final Skill METAL_DETECTION = new Skill("metal_detection", false, 400f, 0.6f, 180f, 0f)
			.setAction(actor -> {
				if (actor.world.isRemote) {
					if (actor instanceof EntityPlayer) {
						ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
						final int max_range = 32;
						int range = Math.min(max_range, 16 + (int) (Math.floor(0.2 * (cultivation.getStrengthIncrease())-1)));
						OreUtils.findOres(actor.world, actor.getPosition(), range);
						float duration = 10 * 20; // 10 seconds
						RendererHandler.worldRenderQueue.add(duration, () -> {
							OreUtils.drawFoundOres((EntityPlayer) actor);
							return null;
						});
					}
				}
				return true;
			});

	public static final Skill ORE_SUCTION = new Skill("ore_suction", false, 250f, 2.5f, 160f, 0f).setAction(actor -> {
		boolean activated = false;
		ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
		final int max_range = 64;
		int range = Math.min(max_range, 8 + (int) (Math.floor(0.1 * (1 - cultivation.getStrengthIncrease()))));
		List<BlockPos> positions = OreUtils.findOres(actor.world, actor.getPosition(), range);
		for (BlockPos pos : positions) {
			if (!actor.world.isRemote) {
				IBlockState block = actor.world.getBlockState(pos);
				IBlockState stone = Blocks.STONE.getDefaultState();
				actor.world.setBlockState(pos, stone);
				ItemStack item = new ItemStack(block.getBlock().getItemDropped(block, actor.world.rand, 0));
				item.setCount(block.getBlock().quantityDropped(actor.world.rand));
				EntityItem oreItem = new EntityItem(actor.world, actor.posX, actor.posY, actor.posZ, item);
				oreItem.setNoPickupDelay();
				oreItem.setOwner(actor.getName());
				actor.world.spawnEntity(oreItem);
			}
			activated = true;
		}
		return activated;
	});

	public static final Skill EARTH_SUCTION = new Skill("earth_suction", false, 60f, 0.8f, 20f, 0f).setAction(actor -> {
		boolean activated = false;
		BlockPos pos = SkillUtils.rayTraceBlock(actor, 5f, 1f);
		if (pos != null) {
			if (OreUtils.earthTypes.contains(actor.world.getBlockState(pos).getBlock())) {
				if (!actor.world.isRemote) {
					IBlockState block = actor.world.getBlockState(pos);
					actor.world.setBlockToAir(pos);
					ItemStack item = new ItemStack(block.getBlock().getItemDropped(block, actor.world.rand, 0));
					item.setCount(block.getBlock().quantityDropped(actor.world.rand));
					double dx = actor.posX - (pos.getX() + 0.5);
					double dy = (actor.posY + actor.getEyeHeight()) - (pos.getY() + 0.5);
					double dz = actor.posZ - (pos.getZ() + 0.5);
					double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
					EntityItem earthItem = new EntityItem(actor.world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, item);
					earthItem.motionX = dx / dist * 0.6f;
					earthItem.motionY = dy / dist * 0.6f;
					earthItem.motionZ = dz / dist * 0.6f;
					earthItem.setNoPickupDelay();
					earthItem.setOwner(actor.getName());
					actor.world.spawnEntity(earthItem);
				}
				activated = true;
			}
		}
		return activated;
	});

	public static final Skill EARTHLY_WALL = new Skill("earthly_wall", false, 320f, 0.8f, 50f, 0f).setAction(actor -> {
		boolean activated = false;
		BlockPos pos = SkillUtils.rayTraceBlock(actor, 5f, 1f);
		if (pos != null) {
			if (OreUtils.earthTypes.contains(actor.world.getBlockState(pos).getBlock())) {
				IBlockState block = actor.world.getBlockState(pos);
				IBlockState newBlock;
				if (block.getBlock() == Blocks.GRASS || block.getBlock() == Blocks.GRASS_PATH) {
					newBlock = Blocks.DIRT.getDefaultState();
				} else if (block.getBlock() == Blocks.GRASS) {
					newBlock = Blocks.COBBLESTONE.getDefaultState();
				} else {
					newBlock = block.getBlock().getDefaultState();
				}
				World world = actor.world;
				List<BlockPos> wall_position = new ArrayList<>();
				EnumFacing direction = EnumFacing.fromAngle(actor.rotationYaw);
				switch (direction) {
					case EAST:
					case WEST:
						for (int i = -3; i < 4; i++) {
							BlockPos wallPos = new BlockPos(pos.getX(), pos.getY(), pos.getZ() + i);
							int height = 3;
							if (i == 0) height = 4;
							else if (Math.abs(i) == 3) height = 1;
							for (int j = 0; j < height; j++) {
								wall_position.add(wallPos.up(j + 1));
							}
						}
						break;
					case NORTH:
					case SOUTH:
						for (int i = -3; i < 4; i++) {
							BlockPos wallPos = new BlockPos(pos.getX() + i, pos.getY(), pos.getZ());
							int height = 3;
							if (i == 0) height = 4;
							else if (Math.abs(i) == 3) height = 1;
							for (int j = 0; j < height; j++) {
								wall_position.add(wallPos.up(j + 1));
							}
						}
						break;
				}
				for (BlockPos toPlace : wall_position) {
					if (world.isAirBlock(toPlace) || world.getBlockState(toPlace).getBlock() == Blocks.TALLGRASS) {
						world.setBlockState(toPlace, newBlock);
						activated = true;
					}
				}
			}
		}
		return activated;
	});

	// Credits : My Girlfriend
	public static Skill WATER_NEEDLE = new Skill("water_needle", true, 100f, 1.1f, 20f, 0f, "Lysian Prieto")
			.setAction(actor -> {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
				WaterNeedleThrowable needle = new WaterNeedleThrowable(actor.world, actor, Math.min(17f, 4 + cultivation.getStrengthIncrease() * 0.3f), 300);
				needle.shoot(actor, actor.rotationPitch, actor.rotationYawHead, 0.3f, Math.min(1.8f, 0.8f + cultivation.getSpeedIncrease() * 0.12f), 0.2f);
				actor.world.spawnEntity(needle);
				return true;
			});

	public static Skill WATER_BLADE = new Skill("water_blade", true, 380f, 2.0f, 120f, 0f).setAction(actor -> {
		ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
		ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(actor);
		float swordModifier = 1f;
		if (actor.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSword) {
			swordModifier = 1.5f;
			actor.swingArm(EnumHand.MAIN_HAND);
			skillCap.stepCastProgress(cultivation.getSpeedIncrease() * 0.5f);
		}
		float damage = Math.min(60f, 8 + cultivation.getStrengthIncrease() * 1.5f * swordModifier);
		WaterBladeThrowable blade = new WaterBladeThrowable(actor.world, actor, damage, 300);
		blade.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0.3f, 0.7f + cultivation.getSpeedIncrease() * 0.5f * swordModifier, 0.2f);
		actor.world.spawnEntity(blade);
		return true;
	});

	public static Skill SELF_HEALING = new Skill("self_healing", false, 160f, 1f, 80f, 0f).setAction(actor -> {
		ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
		actor.heal(Math.max(15f, cultivation.getStrengthIncrease() * 0.05f));
		return true;
	});

	public static Skill HEALING_HANDS = new Skill("healing_hands", false, 220f, 1.4f, 120f, 0f).setAction(actor -> {
		boolean activated = false;
		Entity result = SkillUtils.rayTraceEntities(actor, 10f, 1f);
		if (result instanceof EntityLiving) {
			EntityLiving entity = (EntityLiving) result;
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
			entity.heal(Math.max(18f, cultivation.getStrengthIncrease() * 0.05f));
			WuxiaCraft.logger.info("Healing a " + entity + "by " + ((int) Math.max(10f, cultivation.getStrengthIncrease() * 0.05f)));
			activated = true;
		}
		return activated;
	});

	//Credits: My Girlfriend
	public static Skill WALL_CROSSING = new Skill("wall_crossing", false, 400f, 3.0f, 130f, 0f, "Lysian Prieto").setAction(actor -> {
		boolean activated = false;
		EnumFacing facing = EnumFacing.fromAngle(actor.rotationYaw);
		BlockPos pos = SkillUtils.rayTraceBlock(actor, 4f, 1f);
		if (pos != null) {
			int range = 15;
			int test = 0;
			while (!actor.world.isAirBlock(pos) && test < range) {
				pos = pos.offset(facing);
				test++;
			}
			if (actor.world.isAirBlock(pos) && actor.world.isAirBlock(pos.up())) {
				actor.setPositionAndUpdate(pos.getX() + 0.5d, pos.getY() + 0.1d, pos.getZ() + 0.5d);
				activated = true;
			}
		}
		return activated;
	});

	public static Skill WEAK_SWORD_BEAM = new Skill("weak_sword_beam", true, 180f, 2.5f, 40f, 0f)
			.setAction(actor -> {
				boolean activated = false;
				if (actor.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSword) {
					activated = true;
					float strength = CultivationUtils.getCultivationFromEntity(actor).getStrengthIncrease();
					float sword = ((ItemSword) actor.getHeldItem(EnumHand.MAIN_HAND).getItem()).getAttackDamage();
					int enchantment = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, actor.getHeldItem(EnumHand.MAIN_HAND));
					sword += enchantment > 0 ? 0.5 * (1 + enchantment) : 0;
					float damage = Math.min(60f, strength + sword);
					SwordBeamThrowable sbt = new SwordBeamThrowable(actor.world, actor, damage, 0xEF890A, 300);
					sbt.shoot(actor, actor.rotationPitch, actor.rotationYawHead, 1.0f, 1.6f, 0f);
					actor.world.spawnEntity(sbt);
					actor.swingArm(EnumHand.MAIN_HAND);
				}
				return activated;
			});

	public static final ISkillAction BARRAGE_MINOR_BEAM = actor -> {
		float strength = CultivationUtils.getCultivationFromEntity(actor).getStrengthIncrease();
		float sword = ((ItemSword) actor.getHeldItem(EnumHand.MAIN_HAND).getItem()).getAttackDamage() * 2;
		int enchantment = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, actor.getHeldItem(EnumHand.MAIN_HAND));
		sword += enchantment > 0 ? 0.5 * (1 + enchantment) : 0;
		float damage = Math.min(20f, strength * 0.3f + sword * 0.3f);
		SwordBeamThrowable sbt = new SwordBeamThrowable(actor.world, actor, damage, 0x89EF0A, 300);
		sbt.shoot(actor, actor.rotationPitch, actor.rotationYawHead, 1.0f, 1.2f, 0f);
		actor.world.spawnEntity(sbt);
		actor.swingArm(EnumHand.MAIN_HAND);
		return true;
	};

	public static Skill SWORD_BEAM_BARRAGE = new Skill("sword_beam_barrage", true, 220f, 4f, 150f, 60f)
			.setAction(actor -> {
				boolean activated = false;
				ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(actor);
				if (actor.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSword) {
					activated = true;
					float strength = CultivationUtils.getCultivationFromEntity(actor).getStrengthIncrease();
					float sword = ((ItemSword) actor.getHeldItem(EnumHand.MAIN_HAND).getItem()).getAttackDamage() * 2;
					int enchantment = EnchantmentHelper.getEnchantmentLevel(Enchantments.SHARPNESS, actor.getHeldItem(EnumHand.MAIN_HAND));
					sword += enchantment > 0 ? 0.5 * (1 + enchantment) : 0;
					float damage = Math.min(60f, strength + sword);
					SwordBeamThrowable sbt = new SwordBeamThrowable(actor.world, actor, damage, 0xEF890A, 300);
					sbt.shoot(actor, actor.rotationPitch, actor.rotationYawHead, 1.0f, 1.6f, 0f);
					actor.world.spawnEntity(sbt);
					actor.swingArm(EnumHand.MAIN_HAND);
					skillCap.resetBarrageCounter();
				}
				return activated;
			})
			.setWhenCasting(actor -> {
				boolean activated = false;
				ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(actor);
				for (int i = 0; i < 4; i++) {
					if (skillCap.getBarrageReleased() <= i && skillCap.getCastProgress() >= (i + 1) * 150 / 5f) {
						skillCap.increaseBarrageToRelease();
					}
				}
				for (int i = 0; i < skillCap.getBarrageToRelease(); i++) {
					activated = true;
					if (actor.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSword) {
						BARRAGE_MINOR_BEAM.activate(actor);
						CultivationUtils.getCultivationFromEntity(actor).remEnergy(120f);
						NetworkWrapper.INSTANCE.sendToServer(new ActivatePartialSkillMessage("barrageMinorBeam", 120f));
					}
					skillCap.increaseBarrageReleased();
				}
				skillCap.resetBarrageToRelease();
				return activated;
			});

	public static Skill WEAK_SWORD_FLIGHT = new Skill("weak_sword_flight", false, 9f, 0.1f, 500f, 200f)
			.setAction(actor -> true)
			.setWhenCasting(actor -> {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
				ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(actor);
				skillCap.stepCastProgress(-cultivation.getSpeedIncrease() + 1f);
				if (actor.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSword) {
					if (cultivation.hasEnergy(9f)) {
						cultivation.remEnergy(9f);
						if ((int) skillCap.getCastProgress() % 5 == 4)
							NetworkWrapper.INSTANCE.sendToServer(new EnergyMessage(1, 9 * 5));
						float speed = cultivation.getSpeedIncrease() * 0.6f;
						speed = Math.min(1.5f, speed);
						float yaw = actor.rotationYawHead;
						float pitch = actor.rotationPitch;
						float x = speed * -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
						float y = speed * -MathHelper.sin((pitch) * 0.017453292F);
						float z = speed * MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
						actor.motionX = x;
						actor.motionY = y;
						actor.motionZ = z;
						actor.fallDistance = 0;
						//swinging
						actor.swingingHand = EnumHand.MAIN_HAND;
						actor.swingProgressInt = 2;
						actor.isSwingInProgress = true;
					}
				}
				return true;
			});

	public static final Skill LIGHT_FEET_SKILL = new Skill("light_feet_skill", false, 180f, 3f, 120f, 20f)
			.setAction(actor -> {
				PotionEffect effect = new PotionEffect(MobEffects.SPEED, 2400, 2, false, true);
				actor.addPotionEffect(effect);
				return true;
			});

	public static final Skill LIGHT_FEET_LEAP = new Skill("light_feet_leap", false, 90f, 1.2f, 30f, 20f)
			.setAction(actor -> {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
				float speed = Math.min(cultivation.getSpeedIncrease() * 6f, 12f);
				float yaw = actor.rotationYawHead;
				float pitch = actor.rotationPitch;
				float x = speed * -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
				float y = 2f;
				float z = speed * MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
				actor.motionX += x;
				actor.motionY += y;
				actor.motionZ += z;
				actor.fallDistance = -50;
				return true;
			});

	public static final Skill MINOR_POWER_PUNCH = new Skill("minor_power_punch", true, 150f, 1.2f, 20f, 10f)
			.setAction(actor -> {
				boolean activated = false;
				Entity target = SkillUtils.rayTraceEntities(actor, 3f, 1f);
				if (target != null) {
					if (target instanceof EntityLivingBase) {
						activated = true;
						actor.swingArm(EnumHand.MAIN_HAND);
						ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
						float strength = cultivation.getStrengthIncrease() * 1.5f;
						for (PotionEffect effect : actor.getActivePotionEffects()) {
							if (effect.getPotion() == MobEffects.STRENGTH) {
								strength += (effect.getAmplifier() + 1) * 3;
							}
						}
						strength = Math.min(60f, strength);
						target.attackEntityFrom(DamageSource.causeMobDamage(actor), strength);
						float x = +MathHelper.sin(actor.rotationYawHead * 0.017453292F);
						float z = -MathHelper.cos(actor.rotationYawHead * 0.017453292F);
						((EntityLivingBase) target).knockBack(actor, strength * 0.3f, x, z);
					}
				}
				return activated;
			});

	public static final Skill MINOR_BODY_REINFORCEMENT = new Skill("minor_body_reinforcement", false, 120f, 1.2f, 180f, 20f)
			.setAction(actor -> {
				PotionEffect effect = new PotionEffect(MobEffects.STRENGTH, 1800, 2, false, true);
				actor.addPotionEffect(effect);
				return true;
			});

	public static Skill ADEPT_SWORD_FLIGHT = new Skill("adept_sword_flight", false, 26f, 0.1f, 2000f, 100f)
			.setAction(actor -> true)
			.setWhenCasting(actor -> {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
				ISkillCap skillCap = CultivationUtils.getSkillCapFromEntity(actor);
				skillCap.stepCastProgress(-cultivation.getSpeedIncrease() + 1f);
				if (actor.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSword) {
					if (cultivation.hasEnergy(26f)) {
						cultivation.remEnergy(26f);
						if ((int) skillCap.getCastProgress() % 5 == 4)
							NetworkWrapper.INSTANCE.sendToServer(new EnergyMessage(1, 26 * 5));
						float speed = cultivation.getSpeedIncrease() * 0.8f;
						speed = Math.min(3.5f, speed);
						float yaw = actor.rotationYawHead;
						float pitch = actor.rotationPitch;
						float x = speed * -MathHelper.sin(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
						float y = speed * -MathHelper.sin((pitch) * 0.017453292F);
						float z = speed * MathHelper.cos(yaw * 0.017453292F) * MathHelper.cos(pitch * 0.017453292F);
						actor.motionX = x;
						actor.motionY = y;
						actor.motionZ = z;
						actor.fallDistance = 0;
						//swinging
						actor.swingingHand = EnumHand.MAIN_HAND;
						actor.swingProgressInt = 2;
						actor.isSwingInProgress = true;
					}
				}
				return true;
			});
}
