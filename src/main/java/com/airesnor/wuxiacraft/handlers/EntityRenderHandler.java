package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.aura.Aura;
import com.airesnor.wuxiacraft.aura.Auras;
import com.airesnor.wuxiacraft.aura.IAuraCap;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.RequestAuraForOtherPlayerMessage;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.List;
import java.util.UUID;


public class EntityRenderHandler {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void playerAuraPre(RenderLivingEvent.Pre<EntityPlayer> event) {
		if (event.getEntity() instanceof EntityPlayer) {
			IAuraCap auraCap = CultivationUtils.getAuraFromEntity(event.getEntity());
			for(Aura aura : auraCap.getAuraInstances()) {
				aura.renderPre(event.getX(), event.getY(), event.getZ());
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void playerAuraPost(RenderLivingEvent.Post<EntityPlayer> event) {
		if (event.getEntity() instanceof EntityPlayer) {
			IAuraCap auraCap = CultivationUtils.getAuraFromEntity(event.getEntity());
			for(Aura aura : auraCap.getAuraInstances()) {
				aura.renderPost(event.getX(), event.getY(), event.getZ());
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void auraDataUpdate(TickEvent.PlayerTickEvent  event) {
		if(event.phase == TickEvent.Phase.END && event.side == Side.CLIENT) {
			for (Aura aura : Auras.AURAS.values()) {
				aura.update();
			}
		}
	}

	private static int timer = 0;

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerRequestAura(TickEvent.PlayerTickEvent event) {
		if(event.phase == TickEvent.Phase.END && event.side == Side.CLIENT) {
			if(timer >= 20) {
				if (Minecraft.getMinecraft().player != null) {
					AxisAlignedBB range = new AxisAlignedBB(Minecraft.getMinecraft().player.getPosition()).grow(72);
					List<EntityOtherPlayerMP> others = event.player.world.getEntitiesWithinAABB(EntityOtherPlayerMP.class, range);
					for (EntityOtherPlayerMP other : others) {
						NetworkWrapper.INSTANCE.sendToServer(new RequestAuraForOtherPlayerMessage(Minecraft.getMinecraft().player.getUniqueID(), other.getUniqueID()));
					}
					timer = 0;
				}
			}
			timer++;
		}
	}

}
