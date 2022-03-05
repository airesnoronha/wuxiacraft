package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.cultivation.Cultivation;
import com.lazydragonstudios.wuxiacraft.cultivation.System;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerElementalStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerStat;
import com.lazydragonstudios.wuxiacraft.cultivation.stats.PlayerSystemElementalStat;
import com.lazydragonstudios.wuxiacraft.cultivation.technique.aspects.*;
import com.lazydragonstudios.wuxiacraft.event.CultivatingEvent;
import com.lazydragonstudios.wuxiacraft.util.TechniqueUtil;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import com.lazydragonstudios.wuxiacraft.WuxiaCraft;

import java.math.BigDecimal;

@SuppressWarnings("unused")
public class WuxiaTechniqueAspects {

	public static DeferredRegister<TechniqueAspect> ASPECTS = DeferredRegister.create(TechniqueAspect.class, WuxiaCraft.MOD_ID);

	public static RegistryObject<TechniqueAspect> START = ASPECTS.register("start", StartAspect::new);

	/**
	 * this is an empty aspect should, should not even be mentioned
	 * but is there to serve as default for when the grid is empty and avoid null pointers
	 * plus it says it won't connect to anyone
	 */
	public static RegistryObject<TechniqueAspect> EMPTY = ASPECTS.register("aspect_empty",
			() -> new TechniqueAspect() {
				@Override
				public boolean canConnect(TechniqueAspect aspect) {
					return false;
				}
			}
	);

