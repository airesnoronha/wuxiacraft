package com.airesnor.wuxiacraft.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

public class BlockRune extends Block {

	public static final IProperty<RuneCharacter> RUNE_CHAR = PropertyEnum.create("rune", RuneCharacter.class);

	public BlockRune(String name) {
		super(Materials.RUNE);
		this.setUnlocalizedName(name);
		this.setRegistryName(name);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
		Blocks.BLOCKS.add(this);
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
