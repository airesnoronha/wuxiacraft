package com.lazydragonstudios.wuxiacraft.container;

import com.lazydragonstudios.wuxiacraft.blocks.entity.RunemakingTable;
import com.lazydragonstudios.wuxiacraft.container.slots.RunemakingBlockSlot;
import com.lazydragonstudios.wuxiacraft.container.slots.RunemakingResultSlot;
import com.lazydragonstudios.wuxiacraft.container.slots.RunemakingStencilSlot;
import com.lazydragonstudios.wuxiacraft.formation.FormationMaterialTier;
import com.lazydragonstudios.wuxiacraft.init.WuxiaMenuTypes;
import com.lazydragonstudios.wuxiacraft.item.RuneStencil;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RunemakingMenu extends AbstractContainerMenu {

	@SuppressWarnings("unused")
	public static RunemakingMenu create(int id, Inventory inventory, FriendlyByteBuf buf) {
		return new RunemakingMenu(id, inventory);
	}

	private final Container container;

	public RunemakingMenu(int pContainerId, Inventory playerInventory) {
		this(pContainerId, playerInventory, new SimpleContainer(3));
	}

	public RunemakingMenu(int pContainerId, Inventory playerInventory, Container container) {
		super(WuxiaMenuTypes.RUNEMAKING_MENU.get(), pContainerId);
		this.container = container;

		this.addSlot(new RunemakingStencilSlot(this.container, 0, 177, 7)); //this accepts stencil
		this.addSlot(new RunemakingBlockSlot(this.container, 1, 177, 27)); //this accepts blocks
		this.addSlot(new RunemakingResultSlot(this.container, 2, 177, 71)); //this is the output

		//inventory
		for (int i = 0; i < 3; ++i) {
			for (int j = 0; j < 9; ++j) {
				this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 9 + j * 18, 91 + i * 18));
			}
		}
		//hot bar
		for (int k = 0; k < 9; ++k) {
			this.addSlot(new Slot(playerInventory, k, 9 + k * 18, 149));
		}
	}

	@Override
	public boolean clickMenuButton(Player player, int buttonId) {
		if (!(this.container instanceof RunemakingTable table)) return false;
		table.setSelectedRune(buttonId);
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
			var stencilSlot = this.slots.get(0);
			var blockSlot = this.slots.get(1);
			if (blockSlot.hasItem() && stencilSlot.hasItem()) {
				int count = blockSlot.getItem().getCount();
				count = Math.min(count, stencilSlot.getItem().getMaxDamage() - stencilSlot.getItem().getDamageValue());
				slotStack.setCount(count);
				slotStack = this.container.removeItem(2, count);
			}
			if (!this.moveItemStackTo(slotStack, 3, 39, true)) return ItemStack.EMPTY;
			selectedSlot.onQuickCraft(slotStack, resultStack);
		} else if (slot != 0 && slot != 1) {
			if (slotStack.getItem() instanceof RuneStencil) {
				if (!this.moveItemStackTo(slotStack, 0, 1, false)) return ItemStack.EMPTY;
			} else if (FormationMaterialTier.getTierFromItem(slotStack.getItem()) != null) {
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

	public boolean stillValid(Player player) {
		return this.container.stillValid(player);
	}

	@Override
	public void slotsChanged(Container pInventory) {
		if (!(pInventory instanceof RunemakingTable table)) return;
		var resultStack = table.getResultItemStack();
		table.setItem(2, resultStack);
		super.slotsChanged(pInventory);
	}
}
