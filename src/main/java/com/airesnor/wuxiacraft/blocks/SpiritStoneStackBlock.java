package com.airesnor.wuxiacraft.blocks;

import com.airesnor.wuxiacraft.entities.tileentity.CauldronTESR;
import com.airesnor.wuxiacraft.entities.tileentity.CauldronTileEntity;
import com.airesnor.wuxiacraft.entities.tileentity.SpiritStoneStackTESR;
import com.airesnor.wuxiacraft.entities.tileentity.SpiritStoneStackTileEntity;
import com.airesnor.wuxiacraft.items.IHasModel;
import net.minecraft.block.Block;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

public class SpiritStoneStackBlock extends BlockContainer implements IHasModel {

	public SpiritStoneStackBlock(String name) {
		super(Materials.RUNE);
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		this.setLightLevel(5f);
		Blocks.BLOCKS.add(this);
		this.setHardness(1000f);
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		boolean all = !playerIn.isSneaking();
		SpiritStoneStackTileEntity te = this.getTileEntity(worldIn, pos);
		ItemStack stack = te.stack.copy();
		int quantity = stack.getCount();
		if (!all) {
			if (stack.getCount() > 0) {
				quantity = 1;
			}
		}
		stack.setCount(quantity);
		te.stack.shrink(quantity);
		if (!worldIn.isRemote) {
			if (te.stack.isEmpty()) {
				worldIn.setBlockToAir(pos);
			}
			double dx = playerIn.posX - (pos.getX() + 0.5);
			double dy = (playerIn.posY + playerIn.getEyeHeight()) - (pos.getY()+0.5);
			double dz = playerIn.posZ - (pos.getZ() + 0.5);
			double dist = Math.sqrt(dx * dx + dy * dy + dz * dz);
			EntityItem item = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY()+0.5, pos.getZ() + 0.5, stack);
			item.setOwner(playerIn.getName());
			item.setNoPickupDelay();
			float speed = 0.4f;
			item.motionX = dx / dist * speed;
			item.motionY = dy / dist * speed;
			item.motionZ = dz / dist * speed;
			worldIn.spawnEntity(item);
		}
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		SpiritStoneStackTileEntity te = this.getTileEntity(worldIn, pos);
		boolean isItem = te.stack.getItem() == playerIn.getHeldItem(hand).getItem();
		boolean all = !playerIn.isSneaking();
		if (isItem) {
			ItemStack playerStack = playerIn.getHeldItem(hand);
			if (te.stack.getCount() < te.stack.getMaxStackSize()) {
				int remaining = te.stack.getMaxStackSize() - te.stack.getCount();
				if(remaining > 0) {
					int having = playerStack.getCount();
					int applying = all ? Math.min(having, remaining) : Math.min(1, remaining);
					int playerWillRemain = Math.max(0, having - applying);
					te.stack.setCount(te.stack.getCount() + applying);
					playerStack.setCount(playerWillRemain);
				}
			}
			return true;
		}
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isBlockNormalCube(IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isTranslucent(IBlockState state) {
		return true;
	}

	@Nullable
	@Override
	@ParametersAreNonnullByDefault
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new SpiritStoneStackTileEntity();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void registerModels() {
		ClientRegistry.bindTileEntitySpecialRenderer(SpiritStoneStackTileEntity.class, new SpiritStoneStackTESR());
	}

	@Override
	public boolean isPassable(IBlockAccess worldIn, BlockPos pos) {
		return true;
	}

	@SuppressWarnings("deprecation")
	@Override
	@ParametersAreNonnullByDefault
	public void addCollisionBoxToList(IBlockState state, World worldIn, BlockPos pos, AxisAlignedBB entityBox, List<AxisAlignedBB> collidingBoxes, @Nullable Entity entityIn, boolean isActualState) {
	}

	private SpiritStoneStackTileEntity getTileEntity(World world, BlockPos pos) {
		return (SpiritStoneStackTileEntity) world.getTileEntity(pos);
	}

	@SuppressWarnings("deprecation")
	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		SpiritStoneStackTileEntity te = getTileEntity(worldIn, pos);
		if(te != null) {
			if(te.stack!=null)
				return te.stack;
		}
		return super.getItem(worldIn, pos, state);
	}
}
