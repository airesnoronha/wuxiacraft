package wuxiacraft.client.gui.widgets;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Arrays;
import java.util.LinkedList;

@ParametersAreNonnullByDefault
public class WuxiaLabelBox extends AbstractWidget {

	private LinkedList<LinkedList<String>> words;

	public WuxiaLabelBox(int x, int y, int widget, Component message) {
		super(x, y, widget, 10, message);
		setMessage(message);
	}

	@Override
	public boolean mouseClicked(double p_93641_, double p_93642_, int p_93643_) {
		return false;
	}

	@Override
	public void setMessage(Component p_93667_) {
		String messageString = this.getMessage().getString();
		LinkedList<String> paragraphs = new LinkedList<>(Arrays.stream(messageString.split("\n")).toList());
		words = new LinkedList<>();
		for (var p : paragraphs) {
			var pWords = new LinkedList<>(Arrays.stream(p.split(" ")).toList());
			words.add(pWords);
		}
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float partialTicks) {
		var font = Minecraft.getInstance().font;
		var spaceWidth = font.width(" ");
		var currentTopPos = this.y - font.lineHeight - 1;
		for (var paragraph : words) {
			currentTopPos += font.lineHeight + 1;
			var currentLeftPos = 0;
			for (var word : paragraph) {
				int wordWidth = font.width(word);
				if (currentLeftPos != 0 && currentLeftPos + wordWidth > this.width) {
					currentTopPos += font.lineHeight + 1;
					currentLeftPos = 0;
				}
				font.draw(poseStack, word, this.x + currentLeftPos, currentTopPos, 0xFFAA00);
				currentLeftPos += wordWidth + spaceWidth;
			}
		}
		//just so we can get a hold of the current height of this, perhaps for the future make something that depends on this
		this.setHeight(currentTopPos + font.lineHeight);
	}

	@Override
	public void updateNarration(NarrationElementOutput p_169152_) {
	}
}
