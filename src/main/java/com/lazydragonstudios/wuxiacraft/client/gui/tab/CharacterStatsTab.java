package com.lazydragonstudios.wuxiacraft.client.gui.tab;

import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaSliderButton;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaVerticalFlowPanel;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.SystemContainer;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemElementalStat;
import com.lazydragonstudios.wuxiacraft.networking.UpdateRegulatorsMessage;
import com.lazydragonstudios.wuxiacraft.networking.WuxiaPacketHandler;
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
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;
import java.util.LinkedList;

public class CharacterStatsTab extends IntrospectionTab {

	private final HashMap<PlayerStat, WuxiaLabel> displayLabels = new HashMap<>();
	private final HashMap<System, HashMap<PlayerSystemStat, WuxiaLabel>> displaySystemLabels = new HashMap<>();
	private final HashMap<System, HashMap<ResourceLocation, HashMap<PlayerSystemElementalStat, WuxiaLabel>>> displaySystemElementalLabels = new HashMap<>();

	private WuxiaScrollPanel statsPanel;
	private HashMap<System, WuxiaVerticalFlowPanel> systemStats;
	private WuxiaSliderButton agilitySlider;
	private WuxiaSliderButton strengthSlider;
	private WuxiaLabel strengthSliderLabel;
	private WuxiaLabel agilitySliderLabel;

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
			BigDecimal statDecimal = cultivation.getStat(stat);
			var statValue = statDecimal.setScale(Math.min(statDecimal.scale(), 2), RoundingMode.HALF_UP).toEngineeringString();
			int labelX = stat.locationInStatsSheet.x == -1 ? 150 : stat.locationInStatsSheet.x;
			var label = new WuxiaLabel(labelX, stat.locationInStatsSheet.y, new TranslatableComponent("wuxiacraft.gui." + stat.name().toLowerCase(), statValue), color);
			displayLabels.put(stat, label);
			statsPanel.addChild(label);
		}
		for (var system : System.values()) {
			displaySystemLabels.put(system, new HashMap<>());
			WuxiaVerticalFlowPanel systemStatPanel = new WuxiaVerticalFlowPanel(0, 0, 100, 100, new TextComponent(""));
			systemStatPanel.margin = 2;
			systemStats.put(system, systemStatPanel);
			SystemContainer systemData = cultivation.getSystemData(system);
			ResourceLocation realmLocation = systemData.getStage().realm;
			var realmNameLabel = new WuxiaLabel(0, 0, new TranslatableComponent(realmLocation.getNamespace() + ".realm." + realmLocation.getPath()), color);
			ResourceLocation currentStageLocation = systemData.currentStage;
			var stageNameLabel = new WuxiaLabel(0, 0, new TranslatableComponent(currentStageLocation.getNamespace() + ".stage." + currentStageLocation.getPath()), color);
			systemStatPanel.addChild(realmNameLabel);
			systemStatPanel.addChild(stageNameLabel);
			for (var stat : PlayerSystemStat.values()) {
				BigDecimal statDecimal = systemData.getStat(stat);
				var statValue = statDecimal.setScale(Math.min(statDecimal.scale(), 2), RoundingMode.HALF_UP).toEngineeringString();
				var label = new WuxiaLabel(0, 0, new TranslatableComponent("wuxiacraft.gui." + stat.name().toLowerCase(), statValue), color);
				displaySystemLabels.get(system).put(stat, label);
				systemStats.get(system).addChild(label);
			}
			for (var element : systemData.getStatElements()) {
				for (var stat : systemData.getElementalStatsForElement(element)) {
					BigDecimal statDecimal = cultivation.getSystemData(system).getStat(element, stat);
					var statValue = statDecimal.setScale(Math.min(statDecimal.scale(), 2), RoundingMode.HALF_UP).toEngineeringString();
					var label = new WuxiaLabel(0, 0, new TranslatableComponent("wuxiacraft.gui." + stat.name().toLowerCase(),
							new TranslatableComponent(element.getNamespace() + ".element." + element.getPath()),
							statValue), color);
					displaySystemElementalLabels.putIfAbsent(system, new HashMap<>());
					displaySystemElementalLabels.get(system).putIfAbsent(element, new HashMap<>());
					displaySystemElementalLabels.get(system).get(element).putIfAbsent(stat, label);
					systemStats.get(system).addChild(label);
				}
			}
			statsPanel.recalculateContentSpace();
			screen.addRenderableWidget(systemStats.get(system));
		}
		strengthSliderLabel = new WuxiaLabel(38, 51, new TranslatableComponent("wuxiacraft.gui.strength", ""), 0xFFAA00);
		screen.addRenderableWidget(strengthSliderLabel);
		strengthSlider = new WuxiaSliderButton(68, 44, 160, 20, cultivation.getStrengthRegulator(), v -> {
			cultivation.setStrengthRegulator(v);
			sendRegulatorsMessage();
		});
		agilitySliderLabel = new WuxiaLabel(230, 51, new TranslatableComponent("wuxiacraft.gui.agility", ""), 0xFFAA00);
		screen.addRenderableWidget(agilitySliderLabel);
		agilitySlider = new WuxiaSliderButton(260, 44, 160, 20, cultivation.getAgilityRegulator(), v -> {
			cultivation.setAgilityRegulator(v);
			sendRegulatorsMessage();
		});
		screen.addRenderableWidget(agilitySlider);
		screen.addRenderableWidget(strengthSlider);
	}

	public void sendRegulatorsMessage() {
		WuxiaPacketHandler.INSTANCE.sendToServer(new UpdateRegulatorsMessage(strengthSlider.getValue(), agilitySlider.getValue()));
	}

	@Override
	public void renderBG(PoseStack poseStack, int mouseX, int mouseY) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		var leftPostX = 0;
		var secondColumn = new LinkedList<WuxiaLabel>();
		for (var stat : PlayerStat.values()) {
			BigDecimal statDecimal = cultivation.getStat(stat);
			var statValue = statDecimal.setScale(Math.min(2, statDecimal.scale()), RoundingMode.HALF_UP).toEngineeringString();
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
				BigDecimal statDecimal = cultivation.getSystemData(system).getStat(stat);
				var statValue = statDecimal.setScale(Math.min(statDecimal.scale(), 2), RoundingMode.HALF_UP).toEngineeringString();
				displaySystemLabels.get(system).get(stat).setMessage(new TranslatableComponent("wuxiacraft.gui." + stat.name().toLowerCase(), statValue));
			}
			if (displaySystemElementalLabels.containsKey(system)) {
				if (!displaySystemElementalLabels.get(system).isEmpty()) {
					for (var element : displaySystemElementalLabels.get(system).keySet()) {
						for (var stat : displaySystemElementalLabels.get(system).get(element).keySet()) {
							BigDecimal statDecimal = cultivation.getSystemData(system).getStat(element, stat);
							var statValue = statDecimal.setScale(Math.min(statDecimal.scale(), 2), RoundingMode.HALF_UP).toEngineeringString();
							displaySystemElementalLabels.get(system).get(element).get(stat)
									.setMessage(new TranslatableComponent("wuxiacraft.gui." + stat.name().toLowerCase(),
											new TranslatableComponent(element.getNamespace() + ".element." + element.getPath()),
											statValue));
						}
					}
				}
			}
		}
		var scaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		var scaledHeight = Minecraft.getInstance().getWindow().getGuiScaledHeight();
		int availableXSpace = scaledWidth - 36;
		int availableYSpace = scaledHeight - 72;
		statsPanel.x = 36;
		statsPanel.y = 72;
		statsPanel.setWidth(availableXSpace);
		statsPanel.setHeight(availableYSpace / 2);
		for (var system : System.values()) {
			this.systemStats.get(system).x = 36 + (int) ((double) system.ordinal() * (double) availableXSpace / 3.0d);
			this.systemStats.get(system).y = 72 + availableYSpace / 2;
			this.systemStats.get(system).setWidth(availableXSpace / 3);
			this.systemStats.get(system).setHeight(availableYSpace / 2);
		}
		int strengthLabelWidth = Minecraft.getInstance().font.width(strengthSliderLabel.getMessage());
		int agilityLabelWidth = Minecraft.getInstance().font.width(agilitySliderLabel.getMessage());
		this.strengthSlider.x = 40 + strengthLabelWidth;
		this.agilitySliderLabel.x = 42 + strengthLabelWidth + strengthSlider.getWidth();
		this.agilitySlider.x = 44 + strengthLabelWidth + strengthSlider.getWidth() + agilityLabelWidth;
	}

	@Override
	public void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {

	}
}
