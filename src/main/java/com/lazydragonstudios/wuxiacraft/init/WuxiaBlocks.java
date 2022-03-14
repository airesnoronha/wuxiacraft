package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.blocks.FormationCoreBlock;
import com.lazydragonstudios.wuxiacraft.blocks.TechniqueInscriber;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class WuxiaBlocks {

	public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(Block.class, WuxiaCraft.MOD_ID);

	public static RegistryObject<Block> TECHNIQUE_INSCRIBER = BLOCKS.register("technique_inscriber",
			() -> new TechniqueInscriber(BlockBehaviour.Properties.of(Material.WOOD).strength(3f)));

	public static RegistryObject<Block> WOOD_FORMATION_CORE = BLOCKS.register("iron_formation_core",
			() -> new FormationCoreBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f)));

	public static RegistryObject<Block> IRON_FORMATION_CORE = BLOCKS.register("iron_formation_core",
			() -> new FormationCoreBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f)));

	public static RegistryObject<Block> _FORMATION_CORE = BLOCKS.register("iron_formation_core",
			() -> new FormationCoreBlock(BlockBehaviour.Properties.of(Material.METAL).strength(2f)));

}
