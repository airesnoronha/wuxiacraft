package com.lazydragonstudios.wuxiacraft.blocks.entity;

import com.lazydragonstudios.wuxiacraft.blocks.StatRuneBlock;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.formation.FormationStat;
import com.lazydragonstudios.wuxiacraft.formation.FormationSystemStat;
import com.lazydragonstudios.wuxiacraft.init.WuxiaBlockEntities;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.HashSet;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class FormationCore extends BlockEntity {

	private final HashMap<FormationStat, BigDecimal> formationStats;

	private final HashMap<System, HashMap<FormationSystemStat, BigDecimal>> formationSystemStats;

	private int runeRange;

	private final HashSet<BlockPos> runePositions;

	private boolean active;

	private Player owner;

	public FormationCore(BlockPos pos, BlockState blockState) {
		super(WuxiaBlockEntities.FORMATION_CORE.get(), pos, blockState);
		this.runeRange = 1;
		this.formationStats = new HashMap<>();
		this.formationSystemStats = new HashMap<>();
		this.runePositions = new HashSet<>();
		this.owner = null;
	}

	public int getRuneRange() {
		return runeRange;
	}

	public FormationCore setRuneRange(int runeRange) {
		this.runeRange = runeRange;
		return this;
	}

	@Override
	protected void saveAdditional(CompoundTag tag) {
		super.saveAdditional(tag);
		tag.putInt("rune-range", this.runeRange);
		tag.putBoolean("active", this.active);
		if (this.owner != null) {
			tag.putUUID("owner", this.owner.getUUID());
		}
	}

	@Override
	public void load(CompoundTag tag) {
		super.load(tag);
		if (tag.contains("rune-range")) {
			this.runeRange = tag.getInt("rune-range");
		}
		if (tag.contains("active")) {
			this.active = tag.getBoolean("active");
		}
		if (tag.contains("owner") && this.level != null && this.active) {
			var owner = this.level.getPlayerByUUID(tag.getUUID("owner"));
			this.active = false;
			this.owner = null;
			if (owner != null) this.activate(owner);
		}
	}

	@Override
	public CompoundTag getUpdateTag() {
		var tag = super.getUpdateTag();
		this.saveAdditional(tag);
		return tag;
	}

	@Nullable
	@Override
	public Packet<ClientGamePacketListener> getUpdatePacket() {
		return ClientboundBlockEntityDataPacket.create(this);
	}

	@Override
	public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
		super.onDataPacket(net, pkt);
	}

	public boolean containsRune(BlockPos pos) {
		return this.runePositions.contains(pos);
	}

	public boolean activate(Player player) {
		if (this.active) return false;
		if (this.owner != null) return false;
		if (this.level == null) return false;
		var centerPos = this.getBlockPos();
		for (int i = -this.runeRange; i <= this.runeRange; i++) {
			for (int j = -this.runeRange; j <= this.runeRange; j++) {
				for (int k = -this.runeRange; k <= this.runeRange; k++) {
					if (i == 0 && j == 0 && k == 0) continue;
					var currentPos = new BlockPos(centerPos.getX() - i, centerPos.getY() - j, centerPos.getZ() - k);
					var state = this.level.getBlockState(currentPos);
					var block = state.getBlock();
					if (!(block instanceof StatRuneBlock rune)) continue;
					this.runePositions.add(currentPos);
					for (var stat : rune.formationStats.keySet()) {
						if (stat.isModifiable) continue;
						var initialValue = this.formationStats.getOrDefault(stat, BigDecimal.ZERO);
						var runeValue = rune.formationStats.get(stat);
						this.formationStats.put(stat, stat.join(initialValue, runeValue));
					}
					for (var system : rune.formationSystemStats.keySet()) {
						for (var stat : rune.formationSystemStats.get(system).keySet()) {
							this.formationSystemStats.putIfAbsent(system, new HashMap<>());
							if (stat.isModifiable) continue;
							var initialValue = this.formationSystemStats.get(system).getOrDefault(stat, BigDecimal.ZERO);
							var runeValue = rune.formationSystemStats.get(system).get(stat);
							this.formationSystemStats.get(system).put(stat, stat.join(initialValue, runeValue));
						}
					}
				}
			}
		}
		var energyCost = this.getStat(FormationStat.ENERGY_COST);
		var energyGeneration = this.getStat(FormationStat.ENERGY_GENERATION);
		if (energyCost.compareTo(energyGeneration) <= 0) {
			this.owner = player;
			var cultivation = Cultivation.get(player);
			cultivation.setFormation(this.getBlockPos());
			this.active = true;
		} else {
			deactivate();
		}
		this.setChanged();
		if (!this.level.isClientSide) {
			this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
		}
		return this.active;
	}

	public void deactivate() {
		this.active = false;
		this.runePositions.clear();
		this.formationStats.clear();
		this.formationSystemStats.clear();
		if (this.owner != null) {
			var cultivation = Cultivation.get(this.owner);
			cultivation.setFormation(null);
			this.owner = null;
		}
		this.setChanged();
		if (this.level != null && !this.level.isClientSide) {
			this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 2);
		}
	}

	public BigDecimal getStat(FormationStat stat) {
		return this.formationStats.getOrDefault(stat, BigDecimal.ZERO);
	}

	public BigDecimal getStat(System system, FormationSystemStat stat) {
		return this.formationSystemStats.getOrDefault(system, new HashMap<>()).getOrDefault(stat, BigDecimal.ZERO);
	}

	public void setStat(FormationStat stat, BigDecimal value) {
		if (!stat.isModifiable) return;
		this.formationStats.put(stat, value.max(BigDecimal.ZERO));
	}

	public void setStat(System system, FormationSystemStat stat, BigDecimal value) {
		if (!stat.isModifiable) return;
		this.formationSystemStats.putIfAbsent(system, new HashMap<>());
		this.formationSystemStats.get(system).put(stat, value.max(BigDecimal.ZERO));
	}

	@Nullable
	public Player getOwner() {
		return owner;
	}

	public boolean isActive() {
		return active;
	}
}
