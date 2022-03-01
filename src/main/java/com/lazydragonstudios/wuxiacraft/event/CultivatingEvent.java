package com.lazydragonstudios.wuxiacraft.event;

import com.lazydragonstudios.wuxiacraft.cultivation.System;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event;

import java.math.BigDecimal;

public class CultivatingEvent extends Event {

	private final Player player;

	private final System system;

	private BigDecimal amount;

	public CultivatingEvent(Player player, System system, BigDecimal amount) {
		this.player = player;
		this.system = system;
		this.amount = amount;
	}

	public Player getPlayer() {
		return player;
	}

	public System getSystem() {
		return system;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
}
