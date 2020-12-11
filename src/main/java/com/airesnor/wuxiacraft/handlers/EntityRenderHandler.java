package com.airesnor.wuxiacraft.handlers;

import com.airesnor.wuxiacraft.aura.Aura;
import com.airesnor.wuxiacraft.aura.Auras;
import com.airesnor.wuxiacraft.aura.IAuraCap;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.networking.AskCultivationLevelMessage;
import com.airesnor.wuxiacraft.networking.NetworkWrapper;
import com.airesnor.wuxiacraft.networking.RequestAuraForOtherPlayerMessage;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import com.airesnor.wuxiacraft.utils.MathUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


public class EntityRenderHandler {

	@SideOnly(Side.CLIENT)
	public static final Map<UUID, ICultivation> knownCultivations = new HashMap<>();

	@SideOnly(Side.CLIENT)
	public static final Map<UUID, Long> knownCultivationGetTimes = new HashMap<>();

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void playerAuraPre(RenderLivingEvent.Pre<EntityPlayer> event) {
		if (event.getEntity() instanceof EntityPlayer) {
			IAuraCap auraCap = CultivationUtils.getAuraFromEntity(event.getEntity());
			for (Aura aura : auraCap.getAuraInstances()) {
				aura.renderPre(event.getX(), event.getY(), event.getZ());
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void playerAuraPost(RenderLivingEvent.Post<EntityPlayer> event) {
		if (event.getEntity() instanceof EntityPlayer) {
			IAuraCap auraCap = CultivationUtils.getAuraFromEntity(event.getEntity());
			for (Aura aura : auraCap.getAuraInstances()) {
				aura.renderPost(event.getX(), event.getY(), event.getZ());
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void renderPlayerThroughWallsPre(RenderLivingEvent.Pre<EntityOtherPlayerMP> event) {
		if (event.getEntity() instanceof EntityOtherPlayerMP) {
			ICultivation other = CultivationUtils.getCultivationFromEntity(event.getEntity());
			ICultivation mine = CultivationUtils.getCultivationFromEntity(Minecraft.getMinecraft().player);
			if (MathUtils.between(mine.getDivineModifier(), other.getDivineModifier(), other.getDivineModifier() * 3) &&
					event.getEntity().getDistance(Minecraft.getMinecraft().player) <= mine.getDivineModifier() * 0.6 &&
					Minecraft.getMinecraft().player.isSneaking()) {
				GlStateManager.disableDepth();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void renderPlayerThroughWallsPost(RenderLivingEvent.Post<EntityOtherPlayerMP> event) {
		if (event.getEntity() instanceof EntityOtherPlayerMP) {
			if (Minecraft.getMinecraft().player.isSneaking()) {
				GlStateManager.enableDepth();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void auraDataUpdate(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END && event.side == Side.CLIENT) {
			for (Aura aura : Auras.AURAS.values()) {
				aura.update();
			}
		}
	}

	private static int timer = 0;

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void onPlayerRequestAura(TickEvent.PlayerTickEvent event) {
		if (event.phase == TickEvent.Phase.END && event.side == Side.CLIENT) {
			if (timer >= 20) {
				if (Minecraft.getMinecraft().player != null) {
					AxisAlignedBB range = new AxisAlignedBB(Minecraft.getMinecraft().player.getPosition()).grow(72);
					List<EntityOtherPlayerMP> others = event.player.world.getEntitiesWithinAABB(EntityOtherPlayerMP.class, range);
					for (EntityOtherPlayerMP other : others) {
						NetworkWrapper.INSTANCE.sendToServer(new RequestAuraForOtherPlayerMessage(Minecraft.getMinecraft().player.getUniqueID(), other.getUniqueID()));
					}
					timer = 0;
				}
				//improvisation, using the same method just -- post edit -> i don't remember what this just was about
				for (UUID player : knownCultivations.keySet()) {
					if (knownCultivationGetTimes.containsKey(player)) {
						if (knownCultivationGetTimes.get(player) > System.currentTimeMillis() + 30000) {
							knownCultivations.remove(player);
							knownCultivationGetTimes.remove(player);
						}
					} else {
						knownCultivations.remove(player);
						knownCultivationGetTimes.remove(player);
					}
				}
				NetworkWrapper.INSTANCE.sendToServer(new AskCultivationLevelMessage(event.player.getUniqueID()));
			}
			timer++;
		}
	}

}
