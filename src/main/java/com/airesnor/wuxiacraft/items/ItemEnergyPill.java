package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.IFoundation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemEnergyPill extends ItemBase {

	final float amount;

	public ItemEnergyPill(String item_name, float amount) {
		super(item_name);
		this.amount = amount;
		setCreativeTab(WuxiaItems.PILLS);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			stack.shrink(player.isCreative() ? 0 : 1);
			if (stack.isEmpty())
				stack = ItemStack.EMPTY;
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
			IFoundation foundation = CultivationUtils.getFoundationFromEntity(player);
			if(this.amount < (float)cultivation.getMaxEnergy(foundation) * 0.3F) {
				cultivation.addEnergy(this.amount);
			} else {
				worldIn.createExplosion(player, player.posX, player.posY, player.posZ, 3, true);
				player.attackEntityFrom(DamageSource.causeExplosionDamage(player), this.amount*2);
			}
		}
		return stack;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack stack) {
		return EnumAction.EAT;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 6;
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
}
