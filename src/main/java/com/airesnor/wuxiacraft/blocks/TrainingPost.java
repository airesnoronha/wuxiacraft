package com.airesnor.wuxiacraft.blocks;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class TrainingPost extends BlockBase {

	private double amount;

	public TrainingPost(String name) {
		super(name, Material.WOOD);
		this.setHardness(1000f);
		this.setResistance(10f);
		this.amount = 1d;
	}

	public TrainingPost setAmount(double amount) {
		this.amount = amount;
		return this;
	}

	@Override
	public void onBlockClicked(World worldIn, BlockPos pos, EntityPlayer playerIn) {
		super.onBlockClicked(worldIn, pos, playerIn);
		ICultivation cultivation = CultivationUtils.getCultivationFromEntity(playerIn);
		CultivationUtils.cultivatorAddProgress(playerIn, cultivation, this.amount);
	}

	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		this.breakBlock(worldIn, pos, state);
		return true;
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return super.getBoundingBox(state, source, pos).expand(0,1,0);
	}
}
