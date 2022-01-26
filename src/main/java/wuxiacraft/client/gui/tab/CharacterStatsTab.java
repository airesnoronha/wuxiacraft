package wuxiacraft.client.gui.tab;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import wuxiacraft.client.gui.IntrospectionScreen;
import wuxiacraft.client.gui.widgets.WuxiaLabel;
import wuxiacraft.client.gui.widgets.WuxiaScrollPanel;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.System;
import wuxiacraft.cultivation.stats.PlayerStat;
import wuxiacraft.cultivation.stats.PlayerSystemStat;

import java.awt.*;
import java.util.HashMap;

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
		//statsPanel.setOverflow(WuxiaScrollPanel.OverflowType.SCROLL_Y);
		systemStats = new HashMap<>();
		screen.addRenderableWidget(statsPanel);
		for (var stat : PlayerStat.values()) {
			var statValue = cultivation.getPlayerStat(stat).toEngineeringString();
			var label = new WuxiaLabel(stat.locationInStatsSheet.x, stat.locationInStatsSheet.y, new TextComponent(String.format(stat.displayFormat, statValue)), color);
			displayLabels.put(stat, label);
			statsPanel.addChild(label);
		}
		for (var system : System.values()) {
			displaySystemLabels.put(system, new HashMap<>());
			WuxiaScrollPanel systemStatPanel = new WuxiaScrollPanel(0, 0, 100, 100, new TextComponent(""));
			//systemStatPanel.setOverflow(WuxiaScrollPanel.OverflowType.SCROLL_Y);
			systemStats.put(system, systemStatPanel);
			for (var stat : PlayerSystemStat.values()) {
				var statValue = cultivation.getSystemData(system).getStat(stat).toEngineeringString();
				var label = new WuxiaLabel(stat.locationInStatsSheet.x, stat.locationInStatsSheet.y, new TextComponent(String.format(stat.displayFormat, statValue)), color);
				displaySystemLabels.get(system).put(stat, label);
				systemStats.get(system).addChild(label);
			}
			screen.addRenderableWidget(systemStats.get(system));
		}
	}

	@Override
	public void close(IntrospectionScreen screen) {
		screen.clearWidgets();
	}

	@Override
	public void renderBG(PoseStack poseStack, int mouseX, int mouseY) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		for (var stat : PlayerStat.values()) {
			var statValue = cultivation.getPlayerStat(stat).toEngineeringString();
			displayLabels.get(stat).setMessage(new TextComponent(String.format(stat.displayFormat, statValue)));
		}
		for (var system : System.values()) {
			for (var stat : PlayerSystemStat.values()) {
				var statValue = cultivation.getSystemData(system).getStat(stat).toEngineeringString();
				displaySystemLabels.get(system).get(stat).setMessage(new TextComponent(String.format(stat.displayFormat, statValue)));
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
			this.systemStats.get(system).x = 36 + (int)((double)system.ordinal() * (double) availableXSpace / 3.0d);
			this.systemStats.get(system).y = 36+availableYSpace / 2;
			this.systemStats.get(system).setWidth(availableXSpace / 3);
			this.systemStats.get(system).setHeight(availableYSpace / 2);
		}
	}

	@Override
	public void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {

	}
}
