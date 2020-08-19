package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ItemProgressPill extends ItemBase {

	final float amount;
	final int cooldown;
	final Cultivation.System system;

	public ItemProgressPill(String item_name, Cultivation.System system, float amount, int cooldown) {
		super(item_name);
		this.amount = amount;
		this.cooldown = cooldown;
		this.system = system;
		setCreativeTab(WuxiaItems.PILLS);
	}

	@Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving) {
		if (entityLiving instanceof EntityPlayer) {
			EntityPlayer player = (EntityPlayer) entityLiving;
			ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
			if (cultivation.getPillCooldown() <= 0) {
				stack.shrink(player.isCreative() ? 0 : 1);
				if (stack.isEmpty())
					stack = ItemStack.EMPTY;
				cultivation.setPillCooldown(cooldown);
				if(this.amount <= cultivation.getSystemLevel(this.system).getProgressBySubLevel(cultivation.getSystemSubLevel(this.system)) * 0.2f) {
					CultivationUtils.cultivatorAddProgress(player, this.system, this.amount, false, false);
				} else {
					worldIn.createExplosion(entityLiving, entityLiving.posX, entityLiving.posY, entityLiving.posZ, 3f, true);
					entityLiving.attackEntityFrom(DamageSource.causeExplosionDamage(entityLiving), this.amount*0.7f);
				}
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
		ICultivation cultivation = CultivationUtils.getCultivationFromEntity(player);
		if (cultivation.getPillCooldown() != 0) {
			return EnumActionResult.FAIL;
		}
		return super.onItemUseFirst(player, world, pos, side, hitX, hitY, hitZ, hand);
	}

	@Override
	public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}

	@Override
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add(String.format("Cooldown %ds", this.cooldown / 20));
	}
}
