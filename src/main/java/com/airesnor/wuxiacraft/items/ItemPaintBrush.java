package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.blocks.Blocks;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class ItemPaintBrush extends ItemBase {

	public ItemPaintBrush(String item_name) {
		super(item_name);
		setMaxDamage(200);
		setMaxStackSize(1);
	}

	@Override
	@Nonnull
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		EnumActionResult result = EnumActionResult.PASS;
		List<Item> bottles = new ArrayList<>();
		bottles.add(Items.BLOOD_BOTTLE);
		ItemStack bottle = null;
		for(ItemStack stack : player.inventory.mainInventory) {
				if(bottles.contains(stack.getItem())) {
					bottle = stack;
					break;
				}
		}
		if(bottle != null) {
			Block rune = null;
			NBTTagCompound tag = bottle.getTagCompound();
			if(tag != null) {
				if(tag.hasKey("bloodLevel")) {
					rune = Blocks.BLOOD_RUNES.get(tag.getString("bloodLevel"));
				}
			}
			if(rune == null) rune = Blocks.BLOOD_RUNES.get("body_refinement");
			BlockPos placement = pos.up();
			if(worldIn.mayPlace(rune, placement, true, EnumFacing.UP, player)) {
				if(!worldIn.isRemote) {
					int runeMeta = worldIn.rand.nextInt(12);
					@SuppressWarnings("deprecation")
					IBlockState state = rune.getStateFromMeta(runeMeta);
					worldIn.setBlockState(placement, state);
					worldIn.notifyBlockUpdate(placement, state, state, 3);
				}
				bottle.damageItem(1, player);
				player.getHeldItem(hand).damageItem(1, player);
				result = EnumActionResult.SUCCESS;
			}
		}
		return result;
	}
}
