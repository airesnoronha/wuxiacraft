package wuxiacraft.formation;

import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

public class FormationTileEntity extends TileEntity implements ITickableTileEntity {

	public FormationTileEntity(TileEntityType<?> tileEntityTypeIn) {
		super(tileEntityTypeIn);
	}

	@Override
	public void tick() {

	}
}
