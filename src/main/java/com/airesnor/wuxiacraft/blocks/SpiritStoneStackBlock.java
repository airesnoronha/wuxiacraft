package com.airesnor.wuxiacraft.blocks;

import com.airesnor.wuxiacraft.entities.tileentity.CauldronTESR;
import com.airesnor.wuxiacraft.entities.tileentity.CauldronTileEntity;
import com.airesnor.wuxiacraft.entities.tileentity.SpiritStoneStackTESR;
import com.airesnor.wuxiacraft.entities.tileentity.SpiritStoneStackTileEntity;
import com.airesnor.wuxiacraft.items.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

public class SpiritStoneStackBlock extends BlockContainer implements IHasModel {

	public SpiritStoneStackBlock(String name) {
		super(Materials.RUNE);
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setLightLevel(5f);
		Blocks.BLOCKS.add(this);
	}

	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@Override
	public boolean isTranslucent(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new SpiritStoneStackTileEntity();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ClientRegistry.bindTileEntitySpecialRenderer(SpiritStoneStackTileEntity.class, new SpiritStoneStackTESR());
	}


}
