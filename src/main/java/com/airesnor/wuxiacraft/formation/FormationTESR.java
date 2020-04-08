package com.airesnor.wuxiacraft.formation;

import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;

public class FormationTESR extends TileEntitySpecialRenderer<FormationTileEntity> {

	@Override
	public void render(FormationTileEntity te, double x, double y, double z, float partialTicks, int destroyStage, float alpha) {
		if(te.getState() == FormationTileEntity.FormationState.ACTIVE) {
			te.getFormation().render(x+0.5, y+0.5,z+0.5);
		}
	}
}
