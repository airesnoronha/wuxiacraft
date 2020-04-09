package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.blocks.BlockRune;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

public class FormationCoreBlock extends BlockRune implements ITileEntityProvider {

	public FormationCoreBlock(String name) {
		super(name);
	}

	@Override
	public int getMetaFromState(IBlockState state) {
		return 0;
	}

	@Override
	@Nonnull
	public IBlockState getStateFromMeta(int meta) {
		return this.getDefaultState();
	}

	@Override
	@Nonnull
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this);
	}

	@Nullable
	@Override
	@ParametersAreNonnullByDefault
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new FormationTileEntity();
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		FormationTileEntity te = this.getTE(worldIn, pos);
		double activationCost;
		if(te.getState() == FormationTileEntity.FormationState.STOPPED) {
			activationCost = te.activateFormation();
		}
		else {
			activationCost = 0;
			te.stopFormation();
		}
		ICultivation cultivation = CultivationUtils.getCultivationFromEntity(playerIn);
		if(cultivation.hasEnergy((float)activationCost)) {
			cultivation.remEnergy((float)activationCost);
		} else {
			float remaining = cultivation.getEnergy() - (float)activationCost;
			remaining /= 500f;
			cultivation.setEnergy(0);
			playerIn.attackEntityFrom(DamageSource.causePlayerDamage(playerIn).setDamageIsAbsolute(), remaining);
		}
		return true;
	}

	public FormationTileEntity getTE(World world, BlockPos pos) {
		return (FormationTileEntity) world.getTileEntity(pos);
	}
}
