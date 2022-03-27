package com.lazydragonstudios.wuxiacraft.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelLastEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.common.Mod;

import java.util.LinkedList;
import java.util.List;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class WorldRenderHandler {

	@FunctionalInterface
	public interface Renderable {
		void render(PoseStack poseStack, float partialTick);
	}

	@OnlyIn(Dist.CLIENT)
	public static class LevelRenderQueue {

		public static class RenderElement {
			private int duration; //in ticks
			private final Renderable rendering;

			public RenderElement(int duration, Renderable rendering) {
				this.duration = duration;
				this.rendering = rendering;
			}

			public void render(PoseStack poseStack, float partialTicks) {
				try {
					rendering.render(poseStack, partialTicks);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			public boolean tick() {
				this.duration--;
				return this.duration <= 0;
			}
		}

		private final List<RenderElement> drawingQueue;

		public void renderQueue(PoseStack poseStack, float partialTicks) {
			for (RenderElement re : drawingQueue) {
				re.render(poseStack, partialTicks);
			}
		}

		public void tick() {
			List<RenderElement> toRemove = new LinkedList<>();
			for (RenderElement re : drawingQueue) {
				if (re.tick()) {
					toRemove.add(re);
				}
			}
			for (RenderElement removed : toRemove) {
				drawingQueue.remove(removed);
			}
		}

		public void add(int duration, Renderable rendering) {
			this.drawingQueue.add(new RenderElement(duration, rendering));
		}

		public LevelRenderQueue() {
			this.drawingQueue = new LinkedList<>();
		}

	}

	@OnlyIn(Dist.CLIENT)
	public static final WorldRenderHandler.LevelRenderQueue LEVEL_RENDER_QUEUE = new LevelRenderQueue();

	@SubscribeEvent
	public static void onRenderLast(RenderLevelLastEvent event) {
		LEVEL_RENDER_QUEUE.renderQueue(event.getPoseStack(), event.getPartialTick());
	}

	@OnlyIn(Dist.CLIENT)
	@SubscribeEvent
	public static void onLevelTick(TickEvent.PlayerTickEvent event) {
		if(event.phase != TickEvent.Phase.END) return;
		if(event.player != Minecraft.getInstance().player) return;
		LEVEL_RENDER_QUEUE.tick();
	}

}
