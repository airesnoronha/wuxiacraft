package com.airesnor.wuxiacraft.blocks;

import com.airesnor.wuxiacraft.items.WuxiaItems;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockFaceShape;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BlockRune extends Block {

	private static final AxisAlignedBB RUNE_SIZE = new AxisAlignedBB(0,0,0,1,0.0625,1); // 1/16 height

	public static final IProperty<RuneCharacter> RUNE_CHAR = PropertyEnum.create("rune", RuneCharacter.class);

	public BlockRune(String name) {
		super(Materials.RUNE);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		WuxiaBlocks.BLOCKS.add(this);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return ((RuneCharacter)state.getProperties().get(RUNE_CHAR)).meta;
	}

	@SuppressWarnings("deprecation")
	@Override
	@Nonnull
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState().withProperty(RUNE_CHAR, RuneCharacter.fromMeta(meta));
	}

	@Override
	@Nonnull
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, RUNE_CHAR);
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
	public boolean isTranslucent(IBlockState state) {
		return true;
	}

	@Override
	@Nonnull
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.TRANSLUCENT;
	}

	@SuppressWarnings("deprecation")
	@Override
	@Nonnull
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return RUNE_SIZE;
	}

	@SuppressWarnings("deprecation")
	@Nonnull
	@Override
	public BlockFaceShape getBlockFaceShape(IBlockAccess worldIn, IBlockState state, BlockPos pos, EnumFacing face) {
		return BlockFaceShape.UNDEFINED;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void neighborChanged(IBlockState state, World worldIn, BlockPos pos, Block blockIn, BlockPos fromPos) {
		super.neighborChanged(state, worldIn, pos, blockIn, fromPos);
		IBlockState bottom = worldIn.getBlockState(pos.down());
		boolean destroy = !bottom.getBlock().isSideSolid(bottom, worldIn, pos.down(), EnumFacing.UP);
		if(destroy) {
			if (!worldIn.isRemote)
			{
				worldIn.destroyBlock(pos, false);
			}
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean canPlaceBlockAt(World worldIn, BlockPos pos) {
		if (pos.getY() >= worldIn.getHeight() - 1)
		{
			return false;
		}
		else
		{
			IBlockState state = worldIn.getBlockState(pos.down());
			return (state.isTopSolid() || state.getBlockFaceShape(worldIn, pos.down(), EnumFacing.UP) == BlockFaceShape.SOLID) && super.canPlaceBlockAt(worldIn, pos);
		}
	}

	@SuppressWarnings("deprecation")
	@Override
	@Nonnull
	@ParametersAreNonnullByDefault
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		Item item = WuxiaItems.PAINT_BOTTLE;
		if(WuxiaBlocks.BLOOD_RUNES.containsValue(state.getBlock())) {
			item = WuxiaItems.BLOOD_BOTTLE;
		}
		ItemStack stack =  new ItemStack(item);
		if(WuxiaBlocks.BLOOD_RUNES.containsValue(state.getBlock())){
			stack.setStackDisplayName(state.getBlock().getLocalizedName());
		}
		return stack;
	}

	@SuppressWarnings("unused")
	public enum RuneCharacter implements IStringSerializable {
		A(0),
		B(1),
		C(2),
		D(3),
		E(4),
		F(5),
		G(6),
		H(7),
		I(8),
		J(9),
		K(10),
		L(11);

		public final int meta;

		RuneCharacter(int meta) {
			this.meta = meta;
		}

		@Override
		public String getName() {
			return this.toString().toLowerCase();
		}

		@Nonnull
		public static RuneCharacter fromMeta(int meta) {
			for(RuneCharacter rc : RuneCharacter.values()) {
				if(meta == rc.meta) {
					return rc;
				}
			}
			return A;
		}

	}
}
