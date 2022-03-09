package com.lazydragonstudios.wuxiacraft.client.gui.widgets;

import com.lazydragonstudios.wuxiacraft.init.WuxiaRegistries;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects.TechniqueAspect;

import javax.annotation.ParametersAreNonnullByDefault;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.HashMap;

@ParametersAreNonnullByDefault
public class WuxiaCheckpointsWidget extends AbstractWidget {

	private final float[][] colors = new float[][]{
			{1.0f, 0.05f, 0f, 1f}, //red
			{1.0f, 1.0f, 0.2f, 1f}, // yellow
			{1.0f, 1.0f, 0.2f, 1f}, // yellow
			{0.05f, 1.0f, 0.05f, 1f}, // green
			{0.3f, 0.1f, 1f, 1f}, //blue
			{0.9f, 0.1f, 1.0f, 1f}, //purple
			{1.0f, 1.0f, 1.0f, 1f}, // white
	};

	private BigDecimal proficiency;
	private ResourceLocation aspectLocation;
	private TechniqueAspect aspect;
	private final HashMap<TechniqueAspect.Checkpoint, Integer> checkpointsFill;


	public WuxiaCheckpointsWidget(int x, int y, int width, ResourceLocation aspect) {
		super(x, y, width, 12, new TextComponent(""));
		checkpointsFill = new HashMap<>();
		setAspectLocation(aspect);
	}

	public void setAspectLocation(ResourceLocation aspectLocation) throws IllegalArgumentException {
		var aspect = WuxiaRegistries.TECHNIQUE_ASPECT.getValue(aspectLocation);
		if (aspect == null)
			throw new IllegalArgumentException("Wrong aspect location. No aspect found for '" + aspectLocation.toString() + "'!");
		this.aspectLocation = aspectLocation;
		this.aspect = aspect;
		checkpointsFill.clear();
		var maxAmount = this.aspect.checkpoints.getLast().proficiencyRequired();
		var iterator = this.aspect.checkpoints.iterator();
		var aux = iterator.next(); // this is always true because there is always a no_checkpoint at the beginning
		while (iterator.hasNext()) {
			var nextAux = iterator.next();
			checkpointsFill.put(aux, new BigDecimal(this.width)
					.multiply(nextAux.proficiencyRequired().subtract(aux.proficiencyRequired()))
					.divide(maxAmount, RoundingMode.HALF_UP).intValue());
			aux = nextAux;
		}
		checkpointsFill.put(aux, BigDecimal.ZERO.intValue());
	}

	public void setProficiency(BigDecimal proficiency) {
		this.proficiency = proficiency;
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		var player = Minecraft.getInstance().player;
		if (player == null) return;
		var cultivation = Cultivation.get(player);
		this.proficiency = cultivation.getAspects().getAspectProficiency(this.aspectLocation);
		RenderSystem.setShaderTexture(0, WuxiaButton.UI_CONTROLS);
		RenderSystem.enableBlend();

		//render the gb bar
		GuiComponent.blit(poseStack,
				this.x, this.y,
				1, 12,
				0, 25,
				1, 12,
				256, 256
		);
		GuiComponent.blit(poseStack,
				this.x + 1, this.y,
				this.width - 2, 12,
				1, 25,
				1, 12,
				256, 256
		);
		GuiComponent.blit(poseStack,
				this.x + this.width - 1, this.y,
				1, 12,
				2, 25,
				1, 12,
				256, 256
		);
		//render the actual fills now
		var currentCheckpoint = this.aspect.getCurrentCheckpoint(proficiency);
		int currentFillLeft = 0;
		BigDecimal requiredProficiency = BigDecimal.ZERO; //keeps track of previous proficiency required
		int index = 0; //using indexOf while iterating a linked list is rather stupid so just add +1 to each iteration
		for (var checkpoint : this.aspect.checkpoints) {
			int fillWidth = checkpointsFill.get(checkpoint);
			if (checkpoint == currentCheckpoint) {
				int barFill = new BigDecimal(fillWidth)
						.multiply(this.proficiency.subtract(requiredProficiency.add(checkpoint.proficiencyRequired())))
						.divide(checkpoint.proficiencyRequired().max(BigDecimal.ONE).subtract(requiredProficiency), RoundingMode.HALF_UP)
						.intValue();
				GuiComponent.blit(poseStack, // poseStack
						this.x + currentFillLeft + 1, this.y + 1, // in screen coords
						barFill, 10, //in screen width and height
						3 + index, 26, // in texture coords
						1, 10, // in texture width and height
						256, 256 // whole texture size (width and height)
				);
				break;
			} else {
				GuiComponent.blit(poseStack,
						this.x + currentFillLeft + 1, this.y + 1,
						fillWidth, 10,
						3 + index, 26,
						1, 10,
						256, 256
				);
			}
			currentFillLeft += fillWidth;
			requiredProficiency = checkpoint.proficiencyRequired();
			index++;
		}
		RenderSystem.disableBlend();
	}

	@Override
	public void renderToolTip(PoseStack p_93653_, int p_93654_, int p_93655_) {
		super.renderToolTip(p_93653_, p_93654_, p_93655_);
	}

	@Override
	public void updateNarration(NarrationElementOutput p_169152_) {
	}
}
