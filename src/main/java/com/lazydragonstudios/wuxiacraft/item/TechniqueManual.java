package com.lazydragonstudios.wuxiacraft.item;

import com.lazydragonstudios.wuxiacraft.client.gui.ManualScreen;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.ICultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.TechniqueGrid;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects.TechniqueAspect;
import com.lazydragonstudios.wuxiacraft.init.WuxiaRegistries;
import com.lazydragonstudios.wuxiacraft.init.WuxiaTechniqueAspects;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.math.BigDecimal;
import java.util.LinkedList;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class TechniqueManual extends Item {

	private final System system;

	public TechniqueManual(Properties properties, System system) {
		super(properties);
		this.system = system;
	}

	@Override
	public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
		ItemStack itemstack = player.getItemInHand(hand);
		if (!(itemstack.getItem() instanceof TechniqueManual)) return InteractionResultHolder.pass(itemstack);
		var itemTag = itemstack.getTag();
		if (itemTag == null) return InteractionResultHolder.pass(itemstack);
		if (!(itemstack.getTag().contains("technique-grid"))) return InteractionResultHolder.pass(itemstack);
		var techGrid = new TechniqueGrid();
		techGrid.deserialize(itemTag.getCompound("technique-grid"));
		ICultivation cultivation = Cultivation.get(player);
		var aspectData = cultivation.getAspects();
		var toBecomeUnknown = new LinkedList<Point>();
		for (var hexC : techGrid.getGrid().keySet()) {
			var aspectLocation = techGrid.getAspectAtGrid(hexC);
			var techAspect = WuxiaRegistries.TECHNIQUE_ASPECT.get().getValue(aspectLocation);
			if (techAspect == null) continue;
			if (!aspectData.knowsAspect(aspectLocation)) {
				if (aspectData.learnAspect(aspectLocation, cultivation)) {
					if (!level.isClientSide()) {
						if (player instanceof ServerPlayer serverPlayer) {
							serverPlayer.sendMessage(new TranslatableComponent("wuxiacraft.learn_successful")
											.append(new TranslatableComponent("wuxiacraft.aspect." + aspectLocation.getPath() + ".name")),
									Util.NIL_UUID);
						}
					}
				} else {
					toBecomeUnknown.add(hexC);
				}
			}
		}
		for (var hexC : toBecomeUnknown) {
			techGrid.removeGridNode(hexC);
			techGrid.addGridNode(hexC, WuxiaTechniqueAspects.UNKNOWN.getId(), BigDecimal.TEN);
		}
		if (level.isClientSide()) {
			int radius = 5;
			if (itemTag.contains("radius")) {
				radius = itemTag.getInt("radius");
			}
			openManualScreen(techGrid, radius);
		}
		return super.use(level, player, hand);
	}

	@Override
	public Component getName(ItemStack itemStack) {
		if (itemStack.getTag() == null) return super.getName(itemStack);
		if (itemStack.getTag().contains("name")) {
			return new TranslatableComponent(itemStack.getTag().getString("name"));
		}
		return super.getName(itemStack);
	}

	@OnlyIn(Dist.CLIENT)
	public void openManualScreen(TechniqueGrid grid, int radius) {
		Minecraft.getInstance().setScreen(new ManualScreen(grid, radius));
	}


}
