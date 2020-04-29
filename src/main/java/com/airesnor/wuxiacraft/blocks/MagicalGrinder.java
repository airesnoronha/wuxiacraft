package com.airesnor.wuxiacraft.blocks;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.entities.tileentity.GrinderTESR;
import com.airesnor.wuxiacraft.entities.tileentity.GrinderTileEntity;
import com.airesnor.wuxiacraft.handlers.GuiHandler;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class MagicalGrinder extends BlockBase implements ITileEntityProvider {

	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	public MagicalGrinder(String name) {
		super(name, Material.IRON);
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}

	@SuppressWarnings("deprecation")
	@Override
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = getDefaultState();
		switch (meta) {
			case 0:
				state = state.withProperty(FACING, EnumFacing.NORTH);
				break;
			case 1:
				state = state.withProperty(FACING, EnumFacing.EAST);
				break;
			case 2:
				state = state.withProperty(FACING, EnumFacing.SOUTH);
				break;
			case 3:
				state = state.withProperty(FACING, EnumFacing.WEST);
				break;
		}
		return state;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int meta;
		switch (state.getValue(FACING)) {
			case EAST:
				meta = 1;
				break;
			case SOUTH:
				meta = 2;
				break;
			case WEST:
				meta = 3;
				break;
			default:
				meta = 0;
				break;
		}
		return meta;
	}

	@Nullable
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new GrinderTileEntity();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		playerIn.openGui(WuxiaCraft.instance, GuiHandler.GRINDER_GUI_ID, worldIn, pos.getX(), pos.getY(), pos.getZ());
		return true;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		super.registerModels();
		ClientRegistry.bindTileEntitySpecialRenderer(GrinderTileEntity.class, new GrinderTESR());
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isTranslucent(IBlockState state) {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isTopSolid(IBlockState state) {
		return false;
	}
}
