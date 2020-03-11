package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.capabilities.CultivationProvider;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.handlers.EventHandler;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ItemProgressPill extends ItemBase {

	float amount;
	int cooldown;

	public ItemProgressPill(String item_name, float amount, int cooldown) {
		super(item_name);
		this.amount = amount;
		this.cooldown = cooldown;
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
			assert cultivation != null;
			if (cultivation.getPelletCooldown() == 0) {
				stack.shrink(player.isCreative() ? 0 : 1);
				if (stack.isEmpty())
					stack = ItemStack.EMPTY;
				cultivation.setPelletCooldown(cooldown);
				EventHandler.playerAddProgress(player, cultivation, this.amount);
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
		return 30;
	}

	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ, EnumHand hand) {
		ICultivation cultivation = player.getCapability(CultivationProvider.CULTIVATION_CAP, null);
		if (cultivation.getPelletCooldown() != 0) {
			return EnumActionResult.FAIL;
		}
		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(String.format("Cooldown %ds", this.cooldown / 20));
	}
}
