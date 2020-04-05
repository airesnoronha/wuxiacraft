package com.airesnor.wuxiacraft.formation;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.math.BlockPos;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;

public class FormationTileEntity extends TileEntity implements ITickable {

	private Formation formation = null;

	private double energy = 0;

	private FormationState state = FormationState.STOPPED;

	private FormationEventListener eventListener;

	public FormationTileEntity() {
	}

	private void stopFormation() {
		this.state = FormationState.STOPPED;
		this.formation = null;
		this.world.removeEventListener(eventListener);
		this.eventListener = null;
	}

	public double activateFormation() {
		double activationCost = 0;
			if (this.state == FormationState.STOPPED) {
				Pair<FormationUtils.FormationDiagram, EnumFacing> pair = FormationUtils.searchWorldForFormations(this.world, this.pos);
				FormationUtils.FormationDiagram diagram = pair.getLeft();
				if (diagram != null) {
					Formation formation = FormationUtils.getFormationFromResource(diagram.getFormationName());
					if (formation != null) {
						this.eventListener = new FormationEventListener(this, diagram, pair.getRight());
						this.world.addEventListener(this.eventListener);
					}
				}
			} else if (this.state == FormationState.ACTIVE) {
				this.stopFormation();
			}
		return activationCost;
	}

	@Override
	public void update() {
		if (!this.world.isRemote) {
			if (this.state == FormationState.ACTIVE) {
				if (this.formation == null) {
					this.stopFormation();
					return;
				}
				int activated = this.formation.doUpdate(this.world, this.pos);
				if (activated > 0) {
					if(this.remEnergy(this.formation.getOperationCost() * activated)) {
						this.interruptFormation(this.pos, new ArrayList<>());
					}
				} else {
					this.stopFormation();
				}
			} else if (this.state == FormationState.INTERRUPTED) {
				this.stopFormation();
			}
		}
	}

	public void interruptFormation(BlockPos interruptionSource, List<EntityLivingBase> interrupters) {
		if(this.state == FormationState.ACTIVE) {
			this.state = FormationState.INTERRUPTED;
			if (this.formation != null) {
				this.formation.onInterrupt(this.world, interruptionSource, interrupters);
			}
		}
	}

	public void addEnergy(double amount) {
		double limit = 50d;
		if (this.formation != null) {
			limit = this.formation.getOperationCost() * 100;
		}
		this.energy = Math.min(this.energy + amount, limit);
	}

	public boolean remEnergy(double amount) {
		boolean empty = false;
		this.energy -= amount;
		if(this.energy <= 0) {
			this.energy = 0;
			empty = true;
		}
		return empty;
	}

	public enum FormationState {
		ACTIVE, STOPPED, INTERRUPTED
	}
}
