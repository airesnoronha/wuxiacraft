package wuxiacraft.client.handler;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import wuxiacraft.client.SkillValues;
import wuxiacraft.cultivation.*;
import wuxiacraft.cultivation.skill.Skill;
import wuxiacraft.network.ActivateSkillMessage;
import wuxiacraft.network.EnergyMessage;
import wuxiacraft.network.WuxiaPacketHandler;

public class ClientSideHandler {

	/**
	 * A method to reduce the amount of energy messages to the server
	 */
	private static double accumulatedEssenceEnergy = 0;

	/**
	 * Accumulated mental energy player got
	 */
	private static double accumulatedMentalEnergy = 0;

	/**
	 * Accumulated body energy
	 */
	private static double accumulatedBodyEnergy = 0;

	/**
	 * Calculates if players should fly and takes away the energy cost
	 *
	 * @param event A description of what's happening
	 */
	@SubscribeEvent
	public void onHandlePlayerFlight(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.END) return;
		if (event.side == LogicalSide.SERVER) return;
		PlayerEntity player = event.player;
		ICultivation cultivation = Cultivation.get(player);

		double distance = Math.sqrt(Math.pow(player.lastTickPosX - player.getPosX(), 2) + Math.pow(player.lastTickPosY - player.getPosY(), 2) + Math.pow(player.lastTickPosZ - player.getPosZ(), 2));

		if (player.abilities.isFlying) {
			double totalRem = 0;
			double fly_cost = 250;
			double dist_cost = 132;
			totalRem += fly_cost;
			totalRem += distance * dist_cost;
			if (!player.isCreative()) {
				cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE).addEnergy(-totalRem);
				accumulatedEssenceEnergy -= totalRem;
			}
		}

		//walking around will clear the head
		if (!player.isSprinting()) {
			double recoveryPerDistance = 0.015;
			if (cultivation.getStatsBySystem(CultivationLevel.System.DIVINE).getEnergy() < cultivation.getMaxDivineEnergy()) {
				cultivation.getStatsBySystem(CultivationLevel.System.DIVINE).addEnergy(distance * recoveryPerDistance);
				accumulatedMentalEnergy += distance * recoveryPerDistance;
			}
		}
		if (cultivation.getTickerTime() % 20 == 5 && accumulatedMentalEnergy != 0) {
			WuxiaPacketHandler.INSTANCE.sendToServer(new EnergyMessage(accumulatedMentalEnergy, CultivationLevel.System.DIVINE, 0));
			accumulatedMentalEnergy = 0;
		}
		if (cultivation.getTickerTime() % 20 == 10 && accumulatedEssenceEnergy != 0) {
			WuxiaPacketHandler.INSTANCE.sendToServer(new EnergyMessage(accumulatedEssenceEnergy, CultivationLevel.System.ESSENCE, 0));
			accumulatedEssenceEnergy = 0;
		}
		if (cultivation.getTickerTime() % 20 == 15 && accumulatedBodyEnergy != 0) {
			WuxiaPacketHandler.INSTANCE.sendToServer(new EnergyMessage(accumulatedBodyEnergy, CultivationLevel.System.BODY, 0));
			accumulatedBodyEnergy = 0;
		}
	}

	/**
	 * This will handle skill casting
	 *
	 * @param event a description of what is happening
	 */
	@SubscribeEvent
	public void onHandlePlayerWalking(TickEvent.PlayerTickEvent event) {
		if (event.phase != TickEvent.Phase.START) return;
		if (event.side == LogicalSide.SERVER) return;
		if (event.player == null) return;
		ClientPlayerEntity player = (ClientPlayerEntity) event.player;
		ICultivation cultivation = Cultivation.get(player);
		player.stepHeight = 0.6f + (float) cultivation.getStepHeight();
		if (cultivation.isPowerWalk() && player.movementInput.isMovingForward()) {
			SystemStats bodyStats = cultivation.getStatsBySystem(CultivationLevel.System.BODY);
			SystemStats essenceStats = cultivation.getStatsBySystem(CultivationLevel.System.ESSENCE);
			double bodyEnergyFactor = cultivation.getBodyModifier() * 0.2 / cultivation.getFinalModifiers().movementSpeed;
			double divineEnergyFactor = cultivation.getDivineModifier() * 0.1 / cultivation.getFinalModifiers().movementSpeed;
			double essenceEnergyFactor = cultivation.getEssenceModifier() * 0.4 / cultivation.getFinalModifiers().movementSpeed;
			double speed = player.isSprinting() ? cultivation.getRunningSpeed() : cultivation.getWalkingSpeed();
			double dist_cost = 0.7;
			if (bodyStats.getEnergy() > speed * bodyEnergyFactor) {
				player.moveRelative((float) (speed * bodyEnergyFactor) / 20f, new Vector3d(0, 0, 1));
				bodyStats.addEnergy(-speed * bodyEnergyFactor * dist_cost);
				accumulatedBodyEnergy -= speed * bodyEnergyFactor * dist_cost;
			}
			if (essenceStats.getEnergy() > speed * (essenceEnergyFactor + divineEnergyFactor)) {
				player.moveRelative((float) (speed * (essenceEnergyFactor + divineEnergyFactor)) / 20f, new Vector3d(0, 0, 1));
				essenceStats.addEnergy(-speed * (essenceEnergyFactor + divineEnergyFactor) * dist_cost);
				accumulatedEssenceEnergy -= speed * (essenceEnergyFactor + divineEnergyFactor) * dist_cost;
			}

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
					//client side -- if anything should happen on server, send an activate action message to server from casting action
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
