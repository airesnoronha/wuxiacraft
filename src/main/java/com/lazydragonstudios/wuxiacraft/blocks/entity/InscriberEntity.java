package com.lazydragonstudios.wuxiacraft.blocks.entity;

import com.lazydragonstudios.wuxiacraft.container.InscriberMenu;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import com.lazydragonstudios.wuxiacraft.init.WuxiaBlockEntities;
import com.lazydragonstudios.wuxiacraft.init.WuxiaItems;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class InscriberEntity extends BaseContainerBlockEntity {

	protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

	public InscriberEntity(BlockPos pos, BlockState state) {
		super(WuxiaBlockEntities.INSCRIBER_TYPE.get(), pos, state);
	}

	@Override
	protected Component getDefaultName() {
		return new TranslatableComponent("wuxiacraft.inscriber");
	}

	@Override
	protected AbstractContainerMenu createMenu(int id, Inventory inventory) {
		return new InscriberMenu(id, inventory, this);
	}

	@Override
	public int getContainerSize() {
		return 3;
	}

	public boolean isEmpty() {
		for (ItemStack itemstack : this.items) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}
		return true;
	}

	@Override
	public ItemStack getItem(int slot) {
		return this.items.get(slot);
	}

	@Override
	public ItemStack removeItem(int id, int count) {
		return ContainerHelper.removeItem(this.items, id, count);
	}

	@Override
	public ItemStack removeItemNoUpdate(int slot) {
		return ContainerHelper.takeItem(this.items, slot);
	}

	@Override
	public void setItem(int slot, ItemStack stack) {
		this.items.set(slot, stack);
		if (stack.getCount() > this.getMaxStackSize()) {
			stack.setCount(this.getMaxStackSize());
		}
	}

	@Override
	public int getMaxStackSize() {
		return 64;
	}

	@Override
	public boolean stillValid(Player player) {
		if(this.level == null) return false;
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, this.items);
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		ContainerHelper.saveAllItems(tag, this.items);
	}

	public void createTechnique(String techniqueName, Player player, System system) {
		var bookSlot = this.items.get(0);
		var inkSlot = this.items.get(1);
		var outputSlot = this.items.get(2);
		if (!outputSlot.isEmpty()) return;
		if (bookSlot.isEmpty() || inkSlot.isEmpty()) return;
		bookSlot.shrink(1);
		inkSlot.shrink(1);
		new ItemStack(WuxiaItems.ESSENCE_MANUAL.get());
		var techniqueItem = switch (system) {
			case BODY -> new ItemStack(WuxiaItems.BODY_MANUAL.get());
			case DIVINE -> new ItemStack(WuxiaItems.DIVINE_MANUAL.get());
			case ESSENCE -> new ItemStack(WuxiaItems.ESSENCE_MANUAL.get());
		};
		if (techniqueItem.getTag() == null) {
			techniqueItem.setTag(new CompoundTag());
		}
		var itemTag = techniqueItem.getTag();
		itemTag.putString("name", techniqueName);
		var cultivation = Cultivation.get(player);
		var systemData = cultivation.getSystemData(system);
		var radius = BigDecimal.ONE.add(systemData.getStat(PlayerSystemStat.ADDITIONAL_GRID_RADIUS));
		var grid = systemData.techniqueData.grid;
		itemTag.putInt("radius", radius.intValue());
		itemTag.put("technique-grid", grid.serialize());
		this.items.set(2, techniqueItem);
	}

	@Override
	public void clearContent() {
		this.items.clear();
	}
}
