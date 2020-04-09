package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemDagger extends ItemBase {

	public ItemDagger(String item_name) {
		super(item_name);
		setMaxDamage(ToolMaterial.GOLD.getMaxUses());
		setMaxStackSize(1);
	}

	@Override
	public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
		stack.damageItem(1, attacker);
		if(attacker instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) attacker;
			boolean found = false;
			for(ItemStack stack1 :player.inventory.mainInventory) {
				if(stack1.getItem() == Items.EMPTY_BOTTLE) {
					stack1.shrink(1);
					found = true;
					break;
				}
			}
			if(found && !attacker.world.isRemote) {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(target);
				ItemStack bloodBottle = new ItemStack(Items.BLOOD_BOTTLE, 1);
				NBTTagCompound tag;
				if(bloodBottle.hasTagCompound()) {
					tag = bloodBottle.getTagCompound();
				} else {
					tag = new NBTTagCompound();
				}
				tag.setString("bloodLevel", cultivation.getCurrentLevel().toString());
				bloodBottle.setTagCompound(tag);
				EntityItem item = new EntityItem(attacker.world, attacker.posX, attacker.posY+0.1, attacker.posZ, bloodBottle);
				item.setNoPickupDelay();
				item.setOwner(attacker.getName());
				attacker.world.spawnEntity(item);
			}
		}
		return true;
	}

	@Override
	@Nonnull
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
		boolean found = false;
		for(ItemStack stack1 :player.inventory.mainInventory) {
			if(stack1.getItem() == Items.EMPTY_BOTTLE) {
				stack1.shrink(1);
				found = true;
				break;
			}
		}
		if(found && !worldIn.isRemote) {
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
			ItemStack bloodBottle = new ItemStack(Items.BLOOD_BOTTLE, 1);
			NBTTagCompound tag;
			if(bloodBottle.hasTagCompound()) {
				tag = bloodBottle.getTagCompound();
			} else {
				tag = new NBTTagCompound();
			}
			tag.setString("bloodLevel", cultivation.getCurrentLevel().toString());
			bloodBottle.setTagCompound(tag);
			EntityItem item = new EntityItem(worldIn, player.posX, player.posY+0.1, player.posZ, bloodBottle);
			item.setNoPickupDelay();
			item.setOwner(player.getName());
			worldIn.spawnEntity(item);
		}
		return found ? EnumActionResult.SUCCESS : EnumActionResult.PASS;
	}
}
