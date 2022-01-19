package wuxiacraft.handler;

import com.google.common.collect.ImmutableList;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.merchant.villager.VillagerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import wuxiacraft.cultivation.Cultivation;
import wuxiacraft.cultivation.CultivationLevel;
import wuxiacraft.cultivation.ICultivation;
import wuxiacraft.cultivation.KnownTechnique;
import wuxiacraft.cultivation.technique.Technique;
import wuxiacraft.init.WuxiaTechniques;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.FORGE)
public class TechniqueHandler {

	private static final ImmutableList<Technique> demonicTechniques = ImmutableList.of(WuxiaTechniques.SOUL_SLAUGHTERING_TECHNIQUE);

	@SubscribeEvent
	public static void onCultivatorKillSomeone(LivingDamageEvent event) {
		if (!(event.getEntityLiving() instanceof PlayerEntity)) return;
		if (!(event.getSource().getTrueSource() instanceof PlayerEntity)) return;
		ICultivation cultivation = Cultivation.get(event.getEntityLiving());
		if (event.getAmount() > cultivation.getHP()) {
			ICultivation attackerCult = Cultivation.get((LivingEntity) event.getSource().getTrueSource());
			KnownTechnique bodyKnownTechnique = attackerCult.getTechniqueBySystem(CultivationLevel.System.BODY);
			if (bodyKnownTechnique != null && demonicTechniques.contains(bodyKnownTechnique.getTechnique())) {
				double amount = Math.max(100, cultivation.getDivineModifier() / 100f);
				bodyKnownTechnique.proficiency(amount);
				attackerCult.addBaseToSystem(amount / 10.0, CultivationLevel.System.BODY);
			}
			KnownTechnique divineKnownTechnique = attackerCult.getTechniqueBySystem(CultivationLevel.System.DIVINE);
			if (divineKnownTechnique != null && demonicTechniques.contains(divineKnownTechnique.getTechnique())) {
				double amount = Math.max(100, cultivation.getDivineModifier() / 100f);
				divineKnownTechnique.proficiency(amount);
				attackerCult.addBaseToSystem(amount / 10.0, CultivationLevel.System.DIVINE);
			}
			KnownTechnique essenceKnownTechnique = attackerCult.getTechniqueBySystem(CultivationLevel.System.ESSENCE);
			if (essenceKnownTechnique != null && demonicTechniques.contains(essenceKnownTechnique.getTechnique())) {
				double amount = Math.max(100, cultivation.getDivineModifier() / 100f);
				essenceKnownTechnique.proficiency(amount);
				attackerCult.addBaseToSystem(amount / 10.0, CultivationLevel.System.ESSENCE);
			}
		}
	}

	@SubscribeEvent
	public static void onCultivatorKillVillager(LivingDamageEvent event) {
		if (!(event.getEntityLiving() instanceof VillagerEntity)) return;
		if (!(event.getSource().getTrueSource() instanceof PlayerEntity)) return;
		if (event.getAmount() > event.getEntityLiving().getHealth()) {
			ICultivation attackerCult = Cultivation.get((LivingEntity) event.getSource().getTrueSource());
			KnownTechnique bodyKnownTechnique = attackerCult.getTechniqueBySystem(CultivationLevel.System.BODY);
			if (bodyKnownTechnique != null && demonicTechniques.contains(bodyKnownTechnique.getTechnique())) {
				double amount = 20;
				bodyKnownTechnique.proficiency(amount);
				attackerCult.addBaseToSystem(amount / 10.0, CultivationLevel.System.BODY);
			}
			KnownTechnique divineKnownTechnique = attackerCult.getTechniqueBySystem(CultivationLevel.System.DIVINE);
			if (divineKnownTechnique != null && demonicTechniques.contains(divineKnownTechnique.getTechnique())) {
				double amount = 20;
				divineKnownTechnique.proficiency(amount);
				attackerCult.addBaseToSystem(amount / 10.0, CultivationLevel.System.DIVINE);
			}
			KnownTechnique essenceKnownTechnique = attackerCult.getTechniqueBySystem(CultivationLevel.System.ESSENCE);
			if (essenceKnownTechnique != null && demonicTechniques.contains(essenceKnownTechnique.getTechnique())) {
				double amount = 20;
				essenceKnownTechnique.proficiency(amount);
				attackerCult.addBaseToSystem(amount / 10.0, CultivationLevel.System.ESSENCE);
			}
		}
	}
}
