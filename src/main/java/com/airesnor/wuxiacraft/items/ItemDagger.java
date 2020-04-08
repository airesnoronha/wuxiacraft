package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

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


}
