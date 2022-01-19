package wuxiacraft.client.handler;

import net.minecraft.client.entity.player.AbstractClientPlayerEntity;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import javax.annotation.Nullable;

public class CosmeticsRenderHandler {

	@SubscribeEvent
	public void renderAuraForPlayerPre(RenderLivingEvent.Pre<AbstractClientPlayerEntity, ? extends Model> event) {
		if(!(event.getEntity() instanceof AbstractClientPlayerEntity)) return;
	}

	@SubscribeEvent
	public void renderAuraForPlayerPos(RenderLivingEvent.Post<AbstractClientPlayerEntity, ? extends Model> event) {
		if(!(event.getEntity() instanceof AbstractClientPlayerEntity)) return;
	}

	@Nullable
	protected RenderType getRenderTypeForEntity(EntityModel<? extends PlayerEntity> model, AbstractClientPlayerEntity entity, boolean p_230496_2_, boolean p_230496_3_, boolean p_230496_4_) {
		ResourceLocation resourcelocation = entity.getLocationSkin();
		if (p_230496_3_) {
			return RenderType.getItemEntityTranslucentCull(resourcelocation);
		} else if (p_230496_2_) {
			return model.getRenderType(resourcelocation);
		} else {
			return p_230496_4_ ? RenderType.getOutline(resourcelocation) : null;
		}
	}
}
