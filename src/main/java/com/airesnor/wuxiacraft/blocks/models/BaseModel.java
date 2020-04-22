package com.airesnor.wuxiacraft.blocks.models;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.blocks.OBJBlockModelLoader;
import com.google.common.collect.ImmutableMap;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.common.model.IModelState;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class BaseModel implements IModel {

	private final ModelResourceLocation modelLocation;

	public BaseModel(String ModelName) {
		this.modelLocation = new ModelResourceLocation(WuxiaCraft.MOD_ID + ":models/block/" + ModelName + ".obj");
		WuxiaCraft.logger.info("Found a model:" + ModelName);
	}

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format, Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		try {
			ImmutableMap.Builder<String, TextureAtlasSprite> builder = ImmutableMap.builder();
			builder.put(ModelLoader.White.LOCATION.toString(), ModelLoader.White.INSTANCE);

			IModel preModel = OBJBlockModelLoader.INSTANCE.parseOBJ(modelLocation);
			return new BaseBakedModel(preModel.bake(state, format, bakedTextureGetter));
		} catch (Exception e) {
			System.err.println("Obj .bake() failed due to exception:" + e);
			return ModelLoaderRegistry.getMissingModel().bake(state, format, bakedTextureGetter);
		}
	}

	public final Map<ModelResourceLocation, OBJModel> cache = new HashMap<>();

}
