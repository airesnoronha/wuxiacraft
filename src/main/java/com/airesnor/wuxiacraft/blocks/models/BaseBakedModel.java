package com.airesnor.wuxiacraft.blocks.models;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import java.util.List;

public class BaseBakedModel implements IBakedModel {

    private IBakedModel model;

    public BaseBakedModel(IBakedModel model) {
        this.model = model;
    }

    @Override
    public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
      ItemCameraTransforms itemCameraTransforms = model.getItemCameraTransforms();
      ItemTransformVec3f itemTransformVec3f = itemCameraTransforms.getTransform(cameraTransformType);
      TRSRTransformation tr = new TRSRTransformation(itemTransformVec3f);
      Matrix4f mat = null;
      if (tr != null) { // && tr != TRSRTransformation.identity()) {
        mat = tr.getMatrix();
      }

      return Pair.of(this, mat);
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable IBlockState state, @Nullable EnumFacing side, long rand) {
        return model.getQuads(state, side, rand);
    }

    @Override
    public boolean isAmbientOcclusion() {
        return model.isAmbientOcclusion();
    }

    @Override
    public boolean isGui3d() {
        return model.isGui3d();
    }

    @Override
    public boolean isBuiltInRenderer() {
        return model.isBuiltInRenderer();
    }

    @Override
    public TextureAtlasSprite getParticleTexture() {
        return model.getParticleTexture();
    }

    @Override
    public ItemOverrideList getOverrides() {
        return model.getOverrides();
    }
}
