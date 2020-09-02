package com.airesnor.wuxiacraft.proxy;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.aura.IAuraCap;
import com.airesnor.wuxiacraft.capabilities.*;
import com.airesnor.wuxiacraft.config.WuxiaCraftConfig;
import com.airesnor.wuxiacraft.cultivation.*;
import com.airesnor.wuxiacraft.cultivation.elements.Element;
import com.airesnor.wuxiacraft.cultivation.skills.ISkillCap;
import com.airesnor.wuxiacraft.cultivation.skills.Skills;
import com.airesnor.wuxiacraft.cultivation.techniques.ICultTech;
import com.airesnor.wuxiacraft.cultivation.techniques.Techniques;
import com.airesnor.wuxiacraft.world.dimensions.WuxiaDimensions;
import com.airesnor.wuxiacraft.world.dimensions.biomes.WuxiaBiomes;
import com.airesnor.wuxiacraft.world.dimensions.worldtypes.WorldTypeRegister;
import com.airesnor.wuxiacraft.formation.FormationEventHandler;
import com.airesnor.wuxiacraft.handlers.EventHandler;
import com.airesnor.wuxiacraft.handlers.GuiHandler;
import com.airesnor.wuxiacraft.handlers.RegistryHandler;
import com.airesnor.wuxiacraft.networking.*;
import com.airesnor.wuxiacraft.utils.FormationUtils;
import com.airesnor.wuxiacraft.utils.OreUtils;
import com.airesnor.wuxiacraft.world.WorldGen;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

public class CommonProxy {

	/**
	 * Does in the server so this method is empty because server doesn't need textures i guess
	 *
	 * @param item The item to be rendered
	 * @param meta The item meta to be rendered
	 * @param id the block state variation
	 */
	public void registerItemRenderer(Item item, int meta, String id) {}

	private static int ServerSideMessageID = 167000;
	private static int ClientSideMessageID = 168000;

	private static int nextServerMessageID() {
		return ServerSideMessageID++;
	}

	private static int nextClientMessageID() {
		return ClientSideMessageID++;
	}

