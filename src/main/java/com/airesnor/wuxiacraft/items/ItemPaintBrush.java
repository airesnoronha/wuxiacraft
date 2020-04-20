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
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
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
		bottles.add(Items.PAINT_BOTTLE);
		ItemStack bottle = null;
		for (ItemStack stack : player.inventory.mainInventory) {
			if (bottles.contains(stack.getItem())) {
				bottle = stack;
				break;
			}
		}
		if (bottle != null) {
			if (worldIn.getBlockState(pos).getBlock() == Blocks.PAINT_RUNE && bottle.getItem() == Items.PAINT_BOTTLE) {
				result = EnumActionResult.SUCCESS;
				bottle.damageItem(1, player);
				player.getHeldItem(hand).damageItem(1, player);
				if (!worldIn.isRemote) {
					IBlockState state = Blocks.FORMATION_CORE.getDefaultState();
					worldIn.setBlockState(pos, state);
					worldIn.notifyBlockUpdate(pos, state, state, 3);
				}
			} else {
				Block rune = null;
				if (bottle.getItem() == Items.BLOOD_BOTTLE) {
					NBTTagCompound tag = bottle.getTagCompound();
					if (tag != null) {
						if (tag.hasKey("bloodLevel")) {
							rune = Blocks.BLOOD_RUNES.get(tag.getString("bloodLevel").toLowerCase());
						}
					}
				} else if (bottle.getItem() == Items.PAINT_BOTTLE) {
					rune = Blocks.PAINT_RUNE;
				}
				if (rune == null) rune = Blocks.BLOOD_RUNES.get("body_refinement");
				BlockPos placement = pos.up();
				if (player.canPlayerEdit(placement, facing, player.getHeldItem(hand)) && worldIn.mayPlace(rune, placement, true, EnumFacing.UP, player) && rune.canPlaceBlockAt(worldIn, placement)) {
					if (!worldIn.isRemote) {
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
		}
		return result;
	}
}
