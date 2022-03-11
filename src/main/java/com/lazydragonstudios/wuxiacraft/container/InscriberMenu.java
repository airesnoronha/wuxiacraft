package com.lazydragonstudios.wuxiacraft.container;

import com.lazydragonstudios.wuxiacraft.blocks.entity.InscriberEntity;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.util.MathUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class InscriberMenu extends AbstractContainerMenu {

	public static MenuType<InscriberMenu> registryType;

	public static InscriberMenu create(int id, Inventory inventory, FriendlyByteBuf buf) {
		return new InscriberMenu(id, inventory);
	}

	private Container container;

	private String itemName;

	private System system = System.ESSENCE;

	public InscriberMenu(int id, Inventory playerInventory) {
		this(id, playerInventory, new SimpleContainer(3));
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public InscriberMenu(int id, Inventory playerInventory, Container container) {
		super(registryType, id);

		this.container = container;

		this.addSlot(new InscriberBookSlot(this.container, 0, 128, 31)); //this accepts book
		this.addSlot(new InscriberPaintSlot(this.container, 1, 128, 12)); //this accepts ink
		this.addSlot(new InscriberResultSlot(this.container, 2, 177, 20)); //this is the output

		//inventory
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 21 + j * 18, 59 + i * 18));
			}
		}
		//hotbar
		for (int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 21 + k * 18, 113));
		}
	}

	@Override
	public boolean clickMenuButton(Player player, int buttonId) {
		if (MathUtil.between(buttonId, 0, 2)) {
			this.system = System.values()[buttonId];
		}
		if (buttonId == 3) {
			if (!(this.container instanceof InscriberEntity inscriber)) return false;
			inscriber.createTechnique(this.itemName, player, this.system);
		}
		return false;
	}

	@Override
	public boolean stillValid(Player player) {
		return this.container.stillValid(player);
	}
}
