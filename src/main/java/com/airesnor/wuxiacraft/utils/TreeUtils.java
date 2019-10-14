package com.airesnor.wuxiacraft.utils;

import com.airesnor.wuxiacraft.WuxiaCraft;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.*;

public class TreeUtils {

    public static Stack<BlockPos> findTree(World worldIn, BlockPos pos) {
        Stack<BlockPos> treeBlocks = new Stack<>();
        BlockPos aux = pos;

        //Check if it's the bottom most part of the tree
        boolean isBottomMost = false;
        while(!isBottomMost) {
            if(worldIn.getBlockState(aux.down()).getBlock() == Blocks.LOG) {
                aux = aux.down();
            } else {
                isBottomMost = true;
            }
        }

        //Now it'll check block for block after logs
        Stack<BlockPos> candidates = new Stack<>();
        candidates.add(aux);
        while(!candidates.isEmpty()) {
            aux = candidates.pop();
            if(worldIn.getBlockState(aux).getBlock() == Blocks.LOG) {
                if(!treeBlocks.contains(aux))
                    treeBlocks.add(aux);
            }
            if(worldIn.getBlockState(aux).getBlock() == Blocks.LOG || worldIn.getBlockState(aux).getBlock() == Blocks.LEAVES) {
                if(!candidates.contains(aux.north()) && !treeBlocks.contains(aux.north()))
                    candidates.add(aux.north());
                if(!candidates.contains(aux.south()) && !treeBlocks.contains(aux.south()))
                    candidates.add(aux.south());
                if(!candidates.contains(aux.east()) && !treeBlocks.contains(aux.east()))
                    candidates.add(aux.east());
                if(!candidates.contains(aux.west()) && !treeBlocks.contains(aux.west()))
                    candidates.add(aux.west());
                if(!candidates.contains(aux.up()) && !treeBlocks.contains(aux.up()))
                    candidates.add(aux.up());
            }
        }
        return treeBlocks;
    };

    public static void breakTree(World world, Stack<BlockPos> tree) {
        WuxiaCraft.logger.info("Tree contains " + tree.size() + " logs.");
        while(!tree.isEmpty()) {
            BlockPos aux = tree.pop();
            world.destroyBlock(aux, true);
        }
    }

}
