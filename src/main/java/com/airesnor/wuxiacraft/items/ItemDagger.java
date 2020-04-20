package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
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
				NBTTagCompound tag = null;
				if(bloodBottle.hasTagCompound()) {
					tag = bloodBottle.getTagCompound();
				}
				if(tag == null){
					tag = new NBTTagCompound();
				}
				tag.setString("bloodLevel", cultivation.getCurrentLevel().levelName);
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
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		ItemStack dagger = playerIn.getHeldItem(handIn);
		boolean found = false;
		for(ItemStack stack1 :playerIn.inventory.mainInventory) {
			if(stack1.getItem() == Items.EMPTY_BOTTLE) {
				stack1.shrink(1);
				found = true;
				break;
			}
		}
		if(found && !worldIn.isRemote) {
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(playerIn);
			ItemStack bloodBottle = new ItemStack(Items.BLOOD_BOTTLE, 1);
			NBTTagCompound tag = null;
			if(bloodBottle.hasTagCompound()) {
				tag = bloodBottle.getTagCompound();
			}
			if(tag == null){
				tag = new NBTTagCompound();
			}
			tag.setString("bloodLevel", cultivation.getCurrentLevel().levelName);
			bloodBottle.setTagCompound(tag);
			EntityItem item = new EntityItem(worldIn, playerIn.posX, playerIn.posY+0.1, playerIn.posZ, bloodBottle);
			item.setNoPickupDelay();
			item.setOwner(playerIn.getName());
			worldIn.spawnEntity(item);
		}
		if(found) dagger.damageItem(1, playerIn);
		return new ActionResult<>(found ? EnumActionResult.SUCCESS : EnumActionResult.PASS, dagger) ;
	}
}
