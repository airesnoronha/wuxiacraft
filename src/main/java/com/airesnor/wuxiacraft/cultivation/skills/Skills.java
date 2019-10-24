package com.airesnor.wuxiacraft.cultivation.skills;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.CultTechProvider;
import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.KnownTechnique;
import com.airesnor.wuxiacraft.cultivation.techniques.Technique;
import com.airesnor.wuxiacraft.entities.skills.FireThrowable;
import com.airesnor.wuxiacraft.handlers.EventHandler;
import com.airesnor.wuxiacraft.handlers.RendererHandler;
import com.airesnor.wuxiacraft.networking.*;
import com.airesnor.wuxiacraft.utils.OreUtils;
import com.airesnor.wuxiacraft.utils.TreeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDye;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.*;
import java.util.concurrent.Callable;

public class Skills {

    public static final List<Skill> SKILLS = new ArrayList<>();

    public static void init() {
        SKILLS.add(CULTIVATE);
        SKILLS.add(GATHER_WOOD);
        SKILLS.add(ACCELERATE_GROWTH);
        SKILLS.add(FLAMES);
        SKILLS.add(FIRE_BAll);
        SKILLS.add(METAL_DETECTION);
    }

    public static final Skill CULTIVATE = new Skill("cultivate", 50f, 10f, 100f, 0f).setAction(new ISkillAction() {
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
                            sendMessageWithinRange(ws, actor.getPosition(), spm.isIgnoreRange() ? 262144.0D : 1024.0D, spm);
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
                            sendMessageWithinRange(ws, actor.getPosition(), spm.isIgnoreRange() ? 262144.0D : 1024.0D, spm);
                        }
                    }
                }
            }
            return true;
        }
    });

    public static final Skill GATHER_WOOD = new Skill("gather_wood", 20f, 1f, 10f, 0f)
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

    public static final Skill FLAMES = new Skill("flames", 40f, 1.5f, 5f, 0f).setAction(new ISkillAction() {
        @Override
        public boolean activate(EntityPlayer actor) {
            ICultivation cultivation = actor.getCapability(CultivationProvider.CULTIVATION_CAP, null);
            for (int i = 0; i < 3; i++) {
                FireThrowable ft = new FireThrowable(actor.world, actor, cultivation.getCurrentLevel().getStrengthModifierBySubLevel(cultivation.getCurrentSubLevel()) / 3f);
                ft.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0.3f, 1f, 0.4f);
                actor.world.spawnEntity(ft);
            }
            return true;
        }
    });

    public static final Skill FIRE_BAll = new Skill("fire_ball", 120f, 3f, 160f, 0f).setAction(new ISkillAction() {
        @Override
        public boolean activate(EntityPlayer actor) {
            ICultivation cultivation = actor.getCapability(CultivationProvider.CULTIVATION_CAP, null);
            for (int i = 0; i < 5; i++) {
                FireThrowable ft = new FireThrowable(actor.world, actor, cultivation.getCurrentLevel().getStrengthModifierBySubLevel(cultivation.getCurrentSubLevel()), 300, 20, 0.3f);
                ft.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0.3f, 0.3f * cultivation.getCurrentLevel().getSpeedModifierBySubLevel(cultivation.getCurrentSubLevel()), 0.4f);
                actor.world.spawnEntity(ft);
            }
            return true;
        }
    });

    public static final Skill METAL_DETECTION = new Skill("metal_detection", 10f, 0.6f, 30f, 0f).setAction(new ISkillAction() {
        @Override
        public boolean activate(EntityPlayer actor) {
            if (actor.world.isRemote) {
                OreUtils.findOres(actor.world, actor.getPosition(), 16);
                int duration = 10 * 60; // 10 seconds * something is off maybe it has to do with partial ticks, later i'll try understand what it is
                RendererHandler.worldRenderQueue.add(duration, () -> {
                    OreUtils.drawFoundOres(actor);
                    return null;
                });
            }
            return true;
        }
    });

    public static final Skill ORE_SUCTION = new Skill("ore_suction", 120f, 0.6f, 40f, 0f).setAction(new ISkillAction() {
        @Override
        public boolean activate(EntityPlayer actor) {
            OreUtils.findOres(actor.world, actor.getPosition(), 8);
            return false;
        }
    });

    public static void sendMessageWithinRange(WorldServer worldIn, BlockPos source, double range, IMessage message) {
        for (int i = 0; i < worldIn.playerEntities.size(); ++i) {
            EntityPlayerMP player = (EntityPlayerMP) worldIn.playerEntities.get(i);
            BlockPos destination = player.getPosition();
            double dist = source.getDistance(destination.getX(), destination.getY(), destination.getZ());
            if (dist < range) {
                NetworkWrapper.INSTANCE.sendTo(message, player);
            }
        }
    }

}
