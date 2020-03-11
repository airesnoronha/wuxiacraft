package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.cultivation.skills.ISkillAction;
import com.airesnor.wuxiacraft.cultivation.techniques.Techniques;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;

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

	/**
	 * A test item, not in use right now
	 */
	public static final Item NATURAL_ODDITY_LOW = new ItemBase("natural_oddity_low").setCreativeTab(CreativeTabs.MATERIALS);

	//pellets
	public static final Item BODY_REFINEMENT_PILL = new ItemProgressPill("body_refinement_pill", 50f, 100);
	public static final Item ENERGY_RECOVERY_PILL = new ItemEnergyPill("energy_recovery_pill", 30f);
	public static final Item MINOR_HEALING_PILL = new ItemHealPill("minor_healing_pill", 10f);
	public static final Item MINOR_RECOVERY_PILL = new ItemEffectPill("minor_recovery_pill")
			.addEffect(new PotionEffect(MobEffects.REGENERATION,20*60*2, 0, true, true));
	public static final Item CAT_VISION_PILL = new ItemEffectPill("cat_vision_pill")
			.addEffect(new PotionEffect(MobEffects.NIGHT_VISION, 20*60*10, 0, false, true));
	public static final Item RUNNERS_ESSENCE_PILL = new ItemEffectPill("runners_essence_pill")
			.addEffect(new PotionEffect(MobEffects.SPEED, 20*60*10));
	public static final Item JUMPING_RABBIT_PILL = new ItemEffectPill("jumping_rabbit_pill")
			.addEffect(new PotionEffect(MobEffects.JUMP_BOOST, 20*60*2, 1, false, true));
	public static final Item BULL_RAGE_PILL = new ItemEffectPill("bull_rage_pill")
			.addEffect(new PotionEffect(MobEffects.STRENGTH, 20*60*2, 1, false, true));
	public static final Item FIRE_WALKER_PILL = new ItemEffectPill("fire_walker_pill")
			.addEffect(new PotionEffect(MobEffects.FIRE_RESISTANCE, 20*60*2, 1, false, true));
	public static final Item BIRD_GRACE_PILL = new ItemEffectPill("bird_grace_pill")
			.addEffect(new PotionEffect(MobEffects.LEVITATION, 20*60*5, 0, false, true));
	public static final Item TURTLE_SHELL_PILL = new ItemEffectPill("turtle_shell_pill")
			.addEffect(new PotionEffect(MobEffects.RESISTANCE, 20*60*2, 1, false, true));
	public static final Item FISH_SOUL_PILL = new ItemEffectPill("fish_soul_pill")
			.addEffect(new PotionEffect(MobEffects.WATER_BREATHING, 20*60*5, 0, true, true));
	public static final Item MINOR_FASTING_PILL = new ItemSkillPill("minor_fasting_pill")
			.setAction(new ISkillAction() {
				@Override
				public boolean activate(EntityPlayer actor) {
					actor.getFoodStats().setFoodLevel(20);
					actor.getFoodStats().setFoodSaturationLevel(50);
					return true;
				}
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

	//FANS
	public static final Item FEATHER_FAN = new ItemFan("feather_fan").setMaxStrength(10f).setStrength(1f);

}
