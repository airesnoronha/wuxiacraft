package com.airesnor.wuxiacraft.cultivation.skills;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.CultTechProvider;
import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.KnownTechnique;
import com.airesnor.wuxiacraft.entities.skills.FireThrowable;
import com.airesnor.wuxiacraft.entities.skills.WaterBladeThrowable;
import com.airesnor.wuxiacraft.entities.skills.WaterNeedleThrowable;
import com.airesnor.wuxiacraft.handlers.EventHandler;
import com.airesnor.wuxiacraft.handlers.RendererHandler;
import com.airesnor.wuxiacraft.networking.SpawnParticleMessage;
import com.airesnor.wuxiacraft.utils.OreUtils;
import com.airesnor.wuxiacraft.utils.SkillUtils;
import com.airesnor.wuxiacraft.utils.TreeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDye;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
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
	}

	public static final Skill CULTIVATE = new Skill("cultivate", 50f, 10f, 150f, 0f).setAction(new ISkillAction() {
		@Override
		public boolean activate(EntityPlayer actor) {
			ICultTech cultTech = actor.getCapability(CultTechProvider.CULT_TECH_CAPABILITY, null);
			if (actor.world instanceof WorldServer) {
				WorldServer ws = (WorldServer) actor.world;
				int particles = 10;
				for (KnownTechnique kt : cultTech.getKnownTechniques()) {
					for (Element e : kt.getTechnique().getElements()) {
						for (int i = 0; i < particles; i++) {
							float randX = 2 * (float) actor.world.rand.nextFloat() - 1;
							float randY = 2 * (float) actor.world.rand.nextFloat() - 1;
							float randZ = 2 * (float) actor.world.rand.nextFloat() - 1;
							float dist = (float) Math.sqrt(randX * randX + randY * randY + randZ * randZ) * 30f;
							SpawnParticleMessage spm = new SpawnParticleMessage(e.getParticle(), false, actor.posX + randX, actor.posY + 0.9f + randY, actor.posZ + randZ, -randX / dist, -randY / dist, -randZ / dist, 0);
							SkillUtils.sendMessageWithinRange(ws, actor.getPosition(), spm.isIgnoreRange() ? 262144.0D : 1024.0D, spm);
						}
					}
				}
			}
			ICultivation cultivation = actor.getCapability(CultivationProvider.CULTIVATION_CAP, null);
			EventHandler.playerAddProgress(actor, cultivation, cultTech.getOverallCultivationSpeed());
			return true;
		}
	}).setWhenCasting(new ISkillAction() {
		@Override
		public boolean activate(EntityPlayer actor) {
			ICultTech cultTech = actor.getCapability(CultTechProvider.CULT_TECH_CAPABILITY, null);
			for (KnownTechnique kt : cultTech.getKnownTechniques()) {
				if (actor.world instanceof WorldServer) {
					WorldServer ws = (WorldServer) actor.world;
					int particles = 2;
					for (Element e : kt.getTechnique().getElements()) {
						for (int i = 0; i < particles; i++) {
							float randX = 2 * (float) actor.world.rand.nextFloat() - 1;
							float randY = 2 * (float) actor.world.rand.nextFloat() - 1;
							float randZ = 2 * (float) actor.world.rand.nextFloat() - 1;
							float dist = (float) Math.sqrt(randX * randX + randY * randY + randZ * randZ) * 30f;
							SpawnParticleMessage spm = new SpawnParticleMessage(e.getParticle(), false, actor.posX + randX, actor.posY + 0.9f + randY, actor.posZ + randZ, -randX / dist, -randY / dist, -randZ / dist, 0);
							SkillUtils.sendMessageWithinRange(ws, actor.getPosition(), spm.isIgnoreRange() ? 262144.0D : 1024.0D, spm);
						}
					}
				}
			}
			ICultivation cultivation = actor.getCapability(CultivationProvider.CULTIVATION_CAP, null);
			EventHandler.playerAddProgress(actor, cultivation, cultTech.getOverallCultivationSpeed() * 0.06f);
			return true;
		}
	});

	public static final Skill GATHER_WOOD = new Skill("gather_wood", 20f, 1f, 6f, 0f)
			.setAction(new ISkillAction() {
				@Override
				public boolean activate(EntityPlayer actor) {
					World worldIn = actor.world;
					ISkillCap skillCap = actor.getCapability(SkillsProvider.SKILL_CAP_CAPABILITY, null);
					RayTraceResult rtr = actor.rayTrace(6f, 1f);
					BlockPos pos = rtr.getBlockPos();
					boolean activated = false;
					IBlockState blockState = worldIn.getBlockState(pos);
					List<Block> logs = new ArrayList<>();
					logs.add(Blocks.LOG);
					logs.add(Blocks.LOG2);
					if (logs.contains(blockState.getBlock())) {
						Stack<BlockPos> tree = TreeUtils.findTree(worldIn, pos);
						if (!tree.isEmpty()) {
							WuxiaCraft.logger.info("Added block to break");
							BlockPos pos2 = tree.pop();
							skillCap.addScheduledBlockBreaks(pos2);
							ItemDye.spawnBonemealParticles(worldIn, pos2, 10);
							activated = true;
						}
					}
					return activated;
				}
			});

	public static final Skill ACCELERATE_GROWTH = new Skill("accelerate_growth", 60f, 1f, 25f, 0f)
			.setAction(new ISkillAction() {
				@Override
				public boolean activate(EntityPlayer actor) {
					World worldIn = actor.world;
					RayTraceResult rtr = actor.rayTrace(8f, 1f);
					BlockPos pos = rtr.getBlockPos();
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
									ItemDye.spawnBonemealParticles(worldIn, pos, 20);
									activated = true;
								}
							}
						}
					}
					return activated;
				}
			});

	public static final Skill FLAMES = new Skill("flames", 40f, 1.5f, 6f, 0f).setAction(new ISkillAction() {
		@Override
		public boolean activate(EntityPlayer actor) {
			ICultivation cultivation = actor.getCapability(CultivationProvider.CULTIVATION_CAP, null);
			for (int i = 0; i < 3; i++) {
				FireThrowable ft = new FireThrowable(actor.world, actor, cultivation.getCurrentLevel().getStrengthModifierBySubLevel(cultivation.getCurrentSubLevel()) / 3f);
				ft.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0.3f, 1.2f, 0.4f);
				actor.world.spawnEntity(ft);
			}
			return true;
		}
	});

	public static final Skill FIRE_BAll = new Skill("fire_ball", 120f, 3f, 110f, 0f).setAction(new ISkillAction() {
		@Override
		public boolean activate(EntityPlayer actor) {
			ICultivation cultivation = actor.getCapability(CultivationProvider.CULTIVATION_CAP, null);
			for (int i = 0; i < 5; i++) {
				FireThrowable ft = new FireThrowable(actor.world, actor, 4 + cultivation.getStrengthIncrease() * 1.2f, 400, 30, 0.3f);
				ft.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0.3f, Math.min(0.8f * cultivation.getSpeedIncrease() * 0.08f, 2.2f), 0.4f);
				actor.world.spawnEntity(ft);
			}
			return true;
		}
	});

	public static final Skill METAL_DETECTION = new Skill("metal_detection", 10f, 0.6f, 10f, 0f).setAction(new ISkillAction() {
		@Override
		public boolean activate(EntityPlayer actor) {
			if (actor.world.isRemote) {
				ICultivation cultivation = actor.getCapability(CultivationProvider.CULTIVATION_CAP, null);
				final int max_range = 128;
				int range = Math.min(max_range, 16 + (int) (Math.floor(0.2 * (1 - cultivation.getStrengthIncrease()))));
				OreUtils.findOres(actor.world, actor.getPosition(), range);
				float duration = 10 * 20; // 10 seconds
				RendererHandler.worldRenderQueue.add(duration, () -> {
					OreUtils.drawFoundOres(actor);
					return null;
				});
			}
			return true;
		}
	});

	public static final Skill ORE_SUCTION = new Skill("ore_suction", 150f, 2.5f, 160f, 0f).setAction(new ISkillAction() {
		@Override
		public boolean activate(EntityPlayer actor) {
			boolean activated = false;
			ICultivation cultivation = actor.getCapability(CultivationProvider.CULTIVATION_CAP, null);
			final int max_range = 64;
			int range = Math.min(max_range, 8 + (int) (Math.floor(0.1 * (1 - cultivation.getStrengthIncrease()))));
			List<BlockPos> positions = OreUtils.findOres(actor.world, actor.getPosition(), range);
			for (BlockPos pos : positions) {
				IBlockState block = actor.world.getBlockState(pos);
				ItemStack item = new ItemStack(block.getBlock().getItemDropped(block, actor.world.rand, 0));
				item.setCount(block.getBlock().quantityDropped(actor.world.rand));
				IBlockState stone = Blocks.STONE.getDefaultState();
				actor.world.setBlockState(pos, stone);
				EntityItem oreItem = actor.dropItem(item, false);
				oreItem.setNoPickupDelay();
				oreItem.setOwner(actor.getName());
				activated = true;
			}
			return activated;
		}
	});

	public static final Skill EARTH_SUCTION = new Skill("earth_suction", 20f, 0.8f, 5f, 0f).setAction(new ISkillAction() {
		@Override
		public boolean activate(EntityPlayer actor) {
			boolean activated = false;
			RayTraceResult rtr = actor.rayTrace(5, 1f);
			if (rtr.typeOfHit == RayTraceResult.Type.BLOCK) {
				BlockPos pos = rtr.getBlockPos();
				if (OreUtils.earthTypes.contains(actor.world.getBlockState(pos).getBlock())) {
					IBlockState block = actor.world.getBlockState(pos);
					ItemStack item = new ItemStack(block.getBlock().getItemDropped(block, actor.world.rand, 0));
					item.setCount(block.getBlock().quantityDropped(actor.world.rand));
					actor.world.setBlockToAir(pos);
					EntityItem earthItem = actor.dropItem(item, false);
					earthItem.setNoPickupDelay();
					earthItem.setOwner(actor.getName());
					activated = true;
				}
			}
			return activated;
		}
	});

	public static final Skill EARTHLY_WALL = new Skill("earthly_wall", 20f, 0.8f, 50f, 0f).setAction(new ISkillAction() {
		@Override
		public boolean activate(EntityPlayer actor) {
			boolean activated = false;
			RayTraceResult rtr = actor.rayTrace(5, 1f);
			if (rtr.typeOfHit == RayTraceResult.Type.BLOCK) {
				BlockPos pos = rtr.getBlockPos();
				if (OreUtils.earthTypes.contains(actor.world.getBlockState(pos).getBlock())) {
					IBlockState block = actor.world.getBlockState(pos);
					IBlockState newBlock = null;
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
		}
	});

	// Credits : My Girlfriend
	public static Skill WATER_NEEDLE = new Skill("water_needle", 60f, 1.1f, 20f, 0f, "Lysian Prieto")
			.setAction(new ISkillAction() {
				@Override
				public boolean activate(EntityPlayer actor) {
					ICultivation cultivation = actor.getCapability(CultivationProvider.CULTIVATION_CAP, null);
					WaterNeedleThrowable needle = new WaterNeedleThrowable(actor.world, actor, 4 + cultivation.getStrengthIncrease() * 0.3f, 300);
					needle.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0.3f, Math.min(1.8f, 0.8f + cultivation.getSpeedIncrease() * 0.12f), 0.2f);
					actor.world.spawnEntity(needle);
					return true;
				}
			});

	public static Skill WATER_BLADE = new Skill("water_blade", 200f, 2.0f, 85f, 0f).setAction(actor -> {
		ICultivation cultivation = actor.getCapability(CultivationProvider.CULTIVATION_CAP, null);
		float swordModifier = 1f;
		if(actor.getHeldItem(EnumHand.MAIN_HAND).getItem() instanceof ItemSword) {
			swordModifier = 1.5f;
		};
		WaterBladeThrowable blade = new WaterBladeThrowable(actor.world, actor, 8 + cultivation.getStrengthIncrease() * 1.5f*swordModifier, 300);
		blade.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0.3f, 0.7f + cultivation.getSpeedIncrease() * 0.5f*swordModifier, 0.2f);
		actor.world.spawnEntity(blade);
		return true;
	});

	public static Skill SELF_HEALING = new Skill("self_healing", 80f, 1f, 80f, 0f).setAction(actor -> {
		ICultivation cultivation = actor.getCapability(CultivationProvider.CULTIVATION_CAP, null);
		actor.heal(Math.max(10f, cultivation.getStrengthIncrease() * 0.05f));
		return true;
	});

	public static Skill HEALING_HANDS = new Skill("healing_hands", 80f, 1.4f, 120f, 0f).setAction(actor -> {
		boolean activated = false;
		Entity result = SkillUtils.rayTraceEntities(actor, 10f, 1f);
		if (result instanceof EntityLiving) {
			EntityLiving entity = (EntityLiving) result;
			ICultivation cultivation = actor.getCapability(CultivationProvider.CULTIVATION_CAP, null);
			entity.heal(Math.max(10f, cultivation.getStrengthIncrease() * 0.05f));
			WuxiaCraft.logger.info("Healing a " + entity + "by " + ((int) Math.max(10f, cultivation.getStrengthIncrease() * 0.05f)));
			activated = true;
		}
		return activated;
	});

	//Credits: My Girlfirend
	public static Skill WALL_CROSSING = new Skill("wall_crossing", 200f, 3.0f, 130f, 0f, "Lysian Prieto").setAction(actor -> {
		boolean activated = false;
		BlockPos pos = actor.getPosition();
		EnumFacing facing = EnumFacing.fromAngle(actor.rotationYaw);
		RayTraceResult rtr = actor.rayTrace(3f, 1f);
		if (rtr.typeOfHit == RayTraceResult.Type.BLOCK) {
			BlockPos block = rtr.getBlockPos();
			int range = 15;
			int test = 0;
			while (!actor.world.isAirBlock(block) && test < range) {
				block = block.offset(facing);
				test++;
			}
			if (actor.world.isAirBlock(block) && actor.world.isAirBlock(block.up())) {
				actor.setPositionAndUpdate(block.getX() + 0.5d, block.getY() + 0.1d, block.getZ() + 0.5d);
				activated = true;
			}
		}
		return activated;
	});

}
