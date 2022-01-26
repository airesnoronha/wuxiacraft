package wuxiacraft.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.antlr.v4.misc.OrderedHashMap;
import org.jetbrains.annotations.NotNull;
import wuxiacraft.WuxiaCraft;
import wuxiacraft.client.gui.tab.*;
import wuxiacraft.container.IntrospectionMenu;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.System;
import wuxiacraft.cultivation.stats.PlayerSystemStat;
import wuxiacraft.util.MathUtil;

import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

@ParametersAreNonnullByDefault
public class IntrospectionScreen extends AbstractContainerScreen<IntrospectionMenu> {

	public static final ResourceLocation introspection_gui = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/gui/introspection_gui.png");

	public OrderedHashMap<String, IntrospectionTab> tabs = new OrderedHashMap<>();
	public String selectedTab;


	public IntrospectionScreen(IntrospectionMenu menu, Inventory inventory, Component title) {
		super(menu, inventory, title);
	}

	@Override
	public <T extends GuiEventListener & Widget & NarratableEntry> @NotNull T addRenderableWidget(T widget) {
		return super.addRenderableWidget(widget);
	}

	@Override
	protected void init() {
		super.init();
		tabs.put("stats", new CharacterStatsTab("stats"));
		tabs.put("aspects", new AspectsTab("aspects"));
		tabs.put("body_technique", new TechniqueTab("body_technique", new Point(64, 36), System.BODY));
		tabs.put("divine_technique", new TechniqueTab("divine_technique", new Point(0, 68), System.DIVINE));
		tabs.put("essence_technique", new TechniqueTab("essence_technique", new Point(32, 68), System.ESSENCE));
		tabs.put("skills", new SkillsTab("skills"));
		tabs.put("professions", new ProfessionsTab("professions"));
		selectedTab = "stats";
		tabs.get(selectedTab).init(this);
	}

	@Override
	public boolean mouseClicked(double mouseX, double mouseY, int button) {
		boolean didSomething = false;
		int i = 0;
		for (var tab : this.tabs.keySet()) {
			if (MathUtil.inBounds(mouseX, mouseY, 0, i * 36, 36, 36)) {
				tabs.get(selectedTab).close(this);
				this.selectedTab = tab;
				tabs.get(selectedTab).init(this);
				didSomething = true;
			}
			i++;
		}
		return didSomething || super.mouseClicked(mouseX, mouseY, button);
	}

	@Override
	public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseDeltaX, double mouseDeltaY) {
		boolean clicked = false;
		for (GuiEventListener guieventlistener : this.children()) {
			clicked = guieventlistener.mouseDragged(mouseX, mouseY, button, mouseDeltaX, mouseDeltaY);
		}
		clicked = clicked || super.mouseDragged(mouseX, mouseY, button, mouseDeltaX, mouseDeltaY);
		return clicked;
	}

	@Override
	protected void renderBg(PoseStack poseStack, float partialTicks, int mouseX, int mouseY) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		RenderSystem.setShaderTexture(0, introspection_gui);
		RenderSystem.enableBlend();
		int i = 0;
		for (var tab : this.tabs.keySet()) {
			int texX = tab.equals(selectedTab) ? 36 : 0;
			GuiComponent.blit(poseStack,
					0, i * 36, //position in screen
					36, 36, //size in screen
					texX, 0, //tex coordinates
					36, 36, //tex size
					256, 256 //image size
			);
			GuiComponent.blit(poseStack,
					2, i * 36 + 2, //position in screen
					32, 32, //size in screen
					tabs.get(tab).icon.x, tabs.get(tab).icon.y, //tex coordinates
					32, 32, //tex size
					256, 256 //image size
			);
			i++;
		}
		int margin = 20;
		int guiScaledWidth = Minecraft.getInstance().getWindow().getGuiScaledWidth();
		int totalAvailableSpace = guiScaledWidth - 36 - 4 * margin;
		int totalBarSpace = totalAvailableSpace / 3;
		int stretchedSpace = totalBarSpace - 14 * 2;
		int stretchSteps = stretchedSpace / 12;
		int stretchedRemainingFill = stretchedSpace % 12;
		var leftPos = new HashMap<System, Integer>();
		leftPos.put(System.BODY, 36 + margin);
		leftPos.put(System.DIVINE, 36 + margin * 2 + totalBarSpace);
		leftPos.put(System.ESSENCE, 36 + margin * 3 + totalBarSpace * 2);
		for (var system : System.values()) {
			//left border
			GuiComponent.blit(poseStack,
					leftPos.get(system), 3,
					14, 30,
					72, 0,
					14, 30,
					256, 256
			);
			//right border
			GuiComponent.blit(poseStack,
					leftPos.get(system) + 14 + stretchedSpace, 3,
					14, 30,
					98, 0,
					14, 30,
					256, 256
			);
			for (int x = 0; x < stretchSteps; x++) {
				GuiComponent.blit(poseStack,
						leftPos.get(system) + 14 + x * 12, 3,
						12, 30,
						86, 0,
						12, 30,
						256, 256
				);
			}
			GuiComponent.blit(poseStack,
					leftPos.get(system) + 14 + stretchedSpace - stretchedRemainingFill, 3,
					stretchedRemainingFill, 30,
					86, 0,
					stretchedRemainingFill, 30,
					256, 256
			);
			var systemData = cultivation.getSystemData(system);
			int barFill = new BigDecimal((stretchedSpace + 10) + ".000").multiply(systemData.getStat(PlayerSystemStat.ENERGY)).divide(systemData.getStat(PlayerSystemStat.MAX_ENERGY), RoundingMode.HALF_UP).intValue();
			GuiComponent.blit(poseStack,
					leftPos.get(system) + 9, 8,
					Math.min(barFill, 5), 20,
					112 + 22 * system.ordinal(), 0,
					Math.min(barFill, 5), 20,
					256, 256
			);
			if (barFill > 5) {
				GuiComponent.blit(poseStack,
						leftPos.get(system) + 14, 8,
						Math.min(barFill-10, stretchedSpace), 20,
						112 + 22 * system.ordinal()+5, 0,
						12, 20,
						256, 256
				);
			}
			if (barFill > stretchedSpace + 5) {
				GuiComponent.blit(poseStack,
						leftPos.get(system) + 14+stretchedSpace, 8,
						Math.min(barFill-stretchedSpace-5, 5), 20,
						112 + 22 * system.ordinal()+17, 0,
						Math.min(barFill-stretchedSpace-5, 5), 20,
						256, 256
				);
			}

		}
		RenderSystem.disableBlend();
		//render the tab
		this.tabs.get(this.selectedTab).renderBG(poseStack, mouseX, mouseY);
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		this.renderBackground(poseStack);
		super.render(poseStack, mouseX, mouseY, partialTicks);
		super.renderTooltip(poseStack, mouseX, mouseY);
	}

	@Override
	protected void renderLabels(PoseStack poseStack, int mouseX, int mouseY) {
	}
}
