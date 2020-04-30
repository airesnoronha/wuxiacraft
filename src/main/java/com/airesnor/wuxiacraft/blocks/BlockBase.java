package com.airesnor.wuxiacraft.blocks;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.items.IHasModel;
import com.airesnor.wuxiacraft.items.WuxiaItems;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.IPlantable;

public class BlockBase extends Block implements IHasModel {

	private static boolean canSupportPlant;

	public BlockBase(String name, Material materialIn, boolean canSupportPlant) {
		super(materialIn);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(WuxiaBlocks.BLOCKS_TAB);

		WuxiaBlocks.BLOCKS.add(this);
		WuxiaItems.ITEMS.add(new ItemBlock(this).setRegistryName(name));
		this.canSupportPlant = canSupportPlant;
	}

	@Override
	public void registerModels() {
		WuxiaCraft.proxy.registerItemRenderer(Item.getItemFromBlock(this), 0, "inventory");
	}

	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction, IPlantable plantable) {
		return canSupportPlant;
	}
}
