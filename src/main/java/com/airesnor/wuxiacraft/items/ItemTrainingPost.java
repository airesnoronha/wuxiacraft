package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.blocks.BlockTrainingPost;
import com.airesnor.wuxiacraft.blocks.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nonnull;

public class ItemTrainingPost extends ItemBase {

	public ItemTrainingPost(String item_name) {
		super(item_name);
	}

	@Override
	@Nonnull
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		if(facing == EnumFacing.UP) {

			Block targetBlock = Blocks.TRAINING_POSTS.get(this.getUnlocalizedName().substring(5)); //remove item. at the beginning
			if (targetBlock != null) {
				IBlockState iblockstate = worldIn.getBlockState(pos);
				Block block = iblockstate.getBlock();

				if (!block.isReplaceable(worldIn, pos)) {
					pos = pos.offset(facing);
				}

				ItemStack itemstack = player.getHeldItem(hand);

				if (player.canPlayerEdit(pos, facing, itemstack) && targetBlock.canPlaceBlockAt(worldIn, pos)) {
					EnumFacing enumfacing = EnumFacing.fromAngle(player.rotationYaw);
					placePost(worldIn, pos, enumfacing, targetBlock);
					SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, player);
					worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
					itemstack.shrink(1);
					return EnumActionResult.SUCCESS;
				}
			}
		}
		return EnumActionResult.FAIL;
	}

	public static void placePost(World world, BlockPos pos, EnumFacing facing, Block targetBlock){
		IBlockState state = targetBlock.getDefaultState().withProperty(BlockTrainingPost.FACING, facing);
		BlockPos posUp = pos.up();
		world.setBlockState(pos, state.withProperty(BlockTrainingPost.HALF, BlockDoor.EnumDoorHalf.LOWER), 2);
		world.setBlockState(posUp, state.withProperty(BlockTrainingPost.HALF, BlockDoor.EnumDoorHalf.UPPER), 2);
		world.notifyNeighborsOfStateChange(pos, targetBlock, false);
		world.notifyNeighborsOfStateChange(posUp, targetBlock, false);
	}

	@SideOnly(Side.CLIENT)
	@Override
	@Nonnull
	public String getUnlocalizedNameInefficiently(ItemStack stack) {
		String [] parts = stack.getItem().getUnlocalizedName().split("_");
		return parts[0]+"_"+parts[1]+"_"+(parts[3].equals("oak")?parts[4]:parts[3]);
	}
}
