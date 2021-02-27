package wuxiacraft.handler;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.stats.Stats;
import net.minecraft.util.DamageSource;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.network.PacketDistributor;
import wuxiacraft.combat.ElementalDamageSource;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.init.WuxiaElements;
import wuxiacraft.network.CultivationSyncMessage;
import wuxiacraft.network.WuxiaPacketHandler;
import wuxiacraft.util.MathUtils;

import java.util.Objects;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
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
		if (!(event.getSource() instanceof ElementalDamageSource)) { //Means it wasn't a wuxiacraft damage
			source = getElementalSourceFromVanillaSource(event.getSource());
		} else {
			source = (ElementalDamageSource) event.getSource();
		}
		double resistance = cultivation.getResistanceToElement(source.getElement());

		ForgeHooks.onLivingDamage(event.getEntityLiving(), source, event.getAmount() - (float) resistance);
		event.setCanceled(true);
	}

	private static ElementalDamageSource getElementalSourceFromVanillaSource(DamageSource source) {
		if (source.isFireDamage()) return new ElementalDamageSource(source.getDamageType(), WuxiaElements.FIRE);
		if (source == DamageSource.LIGHTNING_BOLT)
			return new ElementalDamageSource(source.getDamageType(), WuxiaElements.LIGHTNING);
		return new ElementalDamageSource(source.getDamageType(), WuxiaElements.PHYSICAL, source.getTrueSource());
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

		// this is for vanilla statistics i guess!
		event.getEntityLiving().getCombatTracker().trackDamage(event.getSource(), (float) cultivation.getHP() + event.getAmount(), event.getAmount());
		((PlayerEntity) event.getEntityLiving()).addStat(Stats.DAMAGE_TAKEN, (int) event.getAmount());
		((PlayerEntity) event.getEntityLiving()).addExhaustion(event.getAmount());
		//
		if (cultivation.getHP() <= 0) {
			event.getEntityLiving().setHealth(-1); //it really kills a player
		}
		//this will make players health bar always keep up with
		WuxiaPacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> (ServerPlayerEntity) event.getEntityLiving()), new CultivationSyncMessage(cultivation));
		event.setCanceled(true);
	}

	/**
	 * This will add the strength to basic attacks from the player
	 * This way i guess players won't have a ton of modifiers
	 * And i can also send mobs flying away
	 *
	 * @param event A description of what's happening
	 */
	@SubscribeEvent(priority = EventPriority.NORMAL)
	public static void onPlayerDealsDamage(LivingHurtEvent event) {
		if (!(event.getSource().getTrueSource() instanceof PlayerEntity)) return;
		if (event.getSource() instanceof ElementalDamageSource)
			return; // Means it was wuxiacraft that came up with this attack, so damage is already calculated

		ICultivation cultivation = Cultivation.get((LivingEntity) event.getSource().getTrueSource());
		event.setAmount(event.getAmount() + (float) cultivation.getFinalModifiers().strength);

		LivingEntity target = event.getEntityLiving();
		double maxHP = target.getMaxHealth();
		if(target instanceof PlayerEntity) maxHP = Cultivation.get(target).getFinalModifiers().maxHealth;
		double knockSpeed = MathUtils.clamp((event.getAmount()*1.5- maxHP)*0.3, 0, 12);
		Vector3d diff = Objects.requireNonNull(event.getSource().getDamageLocation()).subtract(event.getEntityLiving().getPositionVec());
		diff = diff.normalize();
		target.applyKnockback((float) knockSpeed, diff.x, diff.z);


	}
}
