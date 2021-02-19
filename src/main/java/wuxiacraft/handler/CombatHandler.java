package wuxiacraft.handler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import wuxiacraft.combat.ElementalDamageSource;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.Element;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.init.WuxiaElements;
import wuxiacraft.network.CultivationSyncMessage;
import wuxiacraft.network.WuxiaPacketHandler;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.FORGE)
public class CombatHandler {

	/**
	 * All damage done to players will be intersected and be applied through here!
	 * This will post a LivingDamageEventToPlayers for compatibility
	 *
	 * @param event a description of what is happening
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerHurtEvent(LivingHurtEvent event) {
		if (!(event.getEntity() instanceof PlayerEntity)) return;
		ICultivation cultivation = Cultivation.get(event.getEntityLiving());
		ElementalDamageSource source;
		if (!(event.getSource() instanceof ElementalDamageSource)) {
			source = getElementalSourceFromVanillaSource(event.getSource());
		} else {
			source = (ElementalDamageSource) event.getSource();
		}
		double resistance = cultivation.getResistanceToElement(source.getElement());

		ForgeHooks.onLivingDamage(event.getEntityLiving(), source, event.getAmount() - (float)resistance);
		event.setCanceled(true);
	}

	private static ElementalDamageSource getElementalSourceFromVanillaSource(DamageSource source) {
		if (source.isFireDamage()) return WuxiaElements.getSourceByElement(WuxiaElements.FIRE);
		if (source == DamageSource.LIGHTNING_BOLT) return WuxiaElements.getSourceByElement(WuxiaElements.LIGHTNING);
		return WuxiaElements.getSourceByElement(WuxiaElements.PHYSICAL);
	}

	/**
	 * This will intersect all player damages
	 * This is here mostly for compatibility
	 *
	 * @param event a description of what is happening
	 */
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onPlayerDamage(LivingDamageEvent event) {
		if (!(event.getEntity() instanceof PlayerEntity)) return;
		ICultivation cultivation = Cultivation.get(event.getEntityLiving());
		cultivation.setHP(cultivation.getHP() - event.getAmount());
		if(cultivation.getHP() <= 0) {
			event.getEntityLiving().setHealth(-1);
		}
		WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getEntityLiving()), new CultivationSyncMessage(cultivation, event.getEntityLiving().getUniqueID()));
		event.setCanceled(true);
	}
}
