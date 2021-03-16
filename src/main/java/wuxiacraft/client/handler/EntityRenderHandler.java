package wuxiacraft.client.handler;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.RemoteClientPlayerEntity;
import net.minecraft.client.renderer.RenderState;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingRenderer;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;
import wuxiacraft.client.render.model.GhostModel;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.util.MathUtils;

public class EntityRenderHandler {

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void renderPlayerThroughWallsPre(RenderLivingEvent.Pre<RemoteClientPlayerEntity, ? extends Model> event) {
		if (event.getEntity() instanceof RemoteClientPlayerEntity && Minecraft.getInstance().player != null) {
			ICultivation other = Cultivation.get(event.getEntity());
			ICultivation mine = Cultivation.get(Minecraft.getInstance().player);
			//if (//MathUtils.between(mine.getDivineModifier(), other.getDivineModifier(), other.getDivineModifier() * 3) &&
			//event.getEntity().getDistance(Minecraft.getInstance().player) <= mine.getDivineModifier() * 0.6 &&
			//Minecraft.getInstance().player.isSneaking()) {
			//}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void renderPlayerThroughWallsPost(RenderLivingEvent.Post<RemoteClientPlayerEntity, ? extends Model> event) {
		if (event.getEntity() instanceof RemoteClientPlayerEntity && Minecraft.getInstance().player != null) {
			if (Minecraft.getInstance().player.isSneaking()) {
			}
		}
	}
}
