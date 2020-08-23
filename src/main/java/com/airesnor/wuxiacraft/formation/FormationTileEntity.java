package com.airesnor.wuxiacraft.formation;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.entities.tileentity.SpiritStoneStackTileEntity;
import com.airesnor.wuxiacraft.items.ItemSpiritStone;
import com.airesnor.wuxiacraft.utils.FormationUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;

@ParametersAreNonnullByDefault
public class FormationTileEntity extends TileEntity implements ITickable {

	private Formation formation = null;

	private double energy = 0;

	private FormationState state = FormationState.STOPPED;

	private FormationEventListener eventListener;

	private int timeActivated;

	private NBTTagCompound formationInfo;

	public FormationTileEntity() {
		formationInfo = new NBTTagCompound();
	}

	public NBTTagCompound getFormationInfo() {
		return this.formationInfo;
	}

	public void setFormationInfo(NBTTagCompound formationInfo) {
		this.formationInfo = formationInfo;
	}

	public void stopFormation() {
		this.state = FormationState.STOPPED;
		this.formation = null;
		this.world.removeEventListener(eventListener);
		this.eventListener = null;
		this.formationInfo = new NBTTagCompound();
		this.timeActivated = 0;
		updateOnClient();
	}

	public double activateFormation() {
		double activationCost = 0;
		if (this.state == FormationState.STOPPED || this.state == FormationState.ACTIVE) {
			Pair<FormationUtils.FormationDiagram, EnumFacing> pair = FormationUtils.searchWorldForFormations(this.world, this.pos);
			FormationUtils.FormationDiagram diagram = pair.getLeft();
			if (diagram != null) {
				Formation formation = FormationUtils.getFormationFromResource(diagram.getFormationName());
				if (formation != null) {
					this.eventListener = new FormationEventListener(this, diagram, pair.getRight());
					this.world.addEventListener(this.eventListener);
					this.formation = formation;
					activationCost = formation.getActivationCost();
					this.state = FormationState.ACTIVE;
					this.timeActivated = 0;
				}
			} else {
				this.stopFormation();
			}
		}
		return activationCost;
	}

	public FormationState getState() {
		return this.state;
	}

	public boolean hasEnergy(double amount) {
		return this.energy >= amount;
	}

	public int getTimeActivated() {
		return this.timeActivated;
	}

	@Override
	public void update() {
		if (!this.world.isRemote) {
			if (this.state == FormationState.ACTIVE) {
				if (this.formation == null) {
					this.stopFormation();
					return;
				}
				if (this.eventListener == null) {
					this.activateFormation();
				}
				searchForSpiritStones();
				int activated = this.formation.doUpdate(this.world, this.pos, this);
				if (activated >= 0) {
					if(activated > 0) {
						if (this.remEnergy(this.formation.getOperationCost() * activated)) {
							this.interruptFormation(this.pos, new ArrayList<>());
						}
					}
				} else {
					this.stopFormation();
				}
				this.timeActivated++;
			} else if (this.state == FormationState.INTERRUPTED) {
				this.stopFormation();
			}
		} else {
			if (this.state == FormationState.ACTIVE) {
				this.timeActivated++;
				if (this.formation != null) {
					this.formation.doClientUpdate(this.world, this.getPos(), this);
				}
			}
		}
	}

	public Formation getFormation() {
		return this.formation;
	}

	public void interruptFormation(BlockPos interruptionSource, List<EntityLivingBase> interrupters) {
		if (this.state == FormationState.ACTIVE) {
			this.state = FormationState.INTERRUPTED;
			if (this.formation != null) {
				this.formation.onInterrupt(this.world, interruptionSource, interrupters);
			}
		}
	}

	public void updateOnClient() {
		if (!this.world.isRemote) {
			IBlockState state = this.world.getBlockState(this.getPos());
			world.notifyBlockUpdate(this.getPos(), state, state, 3);
		}
	}

	private void searchForSpiritStones() {
		if (this.energy < this.getMaxEnergy() * 0.1) {
			List<TileEntity> tileEntities = this.world.loadedTileEntityList;
			for (TileEntity tileEntity : tileEntities) {
				if (tileEntity instanceof SpiritStoneStackTileEntity && tileEntity.getPos().getDistance(this.pos.getX(), this.pos.getY(), this.getPos().getZ()) < 24) {
					ItemStack stack = ((SpiritStoneStackTileEntity) tileEntity).stack;
					if (stack.getItem() instanceof ItemSpiritStone) {
						double energy = ((ItemSpiritStone) stack.getItem()).getAmount() * 120;
						this.energy += energy;
						stack.shrink(1);
						this.updateOnClient();
						if (!stack.isEmpty()) {
							IBlockState state = this.world.getBlockState(tileEntity.getPos());
							this.world.notifyBlockUpdate(tileEntity.getPos(), state, state, 3);
						} else {
							this.world.setBlockToAir(tileEntity.getPos());
						}
						break;
					}
				}
			}
		}
	}

	public double getEnergy() {
		return energy;
	}

	public double getMaxEnergy() {
		double limit = 50d;
		if (this.formation != null) {
			limit = this.formation.getOperationCost() * 100;
		}
		return limit;
	}

	public void addEnergy(double amount) {
		this.energy = Math.min(this.energy + amount, this.getMaxEnergy());
		this.updateOnClient();
	}

	public boolean remEnergy(double amount) {
		boolean empty = false;
		this.energy -= amount;
		if (this.energy < 0) {
			this.energy = 0;
			empty = true;
		}
		this.updateOnClient();
		return empty;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if (compound.hasKey("state")) {
			this.state = FormationState.valueOf(compound.getString("state"));
			if (this.state == FormationState.ACTIVE) {
				if (compound.hasKey("formation")) {
					this.formation = FormationUtils.getFormationFromResource(new ResourceLocation(WuxiaCraft.MOD_ID, compound.getString("formation")));
				}
			}
		}
		if (compound.hasKey("energy")) {
			this.energy = compound.getDouble("energy");
		}
		if (compound.hasKey("timeActivated")) {
			this.timeActivated = compound.getInteger("timeActivated");
		}
		if (compound.hasKey("formationInfo")) {
			this.formationInfo = compound.getCompoundTag("formationInfo");
		}
	}

	@Override
	@Nonnull
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setString("state", this.state.toString());
		compound.setDouble("energy", this.energy);
		if (this.formation != null) {
			compound.setString("formation", this.formation.getUName());
		}
		compound.setInteger("timeActivated", this.timeActivated);
		compound.setTag("formationInfo", this.formationInfo);
		return compound;
	}

	@Nullable
	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound nbtTagCompound = new NBTTagCompound();
		this.writeToNBT(nbtTagCompound);
		return new SPacketUpdateTileEntity(getPos(), 0, nbtTagCompound);
	}

	@Override
	@Nonnull
	public NBTTagCompound getUpdateTag() {
		return writeToNBT(new NBTTagCompound());
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		this.readFromNBT(pkt.getNbtCompound());
	}

	public enum FormationState {
		ACTIVE, STOPPED, INTERRUPTED
	}
}