	//////////////////////////////////////////
	//           Gathering Nodes            //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> BODY_GATHERING = ASPECTS.register("body_gathering",
			() -> new SystemGather(System.BODY)
	);

	public static RegistryObject<TechniqueAspect> DIVINE_GATHERING = ASPECTS.register("divine_gathering",
			() -> new SystemGather(System.DIVINE)
	);

	public static RegistryObject<TechniqueAspect> ESSENCE_GATHERING = ASPECTS.register("essence_gathering",
			() -> new SystemGather(System.ESSENCE)
	);

	//////////////////////////////////////////
	//           Fire Generation ones       //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> CINDER = ASPECTS.register("cinder",
			() -> new ElementalGenerator(1d, WuxiaElements.FIRE.getId())
	);

	public static RegistryObject<TechniqueAspect> EMBER = ASPECTS.register("ember",
			() -> new ElementalGenerator(3d, WuxiaElements.FIRE.getId())
	);

	public static RegistryObject<TechniqueAspect> BLAZING = ASPECTS.register("blazing",
			() -> new ElementalGenerator(9d, WuxiaElements.FIRE.getId())
	);

	public static RegistryObject<TechniqueAspect> EARTH_SCORCHING = ASPECTS.register("earth_scorching",
			() -> new ElementalGenerator(27d, WuxiaElements.FIRE.getId())
	);

	public static RegistryObject<TechniqueAspect> HEAVEN_BURNING_FIRE = ASPECTS.register("heaven_burning_fire",
			() -> new ElementalGenerator(81d, WuxiaElements.FIRE.getId())
	);

	public static RegistryObject<TechniqueAspect> EVERLASTING_FLAME = ASPECTS.register("everlasting_flame",
			() -> new ElementalGenerator(243d, WuxiaElements.FIRE.getId())
	);

	public static RegistryObject<TechniqueAspect> SOLAR_FLAME = ASPECTS.register("solar_flame",
			() -> new ElementalGenerator(729d, WuxiaElements.FIRE.getId())
	);

	public static RegistryObject<TechniqueAspect> IMMORTAL_FLAME = ASPECTS.register("immortal_flame",
			() -> new ElementalGenerator(2187d, WuxiaElements.FIRE.getId())
	);

	public static RegistryObject<TechniqueAspect> FLAME_EMPEROR = ASPECTS.register("flame_emperor",
			() -> new ElementalGenerator(6561d, WuxiaElements.FIRE.getId())
	);

	//////////////////////////////////////////
	//       Fire Transformation ones       //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> SCORCH = ASPECTS.register("scorch",
			() -> new ElementSystemConverter(3d, WuxiaElements.FIRE.getId(), System.BODY)
	);

	//////////////////////////////////////////
	//           Water Generation ones      //
	//////////////////////////////////////////
	public static RegistryObject<TechniqueAspect> DROP = ASPECTS.register("drop",
			() -> new ElementalGenerator(1d, WuxiaElements.WATER.getId())
	);

	public static RegistryObject<TechniqueAspect> FLOW = ASPECTS.register("flow",
			() -> new ElementalGenerator(3d, WuxiaElements.WATER.getId())
	);

	public static RegistryObject<TechniqueAspect> WATERFALL = ASPECTS.register("waterfall",
			() -> new ElementalGenerator(9d, WuxiaElements.WATER.getId())
	);

	public static RegistryObject<TechniqueAspect> OCEAN_HEART = ASPECTS.register("ocean_heart",
			() -> new ElementalGenerator(27d, WuxiaElements.WATER.getId())
	);

	public static RegistryObject<TechniqueAspect> HEAVENLY_WATER = ASPECTS.register("heavenly_water",
			() -> new ElementalGenerator(81d, WuxiaElements.WATER.getId())
	);

	public static RegistryObject<TechniqueAspect> OCEAN_TIDE = ASPECTS.register("ocean_tide",
			() -> new ElementalGenerator(243d, WuxiaElements.WATER.getId())
	);

	public static RegistryObject<TechniqueAspect> LUNAR_WATER = ASPECTS.register("lunar_water",
			() -> new ElementalGenerator(729d, WuxiaElements.WATER.getId())
	);

	public static RegistryObject<TechniqueAspect> IMMORTAL_WATER = ASPECTS.register("immortal_water",
			() -> new ElementalGenerator(2187d, WuxiaElements.WATER.getId())
	);

	public static RegistryObject<TechniqueAspect> GOD_OF_WATER = ASPECTS.register("god_of_water",
			() -> new ElementalGenerator(6561d, WuxiaElements.WATER.getId())
	);

	//////////////////////////////////////////
	//       Wood Generation ones           //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> SEED = ASPECTS.register("seed",
			() -> new ElementalGenerator(1d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> MOSS = ASPECTS.register("moss",
			() -> new ElementalGenerator(3d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> SPROUT = ASPECTS.register("sprout",
			() -> new ElementalGenerator(9d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> HERB = ASPECTS.register("herb",
			() -> new ElementalGenerator(27d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> BUSH = ASPECTS.register("bush",
			() -> new ElementalGenerator(81d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> SAPLING = ASPECTS.register("sapling",
			() -> new ElementalGenerator(243d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> TREE = ASPECTS.register("tree",
			() -> new ElementalGenerator(729d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> ANCIENT_TREE = ASPECTS.register("ancient_tree",
			() -> new ElementalGenerator(2187d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> WORLD_TREE = ASPECTS.register("world_tree",
			() -> new ElementalGenerator(6561d, WuxiaElements.WOOD.getId())
	);

	//////////////////////////////////////////
	//       Metal Transformation ones       //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> ORE = ASPECTS.register("seed",
			() -> new ElementalGenerator(1d, WuxiaElements.METAL.getId())
	);

	public static RegistryObject<TechniqueAspect> METAL_NUGGET = ASPECTS.register("metal_nugget",
			() -> new ElementalGenerator(3d, WuxiaElements.METAL.getId())
	);

	public static RegistryObject<TechniqueAspect> METAL_INGOT = ASPECTS.register("metal_ingot",
			() -> new ElementalGenerator(9d, WuxiaElements.METAL.getId())
	);

	public static RegistryObject<TechniqueAspect> METAL_BLOCK = ASPECTS.register("metal_block",
			() -> new ElementalGenerator(27d, WuxiaElements.METAL.getId())
	);

	public static RegistryObject<TechniqueAspect> EARTHLY_METAL = ASPECTS.register("earthly_metal",
			() -> new ElementalGenerator(81d, WuxiaElements.METAL.getId())
	);

	public static RegistryObject<TechniqueAspect> HEAVENLY_METAL = ASPECTS.register("heavenly_metal",
			() -> new ElementalGenerator(243d, WuxiaElements.METAL.getId())
	);

	public static RegistryObject<TechniqueAspect> GODLY_METAL = ASPECTS.register("godly_metal",
			() -> new ElementalGenerator(729d, WuxiaElements.METAL.getId())
	);

	public static RegistryObject<TechniqueAspect> ANCIENT_METAL = ASPECTS.register("ancient_metal",
			() -> new ElementalGenerator(2187d, WuxiaElements.METAL.getId())
	);

	public static RegistryObject<TechniqueAspect> WORLD_METAL = ASPECTS.register("world_metal",
			() -> new ElementalGenerator(6561d, WuxiaElements.METAL.getId())
	);

	//////////////////////////////////////////
	//       Wood Generation ones           //
	//////////////////////////////////////////


	//////////////////////////////////////////
	//       Wooden Special ones            //
	//////////////////////////////////////////
	//TODO make this get stronger by adding a flower factor to it and convert this to special
	public static RegistryObject<TechniqueAspect> FLOWER = ASPECTS.register("flower",
			() -> new ElementalGenerator(1d, WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> LICHEN = ASPECTS.register("lichen",
			() -> new ElementToElementConverter(3d, 1.5d, WuxiaElements.WOOD.getId(), WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> STEM = ASPECTS.register("stem",
			() -> new ElementToElementConverter(5d, 1.7d, WuxiaElements.WOOD.getId(), WuxiaElements.WOOD.getId())
	);

	public static RegistryObject<TechniqueAspect> CHARCOAL = ASPECTS.register("charcoal",
			() -> new ElementToElementConverter(3d, 0.7d, WuxiaElements.WOOD.getId(), WuxiaElements.FIRE.getId())
	);

	//////////////////////////////////////////
	//       Lightning Generation ones      //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> SPARK = ASPECTS.register("spark",
			() -> new ElementalGenerator(1d, WuxiaElements.LIGHTNING.getId())
	);

	public static RegistryObject<TechniqueAspect> CIRCUIT = ASPECTS.register("circuit",
			() -> new ElementalGenerator(3d, WuxiaElements.LIGHTNING.getId())
	);

	public static RegistryObject<TechniqueAspect> THUNDERING = ASPECTS.register("thundering",
			() -> new ElementalGenerator(9d, WuxiaElements.LIGHTNING.getId())
	);

	public static RegistryObject<TechniqueAspect> EARTHEN_THUNDER = ASPECTS.register("earthen_thunder",
			() -> new ElementalGenerator(27d, WuxiaElements.LIGHTNING.getId())
	);

	public static RegistryObject<TechniqueAspect> HEAVENLY_LIGHTNING = ASPECTS.register("heavenly_lightning",
			() -> new ElementalGenerator(81d, WuxiaElements.LIGHTNING.getId())
	);

	public static RegistryObject<TechniqueAspect> IMMORTAL_STORM = ASPECTS.register("immortal_storm",
			() -> new ElementalGenerator(243d, WuxiaElements.LIGHTNING.getId())
	);

	public static RegistryObject<TechniqueAspect> ENDLESS_LIGHTNING = ASPECTS.register("endless_lightning",
			() -> new ElementalGenerator(729d, WuxiaElements.LIGHTNING.getId())
	);

	public static RegistryObject<TechniqueAspect> EMBODIMENT_OF_LIGHTNING = ASPECTS.register("embodiment_of_lightning",
			() -> new ElementalGenerator(2187d, WuxiaElements.LIGHTNING.getId())
	);

	public static RegistryObject<TechniqueAspect> GOD_OF_LIGHTNING = ASPECTS.register("god_of_lightning",
			() -> new ElementalGenerator(6561d, WuxiaElements.LIGHTNING.getId())
	);

	//////////////////////////////////////////
	//       Earth Special ones             //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> ROOT = ASPECTS.register("root",
			() -> new ElementToElementConverter(3d, 0.7d, WuxiaElements.FIRE.getId(), WuxiaElements.WOOD.getId())
	);

	//////////////////////////////////////////
	//       Space Special ones             //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> SPACE_TEARING = ASPECTS.register("space_tearing",
			() -> new ElementToSkillConsumer(WuxiaElements.SPACE.getId(), 10d, WuxiaSkillAspects.SPACE_TEAR_ASPECT.getId())
	);

	//////////////////////////////////////////
	//       Light Generation ones          //
	//////////////////////////////////////////
	public static RegistryObject<TechniqueAspect> STARRY_BATH = ASPECTS.register("starry_bath",
			() -> new ConditionalElementalGenerator(3d, WuxiaElements.LIGHT.getId()) {
				@Override
				public void onCultivate(CultivatingEvent event) {
					var timeOfDay = event.getPlayer().level.getDayTime();
					var techniqueData = Cultivation.get(event.getPlayer()).getSystemData(event.getSystem()).techniqueData;
					var grid = techniqueData.grid;
					int starryBathCount = 0;
					for (var aspect : grid.getGrid().values()) {
						if (aspect == STARRY_BATH.getId()) {
							starryBathCount++;
						}
					}
					var amount = event.getAmount();
					var modifier = new BigDecimal("1");
					if (timeOfDay > 15000 && timeOfDay < 18000) {
						// modifier = modifier +  starryBathCount * 0.5
						modifier = modifier.add(new BigDecimal(starryBathCount).multiply(new BigDecimal("0.5")));
					} else if (timeOfDay > 12000) {
						// modifier = modifier + starryBathCount * 0.2
						modifier = modifier.add(new BigDecimal(starryBathCount).multiply(new BigDecimal("0.2")));
					}
					event.setAmount(amount.multiply(modifier));
				}
			}
	);

	//////////////////////////////////////////
	//       Light Special ones             //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> LEAF = ASPECTS.register("leaf",
			() -> new ElementToElementConverter(3d, 0.7d, WuxiaElements.LIGHT.getId(), WuxiaElements.WOOD.getId())
	);

	//////////////////////////////////////////
	//       Neutral Generation ones        //
	//////////////////////////////////////////

	public static RegistryObject<TechniqueAspect> BLOOD_BURNING = ASPECTS.register("leaf",
			() -> new ConditionalElementalGenerator(2d, WuxiaElements.PHYSICAL.getId()) {
				@Override
				public void onCultivate(CultivatingEvent event) {
					var cultivation = Cultivation.get(event.getPlayer());
					var health = cultivation.getStat(PlayerStat.HEALTH);
					health = health.subtract(event.getAmount().multiply(new BigDecimal("0.7")));
					event.setAmount(event.getAmount().multiply(new BigDecimal("1.4")));
					cultivation.setStat(PlayerStat.HEALTH, health);
				}
			}
	);

	public static RegistryObject<TechniqueAspect> DEVOURING = ASPECTS.register("devouring",
			() -> new ConditionalElementalGenerator(1d, WuxiaElements.PHYSICAL.getId()) {
				@Override
				public void onCultivate(CultivatingEvent event) {
					var player = event.getPlayer();
					var cultivation = Cultivation.get(player);
					var systemData = cultivation.getSystemData(event.getSystem());
					var itemStack = player.getMainHandItem();
					var devourData = TechniqueUtil.getDevouringDataPerItem(itemStack.getItem());
					if (devourData.size() <= 0) return;
					var baseAmount = event.getAmount();
					for (var element : devourData.keySet()) {
						baseAmount = baseAmount.add(devourData.get(element));
						systemData.addStat(element, PlayerSystemElementalStat.FOUNDATION, baseAmount.multiply(new BigDecimal("0.1")));
						cultivation.addStat(element, PlayerElementalStat.COMPREHENSION, baseAmount.multiply(new BigDecimal("0.3")));
					}
					event.setAmount(baseAmount);
				}
			}
	);

	/*

devouring -> devours things to turn it into cultivation base. Probably gets different elements from this one
  image = image.empty

Author = @Vermilion Bird
space tearing -> creates a hole in space and connects to location through it
  image = image.empty

Author =  @Vermilion Bird
Aires — 01/23/2022
charcoal (fire_aspect) -> converts wood type cultivation base into fire base cultivation base
  image = image.empty
Aires — 01/23/2022
flower (wood aspect) -> gather wood cultivation base and gets a bonus when near flowers
  image = image.empty

Author = @Zigresho
snow (water aspect) -> gather water cultivation base and gets a bonus when freezing inside snow powder (probably greater than the next one) or standing on snow as well
  image = image.empty

Author = @Zigresho
metallic heart (metal) -> gather metal cultivation base when near metal blocks
  image = image.empty

Author = @[Dao of luck breeding] syn

beast comprehension -> tamed animals might give a raw soul cultivation base
  image = image.empty

Author = @[Dao of luck breeding] syn
Aires — 01/24/2022
enlightenment -> gets a small chance of getting enlightened that increases cult speed for a while
  image = image.empty
Aires — 01/26/2022
slaugther -> gets soul cultivation base from killing
  image = image.empty

Author = @Seteron
hungry for earth -> gets cultivation base by breaking blocks (chanced) and increases proficiency in breaking earth element blocks
  image = image.empty

Author = @[Ruler of Dragons] Wu Long
Aires — 01/28/2022
chronological return -> gets time cultivation base from continuously going back to the start of the session and repeatedly increasing the cultivation based gained from it. From outside (and to the player) you might just be cultivating at the same place. Cultivation base increases exponentially as long as you don't move and keeps cultivating
  image = image.empty

Author = @Everyone's Junior Sister
Aires — 02/27/2022
Aspect of Luck = Transforms raw cultivation base (the refined part) into luck.
  image = image.empty

Author = @[Dao of Forests] Mt Febian
Sound manipulation = Unlocks search abilities using sound
  image = image.empty

Author = @[Dao of Forests] Mt Febian
Heavenly Dao Bot
BOT
 — 02/28/2022
Rain Aspect
Converts a little bit of water cultivation base into water elemental pierce for attacks
Author
@[Dao of Forests] Mt Febian
Sigh Aspect
Uses body cultivation base to enhance sight
Author
@[Dao of Forests] Mt Febian
Poisoned Qi
Converts a little bit of cultivation base to make attacks poison on contact
Author
@[Dao of Forests] Mt Febian
Heavenly Dao Bot
BOT
 — 02/28/2022
Aspect of Stars
Uses stars energy to generate a little bit of raw body cultivation base, works better at night
Author
@Asura
Yin Yang Exchange
Generates raw cultivation base if there is a partner on the bed same bed while cultivating (Only works up to 1 partner)
Author
@[Dao of Retardism]Gavatron80
Yin Aspect Dual Cultivation
Yin counterpart of the Yin Yang Exchange, gives bonus to yin elements connected
Author
@Vermilion Bird
Yang Aspect Dual Cultivaiton
Yang counterpart of the Yin Yang Exchange, gives bonus to yang elements connected
Author
@Vermilion Bird
Aspect of chaos
Generate good amounts of cultivation base at the cost of foundation
Author
<@!338712089650790401>
Aspect of order
Generate good amounts of cultivation base if foundation is Strong
Author
<@!338712089650790401>
Primordial Chaos
Uses spatial energy from the outer chaos to generate cultivation base
Author
@Vermilion Bird
Demon Aspect
Uses raw cultivation base and turns it into demonic cultivation base
Author
@Vermilion Bird
Dragon Aspect
Uses body cultivation base and starts transforming body into dragon
Author
@Vermilion Bird
Phoenix Aspect
Uses body cultivation base and starts transforming body into Phoenix
Author
@Vermilion Bird
Tiger Aspect
Uses body cultivation base and starts transforming body into Tiger
Author
@Vermilion Bird
Tortoise Aspect
Uses body cultivation base and starts transforming body into Tortoise
Author
@Vermilion Bird
Qilin Aspect
Uses body cultivation base and starts transforming body into Qilin
Author
@Vermilion Bird
Fox Aspect
Uses body cultivation base and starts transforming body into Fox
Author
@Everyone's Junior Sister
Five elements conversion
Generates cultivation base from the most efficient way of the cycle of the five base elements
Author
<@!590116241319133204>
Alchemy Cultivation
Gain cultivation base from performing alchemy
Author
@Zigresho
Defiance
Gains demonic cultivation base from killing stronger foes
Author
@Zigresho
Constellation aspects
A plethora of aspects that can have a constellation linked that can even perhaps contain some law knowledge behind
Author
@Harvey
Enhancement
When paired with another generation aspect will enhance a that aspect
Author
@[Dao of Retardism]Gavatron80
Aspect of Steam
By using both fire and water cultivation base to practice in a form that they benefit each other in the form of steam
Author
<@!491663363676307466>
Aspect of Ice
By freezing water cultivation base, can be used to practice in a form that water will be frozen
Author
<@!491663363676307466>
Aspect of Tribulation
By comprehending tribulation energy, can use it to attack foes
Author
@[Dao of Retardism]Gavatron80
Vitality Aspect
Uses wooden cultivation base to add quick healing properties to the body, causing the a phenomenon that will seem like the practitioner has a great health
Author
@Asura
Healing
Uses wooden cultivation base to transform some of it into a powerful healing skill to aid allies
Author
@Asura
Vermilion Flames
By comprehending a super rare form of fire, the Vermilion Flames from the Vermilion Bird, allows you to generate that fire to cultivate with it
Author
@Vermilion Bird
Dryad Life Link
By comprehending a rare wooden law, you learn to link your life essence to a plant. As long as that plant exists, you'll exist, and the damage will be shared with that plant
Author
@Vermilion Bird
Blending Aspect
Allows to enhance stats gained by the elements in the body system by further blending with the elements in your body
Author
@[DSS Leader] MrEizy
Gravity Manipulation
By comprehending rare wave forms released by the earth element spatial form, the practitioner can alter the gravity in a certain region
Author
@Asura
Poising Coexistence
By absorbing poison, generates raw cultivation base
Author
@Ancient Devouring Beast
Existence Paradox Aspect
By comprehending a very rare part of some law, gives the practitioner the ability of temporarily fade into non existence
Author
<@!881552223971192844>
Life and Death Exchange
By comprehending a rare part of the wood law, allows to exchange the vitality of nearby living things to bring to life someone who almost died
Samsara Experience
By comprehending the reincarnation cycle of life and death, allows to pull someone from this cycle to continue living in this life
Author
@Vermilion Bird
Metallic Body Aspect
Consume random metals and increase body metallic nature
Author
@Zigresho
Crystallic Body Aspect
Consume random metals and increase body crystallic nature
Author
@Zigresho
Lightning Aspect
When struck by lightning gains a little of cultivation base
Author
@Zigresho
Lightning Circuit
By further understanding the lightning strikes, enables the practitioner to gain more cultivation base from Lightning Aspect (use both together)
Author
@Zigresho
Scholarly Aspect
Gains cultivation base from reading or writing books
Author
@Seteron
Bleeding edge
Enhance body cultivation base gain from being with low health
Author
@Seteron
Heavenly Dao Bot
BOT
 — 02/28/2022
Oneironautics
Gains cultivation base from cultivating inside of the dreams
Author
@Zigresho
Lay Down Roots
Passively lay down energy roots that allows for better energy recovery in the essence system, only works for essence system
Author
@Seteron
Boundless Sea
By using water comprehension on how water is in the nature, allows for the practitioner to use the same logic to store it's own energy and further increase max energy
Author
@Seteron
Heavenly Dao Bot
BOT
 — 02/28/2022
Aspect of training
A peculiar aspect that allows you to gain body cultivation base from physical activities the cultivator do
Author
@[Dao of Dimension] Blue Phoenix
Aspect of running
A peculiar aspect that allows the practitioner to have extra speed based on how much the practitioner have traversed on ground
Author
@[Dao of Dimension] Blue Phoenix
Undead Slayer
Gain stats for when nearby undead are detected and have increased cultivation base when undead detected undead are killed in the detection range
Author
@[Dao of Dimension] Blue Phoenix
Aspect of Clear Minded
Gains divine cultivation base from doing activities that make your mind clear in the mundane world, like fishing
Author
@Heavenly Fruit Deity IV
Heavenly Dao Bot
BOT
 — 02/28/2022
Aspect of Moss
Collects wooden energy from nature and transforms it into wooden cultivation base
Author
@[Dao of Forests] Mt Febian
Aspect of Lichen
When combined with other wood element generation aspect symbionts will further enhance the wooden energy, enhancing that aspect generated resources
Author
@[Dao of Forests] Mt Febian
Aspect of Stem
When combined with other wood element generation aspect will make the wood energy grow into stems, enhancing that aspect generated resources
Author
@[Dao of Forests] Mt Febian
Herbalist
Growing herbs will generate wooden cultivation base
Author
@papryk
Concept of Buddhism
Grants an increased soul cultivation base, but in return hurting living things will cause a loss of cultivation base and foundation
Author
@Seteron
Heavenly Dao Bot
BOT
 — 02/28/2022
Sword Affinity
Grants cultivation base from the comprehension of the sword
Author
@Zigresho
Heavenly Dao Bot
BOT
 — 03/02/2022
Cinder
Tier 1 Fire generation aspect = Gets fire energy from the environment and with it's cinders, transform that into cultivaiton base
Author
@Sapphire Dragon Emperor
Blaze
Tier 3 Fire generation aspect = Gets fire energy from the environment and make them blaze to transform that into cultivaiton base
Author
@Sapphire Dragon Emperor
	 */

}
