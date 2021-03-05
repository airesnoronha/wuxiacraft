package wuxiacraft.client.handler;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import wuxiacraft.client.SkillValues;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.skill.Skill;
import wuxiacraft.network.ActivateSkillMessage;
import wuxiacraft.network.EnergyMessage;
import wuxiacraft.network.WuxiaPacketHandler;

public class ClientSideHandler {

	/**
	 * A method to reduce the amount of energy messages to the server
	 */
	private static double accumulatedFlyCost = 0;

	/**
	 * Accumulated mental energy player got
	 */
	private static double accumulatedMentalEnergy = 0;

	/**
	 * Calculates if players should fly and takes away the energy cost
	 *
	 * @param event A description of what's happening
	 */
	@SubscribeEvent
	public void onHandleFlightClientSide(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		if (event.side == LogicalSide.SERVER) return;
		PlayerEntity player = event.player;
		ICultivation cultivation = Cultivation.get(player);
		boolean canFly = false;
		if (cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getLevel() instanceof CultivationLevel.EssenceLevel)
			canFly = ((CultivationLevel.EssenceLevel) cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getLevel()).flight;
		if (canFly && player.abilities.isFlying && cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getEnergy() <= 0) {
			player.abilities.isFlying = false;
		}
		if (canFly) {
			player.abilities.allowFlying = true;
			player.abilities.setFlySpeed((float) player.getAttributeValue(Attributes.MOVEMENT_SPEED));
		}
		player.sendPlayerAbilities();

		double distance = Math.sqrt(Math.pow(player.lastTickPosX - player.getPosX(), 2) + Math.pow(player.lastTickPosY - player.getPosY(), 2) + Math.pow(player.lastTickPosZ - player.getPosZ(), 2));

		if (player.abilities.isFlying) {
			double totalRem = 0;
			double fly_cost = 250;
			double dist_cost = 132;
			totalRem += fly_cost;
			totalRem += distance * dist_cost;
			if (!player.isCreative()) {
				cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).addEnergy(-totalRem);
				accumulatedFlyCost += totalRem;
			}
		}
		//walking around will clear the head
		if(!player.isSprinting()) {
			double recoveryPerDistance = 0.005;
			if(cultivation.getStatsBySystem(CultivationLevel.System.DIVINE).getEnergy() < cultivation.getMaxDivineEnergy()) {
				cultivation.getStatsBySystem(CultivationLevel.System.DIVINE).addEnergy(distance*recoveryPerDistance);
				accumulatedMentalEnergy += distance*recoveryPerDistance;
			}
		}
		if (cultivation.getTickerTime() % 10 == 0) {
			WuxiaPacketHandler.INSTANCE.sendToServer(new EnergyMessage(accumulatedMentalEnergy, CultivationLevel.System.DIVINE, 0));
			accumulatedMentalEnergy = 0;
			WuxiaPacketHandler.INSTANCE.sendToServer(new EnergyMessage(accumulatedFlyCost, CultivationLevel.System.ESSENCE, 1));
			accumulatedFlyCost = 0;
		}
	}

	/**
	 * This will handle skill casting
	 *
	 * @param event a description of what is happening
	 */
	@SubscribeEvent
	public void onHandleSkillCasting(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		if (event.side == LogicalSide.SERVER) return;
		if (event.player == null) return;
		ICultivation cultivation = Cultivation.get(event.player);
		if (SkillValues.isCastingSkill && cultivation.getSkillCooldown() <= 0) {
			Skill active = cultivation.getActiveSkill(SkillValues.activeSkill);
			if (active != null) {
				if (cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).getEnergy() >= active.energyCost) {
					; //client side -- if anything should happen on server, send an activate action message to server from casting action
					if (active.casting(event.player) && SkillValues.castProgress < active.castTime)
						SkillValues.castProgress += active.castInTicks ? 1 : cultivation.getCastSpeed();
					if (SkillValues.castProgress >= active.castTime) {
						if (active.activate(event.player)) {//client side
							WuxiaPacketHandler.INSTANCE.sendToServer(new ActivateSkillMessage(active.name, active.energyCost));//server side
							cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).addEnergy(-active.energyCost); //client energy cost -> eventually it'll sync
							SkillValues.castProgress = 0;
						}
					}
				}
			}
		} else if (!SkillValues.isCastingSkill) {
			SkillValues.castProgress = 0;
		}
	}
}
