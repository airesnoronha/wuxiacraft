package com.lazydragonstudios.wuxiacraft.client.gui.tab;

import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import com.lazydragonstudios.wuxiacraft.client.gui.IntrospectionScreen;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaLabel;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaScrollPanel;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemStat;
import net.minecraft.network.chat.TranslatableComponent;

import java.awt.*;
import java.util.HashMap;
import java.util.LinkedList;

public class CharacterStatsTab extends IntrospectionTab {

	private final HashMap<PlayerStat, WuxiaLabel> displayLabels = new HashMap<>();
	private final HashMap<System, HashMap<PlayerSystemStat, WuxiaLabel>> displaySystemLabels = new HashMap<>();

	private WuxiaScrollPanel statsPanel;
	private HashMap<System, WuxiaScrollPanel> systemStats;

	public CharacterStatsTab(String name) {
		super(name, new Point(0, 36));
	}

	@Override
	public void init(IntrospectionScreen screen) {
		int color = 0xFFAA00;
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		statsPanel = new WuxiaScrollPanel(0, 0, 100, 100, new TextComponent(""));
		systemStats = new HashMap<>();
		screen.addRenderableWidget(statsPanel);
		for (var stat : PlayerStat.values()) {
			var statValue = cultivation.getStat(stat).toEngineeringString();
			int labelX = stat.locationInStatsSheet.x == -1 ? 150 : stat.locationInStatsSheet.x;
			var label = new WuxiaLabel(labelX, stat.locationInStatsSheet.y, new TranslatableComponent("wuxiacraft.gui." + stat.name().toLowerCase(), statValue), color);
			displayLabels.put(stat, label);
			statsPanel.addChild(label);
		}
		for (var system : System.values()) {
			displaySystemLabels.put(system, new HashMap<>());
			WuxiaScrollPanel systemStatPanel = new WuxiaScrollPanel(0, 0, 100, 100, new TextComponent(""));
			systemStats.put(system, systemStatPanel);
			var realmNameLabel = new WuxiaLabel(5, 5, new TranslatableComponent("wuxiacraft.realm." + cultivation.getSystemData(system).getStage().realm.getPath()), color);
			var stageNameLabel = new WuxiaLabel(5, 15, new TranslatableComponent("wuxiacraft.stage." + cultivation.getSystemData(system).currentStage.getPath()), color);
			for (var stat : PlayerSystemStat.values()) {
				var statValue = cultivation.getSystemData(system).getStat(stat).toEngineeringString();
				var label = new WuxiaLabel(stat.locationInStatsSheet.x, stat.locationInStatsSheet.y, new TranslatableComponent("wuxiacraft.gui." + stat.name().toLowerCase(), statValue), color);
				displaySystemLabels.get(system).put(stat, label);
				systemStats.get(system).addChild(label);
			}
			statsPanel.recalculateContentSpace();
			systemStatPanel.addChild(realmNameLabel);
			systemStatPanel.addChild(stageNameLabel);
			screen.addRenderableWidget(systemStats.get(system));
		}
	}

	@Override
	public void renderBG(PoseStack poseStack, int mouseX, int mouseY) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		var leftPostX = 0;
		var secondColumn = new LinkedList<WuxiaLabel>();
		for (var stat : PlayerStat.values()) {
			var statValue = cultivation.getStat(stat).toEngineeringString();
			displayLabels.get(stat).setMessage(new TranslatableComponent("wuxiacraft.gui." + stat.name().toLowerCase(), statValue));
			if (stat.locationInStatsSheet.x == -1) {
				secondColumn.add(displayLabels.get(stat));
			} else {
				leftPostX = Math.max(leftPostX, stat.locationInStatsSheet.x * 2 + displayLabels.get(stat).getWidth());
			}
		}
		for (var label : secondColumn) {
			label.x = leftPostX;
		}
		statsPanel.recalculateContentSpace();
		for (var system : System.values()) {
			for (var stat : PlayerSystemStat.values()) {
				var statValue = cultivation.getSystemData(system).getStat(stat).toEngineeringString();
				displaySystemLabels.get(system).get(stat).setMessage(new TranslatableComponent("wuxiacraft.gui." + stat.name().toLowerCase(), statValue));
			}
		}
		var scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		var scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		int availableXSpace = scaledWidth - 36;
		int availableYSpace = scaledHeight - 36;
		statsPanel.x = 36;
		statsPanel.y = 36;
		statsPanel.setWidth(availableXSpace);
		statsPanel.setHeight(availableYSpace / 2);
		for (var system : System.values()) {
			this.systemStats.get(system).x = 36 + (int) ((double) system.ordinal() * (double) availableXSpace / 3.0d);
			this.systemStats.get(system).y = 36 + availableYSpace / 2;
			this.systemStats.get(system).setWidth(availableXSpace / 3);
			this.systemStats.get(system).setHeight(availableYSpace / 2);
		}
	}

	@Override
	public void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {

	}
}
