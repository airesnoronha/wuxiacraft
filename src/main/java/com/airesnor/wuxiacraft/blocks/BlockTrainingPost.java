package com.airesnor.wuxiacraft.blocks;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.items.WuxiaItems;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockTrainingPost extends Block {

	public static final PropertyEnum<BlockDoor.EnumDoorHalf> HALF = PropertyEnum.create("half", BlockDoor.EnumDoorHalf.class);
	public static final PropertyDirection FACING = BlockHorizontal.FACING;

	private final double amount;

	public BlockTrainingPost(String name, Material blockMaterialIn, double amount) {
		super(blockMaterialIn);
		setResistance(10f);
		setHardness(100f);
		setUnlocalizedName(name);
		setRegistryName(new ResourceLocation(WuxiaCraft.MOD_ID, name));
		this.amount = amount;
		setHarvestLevel("axe", 1);
		setCreativeTab(WuxiaItems.WUXIACRAFT_GENERAL);
		WuxiaBlocks.BLOCKS.add(this);
	}

	/**
	 * turns into entity item
	 */
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
			return this.onBlockActivated(worldIn, pos.down(), worldIn.getBlockState(pos.down()), playerIn, hand, facing, hitX, hitY, hitZ);
		} else {
			worldIn.setBlockToAir(pos);
			worldIn.setBlockToAir(pos.up());
			if(state.getBlock() != this) {
				return false;
			}
			if(!worldIn.isRemote) {
				ItemStack stack = getItem(worldIn, pos, state);
				EntityItem item = new EntityItem(worldIn, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, stack);
				item.setNoPickupDelay();
				worldIn.spawnEntity(item);
			}
		}
		return true;
	}

	/**
	 * Adds progress to player based on its attack speed and this amount
	 */
	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		super.onBlockClicked(worldIn, pos, playerIn);
		double amount = this.amount * playerIn.getCooledAttackStrength(0.5f); //game always use 0.5 idk y
		CultivationUtils.cultivatorAddProgress(playerIn, Cultivation.System.BODY, amount, true, false);
		playerIn.attackEntityFrom(DamageSource.GENERIC.setDamageBypassesArmor(), (float)amount);
	}

	@Override
	@Nonnull
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, HALF, FACING);
	}

	@SuppressWarnings("deprecation")
	@Override
	@Nonnull
	public IBlockState getStateFromMeta(int meta) {
		IBlockState state = this.getDefaultState();
		if(meta % 4 == 0) {
			state = state.withProperty(FACING, EnumFacing.NORTH);
		}
		else if(meta % 4 == 1) {
			state = state.withProperty(FACING, EnumFacing.EAST);
		}
		else if(meta % 4 == 2) {
			state = state.withProperty(FACING, EnumFacing.SOUTH);
		}
		else if(meta % 4 == 3) {
			state = state.withProperty(FACING, EnumFacing.WEST);
		}
		if(meta >= 4) {
			state = state.withProperty(HALF, BlockDoor.EnumDoorHalf.UPPER);
		} else {
			state = state.withProperty(HALF, BlockDoor.EnumDoorHalf.LOWER);
		}
		return state;
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		int value = 0;
		if(state.getValue(HALF) == BlockDoor.EnumDoorHalf.UPPER) {
			value += 4;
		}
		switch(state.getValue(FACING)) {
			case WEST:
				value++;
			case SOUTH:
				value++;
			case EAST:
				value++;
				break;
		}
		return value;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isTopSolid(IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isFullBlock(IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean isOpaqueCube(IBlockState state) {
		return false;
	}

	@SuppressWarnings("deprecation")
	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(this.getItem());
	}

	public Item getItem() {
		if(WuxiaBlocks.TRAINING_POSTS.containsValue(this)) {
			return WuxiaItems.TRAINING_POSTS.get(this.getUnlocalizedName().substring(5)); //removes tile.
		}
		return WuxiaItems.TRAINING_POSTS.get("training_post_oak_stick");
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos)
	{
		if (pos.getY() >= worldIn.getHeight() - 1)
		{
			return false;
		}
		else
		{
			IBlockState state = worldIn.getBlockState(pos.down());
			return (state.isTopSolid() || state.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID) && super.canPlaceBlockAt(worldIn, pos) && super.canPlaceBlockAt(worldIn, pos.up());
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	@Nonnull
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}
}
