package com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.TranslatableComponent;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaLabel;
import com.lazydragonstudios.wuxiacraft.client.gui.widgets.WuxiaLabelBox;

import javax.annotation.Nonnull;
import java.util.LinkedList;

public class StartAspect extends TechniqueAspect {

	@Override
	public boolean canConnect(TechniqueAspect aspect) {
		return aspect instanceof ElementalGenerator;
	}

	@Override
	public int canConnectFromCount() {
		return 0;
	}

	@Override
	public int canConnectToCount() {
		return -1;
	}

	@Nonnull
	@Override
	public LinkedList<AbstractWidget> getStatsSheetDescriptor() {
		var nameLocation = this.getRegistryName();
		if (nameLocation == null) return new LinkedList<>();
		WuxiaLabel nameLabel = new WuxiaLabel(5, 2, new TranslatableComponent("wuxiacraft.technique." + nameLocation.getPath() + ".name"), 0xFFAA00);
		WuxiaLabelBox descriptionLabel = new WuxiaLabelBox(5, 12, 190, new TranslatableComponent("Description: wuxiacraft.technique." + nameLocation.getPath() + ".description"));
		LinkedList<AbstractWidget> widgets = new LinkedList<>();
		widgets.add(nameLabel);
		widgets.add(descriptionLabel);
		return widgets;
	}
}