	public void init() {

		OreUtils.loadOresToFind();

		CapabilityManager.INSTANCE.register(ICultivation.class, new CultivationStorage(), new CultivationFactory());
		CapabilityManager.INSTANCE.register(ICultTech.class, new CultTechStorage(), new CultTechFactory());
		CapabilityManager.INSTANCE.register(ISkillCap.class, new SkillsStorage(), new SkillsFactory());
		CapabilityManager.INSTANCE.register(ISealing.class, new SealingStorage(), new SealingFactory());
		CapabilityManager.INSTANCE.register(IBarrier.class, new BarrierStorage(), new BarrierFactory());
		CapabilityManager.INSTANCE.register(IAuraCap.class, new AuraCapStorage(), new AuraCapFactory());

		NetworkWrapper.INSTANCE.registerMessage(ActivatePartialSkillMessage.Handler.class, ActivatePartialSkillMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(ActivateSkillMessage.Handler.class, ActivateSkillMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(AddRecipeItemMessage.Handler.class, AddRecipeItemMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(AskCultivationLevelMessage.Handler.class, AskCultivationLevelMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(CastSkillMessage.Handler.class, CastSkillMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(EnergyMessage.Handler.class, EnergyMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(ProgressMessage.Handler.class, ProgressMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(RemoveTechniqueMessage.Handler.class, RemoveTechniqueMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(RequestCultGuiMessage.Handler.class, RequestCultGuiMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(SelectSkillMessage.Handler.class, SelectSkillMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(ShrinkEntityItemMessage.Handler.class, ShrinkEntityItemMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(SkillCapMessage.Handler.class, SkillCapMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(SpawnParticleMessage.Handler.class, SpawnParticleMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(SpeedHandicapMessage.Handler.class, SpeedHandicapMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(SuppressCultivationMessage.Handler.class, SuppressCultivationMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(AddProgressToFoundationAttributeMessage.Handler.class, AddProgressToFoundationAttributeMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(SelectFoundationAttributeMessage.Handler.class, SelectFoundationAttributeMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(CapabilityRequestMessage.Handler.class, CapabilityRequestMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(BarrierMessage.Handler.class, BarrierMessage.class, nextServerMessageID(), Side.SERVER);
		NetworkWrapper.INSTANCE.registerMessage(RequestAuraForOtherPlayerMessage.Handler.class, RequestAuraForOtherPlayerMessage.class, nextServerMessageID(), Side.SERVER);

		NetworkWrapper.INSTANCE.registerMessage(CultivationMessage.Handler.class, CultivationMessage.class, nextClientMessageID(), Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(CultTechMessage.Handler.class, CultTechMessage.class, nextClientMessageID(), Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(RespondCultivationLevelMessage.Handler.class, RespondCultivationLevelMessage.class, nextClientMessageID(), Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(SkillCapMessage.Handler.class, SkillCapMessage.class, nextClientMessageID(), Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(SpawnParticleMessage.Handler.class, SpawnParticleMessage.class, nextClientMessageID(), Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(SpeedHandicapMessage.Handler.class, SpeedHandicapMessage.class, nextClientMessageID(), Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(UnifiedCapabilitySyncMessage.Handler.class, UnifiedCapabilitySyncMessage.class, nextClientMessageID(), Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(CultivationLevelsMessage.Handler.class, CultivationLevelsMessage.class, nextClientMessageID(), Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(BarrierMessage.Handler.class, BarrierMessage.class, nextClientMessageID(), Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(SpawnEntityOnClientMessage.Handler.class, SpawnEntityOnClientMessage.class, nextClientMessageID(), Side.CLIENT);
		NetworkWrapper.INSTANCE.registerMessage(RespondAuraForOtherPlayerMessage.Handler.class, RespondAuraForOtherPlayerMessage.class, nextClientMessageID(), Side.CLIENT);

		MinecraftForge.EVENT_BUS.register(new CapabilitiesHandler());
		MinecraftForge.EVENT_BUS.register(new EventHandler());
		MinecraftForge.EVENT_BUS.register(new FormationEventHandler());

		NetworkRegistry.INSTANCE.registerGuiHandler(WuxiaCraft.instance, new GuiHandler());

		Techniques.init();
		Element.init();
		Skills.init();
		FormationUtils.init();

		RegistryHandler.registerSmeltingRecipes();

		WorldTypeRegister.registerWorldTypes();
	}

	public void preInit() {

		BaseSystemLevel.initializeLevels();
		Techniques.init();

		MinecraftForge.EVENT_BUS.register(new RegistryHandler());

		reflectOnField(SharedMonsterAttributes.class, new String[]{"MAX_HEALTH", "field_111267_a"}, (new RangedAttribute(null, "generic.maxHealth", 20.0D, Float.MIN_VALUE, 1000000000000000.0D)).setDescription("Max Health").setShouldWatch(true));
		reflectOnField(SharedMonsterAttributes.class, new String[]{"ARMOR", "field_188791_g"}, (new RangedAttribute(null, "generic.armor", 0.0D, 0.0D, 100000000000000.0D)).setShouldWatch(true));
		reflectOnField(SharedMonsterAttributes.class, new String[]{"MOVEMENT_SPEED", "field_111263_d"}, (new RangedAttribute(null, "generic.movementSpeed", 0.699999988079071D, 0.0D, 4096.0D)).setDescription("Movement Speed").setShouldWatch(true));
		reflectOnField(SharedMonsterAttributes.class, new String[]{"ATTACK_DAMAGE", "field_111264_e"}, new RangedAttribute(null, "generic.attackDamage", 2.0D, 0.0D, 100000000000000.0D));
		reflectOnField(SharedMonsterAttributes.class, new String[]{"ATTACK_SPEED", "field_188790_f"}, (new RangedAttribute(null, "generic.attackSpeed", 4.0D, 0.0D, 100000000000000.0D)).setShouldWatch(true));

		WuxiaCraftConfig.preInit();
		GameRegistry.registerWorldGenerator(new WorldGen(), 3);
		registerRuneBlocks();
		WuxiaBiomes.registerBiomes();
		WuxiaDimensions.registerDimensions();
	}

	private void registerRuneBlocks() {
		//No more blood runes
		//WuxiaBlocks.initBloodRunes();
	}

	public void registerScrollModel(Item item, int meta, String id) {
	}

	public void registerCustomModelLocation(Item item, int meta, String id, String location) {
	}

	public void reflectOnField(Class<?> clazz, String [] names, Object newValue) {
		try {
			Field mf = Field.class.getDeclaredField("modifiers");
			mf.setAccessible(true);

			Field f = ReflectionHelper.findField(clazz, names);
			f.setAccessible(true);

			mf.set(f, f.getModifiers() & ~Modifier.FINAL);
			f.set(null, newValue);


			mf.set(f, f.getModifiers() & Modifier.FINAL);

			WuxiaCraft.logger.info("Overriding field "+names[0]+" succeeded.");
		} catch (Exception e) {
			WuxiaCraft.logger.error("Error overriding field '"+names[0]+"': " + e.getMessage());
			e.printStackTrace();
		}
	}
}
