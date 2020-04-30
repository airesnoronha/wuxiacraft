package com.airesnor.wuxiacraft.entities.tileentity;

import com.airesnor.wuxiacraft.items.ItemSpiritStone;
import com.airesnor.wuxiacraft.utils.GrinderRecipes;
import com.airesnor.wuxiacraft.utils.MathUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class GrinderTileEntity extends TileEntity implements ITickable, IInventory {

	private ItemStack input = ItemStack.EMPTY;
	private ItemStack stones = ItemStack.EMPTY;
	private final NonNullList<ItemStack> output = NonNullList.withSize(4, ItemStack.EMPTY);

	private boolean grinding = false;
	private double energy = 0;

	private int grindingProgress = 0;

	private long ticksAlive = 0;

	private boolean updateEnergyToClient = false;

	public GrinderTileEntity() {
	}

	@Override
	public void update() {
		boolean beforeGrinding = this.isGrinding();
		boolean needUpdate = false;
		this.ticksAlive++;
		if(this.isGrinding()) {
			this.grindingProgress++;
		}
		if (!this.world.isRemote) { //only stuff server side
			if (!this.stones.isEmpty()) {
				if (this.energy < this.getMaxEnergy()) {
					if (this.stones.getItem() instanceof ItemSpiritStone) {
						ItemSpiritStone stone = (ItemSpiritStone) this.stones.getItem();
						this.energy += stone.getAmount() * 90; // formations give 120
						this.stones.shrink(1);
						needUpdate = true;
					}
				}
			}
			if (this.canGrind()) {
				this.grinding = true;
				if (this.grindingProgress >= this.getMaxGrindingProcess()) {
					this.grindItem();
					needUpdate = true;
					this.grindingProgress = 0;
				}
			} else {
				this.grinding = false;
			}
			if(this.ticksAlive%10 == 0 && this.updateEnergyToClient) needUpdate = true;
		}
		needUpdate = needUpdate || this.grinding != beforeGrinding;
		if (needUpdate) {
			IBlockState blockState = this.world.getBlockState(getPos());
			this.world.notifyBlockUpdate(this.getPos(), blockState, blockState, 2);
		}
	}

	protected void grindItem() {
		if (!this.input.isEmpty()) {
			ItemStack[] outputs = GrinderRecipes.getInstance().getOutputFromInput(this.input);
			float[] chances = GrinderRecipes.getInstance().getChancesFromInput(this.input);
			double cost = GrinderRecipes.getInstance().getCostFromInput(this.input);
			for (int i = 0; i < 4; i++) {
				float rnd = this.world.rand.nextFloat();
				if (rnd <= chances[i]) {
					if (this.output.get(i).isEmpty()) {
						this.output.set(i, outputs[i].copy());
					} else {
						if (this.output.get(i).isItemEqual(outputs[i])) {
							this.output.get(i).grow(outputs[i].copy().getCount());
						}
					}
				}
			}
			this.energy = Math.max(0, this.energy - cost);
			this.input.shrink(1);
		}
	}

	public double getMaxEnergy() {
		return 10000.0;// 10k
	}

	protected int getMaxGrindingProcess() {
		return 150;
	}

	private NBTTagCompound prepareClientTagCompound() {
		NBTTagCompound tag = new NBTTagCompound();
		super.writeToNBT(tag);
		tag.setBoolean("grinding", this.grinding);
		tag.setInteger("grindProgress", this.grindingProgress);
		tag.setDouble("energy", this.energy);
		tag.setTag("stonesStack", new NBTTagCompound());
		this.input.writeToNBT(tag.getCompoundTag("stonesStack"));
		return tag;
	}

	public boolean isGrinding() {
		return grinding;
	}

	public boolean hasEnergy(double amount) {
		return this.energy >= amount;
	}

	public boolean canGrind() {
		GrinderRecipes recipes = GrinderRecipes.getInstance();
		ItemStack[] outputs = recipes.getOutputFromInput(this.input);
		boolean canGrind = false;
		if(!this.input.isEmpty()) {
			canGrind = true;
			for (int i = 0; i < 4; i++) {
				if (!outputs[i].isItemEqual(this.output.get(i)) && !this.output.get(i).isEmpty()) {
					canGrind = false;
					break;
				}
				else if (outputs[i].getCount() + this.output.get(i).getCount() > this.output.get(i).getMaxStackSize()) {
					canGrind = false;
					break;
				}
			}
			if (canGrind) {
				canGrind = this.hasEnergy(recipes.getCostFromInput(this.input));
			}
		}
		return canGrind;
	}

	public ItemStack getStones() {
		return this.stones.copy();
	}

	public void addEnergy(double amount) {
		int before = (int)this.energy;
		this.energy = Math.min(this.getMaxEnergy(), this.energy+amount);
		if(before != (int)energy) {
			this.updateEnergyToClient = true;
		}
	}

	//inventory stuff

	@Override
	public int getSizeInventory() {
		return 6;
	}

	@Override
	public boolean isEmpty() {
		boolean outputsEmpty = true;
		for (int i = 0; i < 4; i++) {
			outputsEmpty = outputsEmpty && this.output.get(i).isEmpty();
		}
		return this.input.isEmpty() && this.stones.isEmpty() && outputsEmpty;
	}

	private List<ItemStack> getInventoryList() {
		List<ItemStack> inventory = new ArrayList<>(6);
		inventory.add(0, this.input);
		inventory.add(1, this.stones);
		inventory.add(2, this.output.get(0));
		inventory.add(3, this.output.get(1));
		inventory.add(4, this.output.get(2));
		inventory.add(5, this.output.get(3));
		return inventory;
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		ItemStack stack =  this.getInventoryList().get(index);
		if(stack == null) stack = ItemStack.EMPTY;
		return stack;
	}

	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.getInventoryList(), index, count);
	}

	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.getInventoryList(), index);
	}

	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		if(index == 0)
			this.input = stack;
		else if(index == 1) {
			this.stones = stack;
		}
		else if(MathUtils.between(index, 2, 5, true)) {
			this.output.set(index-2, stack);
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return player.getDistanceSq(this.getPos()) <= 64;
	}

	@Override
	public void openInventory(EntityPlayer player) {

	}

	@Override
	public void closeInventory(EntityPlayer player) {

	}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		if(index == 0) return true;
		if(index == 1) return stack.getItem() instanceof ItemSpiritStone;
		return false;
	}

	@Override
	public int getField(int id) {
		if(id == 0) {
			return this.grindingProgress;
		}
		if(id == 1) {
			return (int)this.energy;
		}
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		if(id == 0) {
			this.grindingProgress = value;
		}
		if(id == 1) {
			this.energy = value;
		}
	}

	@Override
	public int getFieldCount() {
		return 2;
	}

	@Override
	public void clear() {

	}

	@Override
	public String getName() {
		return "Grinder";
	}

	@Override
	public boolean hasCustomName() {
		return false;
	}

	//5 must have methods for syncing to client and storing info

	@Override
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() { //just wanna client know 2 info for rendering
		return new SPacketUpdateTileEntity(getPos(), 0, prepareClientTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		NBTTagCompound tag = pkt.getNbtCompound();
		this.readFromNBT(tag);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("energy"))
			this.energy = compound.getDouble("energy");
		if (compound.hasKey("grinding"))
			this.grinding = compound.getBoolean("grinding");
		if (compound.hasKey("grindProgress"))
			this.grindingProgress = compound.getInteger("grindProgress");
		if (compound.hasKey("stonesStack"))
			this.stones = new ItemStack(compound.getCompoundTag("stonesStack"));
		if (compound.hasKey("inputStack"))
			this.input = new ItemStack(compound.getCompoundTag("inputStack"));
		for (int i = 0; i < 4; i++) {
			if (compound.hasKey("outputStack-" + i))
				this.output.set(i, new ItemStack(compound.getCompoundTag("outputStack-" + i)));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setDouble("energy", this.energy);
		compound.setInteger("grindProgress", this.grindingProgress);
		compound.setTag("stonesStack", new NBTTagCompound());
		this.stones.writeToNBT(compound.getCompoundTag("stonesStack"));
		compound.setTag("inputStack", new NBTTagCompound());
		this.input.writeToNBT(compound.getCompoundTag("inputStack"));
		for (int i = 0; i < 4; i++) {
			compound.setTag("outputStack-" + i, new NBTTagCompound());
			this.output.get(i).writeToNBT(compound.getCompoundTag("outputStack-" + i));
		}
		return compound;
	}

}
