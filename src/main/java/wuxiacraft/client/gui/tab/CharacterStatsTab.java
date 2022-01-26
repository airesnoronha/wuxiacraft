package wuxiacraft.client.gui.tab;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.TextComponent;
import wuxiacraft.client.gui.IntrospectionScreen;
import wuxiacraft.client.gui.widgets.WuxiaLabel;
import wuxiacraft.client.gui.widgets.WuxiaScrollPanel;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.stats.PlayerStat;

import java.awt.*;
import java.util.HashMap;

public class CharacterStatsTab extends IntrospectionTab{

	private final HashMap<PlayerStat, WuxiaLabel> displayLabels = new HashMap<>();

	public CharacterStatsTab(String name) {
		super(name, new Point(0, 36));
	}

	@Override
	public void init(IntrospectionScreen screen) {
		int color = 0xFFAA00;
		var player = Minecraft.getInstance().player;
		if(player == null) return;
		var cultivation = Cultivation.get(player);

		for(var stat : PlayerStat.values()) {
			var statValue = cultivation.getPlayerStat(stat).toEngineeringString();
			var label = new WuxiaLabel(36+stat.locationInStatsSheet.x, 36+stat.locationInStatsSheet.y, new TextComponent(String.format(stat.displayFormat, statValue)), color);
			displayLabels.put(stat, label);
			screen.addRenderableWidget(label);
		}
	}

	@Override
	public void close(IntrospectionScreen screen) {
		for(var stat : PlayerStat.values()) {
			screen.renderables.remove(displayLabels.get(stat));
		}
	}

	@Override
	public void renderBG(PoseStack poseStack, int mouseX, int mouseY) {
		var player = Minecraft.getInstance().player;
		if(player == null) return;
		var cultivation = Cultivation.get(player);
		for(var stat : PlayerStat.values()) {
			var statValue = cultivation.getPlayerStat(stat).toEngineeringString();
			displayLabels.get(stat).setMessage(new TextComponent(String.format(stat.displayFormat, statValue)));
		}
	}

	@Override
	public void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {

	}
}
