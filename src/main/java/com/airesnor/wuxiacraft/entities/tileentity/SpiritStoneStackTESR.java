package com.airesnor.wuxiacraft.entities.tileentity;

import com.airesnor.wuxiacraft.WuxiaCraft;
import com.airesnor.wuxiacraft.items.ItemSpiritStone;
import com.airesnor.wuxiacraft.utils.OBJModelLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3d;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpiritStoneStackTESR extends TileEntitySpecialRenderer<SpiritStoneStackTileEntity> {


	private static Map<String, OBJModelLoader.Part> SPIRIT_STONE_MODEL;

	private static final ResourceLocation TEXTURE = new ResourceLocation(WuxiaCraft.MOD_ID, "textures/blocks/spirit_stone_stack_block.png");

	private static final List<Pair<Vec3d, Vec3d>> transformations = new ArrayList<>();

	public static void init() {
		try {
			SPIRIT_STONE_MODEL = OBJModelLoader.getPartsFromFile(new ResourceLocation(WuxiaCraft.MOD_ID, "models/block/spirit_stone.obj"));
		} catch (IOException e) {
			WuxiaCraft.logger.error("Couldn't find spirit stone model. Lame");
			e.printStackTrace();
		}
		setTransformations();
	}

	private static void setTransformations() {
		transformations.clear();
		transformations.add(Pair.of(new Vec3d(0, 0, 0), new Vec3d(0, 0, 0))); //0
		transformations.add(Pair.of(new Vec3d(0.14870, 0, 0.15401), new Vec3d(0, 0, 0))); //1
		transformations.add(Pair.of(new Vec3d(-0.06904, 0, 0.19650), new Vec3d(0, -110, 0))); //2
		transformations.add(Pair.of(new Vec3d(-0.21243, 0, -0.02655), new Vec3d(0, 0, 0))); //3
		transformations.add(Pair.of(new Vec3d(-0.14339, 0, -0.16463), new Vec3d(0, 0, 0))); //4
		transformations.add(Pair.of(new Vec3d(0.06904, 0, -0.20181), new Vec3d(0, 0, 0))); //5
		transformations.add(Pair.of(new Vec3d(0.21774, 0, -0.05311), new Vec3d(0, 12, 0))); //6
		transformations.add(Pair.of(new Vec3d(-0.07966, 0.075279, 0.07435), new Vec3d(0, 0, 0))); //7
		transformations.add(Pair.of(new Vec3d(-0.01593, 0.075279, -0.11684), new Vec3d(0, 0, 0))); //8
		transformations.add(Pair.of(new Vec3d(0.12746, 0.075279, 0.02655), new Vec3d(0, 0, 0))); //9
		transformations.add(Pair.of(new Vec3d(-0.37706, 0, -0.13808), new Vec3d(0, 0, 0))); //10
		transformations.add(Pair.of(new Vec3d(-0.28147, 0, 0.21774), new Vec3d(0, 0, 0))); //11
		transformations.add(Pair.of(new Vec3d(-0.08497, 0, 0.35582), new Vec3d(0, 0, 0))); //12
		transformations.add(Pair.of(new Vec3d(0.35582, 0, 0.09028), new Vec3d(0, 0, 0))); //13
		transformations.add(Pair.of(new Vec3d(0.32396, 0, -0.24429), new Vec3d(0, 0, 0))); //14
		transformations.add(Pair.of(new Vec3d(0.15932, 0, -0.39300), new Vec3d(0, 0, 0))); //15
		transformations.add(Pair.of(new Vec3d(-0.06904, 0, -0.38237), new Vec3d(0, -32.7, 0))); //16
		transformations.add(Pair.of(new Vec3d(-0.29209, 0, 0.33458), new Vec3d(0, 0, 0))); //17
		transformations.add(Pair.of(new Vec3d(-0.15401, 0, 0.39300), new Vec3d(0, 0, 0))); //18
		transformations.add(Pair.of(new Vec3d(-0.43017, 0, 0.06373), new Vec3d(0, 0, 0))); //19
		transformations.add(Pair.of(new Vec3d(0.30802, 0, 0.31333), new Vec3d(0, 0, 0))); //20
		transformations.add(Pair.of(new Vec3d(-0.15401, 0.075279, 0.27085), new Vec3d(0, 0, 0))); //21
		transformations.add(Pair.of(new Vec3d(-0.28678, 0.075279, 0.10090), new Vec3d(0, 0, 0))); //22
		transformations.add(Pair.of(new Vec3d(-0.23367, 0.075279, -0.08497), new Vec3d(0, 0, 0))); //23
		transformations.add(Pair.of(new Vec3d(-0.164633, 0.075279, -0.28147), new Vec3d(0, 0, 0))); //24
		transformations.add(Pair.of(new Vec3d(0.06904, 0.075279, -0.292091), new Vec3d(0, 0, 0))); //25
		transformations.add(Pair.of(new Vec3d(0.238984, 0.075279, -0.159323), new Vec3d(0, 0, 0))); //26
		transformations.add(Pair.of(new Vec3d(0.074351, 0.075279, 0.233673), new Vec3d(0, 0, 0))); //27
		transformations.add(Pair.of(new Vec3d(0.276159, 0.075279, 0.175255), new Vec3d(0, 0, 0))); //28
		transformations.add(Pair.of(new Vec3d(0.148701, 0.152309, 0.223051), new Vec3d(0, 0, 0))); //29
		transformations.add(Pair.of(new Vec3d(-0.06904, 0.152309, 0.223051), new Vec3d(0, 0, 0))); //30
		transformations.add(Pair.of(new Vec3d(-0.228362, 0.152309, 0.111526), new Vec3d(0, 0, 0))); //31
		transformations.add(Pair.of(new Vec3d(-0.14339, 0.152309, -0.079661), new Vec3d(0, 0, 0))); //32
		transformations.add(Pair.of(new Vec3d(0.079661, 0.152309, -0.141944), new Vec3d(0, -47.4, 0))); //33
		transformations.add(Pair.of(new Vec3d(0.026554, 0.152309, 0.058418), new Vec3d(0, -31.1, 0))); //34
		transformations.add(Pair.of(new Vec3d(0.318645, 0.090219, -0.013411), new Vec3d(-21.9, -15.3, 14.7))); //35
		transformations.add(Pair.of(new Vec3d(0.238984, 0.152309, 0.058418), new Vec3d(4.90875, 63.5428d, 13.6713))); //36
		transformations.add(Pair.of(new Vec3d(-0.01689, 0.224042, -0.100666), new Vec3d(4.90875d, 63.5428d, 13.6713d))); //37
		transformations.add(Pair.of(new Vec3d(0.190229, 0.224042, -0.05818), new Vec3d(4.90875d, 63.5428d, 13.6713d))); //38
		transformations.add(Pair.of(new Vec3d(0.062771, 0.224042, 0.085211), new Vec3d(4.90875d, 63.5428d, 13.6713d))); //39
		transformations.add(Pair.of(new Vec3d(-0.15497, 0.224042, 0.069278), new Vec3d(4.90875d, 63.5428d, 13.6713d))); //40
		transformations.add(Pair.of(new Vec3d(-0.07633, 0.224042, 0.272236), new Vec3d(4.90875d, 63.5428d, 13.6713d))); //41
		transformations.add(Pair.of(new Vec3d(0.264579, 0.224042, 0.133007), new Vec3d(4.90875d, 63.5428d, 13.6713d))); //42
		transformations.add(Pair.of(new Vec3d(0.1265, 0.224042, 0.271087), new Vec3d(4.90875d, 63.5428d, 13.6713d))); //43
		transformations.add(Pair.of(new Vec3d(0.031746, 0.120516, 0.394594), new Vec3d(-45.7543d, -17.4369d, 0.859126d))); //44
		transformations.add(Pair.of(new Vec3d(-0.42486, 0, 0.387685), new Vec3d(0, 0, 0))); //45
		transformations.add(Pair.of(new Vec3d(-0.415145, 0.038776, -0.327429), new Vec3d(-9.05991d, 25.7622d, -28.5423d))); //46
		transformations.add(Pair.of(new Vec3d(-0.425608, 0.042171, 0.25707), new Vec3d(40.0389d, -20.7296d, -1.94913d))); //47
		transformations.add(Pair.of(new Vec3d(-0.035083, 0.145306, -0.315018), new Vec3d(0, 0, 0))); //48
		transformations.add(Pair.of(new Vec3d(-0.273484, 0.14051, -0.304234), new Vec3d(0, 0, 4.16816d))); //49
		transformations.add(Pair.of(new Vec3d(-0.151581, 0.202384, -0.27828), new Vec3d(0, 0, 4.16816d))); //50
		transformations.add(Pair.of(new Vec3d(-0.010758, 0.29757, 0.243231), new Vec3d(-8.67943d, 61.1335d, 6.6228d))); //51
		transformations.add(Pair.of(new Vec3d(-0.132425, 0.29757, 0.065027), new Vec3d(-8.67943d, 61.1335d, 6.6228d))); //52
		transformations.add(Pair.of(new Vec3d(0.133218, 0.29757, -0.072004), new Vec3d(-8.67943d, 61.1335d, 6.6228d))); //53
		transformations.add(Pair.of(new Vec3d(-0.079212, 0.29757, -0.1198), new Vec3d(-8.67943d, 61.1335d, 6.6228d))); //54
		transformations.add(Pair.of(new Vec3d(0.143839, 0.29757, 0.140426), new Vec3d(-8.67943d, 61.1335d, 6.6228d))); //55
		transformations.add(Pair.of(new Vec3d(-0.079212, 0.373067, 0.135115), new Vec3d(-8.67943d, 61.1335d, 6.6228d))); //56
		transformations.add(Pair.of(new Vec3d(0.021692, 0.373067, -0.040139), new Vec3d(-8.67943d, 61.1335d, 6.6228d))); //57
		transformations.add(Pair.of(new Vec3d(0.127907, 0.373067, 0.145737), new Vec3d(-8.67943d, 108.116d, 6.6228d))); //58
		transformations.add(Pair.of(new Vec3d(0.029869, 0.426636, 0.089375), new Vec3d(-8.67943d, 223.379d, 6.6228d))); //59
		transformations.add(Pair.of(new Vec3d(-0.360635, 0.099899, -0.046184), new Vec3d(28.0279d, 220.144d, 46.0232d))); //60
		transformations.add(Pair.of(new Vec3d(-0.318825, 0.099678, -0.174275), new Vec3d(43.5135d, 314.492d, 42.2704d))); //61
		transformations.add(Pair.of(new Vec3d(-0.231992, -0.113313, 0.215496), new Vec3d(-2.34384d, -81.3549d, 5.73922d))); //62
		transformations.add(Pair.of(new Vec3d(-2.34384d, -81.3549d, 5.73922d), new Vec3d(17.0715d, -18.3081d, -333.686d))); //63
	}

	@Override
	public void render(SpiritStoneStackTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {

		int stones = 0;
		int colorH = 0x10EFFF;
		if(te.stack != null) {
			stones = te.stack.getCount();
			if(te.stack.getItem() instanceof ItemSpiritStone) {
				colorH = ((ItemSpiritStone) te.stack.getItem()).color;
			}
		}
		Color color = new Color(colorH);

		GlStateManager.pushAttrib();
		GlStateManager.pushMatrix();
		Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURE);
		GlStateManager.enableBlend();
		GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);

		GlStateManager.translate(x,y,z);

		GlStateManager.color(color.getRed()/256f,color.getGreen()/256f,color.getBlue()/256f,0.9f);

		for(int i = 0; i < stones; i++) {
			GlStateManager.pushMatrix();
			Pair<Vec3d, Vec3d> transformation = transformations.get(i);
			applyTransformations(transformation.getLeft(), transformation.getRight());
			SPIRIT_STONE_MODEL.get("SpiritStone").draw();
			GlStateManager.popMatrix();
		}
		GlStateManager.disableBlend();
		GlStateManager.popMatrix();
		GlStateManager.popAttrib();
	}

	public void applyTransformations(Vec3d translations, Vec3d rotations) {
		GlStateManager.translate(translations.x, translations.y, translations.z);
		GlStateManager.translate(0.47961, 0.04414, 0.52063);
		GlStateManager.rotate(180+(float)rotations.y, 0, 1, 0);
		GlStateManager.rotate((float)rotations.x, 1, 0, 0);
		GlStateManager.rotate((float)rotations.z, 0, 0, 1);
		GlStateManager.translate(-0.47961, -0.04414, -0.52063);
	}
}
