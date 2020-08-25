package com.airesnor.wuxiacraft.items;

import com.airesnor.wuxiacraft.cultivation.Cultivation;
import com.airesnor.wuxiacraft.cultivation.ICultivation;
import com.airesnor.wuxiacraft.cultivation.techniques.Technique;
import com.airesnor.wuxiacraft.cultivation.techniques.Techniques;
import com.airesnor.wuxiacraft.utils.CultivationUtils;
import net.minecraft.block.BlockPlanks;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;

import java.util.*;

@SuppressWarnings("unused")
public class WuxiaItems {

	/**
	 * Contains all the items to be registered
	 */
	public static final List<Item> ITEMS = new ArrayList<>();

	public static final CreativeTabs SCROLLS = new CreativeTabs("wuxiacraft.scrolls") {
		@Override
		public ItemStack getTabIconItem() {
			if(TECHNIQUES_SCROLL.size() > 0)
			return new ItemStack(TECHNIQUES_SCROLL.get(0));
			return new ItemStack(RECIPE_SCROLL);
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

	public static final CreativeTabs WUXIACRAFT_GENERAL = new CreativeTabs("wuxiacraft.general") {
		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(WuxiaItems.PRIMORDIAL_CHAOS_STONE);
		}
	};


	/**
	 * A test item, not in use right now
	 */
	public static final Item NATURAL_ODDITY_LOW = new ItemMonsterCore("natural_oddity_low").setUseDuration(100)
			.setWhenUsing(actor -> {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
				CultivationUtils.cultivatorAddProgress(actor, Cultivation.System.ESSENCE, 0.56874F, false, true);
				return true;
			})
			.setMaxStackSize(64).setCreativeTab(WUXIACRAFT_GENERAL);

	//pills
	public static final Item BODY_REFINEMENT_PILL = new ItemProgressPill("body_refinement_pill", Cultivation.System.BODY, 240f, 100);
	public static final Item TRAINING_PILL = new ItemProgressPill("training_pill", Cultivation.System.BODY, 480f, 150);
	public static final Item REINFORCEMENT_PILL = new ItemProgressPill("reinforcement_pill", Cultivation.System.BODY, 720f, 180);
	public static final Item BODY_CREATION_PILL = new ItemProgressPill("body_creation_pill", Cultivation.System.BODY, 960f, 200);
	public static final Item SOUL_EXPANSION_PILL = new ItemProgressPill("soul_expansion_pill", Cultivation.System.DIVINE, 240f, 100);
	public static final Item SOUL_REFINEMENT_PILL = new ItemProgressPill("soul_refinement_pill", Cultivation.System.DIVINE, 480f, 150);
	public static final Item SOUL_ASPECT_PILL = new ItemProgressPill("soul_aspect_pill", Cultivation.System.DIVINE, 720f, 180);
	public static final Item WANDERING_GHOST_PILL = new ItemProgressPill("wandering_ghost_pill", Cultivation.System.DIVINE, 960f, 200);
	public static final Item QI_FLOW_PILL = new ItemProgressPill("qi_flow_pill", Cultivation.System.ESSENCE, 240f, 100);
	public static final Item QI_PATHS_REFINEMENT_PILL = new ItemProgressPill("qi_paths_refinement_pill", Cultivation.System.ESSENCE, 480f, 150);
	public static final Item MERIDIAN_INJECTION_PILL = new ItemProgressPill("meridian_injection_pill", Cultivation.System.ESSENCE, 720f, 180);
	public static final Item DANTIAN_CONDENSING_PILL = new ItemProgressPill("dantian_condensing_pill", Cultivation.System.ESSENCE, 960f, 200);
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
	public static final Item MINOR_FASTING_PILL = new ItemFastingPill("minor_fasting_pill", 25f);
	public static final Item LESSER_FASTING_PILL = new ItemFastingPill("lesser_fasting_pill", 50f);
	public static final Item SMALLER_FASTING_PILL = new ItemFastingPill("smaller_fasting_pill", 100f);
	public static final Item SMALL_FASTING_PILL = new ItemFastingPill("small_fasting_pill", 200f);

	//Scrolls -- hopes this works
	public static final LinkedList<Item> TECHNIQUES_SCROLL = new LinkedList<>();
	static {
		for(Technique t : Techniques.TECHNIQUES)
		TECHNIQUES_SCROLL.add(new ItemScroll(t));
	}

	//FANS
	public static final Item FEATHER_FAN = new ItemFan("feather_fan").setMaxStrength(10f).setStrength(1f);

	//Monster cores
	public static final Item GIANT_ANT_CORE = new ItemMonsterCore("giant_ant_core").setUseDuration(100)
			.setWhenUsing(actor -> {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
				CultivationUtils.cultivatorAddProgress(actor, Cultivation.System.BODY, 0.45786f, false, true);
				return true;
			})
			.setUseAction(actor -> {
				if(!actor.world.isRemote) {
					actor.world.createExplosion(actor, actor.posX, actor.posY, actor.posZ, 5.0f, true);
				}
				actor.attackEntityFrom(DamageSource.causeExplosionDamage(actor), 120f);
				return true;
			});
	public static final Item GIANT_BEE_CORE = new ItemMonsterCore("giant_bee_core").setUseDuration(80)
			.setWhenUsing(actor -> {
				ICultivation cultivation = CultivationUtils.getCultivationFromEntity(actor);
				CultivationUtils.cultivatorAddProgress(actor, Cultivation.System.BODY, 0.45786f, false, true);
				return true;
			})
			.setUseAction(actor -> {
				PotionEffect effect = new PotionEffect(MobEffects.POISON, 20*60*5, 0, false, false);
				actor.addPotionEffect(effect);
				return true;
			});

	public static final Item RECIPE_SCROLL = new ItemRecipe("recipe_scroll");

	public static final Item WEAK_LIFE_STONE = new ItemSpiritStone("weak_life_stone", 0xC26060); //Body Refinement 1
	public static final Item SOUL_STONE = new ItemSpiritStone("soul_stone", 0x684893).setAmount(1.790); //Soul Refinement 1
	public static final Item PRIMORDIAL_STONE = new ItemSpiritStone("primordial_stone", 0x10EFFF).setAmount(2.260); //Soul Refinement 5
	public static final Item FIVE_ELEMENT_PURE_CRYSTAL = new ItemSpiritStone("five_element_pure_crystal", 0x2f5410).setAmount(3.399); //Qi Paths 1
	public static final Item PURE_QI_CRYSTAL = new ItemSpiritStone("pure_qi_crystal", 0xd3d3d3).setAmount(6.84); //Dantian 1
	public static final Item EARTH_LAW_CRYSTAL = new ItemSpiritStone("earth_law_crystal", 0x7d511c).setAmount(16.393); //Earth Law 1
	public static final Item SKY_LAW_CRYSTAL = new ItemSpiritStone("sky_law_crystal", 0x2a74b8).setAmount(39.288); //Sky Law 1
	public static final Item HEAVENLY_STONE = new ItemSpiritStone("heavenly_stone", 0x004d95).setAmount(59.075); //Sky Law 7
	public static final Item RAINBOW_LAW_STONE = new ItemSpiritStone("rainbow_law_stone", 0xa8bc71).setAmount(99.807); //True Law 1
	public static final Item SKY_AND_EARTH_CRYSTAL = new ItemSpiritStone("sky_and_earth_crystal", 0x746f71).setAmount(126.004); // True Law 5
	public static final Item LAW_NEXUS_STONE = new ItemSpiritStone("law_nexus_stone", 0xa8b48e).setAmount(268.759); // Martial Law 1
	public static final Item WAR_CRYSTAL = new ItemSpiritStone("war_crystal", 0x4f2051).setAmount(540.795); // Martial Law 12
	public static final Item GOLD_SPIRIT_STONE = new ItemSpiritStone("gold_spirit_stone", 0xe1f061).setAmount(861.946); //Immortality Law 1
	public static final Item YIN_YANG_STONE = new ItemSpiritStone("yin_yang_stone", 0x1e1c1c).setAmount(2764.379); // Immortality Refinement 1
	public static final Item TRANSCENDENT_CRYSTAL = new ItemSpiritStone("transcendent_crystal", 0x41e93d).setAmount(9397.684); // Immortal Foundation 1
	public static final Item IMMORTALITY_STONE = new ItemSpiritStone("immortality_stone", 0x8437c5).setAmount(33864.906); // True Immortal 1
	public static final Item ASCENDED_IMMORTALITY_STONE = new ItemSpiritStone("ascended_immortality_stone", 0xb88cc5).setAmount(50920.298); // True Immortal 7
	public static final Item IMMORTAL_WILL_STONE = new ItemSpiritStone("immortal_will_stone", 0xadb7d8).setAmount(145343.801); // Martial Immortal 1
	public static final Item STELLAR_STONE = new ItemSpiritStone("stellar_stone", 0x010101).setAmount(260288.611); // Martial Immortal 11
	public static final Item DIVINE_ORIGIN_STONE = new ItemSpiritStone("divine_origin_stone", 0x785767).setAmount(623796.804); // Divine Law 1
	public static final Item BOUNDLESS_VOID_CRYSTAL = new ItemSpiritStone("boundless_void_crystal", 0x2a0545).setAmount(1410344.662); // Divine Law 15
	public static final Item FALLEN_STAR_CORE = new ItemSpiritStone("fallen_star_core", 0x3e0404).setAmount(2837890.554); // Divine Phenomenon 1
	public static final Item PRIMORDIAL_CHAOS_STONE = new ItemSpiritStone("primordial_chaos_stone", 0xe4de1c).setAmount(13685289.995); // True God 1

	public static final Item SPAWN_TALISMAN = new ItemSpawnTalisman("spawn_talisman");
	public static final Item PAINT_BRUSH = new ItemPaintBrush("paint_brush");
	public static final Item GOLD_DAGGER = new ItemDagger("gold_dagger");
	public static final Item BLOOD_BOTTLE = new ItemBloodContainer("blood_bottle");
	public static final Item EMPTY_BOTTLE = new ItemBase("empty_bottle").setMaxStackSize(16);
	public static final Item PAINT_BOTTLE = new ItemBase("paint_bottle").setMaxStackSize(1).setMaxDamage(100);

	//Training posts
	public static final Map<String, Item> TRAINING_POSTS = new HashMap<>();
	static {
		List<String> tiers =new ArrayList<>();
		tiers.add("stick");
		tiers.add("stone");
		tiers.add("iron");
		tiers.add("diamond");
		for(BlockPlanks.EnumType wood: BlockPlanks.EnumType.values()) {
			for(String tier : tiers) {
				String name = "training_post_"+wood.getName()+"_"+tier;
				TRAINING_POSTS.put(name, new ItemTrainingPost(name));
			}
		}
	}

	public static final Item TINY_DUST_IRON = new ItemBase("tiny_dust_iron");
	public static final Item TINY_DUST_GOLD = new ItemBase("tiny_dust_gold");
	public static final Item TINY_DUST_DIAMOND = new ItemBase("tiny_dust_diamond");
	public static final Item DUST_IRON = new ItemBase("dust_iron");
	public static final Item DUST_GOLD = new ItemBase("dust_gold");
	public static final Item DUST_DIAMOND = new ItemBase("dust_diamond");

	public static final Item SPATIAL_STONE = new ItemSpatialRing("spatial_stone", 1, 5);

	// Spatial Rings
	public static final Item LOW_GRADE_SPATIAL_RING = new ItemSpatialRing("low_grade_spatial_ring", 3, 9);
	public static final Item MID_GRADE_SPATIAL_RING = new ItemSpatialRing("mid_grade_spatial_ring", 4, 10);
	public static final Item HIGH_GRADE_SPATIAL_RING = new ItemSpatialRing("high_grade_spatial_ring", 5, 11);
	public static final Item PEAK_GRADE_SPATIAL_RING = new ItemSpatialRing("peak_grade_spatial_ring", 6, 12);
	public static final Item TOP_GRADE_SPATIAL_RING = new ItemSpatialRing("top_grade_spatial_ring", 7, 13);
	public static final Item SUPREME_GRADE_SPATIAL_RING = new ItemSpatialRing("supreme_grade_spatial_ring", 8, 14);
}
