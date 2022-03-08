package com.lazydragonstudios.wuxiacraft.item;

import com.lazydragonstudios.wuxiacraft.effects.WuxiaEffect;
import com.lazydragonstudios.wuxiacraft.init.WuxiaMobEffects;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class SpiritStone extends Item {

	private final int strength;

	public SpiritStone(Properties properties, int strength) {
		super(properties);
		this.strength = strength;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		player.startUsingItem(hand);
		return InteractionResultHolder.consume(itemstack);
	}

	@Override
	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.EAT;
	}

	@Override
	public ItemStack finishUsingItem(ItemStack stack, Level level, LivingEntity livingEntity) {
		stack.shrink(1);
		livingEntity.addEffect(new MobEffectInstance(WuxiaMobEffects.SPIRITUAL_RESONANCE.get(), 120 * 20, this.strength, true, true, false));
		return stack;
	}

	@Override
	public int getUseDuration(ItemStack stack) {
		return 10;
	}
}
