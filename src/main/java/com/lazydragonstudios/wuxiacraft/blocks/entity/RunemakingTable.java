package com.lazydragonstudios.wuxiacraft.blocks.entity;

import com.lazydragonstudios.wuxiacraft.container.RunemakingMenu;
import com.lazydragonstudios.wuxiacraft.crafting.RuneMakingRecipe;
import com.lazydragonstudios.wuxiacraft.init.WuxiaBlockEntities;
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
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class RunemakingTable extends BaseContainerBlockEntity {

	private int selectedRune;

	protected NonNullList<ItemStack> items = NonNullList.withSize(3, ItemStack.EMPTY);

	public RunemakingTable(BlockPos blockPos, BlockState blockState) {
		super(WuxiaBlockEntities.RUNEMAKING_TABLE_TYPE.get(), blockPos, blockState);
		this.selectedRune = 0;
	}

	@Override
	protected Component getDefaultName() {
		return new TranslatableComponent("wuxiacraft.runemaking_table");
	}

	@Override
	protected AbstractContainerMenu createMenu(int pContainerId, Inventory pInventory) {
		return new RunemakingMenu(pContainerId, pInventory, this);
	}

	@Override
	public int getContainerSize() {
		return 3;
	}

	@Override
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
		if (this.level == null) return false;
		if (this.level.getBlockEntity(this.worldPosition) != this) {
			return false;
		} else {
			return player.distanceToSqr((double) this.worldPosition.getX() + 0.5D, (double) this.worldPosition.getY() + 0.5D, (double) this.worldPosition.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Nullable
	public ItemStack getResultItemStack() {
		if (this.level == null) return ItemStack.EMPTY;
		Recipe<?> recipe = this.level.getRecipeManager().getRecipeFor(RuneMakingRecipe.recipeType, this, this.level).orElse(null);
		if (recipe == null) return ItemStack.EMPTY;
		return recipe.getResultItem();
	}

	public int getSelectedRune() {
		return selectedRune;
	}

	public void setSelectedRune(int selectedRune) {
		this.selectedRune = selectedRune;
	}

	@Override
	public void clearContent() {
		this.items.clear();
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		ContainerHelper.saveAllItems(tag, this.items);
		tag.putInt("selectedRune", this.selectedRune);
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		this.items = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
		ContainerHelper.loadAllItems(tag, this.items);
		this.selectedRune = tag.getInt("selectedRune");
	}

}
