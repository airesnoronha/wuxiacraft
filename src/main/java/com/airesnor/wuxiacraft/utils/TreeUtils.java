package com.airesnor.wuxiacraft.utils;

import com.airesnor.wuxiacraft.WuxiaCraft;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
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
            if(isLog(worldIn, aux)) {
                aux = aux.down();
            } else {
                isBottomMost = true;
            }
        }

        aux = aux.up();

        //Now it'll check block for block after logs
        Stack<BlockPos> candidates = new Stack<>();
        candidates.add(aux);
        candidates.add(aux.north());
        candidates.add(aux.south());
        candidates.add(aux.east());
        candidates.add(aux.west());
        candidates.add(aux.east().north());
        candidates.add(aux.east().south());
        candidates.add(aux.west().north());
        candidates.add(aux.west().south());
        List<BlockPos> oldCandidates = new ArrayList<>();
        while(!candidates.isEmpty()) {
            aux = candidates.pop();
            oldCandidates.add(aux);
            if(isLog(worldIn, aux)) {
                if (!treeBlocks.contains(aux)) {
                    treeBlocks.add(aux);
                }
                aux = aux.up();
                if (!candidates.contains(aux) && !treeBlocks.contains(aux) && !oldCandidates.contains(aux)) {
                    candidates.add(aux);
                }
                if (!candidates.contains(aux.north()) && !treeBlocks.contains(aux.north()) && !oldCandidates.contains(aux.north())) {
                    candidates.add(aux.north());
                }
                if (!candidates.contains(aux.south()) && !treeBlocks.contains(aux.south()) && !oldCandidates.contains(aux.south())) {
                    candidates.add(aux.south());
                }
                if (!candidates.contains(aux.east())  && !treeBlocks.contains(aux.east())&& !oldCandidates.contains(aux.east())) {
                    candidates.add(aux.east());
                }
                if (!candidates.contains(aux.west())  && !treeBlocks.contains(aux.west())&& !oldCandidates.contains(aux.west())) {
                    candidates.add(aux.west());
                }
                if (!candidates.contains(aux.east().north()) && !treeBlocks.contains(aux.east().north()) && !oldCandidates.contains(aux.east().north())) {
                    candidates.add(aux.east().north());
                }
                if (!candidates.contains(aux.east().south()) && !treeBlocks.contains(aux.east().south()) && !oldCandidates.contains(aux.east().south())) {
                    candidates.add(aux.east().south());
                }
                if (!candidates.contains(aux.west().north()) && !treeBlocks.contains(aux.west().north()) && !oldCandidates.contains(aux.west().north())) {
                    candidates.add(aux.west().north());
                }
                if (!candidates.contains(aux.west().south()) && !treeBlocks.contains(aux.west().south()) && !oldCandidates.contains(aux.west().south())) {
                    candidates.add(aux.west().south());
                }
            }
        }
        oldCandidates.clear();
        return treeBlocks;
    };

    public static boolean isLog(World world, BlockPos pos) {
        Block block =  world.getBlockState(pos).getBlock();
        return block instanceof BlockLog;
    }

    public static void breakTree(World world, Stack<BlockPos> tree) {
        WuxiaCraft.logger.info("Tree contains " + tree.size() + " logs.");
        while(!tree.isEmpty()) {
            BlockPos aux = tree.pop();
            world.destroyBlock(aux, true);
        }
    }

}
