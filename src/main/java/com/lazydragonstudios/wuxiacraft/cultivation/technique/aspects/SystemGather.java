package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaLabel;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaLabelBox;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.TranslatableComponent;

import javax.annotation.Nonnull;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedList;

public class SystemGather extends TechniqueAspect {

	public System system;

	public SystemGather(System system) {
		this.system = system;
	}

	@Override
	public void accept(HashMap<String, Object> metaData, BigDecimal proficiency) {
		super.accept(metaData, proficiency);
		String statName = this.system.name().toLowerCase() + "-stat-" + PlayerSystemStat.CULTIVATION_SPEED.name().toLowerCase();
		String systemRawBase = system.name().toLowerCase() + "-raw-cultivation-base";
		if (metaData.containsKey(systemRawBase)) {
			BigDecimal rawBase = BigDecimal.valueOf((double) metaData.remove(systemRawBase));
			rawBase = rawBase.add(this.getCurrentCheckpoint(proficiency).modifier());
			metaData.put(statName,
					//(value or 0) + (rawBase / 10)
					((BigDecimal) metaData.getOrDefault(statName, BigDecimal.ZERO)).add(rawBase.multiply(new BigDecimal("0.1"))));
		}
	}

	@Override
	public int canConnectToCount() {
		return 0;
	}

	@Override
	public int canConnectFromCount() {
		return -1;
	}

	@Override
	public void reject(HashMap<String, Object> metaData) {
		String statName = this.system.name().toLowerCase() + "-stat-" + PlayerSystemStat.CULTIVATION_SPEED.name().toLowerCase();
		metaData.put(statName,
				(double) metaData.getOrDefault(statName, 0d) - 1d);
	}

	@Override
	public void disconnect(HashMap<String, Object> metaData) {
		super.disconnect(metaData);
	}

	@Override
	public boolean canConnect(TechniqueAspect aspect) {
		return false;
	}

	@Override
	public boolean canShowForSystem(System system) {
		return system == this.system;
	}

	@Nonnull
	@Override
	public LinkedList<AbstractWidget> getStatsSheetDescriptor() {
		var nameLocation = this.getRegistryName();
		if (nameLocation == null) return new LinkedList<>();
		WuxiaLabel nameLabel = new WuxiaLabel(5, 2, new TranslatableComponent("wuxiacraft.aspect." + nameLocation.getPath() + ".name"), 0xFFAA00);
		WuxiaLabelBox descriptionLabel = new WuxiaLabelBox(5, 12, 190, new TranslatableComponent("Description: wuxiacraft.aspect." + nameLocation.getPath() + ".description"));
		LinkedList<AbstractWidget> widgets = new LinkedList<>();
		widgets.add(nameLabel);
		widgets.add(descriptionLabel);
		return widgets;
	}
}
