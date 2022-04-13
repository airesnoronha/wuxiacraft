package com.lazydragonstudios.wuxiacraft.container;

import com.lazydragonstudios.wuxiacraft.blocks.entity.InscriberEntity;
import com.lazydragonstudios.wuxiacraft.container.slots.InscriberBookSlot;
import com.lazydragonstudios.wuxiacraft.container.slots.InscriberPaintSlot;
import com.lazydragonstudios.wuxiacraft.container.slots.OutputSlot;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.init.WuxiaMenuTypes;
import com.lazydragonstudios.wuxiacraft.util.MathUtil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class InscriberMenu extends AbstractContainerMenu {

	@SuppressWarnings("unused")
	public static InscriberMenu create(int id, Inventory inventory, FriendlyByteBuf buf) {
		return new InscriberMenu(id, inventory);
	}

	private final Container container;

	private String itemName;

	private System system = System.ESSENCE;

	public InscriberMenu(int id, Inventory playerInventory) {
		this(id, playerInventory, new SimpleContainer(3));
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public InscriberMenu(int id, Inventory playerInventory, Container container) {
		super(WuxiaMenuTypes.INTROSPECTION_MENU.get(), id);

		this.container = container;

		this.addSlot(new InscriberBookSlot(this.container, 0, 128, 31)); //this accepts book
		this.addSlot(new InscriberPaintSlot(this.container, 1, 128, 12)); //this accepts ink
		this.addSlot(new OutputSlot(this.container, 2, 177, 20)); //this is the output

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
	public ItemStack quickMoveStack(Player player, int slot) {
		var resultStack = ItemStack.EMPTY;
		var selectedSlot = this.slots.get(slot);
		if (!selectedSlot.hasItem()) return resultStack;
		var slotStack = selectedSlot.getItem();
		resultStack = slotStack.copy();
		if (slot == 2) {
			if (!this.moveItemStackTo(slotStack, 3, 39, true)) return ItemStack.EMPTY;
			selectedSlot.onQuickCraft(slotStack, resultStack);
		} else if (slot != 0 && slot != 1) {
			if (slotStack.getItem() == Items.BOOK) {
				if (!this.moveItemStackTo(slotStack, 0, 1, false)) return ItemStack.EMPTY;
			} else if (slotStack.getItem() == Items.INK_SAC) {
				if (!this.moveItemStackTo(slotStack, 1, 2, false)) return ItemStack.EMPTY;
			} else if (slot >= 30 && slot < 39 && !this.moveItemStackTo(slotStack, 3, 30, false)) {
				return ItemStack.EMPTY;
			}
		} else if (!this.moveItemStackTo(slotStack, 3, 39, true)) return ItemStack.EMPTY;


		if (slotStack.isEmpty()) {
			selectedSlot.set(ItemStack.EMPTY);
		} else {
			selectedSlot.setChanged();
		}

		if (slotStack.getCount() == resultStack.getCount()) {
			return ItemStack.EMPTY;
		}

		selectedSlot.onTake(player, slotStack);

		return resultStack;
	}

	@Override
	public boolean stillValid(Player player) {
		return this.container.stillValid(player);
	}
}
