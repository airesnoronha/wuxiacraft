package com.airesnor.wuxiacraft.cultivation.skills;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.capabilities.SkillsProvider;
import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.entities.skills.FireThrowable;
import com.airesnor.wuxiacraft.handlers.EventHandler;
import com.airesnor.wuxiacraft.networking.*;
import com.airesnor.wuxiacraft.utils.TreeUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.IGrowable;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemDye;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.*;

public class Skills {

    public static final List<Skill> SKILLS = new ArrayList<>();

    public static void init() {
        SKILLS.add(GATHER_WOOD);
        SKILLS.add(ACCELERATE_GROWTH);
        SKILLS.add(FLAMES);
        SKILLS.add(FIREBAll);
    }

    public static final Skill GATHER_WOOD = new Skill("gather_wood", 20f, 2f, 2f, 0f)
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
                    if(logs.contains(blockState.getBlock())) {
                        Stack<BlockPos> tree = TreeUtils.findTree(worldIn, pos);
                        if(!tree.isEmpty()) {
                            WuxiaCraft.logger.info("Added block to break");
                            skillCap.addScheduledBlockBreaks(tree.pop());
                            activated = true;
                        }
                    }
                    return activated;
                }
            });

    public static final Skill ACCELERATE_GROWTH = new Skill("accelerate_growth", 40f, 4f, 10f, 0f)
            .setAction(new ISkillAction() {
        @Override
        public boolean activate(EntityPlayer actor) {
            World worldIn = actor.world;
            RayTraceResult rtr = actor.rayTrace(8f, 1f);
            BlockPos pos = rtr.getBlockPos();
            boolean activated = false;
            if(pos != null) {
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
                for(BlockPos p : neighbors) {
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

    public static final Skill FLAMES = new Skill("flames", 40f, 3f, 4f, 0f).setAction(new ISkillAction() {
        @Override
        public boolean activate(EntityPlayer actor) {
            ICultivation cultivation = actor.getCapability(CultivationProvider.CULTIVATION_CAP,null);
            for (int i = 0; i < 3; i++) {
                FireThrowable ft = new FireThrowable(actor.world, actor, cultivation.getCurrentLevel().getStrengthModifierBySubLevel(cultivation.getCurrentSubLevel())/3f);
                ft.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0.3f, 1f, 0.4f);
                actor.world.spawnEntity(ft);
            }
            return true;
        }
    });

    public static final Skill FIREBAll = new Skill("fireball", 120f, 6f, 8f, 0f).setAction(new ISkillAction() {
        @Override
        public boolean activate(EntityPlayer actor) {
            ICultivation cultivation = actor.getCapability(CultivationProvider.CULTIVATION_CAP,null);
            for (int i = 0; i < 5; i++) {
                FireThrowable ft = new FireThrowable(actor.world, actor, cultivation.getCurrentLevel().getStrengthModifierBySubLevel(cultivation.getCurrentSubLevel()), 300, 20, 0.3f);
                ft.shoot(actor, actor.rotationPitch, actor.rotationYaw, 0.3f, 1f, 0.4f);
                actor.world.spawnEntity(ft);
            }
            return true;
        }
    });

}
