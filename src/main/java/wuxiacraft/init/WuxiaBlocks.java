package wuxiacraft.init;

import net.minecraft.block.Block;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import wuxiacraft.WuxiaCraft;

public class WuxiaBlocks {

	public static final DeferredRegister<Block> WUXIA_BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, WuxiaCraft.MOD_ID);


	public static void register() {
		WUXIA_BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
		
	}
}
