package com.airesnor.wuxiacraft.blocks.models;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nullable;
import javax.vecmath.Matrix4f;
import javax.vecmath.Quat4f;
import javax.vecmath.Vector3f;
import java.util.List;

public class BaseBakedModel implements IBakedModel {

	private IBakedModel model;

	public BaseBakedModel(IBakedModel model) {
		this.model = model;
	}

	@Override
	public Pair<? extends IBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
		Matrix4f mat = new Matrix4f();
		Matrix4f aux = new Matrix4f();
		mat.setIdentity();
		aux.setIdentity();
		switch (cameraTransformType) {
			case GUI:
				aux.setScale(0.685f);
				mat.mul(aux);
				aux.setIdentity();
				aux.rotX(3.1415f*30f/180f);
				mat.mul(aux);
				aux.setIdentity();
				aux.rotY(3.1415f);
				mat.mul(aux);
				break;
			case GROUND:
				aux.setScale(0.3f);
				mat.mul(aux);
				break;
			case FIRST_PERSON_LEFT_HAND:
			case FIRST_PERSON_RIGHT_HAND:
				mat.setTranslation(new Vector3f(0f,0f,-1f));
				break;
			case THIRD_PERSON_LEFT_HAND:
			case THIRD_PERSON_RIGHT_HAND:
				aux.rotX(3.1415f/2f);
				mat.mul(aux);
				mat.setTranslation(new Vector3f(0f,0.2f,-0.29f));
				break;
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
		return false;
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
