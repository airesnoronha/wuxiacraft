package com.lazydragonstudios.wuxiacraft.init;

import com.lazydragonstudios.wuxiacraft.WuxiaCraft;
import com.lazydragonstudios.wuxiacraft.crafting.RuneMakingRecipe;
import com.lazydragonstudios.wuxiacraft.crafting.RuneMakingRecipeSerializer;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class WuxiaRecipeTypes {

	public static DeferredRegister<RecipeSerializer<?>> RECIPE_TYPE_SERIALIZERS = DeferredRegister.create(ForgeRegistries.RECIPE_SERIALIZERS, WuxiaCraft.MOD_ID);

	public static RegistryObject<RecipeSerializer<RuneMakingRecipe>> RUNEMAKING_SERIALIZER = RECIPE_TYPE_SERIALIZERS
			.register("runemaking", RuneMakingRecipeSerializer::new);

}
