package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.blocks.TechniqueInscriber;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class WuxiaBlocks {

	public static DeferredRegister<Block> BLOCKS = DeferredRegister.create(Block.class, WuxiaCraft.MOD_ID);

	public static RegistryObject<Block> TECHNIQUE_INSCRIBER = BLOCKS.register("technique_inscriber",
			() -> new TechniqueInscriber(BlockBehaviour.Properties.of(Material.WOOD).strength(3f)));

}
