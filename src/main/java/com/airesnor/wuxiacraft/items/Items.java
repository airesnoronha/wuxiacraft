package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillAction;
import com.airesnor.wuxiacraft.cultivation.techniques.Techniques;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

import java.util.ArrayList;
import java.util.List;

public class Items {

	/**
	 * Contains all the items to be registered
	 */
	public static final List<Item> ITEMS = new ArrayList<>();

	public static final CreativeTabs SCROLLS = new CreativeTabs("wuxiacraft.scrolls") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(BODY_STRENGTH_SCROLL);
		}
	};

	public static final CreativeTabs MONSTER_CORES = new CreativeTabs("wuxiacraft.cores") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(GIANT_ANT_CORE);
		}
	};

	public static final CreativeTabs PILLS = new CreativeTabs("wuxiacraft.pills") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(TRAINING_PILL);
		}
	};


	/**
	 * A test item, not in use right now
	 */
	public static final Item NATURAL_ODDITY_LOW = new ItemMonsterCore("natural_oddity_low").setUseDuration(100)
			.setWhenUsing(actor -> {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
				CultivationUtils.cultivatorAddProgress(actor, cultivation, 0.56874F);
				return true;
			});

	//pills
	public static final Item BODY_REFINEMENT_PILL = new ItemProgressPill("body_refinement_pill", 50f, 200);
	public static final Item TRAINING_PILL = new ItemProgressPill("training_pill", 60f, 250);
	public static final Item REINFORCEMENT_PILL = new ItemProgressPill("reinforcement_pill", 70f, 300);
	public static final Item BODY_CREATION_PILL = new ItemProgressPill("body_creation_pill", 80f, 350);
	public static final Item SOUL_REFINEMENT_PILL = new ItemProgressPill("soul_refinement_pill", 89.54f, 220);
	public static final Item SOUL_ASPECT_PILL = new ItemProgressPill("soul_aspect_pill", 110f, 380);
	public static final Item WANDERING_GHOST_PILL = new ItemProgressPill("wandering_ghost_pill", 140f, 440);
	public static final Item QI_PATHS_REFINEMENT_PILL = new ItemProgressPill("qi_paths_refinement_pill", 169.97f, 240);
	public static final Item MERIDIAN_INJECTION_PILL = new ItemProgressPill("meridian_injection_pill", 200f, 360);
	public static final Item DANTIAN_CONDENSING_PILL = new ItemProgressPill("dantian_condensing_pill", 342f, 320);
	public static final Item ENERGY_RECOVERY_PILL = new ItemEnergyPill("energy_recovery_pill", 30f);
	public static final Item MINOR_ENERGY_REPLENISHING_PILL = new ItemEnergyPill("minor_energy_replenishing_pill", 50f);
	public static final Item ENERGY_STREAM_PILL = new ItemEnergyPill("energy_stream_pill", 80f);
	public static final Item LESSER_ENERGY_REPLENISHING_PILL = new ItemEnergyPill("lesser_energy_replenishing_pill", 110f);
	public static final Item EARTH_QI_PILL = new ItemEnergyPill("earth_qi_pill", 140f);
	public static final Item SMALLER_ENERGY_REPLENISHING_PILL = new ItemEnergyPill("smaller_energy_replenishing_pill", 180f);
	public static final Item HEAVEN_QI_PILL = new ItemEnergyPill("heaven_qi_pill", 220f);
	public static final Item SMALL_ENERGY_REPLENISHING_PILL = new ItemEnergyPill("small_energy_replenishing_pill", 260f);
	public static final Item ENERGY_GATHERING_PILL = new ItemEnergyPill("energy_gathering_pill", 340f);
	public static final Item MINOR_HEALING_PILL = new ItemHealPill("minor_healing_pill", 10f);
	public static final Item INVIGORATE_BLOOD_PILL = new ItemHealPill("invigorate_blood_pill", 20f);
	public static final Item LESSER_HEALING_PILL = new ItemHealPill("lesser_healing_pill", 40f);
	public static final Item RECONSTRUCT_BODY_PILL = new ItemHealPill("reconstruct_body_pill", 50f);
	public static final Item SMALLER_HEALING_PILL = new ItemHealPill("smaller_healing_pill", 90f);
	public static final Item HEALTH_INCARNATE_PILL = new ItemHealPill("health_incarnate_pill", 110f);
	public static final Item PHYSICIAN_MIRACLE_PILL = new ItemHealPill("physician_miracle_pill", 140f);
	public static final Item SMALL_HEALING_PILL = new ItemHealPill("small_healing_pill", 150f);
	public static final Item ALMOST_RESURRECT_PILL = new ItemHealPill("almost_resurrect_pill", 180f);
	public static final Item MINOR_RECOVERY_PILL = new ItemEffectPill("minor_recovery_pill",5f)
			.addEffect(new PotionEffect(MobEffects.REGENERATION, 20 * 60 * 2, 0, true, true));
	public static final Item CAT_VISION_PILL = new ItemEffectPill("cat_vision_pill", 5f)
			.addEffect(new PotionEffect(MobEffects.NIGHT_VISION, 20 * 60 * 10, 0, false, true));
	public static final Item RUNNERS_ESSENCE_PILL = new ItemEffectPill("runners_essence_pill", 5f)
			.addEffect(new PotionEffect(MobEffects.SPEED, 20 * 60 * 10));
	public static final Item JUMPING_RABBIT_PILL = new ItemEffectPill("jumping_rabbit_pill", 5f)
			.addEffect(new PotionEffect(MobEffects.JUMP_BOOST, 20 * 60 * 2, 1, false, true));
	public static final Item BULL_RAGE_PILL = new ItemEffectPill("bull_rage_pill", 5f)
			.addEffect(new PotionEffect(MobEffects.STRENGTH, 20 * 60 * 2, 1, false, true));
	public static final Item FIRE_WALKER_PILL = new ItemEffectPill("fire_walker_pill", 5f)
			.addEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 20 * 60 * 2, 1, false, true));
	public static final Item BIRD_GRACE_PILL = new ItemEffectPill("bird_grace_pill", 5f)
			.addEffect(new PotionEffect(MobEffects.LEVITATION, 20 * 60 * 5, 0, false, true));
	public static final Item TURTLE_SHELL_PILL = new ItemEffectPill("turtle_shell_pill", 5f)
			.addEffect(new PotionEffect(MobEffects.RESISTANCE, 20 * 60 * 2, 1, false, true));
	public static final Item FISH_SOUL_PILL = new ItemEffectPill("fish_soul_pill", 5f)
			.addEffect(new PotionEffect(MobEffects.WATER_BREATHING, 20 * 60 * 5, 0, true, true));
	public static final Item MINOR_FASTING_PILL = new ItemSkillPill("minor_fasting_pill", 5f)
			.setAction(actor -> {
				if(actor instanceof EntityPlayer) {
					EntityPlayer player = (EntityPlayer)actor;
					player.getFoodStats().setFoodLevel(20);
					player.getFoodStats().setFoodSaturationLevel(50);
					return true;
				} else return false;
			});

	//Scrolls
	public static final Item BODY_STRENGTH_SCROLL = new ItemScroll(Techniques.BODY_STRENGTH);
	public static final Item LIGHT_FEET_SCROLL = new ItemScroll(Techniques.LIGHT_FEET);
	public static final Item ASSASSIN_MANUAL_SCROLL = new ItemScroll(Techniques.ASSASSIN_MANUAL);
	public static final Item BASIC_MEDICINE_SCROLL = new ItemScroll(Techniques.BASIC_MEDICINE);
	public static final Item SWORD_HEART_SCROLL = new ItemScroll(Techniques.SWORD_HEART);
	public static final Item AXE_RAGE_SCROLL = new ItemScroll(Techniques.AXE_RAGE);
	public static final Item FIRE_BENDING_SCROLL = new ItemScroll(Techniques.FIRE_BENDING);
	public static final Item MOUNTAIN_RAISER_SCROLL = new ItemScroll(Techniques.MOUNTAIN_RAISER);
	public static final Item METAL_MANIPULATION_SCROLL = new ItemScroll(Techniques.METAL_MANIPULATION);
	public static final Item SURGING_WAVES_SCROLL = new ItemScroll(Techniques.SURGING_WAVES);
	public static final Item BOTANICAL_GROWTH_SCROLL = new ItemScroll(Techniques.BOTANICAL_GROWTH);
	public static final Item SWORD_FLIGHT_JOURNAL_SCROLL = new ItemScroll(Techniques.SWORD_FLIGHT_JOURNAL);

	//FANS
	public static final Item FEATHER_FAN = new ItemFan("feather_fan").setMaxStrength(10f).setStrength(1f);

	//Monster cores
	public static final Item GIANT_ANT_CORE = new ItemMonsterCore("giant_ant_core").setUseDuration(100)
			.setWhenUsing(actor -> {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
				CultivationUtils.cultivatorAddProgress(actor, cultivation, 0.45786f);
				return true;
			})
			.setUseAction(actor -> {
				if(!actor.world.isRemote) {
					actor.world.createExplosion(actor, actor.posX, actor.posY, actor.posZ, 5.0f, true);
				}
				actor.attackEntityFrom(DamageSource.causeExplosionDamage(actor), 120f);
				return true;
			});

	public static final Item RECIPE_SCROLL = new ItemRecipe("recipe_scroll");

}
