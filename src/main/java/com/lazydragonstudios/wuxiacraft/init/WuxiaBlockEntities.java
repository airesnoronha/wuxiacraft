package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.blocks.entity.FormationCore;
import com.lazydragonstudios.wuxiacraft.blocks.entity.InscriberEntity;
import com.lazydragonstudios.wuxiacraft.blocks.entity.RunemakingTable;
import net.minecraft.Util;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("ConstantConditions")
public class WuxiaBlockEntities {

	public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITIES, WuxiaCraft.MOD_ID);

	public static RegistryObject<BlockEntityType<InscriberEntity>> INSCRIBER_TYPE = BLOCK_ENTITIES.register("inscriber_type",
			() -> BlockEntityType.Builder
					.of(InscriberEntity::new, WuxiaBlocks.TECHNIQUE_INSCRIBER.get())
					.build(Util.fetchChoiceType(References.BLOCK_ENTITY, "inscriber_type"))
	);

	public static RegistryObject<BlockEntityType<RunemakingTable>> RUNEMAKING_TABLE_TYPE = BLOCK_ENTITIES.register("runemaking_table_type",
			() -> BlockEntityType.Builder
					.of(RunemakingTable::new, WuxiaBlocks.RUNEMAKING_TABLE.get())
					.build(Util.fetchChoiceType(References.BLOCK_ENTITY, "runemaking_table_type"))
	);

	public static RegistryObject<BlockEntityType<FormationCore>> FORMATION_CORE = BLOCK_ENTITIES.register("formation_core",
			() -> BlockEntityType.Builder
					.of(FormationCore::new,
							WuxiaBlocks.OAK_FORMATION_CORE.get(),
							WuxiaBlocks.BIRCH_FORMATION_CORE.get(),
							WuxiaBlocks.ACACIA_FORMATION_CORE.get(),
							WuxiaBlocks.JUNGLE_FORMATION_CORE.get(),
							WuxiaBlocks.SPRUCE_FORMATION_CORE.get(),
							WuxiaBlocks.DARK_OAK_FORMATION_CORE.get(),
							WuxiaBlocks.STONE_FORMATION_CORE.get(),
							WuxiaBlocks.COPPER_FORMATION_CORE.get(),
							WuxiaBlocks.IRON_FORMATION_CORE.get(),
							WuxiaBlocks.GOLD_FORMATION_CORE.get(),
							WuxiaBlocks.LAPIS_FORMATION_CORE.get(),
							WuxiaBlocks.DIAMOND_FORMATION_CORE.get(),
							WuxiaBlocks.EMERALD_FORMATION_CORE.get())
					.build(Util.fetchChoiceType(References.BLOCK_ENTITY, "formation_core"))
			);

}
