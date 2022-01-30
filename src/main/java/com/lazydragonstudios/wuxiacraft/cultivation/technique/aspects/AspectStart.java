package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaLabel;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaLabelBox;

import javax.annotation.Nonnull;
import java.util.LinkedList;

public class AspectStart extends TechniqueAspect {
	public AspectStart(String name, ResourceLocation textureLocation) {
		super(name, textureLocation);
	}

	@Override
	public boolean canConnect(TechniqueAspect aspect) {
		if(aspect instanceof AspectElementalGenerator) return true;
		return false;
	}

	@Nonnull
	@Override
	public LinkedList<AbstractWidget> getStatsSheetDescriptor(ResourceLocation aspectRegistryName) {
		WuxiaLabel nameLabel = new WuxiaLabel(5, 2, new TextComponent(this.name), 0xFFAA00);
		WuxiaLabelBox descriptionLabel = new WuxiaLabelBox(5, 12, 190, new TextComponent("Description: " + this.description));
		LinkedList<AbstractWidget> widgets = new LinkedList<>();
		widgets.add(nameLabel);
		widgets.add(descriptionLabel);
		return widgets;
	}
}
