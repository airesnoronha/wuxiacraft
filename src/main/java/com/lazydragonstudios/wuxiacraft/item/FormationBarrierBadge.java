package com.lazydragonstudios.wuxiacraft.item;

import com.lazydragonstudios.wuxiacraft.blocks.FormationCoreBlock;
import com.lazydragonstudios.wuxiacraft.blocks.entity.FormationCore;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.formation.FormationStat;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;
import java.util.List;

@SuppressWarnings("deprecation")
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FormationBarrierBadge extends Item {
	public FormationBarrierBadge(Properties pProperties) {
		super(pProperties);
	}

	@Override
	public InteractionResult useOn(UseOnContext pContext) {
		var itemStack = pContext.getItemInHand();
		if (pContext.getPlayer() == null) return InteractionResult.PASS;
		var tag = itemStack.getTag();
		if (tag == null) {
			tag = new CompoundTag();
			itemStack.setTag(tag);
		}
		if (tag.contains("formation"))
			return InteractionResult.PASS;
		var cultivation = Cultivation.get(pContext.getPlayer());
		var cultFormationCore = cultivation.getFormation();
		if (cultFormationCore == null) return InteractionResult.PASS;
		var blockPos = pContext.getClickedPos();
		if (blockPos.compareTo(cultFormationCore) != 0) return InteractionResult.PASS;
		var blockEntity = pContext.getLevel().getBlockEntity(blockPos);
		if (!(blockEntity instanceof FormationCore core)) return InteractionResult.PASS;
		if (core.getStat(FormationStat.BARRIER_MAX_AMOUNT).compareTo(BigDecimal.ZERO) <= 0) return InteractionResult.PASS;
		var formationTag = new CompoundTag();
		formationTag.putInt("x", blockPos.getX());
		formationTag.putInt("y", blockPos.getY());
		formationTag.putInt("z", blockPos.getZ());
		formationTag.putString("ownerName", pContext.getPlayer().getDisplayName().getString());
		tag.put("formation", formationTag);
		return InteractionResult.CONSUME;
	}

	@Override
	public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
		super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
		var tag = pStack.getTag();
		if (tag == null) return;
		if (!tag.contains("formation")) return;
		var formationTag = tag.getCompound("formation");
		var ownerName = formationTag.getString("ownerName");
		pTooltipComponents.add(new TextComponent(ownerName));
	}
}
