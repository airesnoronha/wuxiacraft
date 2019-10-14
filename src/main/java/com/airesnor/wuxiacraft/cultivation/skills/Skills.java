package com.airesnor.wuxiacraft.cultivation.skills;

import com.airesnor.wuxiacraft.utils.TreeUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

import java.util.*;

public class Skills {

    public static final List<Skill> SKILLS = new ArrayList<>();

    public static void init() {
        SKILLS.add(GATHER_WOOD);
    }

    public static final Skill GATHER_WOOD = new Skill("gather_wood", 60f, 1.1f)
            .setAction(new ISkillAction() {
                @Override
                public void activate(EntityPlayer actor) {
                    World worldIn = actor.world;
                    RayTraceResult rtr = actor.rayTrace(6f, 1f);
                    BlockPos pos = rtr.getBlockPos();
                    if(worldIn.getBlockState(pos).getBlock() == Blocks.LOG) {
                        TreeUtils.breakTree(worldIn, TreeUtils.findTree(worldIn, pos));
                    }
                }
            });

}
